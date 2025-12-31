package com.mootai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CaseRequest {
    
    @NotBlank(message = "身份不能为空")
    private String identity; // "plaintiff" 或 "defendant"
    
    @NotNull(message = "文件列表不能为空")
    private List<String> fileNames; // 文件名列表
    
    @NotBlank(message = "案件描述不能为空")
    private String caseDescription; // 案件描述
}

