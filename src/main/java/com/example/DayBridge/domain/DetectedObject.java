//package com.example.DayBridge.domain;
//
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.geo.Point;
//
//import java.util.List;
//
//
//@Entity
//@NoArgsConstructor
//@Getter
//@Setter
//public class DetectedObject {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id; // 기본 키 필드
//
//    private String objectName;
//    private float score;
//    private List<Point> normalizedVertices;
//
//}
