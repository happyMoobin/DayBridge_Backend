//package com.example.DayBridge.controller;
//
//import com.example.DayBridge.service.ChatService;
//import io.github.flashvayne.chatgpt.service.ChatgptService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//@Slf4j
//@RequestMapping("/api/v1/chat-gpt")
//public class chatGPTController {
//
//
//    private final ChatService chatService;
//    private final ChatgptService chatgptService;
//
//    // chat-gpt 채팅 서비스 소스 질문에 대한 답
//    @PostMapping("")
//    public String chat(@RequestBody String question) {
//        // question에 AIData의 값을 넣어줘야함
//        return chatService.getChatResponse(question);
//    }
//
//    @PostMapping("/getImageData")
//    public String Image(@RequestBody String fullChat) {
//        return chatService.getImageResponse(fullChat);
//    }
//}
