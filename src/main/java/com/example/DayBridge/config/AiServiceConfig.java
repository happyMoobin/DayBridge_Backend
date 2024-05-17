//package com.example.DayBridge.config;
//
//import com.theokanning.openai.service.OpenAiService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.Duration;
//
//@Configuration
//public class AiServiceConfig {
//
//    @Value("${openai.key}")
//    private String apiKey;
//
//    //gpt에 apikey전달
//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        return restTemplate;
//    }
//
//    @Bean
//    public HttpHeaders httpHeaders() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + apiKey);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        return headers;
//    }
//}
