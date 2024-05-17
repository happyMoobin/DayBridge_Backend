package com.example.DayBridge.service;

import com.example.DayBridge.domain.FormData;
import com.example.DayBridge.domain.Users;
import com.example.DayBridge.repository.FormDataRepository;
import io.github.flashvayne.chatgpt.dto.image.ImageFormat;
import io.github.flashvayne.chatgpt.dto.image.ImageSize;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FormService {

    @Autowired
    private FormDataRepository formdataRepository;

    @Autowired
    private final ChatgptService chatgptService;

    @Autowired
    private final CloudService cloudService;

    public void saveFormData(Users user, String pointColor, String windowPosition, Integer windowNum, String essentialFurniture) {
        FormData formdata = new FormData();
        formdata.setUsers(user);
        formdata.setPointColor(pointColor);
        formdata.setWindowPosition(windowPosition);
        formdata.setWindowNum(windowNum);
        formdata.setEssentialFurniture(essentialFurniture);
        //formdata.setRoomSize(roomSize);
        formdataRepository.save(formdata);
    }

    // 단순히 이미지 생성후 리턴하는게 아니라 b64를 이미지로 변환해서 리턴하도록 수정
    public byte[] getImageResponse(String prompt, ImageSize large, ImageFormat base64) throws Exception {

        List<String> generateImage = chatgptService.imageGenerate(prompt,1,large ,base64);
//        String image = generateImage.toString();
        String image = generateImage.get(0);

        byte[] imageByte = Base64.getDecoder().decode(image);
        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imageByte));

        // 이미지를 PNG 형식으로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpeg", outputStream);
        byte[] jpegImage = outputStream.toByteArray();

        String objectName = UUID.randomUUID().toString(); // 저장될 파일 이름, 중복 없도록 uuid로 생성
        // 생성된 이미지 google cloud에 이미지 업로드
        CloudService.uploadImage(jpegImage, objectName);


        CloudService.importProductSets();
        CloudService.getSimilarProductsGcs("6941dce5-ddd2-4d3a-92b9-0dc6418e3f0a");
        // vision api로 유사 제품 검색
        //CloudService.getSimilarProductsGcs(objectName, model);

        return jpegImage;
    }


}