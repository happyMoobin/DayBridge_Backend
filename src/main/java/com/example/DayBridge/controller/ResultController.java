package com.example.DayBridge.controller;

import com.example.DayBridge.service.CloudService;
import com.example.DayBridge.service.ObjectDetection;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;


@Controller
public class ResultController {
    private final ResourceLoader resourceLoader;

    @Autowired
    public ResultController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

//    @GetMapping("/DayBridge/result")
//    public String detectObject(Model model) {
//                try {
//                    // 이미지 경로 작성
//                    CloudService.getSimilarProductsGcs();
//
//                    // 결과를 모델에 추가
//                    model.addAttribute("message", "Object detection completed successfully");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    // 예외 처리
//                    // 에러 메시지를 모델에 추가
//                    model.addAttribute("error", "Error occurred while detecting objects");
//        }
//
//        // 결과를 보여줄 뷰의 이름을 반환
//        return "example";
//    }
}
