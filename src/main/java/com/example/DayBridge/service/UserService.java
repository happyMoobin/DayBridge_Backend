package com.example.DayBridge.service;

import com.example.DayBridge.domain.JoinRequest;
import com.example.DayBridge.domain.LoginRequest;
import com.example.DayBridge.repository.UserRepository;
import com.example.DayBridge.domain.Users;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;


    //LoginID 중복 체크
    //회원가입 기능 구현시 사용
    //중복일 경우 true를 리턴하도록 함
    public boolean checkUserIDDuplicate(String userID){
        return userRepository.existsByUserID(userID);
    }
    
    //nickName 중복 체크
    //이 부분도 회원가입 기능 구현시 사용
    //중복일 경우 true 리턴
    public boolean checkNickNameDuplicate(String nickName){
        return userRepository.existsByNickName(nickName);
    }
    
    // 회원가입
    public void join(JoinRequest req) {
        userRepository.save(req.toEntity(encoder.encode(req.getUserPW())));
    }
    
    
    // 로그인 기능 구현
    // 화면에서 id, pw 입력받아 일치하면 users 리턴
    // 존재하지 않거나 pw가 다르면 null 리턴
    public Users login(LoginRequest lr){
        Optional<Users> optionalUsers = userRepository.findByUserID(lr.getUserID());

        // id 확인
        if(optionalUsers.isEmpty()){
            System.out.println("optionalUsers is : "+ optionalUsers);
            return null;
        }

        Users users = optionalUsers.get();

        System.out.println("lr.getUserPW() is : " + lr.getUserPW());
        System.out.println("users.getUserPW() is : " + users.getUserPW());
        // pw 확인
        if(!encoder.matches(lr.getUserPW(), users.getUserPW())) {
            return null;
        }
//        if(!users.getUserPW().equals(lr.getUserPW())) {
//            return null;
//        }

        System.out.println("로그인 성공 !!");
        return users;
    }

    // Long 형식의 id를 입력받아 user를 리턴하는 기능
    // 인증시 사용
    public Users getLoginUserByID(Long userID){
        if(userID == null) return null;

        Optional<Users> optionalUsers = userRepository.findById(userID);
        if(optionalUsers.isEmpty()) return null;

        return optionalUsers.get();
    }

    // String 형식의 id를 입력받아 user를 리턴하는 기능
    // 인증시 사용
    public Users getLoginUserByID(String userID) {
        if(userID == null) return null;

        Optional<Users> optionalUsers = userRepository.findByUserID(userID);
        if(optionalUsers.isEmpty()) return null;

        return optionalUsers.get();
    }
}
