package com.example.DayBridge.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

@Service
public class ObjectDetection {

    public static void detectObject (String gcsPath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();

        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(Feature.newBuilder().setType(Type.OBJECT_LOCALIZATION))
                .setImage(img)
                .build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()){
                    System.out.format("Object name: %s%n", entity.getName());
                    System.out.format("Confidence: %s%n", entity.getScore());
                    System.out.format("Normalized Vertices: %n");
                    entity
                            .getBoundingPoly()
                            .getNormalizedVerticesList()
                            .forEach(vertex -> System.out.format("- (%s, %s)%n", vertex.getX(), vertex.getY()));
                }
            }
        }
    }

//    public static void detectLocalizedObjectsGcs(String gcsPath) throws IOException {
//        List<AnnotateImageRequest> requests = new ArrayList<>();
//
//        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
//        Image img = Image.newBuilder().setSource(imgSource).build();
//
//        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
//                .addFeatures(Feature.newBuilder().setType(Type.OBJECT_LOCALIZATION)).setImage(img).build();
//        requests.add(request);
//
//        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
//            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
//            List<AnnotateImageResponse> responses = response.getResponsesList();
//
//            // Construct HTML string to display the results
//            StringBuilder htmlBuilder = new StringBuilder();
//            htmlBuilder.append("<html><head><title>Detected Objects</title></head><body>");
//            for (AnnotateImageResponse res : responses) {
//                for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
//                    htmlBuilder.append("<div>");
//                    htmlBuilder.append("<p>Object name: ").append(entity.getName()).append("</p>");
//                    htmlBuilder.append("<p>Confidence: ").append(entity.getScore()).append("</p>");
//                    htmlBuilder.append("<p>Normalized Vertices:</p>");
//                    htmlBuilder.append("<ul>");
//                    for (NormalizedVertex vertex : entity.getBoundingPoly().getNormalizedVerticesList()) {
//                        htmlBuilder.append("<li>").append(" - (").append(vertex.getX()).append(", ")
//                                .append(vertex.getY()).append(")").append("</li>");
//                    }
//                    htmlBuilder.append("</ul>");
//                    htmlBuilder.append("</div>");
//                }
//            }
//            htmlBuilder.append("</body></html>");
//
//            // Print HTML to console
//            System.out.println(htmlBuilder.toString());
//        }
//    }
}
