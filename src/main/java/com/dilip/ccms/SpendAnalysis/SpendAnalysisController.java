package com.dilip.ccms.SpendAnalysis;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spending")
@RequiredArgsConstructor
public class SpendAnalysisController {
    private SpendAnalysisService spendAnalysisService;


}
