package org.sylph.everything.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sylph.everything.dto.ChartDataResponse;
import org.sylph.everything.dto.StockPriceResponse;
import org.sylph.everything.service.StockCrawlingService;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ApiStockController {

    private final StockCrawlingService stockCrawlingService;

    @GetMapping("/price/{stockCode}")
    public ResponseEntity<StockPriceResponse> getStockPrice(@PathVariable String stockCode) {
        try {
            StockPriceResponse response = stockCrawlingService.getStockPrice(stockCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/chart/{stockCode}")
    public ResponseEntity<ChartDataResponse> getStockChartData(
            @PathVariable String stockCode,
            @RequestParam(defaultValue = "7") int days) {
        try {
            ChartDataResponse response = stockCrawlingService.getStockChartData(stockCode, days);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}