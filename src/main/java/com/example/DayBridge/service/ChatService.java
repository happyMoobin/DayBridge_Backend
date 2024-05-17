package com.example.DayBridge.service;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatgptService chatgptService;


    // AiData의 prompt에 질문을 담아서 함수 실행
    public String getChatResponse(String prompt) {
        // ChatGPT에게 질문
        return chatgptService.sendMessage(prompt);
    }

    public String getImageResponse(String prompt) {
        return chatgptService.imageGenerate(prompt);
        //이미지의 url 반환(테스트 및 url을 json 형식으로 변환 고민)
    }
}
