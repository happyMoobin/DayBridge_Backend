package com.example.DayBridge.repository;

import com.example.DayBridge.domain.FormData;
import com.example.DayBridge.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormDataRepository extends JpaRepository<FormData, Long> {

    List<FormData> findByUsers_Id(Long userId);


}
