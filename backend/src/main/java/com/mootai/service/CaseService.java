package com.mootai.service;

import com.mootai.dto.CaseRequest;
import com.mootai.entity.Case;
import com.mootai.repository.CaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseService {
    
    private final CaseRepository caseRepository;
    
    @Transactional
    public Case createCase(Long userId, CaseRequest request) {
        Case caseEntity = new Case();
        caseEntity.setUserId(userId);
        caseEntity.setIdentity(request.getIdentity());
        caseEntity.setCaseDescription(request.getCaseDescription());
        caseEntity.setFileNames(request.getFileNames());
        
        return caseRepository.save(caseEntity);
    }
    
    public List<Case> getUserCases(Long userId) {
        return caseRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public Case getCaseById(Long caseId, Long userId) {
        return caseRepository.findByIdAndUserId(caseId, userId);
    }
}

