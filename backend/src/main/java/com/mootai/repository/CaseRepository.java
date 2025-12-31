package com.mootai.repository;

import com.mootai.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
    
    List<Case> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    Case findByIdAndUserId(Long id, Long userId);
}

