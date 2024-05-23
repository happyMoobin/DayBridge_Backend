package com.example.DayBridge.controller;

import com.example.DayBridge.domain.FormData;
import com.example.DayBridge.domain.JoinRequest;
import com.example.DayBridge.domain.LoginRequest;
import com.example.DayBridge.domain.Users;
import com.example.DayBridge.repository.FormDataRepository;
import com.example.DayBridge.service.CloudService;
import com.example.DayBridge.service.FormService;
import com.example.DayBridge.service.UserService;

import com.google.cloud.vision.v1.ProductSearchResults;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/DayBridge")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final FormService formService;

    private final FormDataRepository formDataRepository;

    @GetMapping("/")
    public String home(Model model, Authentication auth) {
        if(auth != null) {
            Users loginUser = userService.getLoginUserByID(auth.getName());
            if(loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickName());
            }
        }
        return "home";
    }

    @GetMapping("/signUp")
    public String goToSignUpPage(Model model) {
        model.addAttribute("joinRequest", new JoinRequest());
        return "signUp";
    }

    // 회원가입
    @PostMapping("/signUp")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult) {
        // id 중복 체크
        if(userService.checkUserIDDuplicate(joinRequest.getUserID())) {
            bindingResult.addError(new FieldError("joinRequest", "userID", "이미 존재하는 아이디입니다."));
        }

        // 닉네임 중복 체크
        if(userService.checkNickNameDuplicate(joinRequest.getNickName())) {
            bindingResult.addError(new FieldError("joinRequest", "nickName","이미 존재하는 별명입니다."));
        }

        // password 검증
        if(!joinRequest.getUserPW().equals(joinRequest.getPwCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if(bindingResult.hasErrors()) {
            return "signUp";
        }

        userService.join(joinRequest);
        return "redirect:/DayBridge/";
    }

    @GetMapping("/userID/{userID}/exists")
    public ResponseEntity<Boolean> checkIdDuplicate(@PathVariable String userID) {
        return ResponseEntity.ok(userService.checkUserIDDuplicate(userID));
    }

    @GetMapping("/nickName/{nickName}/exists")
    public ResponseEntity<Boolean> checkNickNameDuplicate(@PathVariable String nickName) {
        return ResponseEntity.ok(userService.checkNickNameDuplicate(nickName));
    }
    // 로그인
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginSuccess(@RequestBody LoginRequest loginRequest) {
        Users user = userService.login(loginRequest);
        System.out.println("user is : " + user);

        if (user == null) {
            // 로그인 실패 시
            return ResponseEntity.badRequest().body("아이디 혹은 비밀번호를 다시 확인하세요.");
        } else {
            // 로그인 성공 시
            return ResponseEntity.ok().body("로그인 성공");
        }
    }

    @GetMapping("/gallery")
    public String goToGallery(Model model) {
        List<FormData> formDataList = formService.getAllFormData();
        model.addAttribute("formDataList", formDataList);
        return "gallery";
    }

}
