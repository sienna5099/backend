package com.aicareermentor.backend.repository;

import com.aicareermentor.backend.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CareerRepository extends JpaRepository<Career, Long> {

    Optional<Career> findByCareerId(String careerId);
}
