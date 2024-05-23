package com.example.DayBridge.domain;

import com.example.DayBridge.domain.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "formdata")
public class FormData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designId; // 생성 디자인 넘버

    // 다대일 연관관계 필요
    // 유저의 FK로 사용
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users; // 유저 넘버
    private String pointColor; // 포인트 색상
    private String windowPosition; // 창문 위치
    private Integer windowNum; // 창문 개수
    private String essentialFurniture; // 필수 가구
    private int roomSize;

    @CreationTimestamp
    private LocalDateTime createTime;

    @Lob
    private String image;
}