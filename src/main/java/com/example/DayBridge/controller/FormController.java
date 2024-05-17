package com.example.DayBridge.controller;


import com.example.DayBridge.repository.UserRepository;
import com.example.DayBridge.service.CloudService;
import com.example.DayBridge.service.FormService;
import com.example.DayBridge.service.ObjectDetection;
import io.github.flashvayne.chatgpt.dto.image.ImageFormat;
import io.github.flashvayne.chatgpt.dto.image.ImageSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Base64;

@Controller
public class FormController {

    @Autowired
    private FormService formService;

    @Autowired
    private UserRepository userRepository;

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

        return "result"; // 결과 표시할 페이지
    }
}