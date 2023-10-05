package com.example.demo.system_service.repository;

import com.example.demo.system_service.entity.LogRec;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRecRepository extends JpaRepository<LogRec, Long> {
}
