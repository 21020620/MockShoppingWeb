package com.example.demo.repository;

import com.example.demo.entities.LogRec;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRecRepository extends JpaRepository<LogRec, Long> {
}
