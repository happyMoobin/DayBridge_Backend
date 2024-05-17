package com.example.DayBridge.service;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.vision.v1.*;
import java.io.IOException;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
public class CloudService {
    private static final String bucketName = "daybridge_bucket_1";
    private static final String projectId = "micro-flight-422110";
    private static final String productSetId = "ikea_furniture";
    private static final String computeRegion = "asia-east1";
    private static final String gcsUri = "gs://daybridge_bucket_1/data/itemdata.csv";


    // 이미지 업로드
    public static void uploadImage(byte[] content, String objectName) throws IOException {

        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();

        BlobId blobId = BlobId.of(bucketName, "/image"+objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build(); // png로 확장자 설정

        // 업로드
        storage.create(blobInfo, content);

        // 업르도 확인용 콘솔 메시지
        System.out.println(
                "Object "
                        + objectName
                        + " uploaded to bucket "
                        + bucketName
                        + " with contents "
                        + objectName);

    }

    // 이미지 메모리에 다운로드
    public static void downloadImage(String objectName) {
        // The ID of your GCS object
        // String objectName = "your-object-name";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        byte[] content = storage.readAllBytes(bucketName, objectName);
        System.out.println(
                "The contents of "
                        + objectName
                        + " from bucket name "
                        + bucketName
                        + " are: "
                        + new String(content, StandardCharsets.UTF_8));
    }

    // product set 나열
    public static void listProductSets() throws IOException {
        try (ProductSearchClient client = ProductSearchClient.create()) {
            // A resource that represents Google Cloud Platform location.
            String formattedParent = LocationName.format(projectId, computeRegion);
            // List all the product sets available in the region.
            for (ProductSet productSet : client.listProductSets(formattedParent).iterateAll()) {
                // Display the product set information
                System.out.println(String.format("Product set name: %s", productSet.getName()));
                System.out.println(
                        String.format(
                                "Product set id: %s",
                                productSet.getName().substring(productSet.getName().lastIndexOf('/') + 1)));
                System.out.println(
                        String.format("Product set display name: %s", productSet.getDisplayName()));
                System.out.println("Product set index time:");
                System.out.println(String.format("\tseconds: %s", productSet.getIndexTime().getSeconds()));
                System.out.println(String.format("\tnanos: %s", productSet.getIndexTime().getNanos()));
            }
        }
    }

    // 제품 모두 가져오기
        public static void listProductsInProductSet() throws IOException {
        try (ProductSearchClient client = ProductSearchClient.create()) {

            // Get the full path of the product set.
            String formattedName = ProductSetName.format(projectId, computeRegion, productSetId);
            // List all the products available in the product set.
            for (Product product : client.listProductsInProductSet(formattedName).iterateAll()) {
                // Display the product information
                System.out.println(String.format("Product name: %s", product.getName()));
                System.out.println(
                        String.format(
                                "Product id: %s",
                                product.getName().substring(product.getName().lastIndexOf('/') + 1)));
                System.out.println(String.format("Product display name: %s", product.getDisplayName()));
                System.out.println(String.format("Product description: %s", product.getDescription()));
                System.out.println(String.format("Product category: %s", product.getProductCategory()));
                System.out.println("Product labels: ");
                for (Product.KeyValue element : product.getProductLabelsList()) {
                    System.out.println(String.format("%s: %s", element.getKey(), element.getValue()));
                }
            }
        }
    }


    // 제품 세트 생성
    /*
     * Import images of different products in the product set.
     *
     * @param projectId - Id of the project.
     * @param computeRegion - Region name.
     * @param gcsUri - Google Cloud Storage URI.Target files must be in Product Search CSV format.
     * @throws Exception - on client errors.
     */
    public static void importProductSets()
            throws Exception {
        try (ProductSearchClient client = ProductSearchClient.create()) {

            // A resource that represents Google Cloud Platform location.
            String formattedParent = LocationName.format(projectId, computeRegion);
            ImportProductSetsGcsSource.Builder gcsSource = ImportProductSetsGcsSource.newBuilder().setCsvFileUri(gcsUri);

            // Set the input configuration along with Google Cloud Storage URI
            ImportProductSetsInputConfig inputConfig =
                    ImportProductSetsInputConfig.newBuilder().setGcsSource(gcsSource).build();

            // Import the product sets from the input URI.
            OperationFuture<ImportProductSetsResponse, BatchOperationMetadata> response =
                    client.importProductSetsAsync(formattedParent, inputConfig);

            System.out.println(String.format("Processing operation name: %s", response.getName()));
            ImportProductSetsResponse results = response.get();
            System.out.println("Processing done.");
            System.out.println("Results of the processing:");

            for (int i = 0; i < results.getStatusesCount()-1; i++) {
                System.out.println(
                        String.format(
                                "Status of processing line %s of the csv: %s", i, results.getStatuses(i)));
                // Check the status of reference image.
                if (results.getStatuses(i).getCode() == 0) {
                    ReferenceImage referenceImage = results.getReferenceImages(i);
                    System.out.println(referenceImage);
                } else {
                    System.out.println("No reference image.");
                }
            }
        }
    }


    // 제품 검색
    public static void getSimilarProductsGcs(String objectName)
            throws Exception {
        try (ImageAnnotatorClient queryImageClient = ImageAnnotatorClient.create()) {

            // Get the full path of the product set.
            String productSetPath = ProductSetName.of(projectId, computeRegion, productSetId).toString();

            // Get the image from Google Cloud Storage
            ImageSource source = ImageSource.newBuilder().setGcsImageUri("gs://daybridge_bucket_1/"+objectName).build();

            // Create annotate image request along with product search feature.
            Feature featuresElement = Feature.newBuilder().setType(Feature.Type.PRODUCT_SEARCH).build();
            Image image = Image.newBuilder().setSource(source).build();
            ImageContext imageContext =
                    ImageContext.newBuilder()
                            .setProductSearchParams(
                                    ProductSearchParams.newBuilder()
                                            .setProductSet(productSetPath)
                                            .addProductCategories("homegoods-v2")
                                            )
                            .build();

            AnnotateImageRequest annotateImageRequest =
                    AnnotateImageRequest.newBuilder()
                            .addFeatures(featuresElement)
                            .setImage(image)
                            .setImageContext(imageContext)
                            .build();
            List<AnnotateImageRequest> requests = Arrays.asList(annotateImageRequest);

            // Search products similar to the image.
            BatchAnnotateImagesResponse response = queryImageClient.batchAnnotateImages(requests);

            List<ProductSearchResults.Result> similarProducts =
                    response.getResponses(0).getProductSearchResults().getResultsList();
            System.out.println("Similar Products: ");
            for (ProductSearchResults.Result product : similarProducts) {
                System.out.println(String.format("\nProduct name: %s", product.getProduct().getName()));
                System.out.println(
                        String.format("Product display name: %s", product.getProduct().getDisplayName()));
                System.out.println(
                        String.format("Product description: %s", product.getProduct().getDescription()));
                System.out.println(String.format("Score(Confidence): %s", product.getScore()));
                System.out.println(String.format("Image name: %s", product.getImage()));
            }

            //model.addAttribute("similarProducts", similarProducts);
        }
    }

    /**
     * List all images in a product.
     *
     * @param projectId - Id of the project.
     * @param computeRegion - Region name.
     * @param productId - Id of the product.
     * @throws IOException - on I/O errors.
     */
    public static void listReferenceImagesOfProduct(
            String projectId, String computeRegion, String productId) throws IOException {
        try (ProductSearchClient client = ProductSearchClient.create()) {

            // Get the full path of the product.
            String formattedParent = ProductName.format(projectId, computeRegion, productId);
            for (ReferenceImage image : client.listReferenceImages(formattedParent).iterateAll()) {

                // Display the reference image information.
                System.out.println(String.format("Reference image name: %s", image.getName()));
                System.out.println(
                        String.format(
                                "Reference image id: %s",
                                image.getName().substring(image.getName().lastIndexOf('/') + 1)));
                System.out.println(String.format("Reference image uri: %s", image.getUri()));
                System.out.println(
                        String.format(
                                "Reference image bounding polygons: %s \n",
                                image.getBoundingPolysList().toString()));
            }
        }
    }
}
