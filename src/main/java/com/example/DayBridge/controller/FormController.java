package com.example.DayBridge.controller;


import com.example.DayBridge.domain.FormData;
import com.example.DayBridge.repository.FormDataRepository;
import com.example.DayBridge.repository.UserRepository;
import com.example.DayBridge.service.CloudService;
import com.example.DayBridge.service.FormService;
import com.example.DayBridge.service.ObjectDetection;
import com.google.cloud.vision.v1.ProductSearchResults;
import io.github.flashvayne.chatgpt.dto.image.ImageFormat;
import io.github.flashvayne.chatgpt.dto.image.ImageSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Controller
public class FormController {

    @Autowired
    private FormService formService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FormDataRepository formDataRepository;

    @Autowired
    private CloudService cloudService;

    @RequestMapping("/DayBridge/form")
    public String showForm() {
        return "form"; // HTML 폼 페이지
    }

    @PostMapping("/DayBridge/form")
    public String submitForm(@RequestParam("pointColor") String pointColor,
                             @RequestParam("windowPosition") String windowPosition,
                             @RequestParam("windowNum") Integer windowNum,
                             @RequestParam("essentialFurniture") String essentialFurniture,
                             @RequestParam("roomSize") int roomSize,
                             Model model) throws Exception {


        String dataToSend =
                "A photograph resembling a real-life room, featuring " + essentialFurniture + " furniture and "
                        + windowNum + " windows positioned " + windowPosition + ", all in a " + pointColor + " tone. " +
                        "The room size is " + roomSize + " square meters.";


        // 여기서 리턴 받는건 이미지 파일
        byte[] image = formService.getImageResponse(dataToSend, ImageSize.LARGE, ImageFormat.BASE64);
        String b64_image = Base64.getEncoder().encodeToString(image);
        // 이부분 수정됨
//        <img src="data:image/jpg;base64,${image}" /> 이렇게 화면에 그냥 넣을 수 있음
//        visionAPI에는 jpg로 넣어줘야함
        model.addAttribute("image", b64_image);


        String objectName = UUID.randomUUID().toString(); // 저장될 파일 이름, 중복 없도록 uuid로 생성
        // 생성된 이미지 google cloud에 이미지 업로드
        CloudService.uploadImage(image, objectName);
        model.addAttribute("objectName", objectName); // 모델에 objectName 추가하여 추후에 @RequestParam으로 받아올 수 있음

        FormData formData = new FormData();
        formData.setPointColor(pointColor);
        formData.setWindowNum(windowNum);
        formData.setWindowPosition(windowPosition);
        formData.setEssentialFurniture(essentialFurniture);
        formData.setRoomSize(roomSize);
        formData.setImage(b64_image);
        formData.setCreateTime(LocalDateTime.now());

        formDataRepository.save(formData);

        return "result"; // 결과 표시할 페이지
    }

    @GetMapping("/DayBridge/furniture")
    public String showSimilarProducts(@RequestParam("objectName") String objectName, Model model) {
        try {
            // objectName 파라미터로 받은 이미지 이름을 통해 storage에서 이미지를 메모리에 다운받아 로드함
            byte[] downloadImage = CloudService.downloadImage(objectName);
            String base64Image = Base64.getEncoder().encodeToString(downloadImage);
            model.addAttribute("downloadImage", base64Image);

            // 제품 세트(csv) 불러오기
            CloudService.importProductSets();
            // 유사 제품 검색해서 리스트 만들기
            List<ProductSearchResults.Result> similarProducts = CloudService.getSimilarProductsGcs(objectName);
            // 유사 제품 이미지 리스트
            List<String> publicUrls = CloudService.getPublicImageUrlLists(similarProducts);

            model.addAttribute("similarProducts", similarProducts);
            model.addAttribute("publicUrls", publicUrls);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "furniture"; // HTML 파일 이름 (furniture.html)
    }

    @GetMapping("/example")
    public String showTest(Model model) {
        try {
            CloudService.importProductSets();
            //CloudService.listProductSets();
            //CloudService.listProductsInProductSet();
            List<ProductSearchResults.Result> similarProducts = CloudService.getSimilarProductsGcs("6941dce5-ddd2-4d3a-92b9-0dc6418e3f0a");
            model.addAttribute("similarProducts", similarProducts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "example"; // HTML 파일 이름 (test.html)
    }
}