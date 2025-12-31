package com.mootai.controller;

import com.mootai.dto.ApiResponse;
import com.mootai.dto.CaseRequest;
import com.mootai.entity.Case;
import com.mootai.service.CaseService;
import com.mootai.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
public class CaseController {
    
    private final CaseService caseService;
    private final UserService userService;
    
    /**
     * 创建案件
     */
    @PostMapping
    public ApiResponse<Case> createCase(@Valid @RequestBody CaseRequest request) {
        try {
            // 从JWT token中获取用户ID（需要实现JWT解析）
            // 暂时使用固定值，后续需要从token中解析
            Long userId = getCurrentUserId();
            Case caseEntity = caseService.createCase(userId, request);
            return ApiResponse.success("案件创建成功", caseEntity);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 获取当前用户的所有案件
     */
    @GetMapping
    public ApiResponse<List<Case>> getUserCases() {
        try {
            Long userId = getCurrentUserId();
            List<Case> cases = caseService.getUserCases(userId);
            return ApiResponse.success("获取成功", cases);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 获取指定案件详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Case> getCase(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            Case caseEntity = caseService.getCaseById(id, userId);
            if (caseEntity == null) {
                return ApiResponse.error(404, "案件不存在");
            }
            return ApiResponse.success("获取成功", caseEntity);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            // 通过用户名查找用户ID
            try {
                com.mootai.entity.User user = userService.findByUsername(username);
                return user.getId();
            } catch (Exception e) {
                throw new RuntimeException("用户不存在");
            }
        }
        throw new RuntimeException("未认证");
    }
}

