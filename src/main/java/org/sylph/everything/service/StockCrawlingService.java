package org.sylph.everything.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.sylph.everything.dto.ChartDataResponse;
import org.sylph.everything.dto.StockPriceResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StockCrawlingService {

    private final Random random = new Random();
    
    public StockPriceResponse getStockPrice(String stockCode) {
        try {
            String url = "https://finance.naver.com/item/main.naver?code=" + stockCode;
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();

            String name = doc.select(".wrap_company h2 a").text();
            if (name.isEmpty()) {
                name = getStockNameByCode(stockCode);
            }

            Element priceElement = doc.select(".no_today .blind").first();
            String price = priceElement != null ? priceElement.text().replaceAll(",", "") : generateMockPrice(stockCode);

            Elements changeElements = doc.select(".no_exday .blind");
            String change = "0";
            String changePercent = "0.00";
            boolean isUp = false;
            boolean isDown = false;

            if (changeElements.size() >= 2) {
                change = changeElements.get(0).text().replaceAll(",", "");
                changePercent = changeElements.get(1).text().replace("%", "");
                
                Element upElement = doc.select(".up").first();
                Element downElement = doc.select(".down").first();
                
                isUp = upElement != null;
                isDown = downElement != null;
            } else {
                int changeValue = random.nextInt(2000) - 1000;
                change = String.valueOf(changeValue);
                changePercent = String.format("%.2f", (changeValue / Double.parseDouble(price)) * 100);
                isUp = changeValue > 0;
                isDown = changeValue < 0;
            }

            String volume = doc.select("table.tbl_data tr:contains(거래량) td .blind").text();
            if (volume.isEmpty()) {
                volume = String.valueOf(random.nextInt(1000000) + 100000);
            }

            return new StockPriceResponse(stockCode, name, price, change, changePercent, volume, isUp, isDown);

        } catch (IOException e) {
            return generateMockStockPrice(stockCode);
        }
    }

    public ChartDataResponse getStockChartData(String stockCode, int days) {
        List<ChartDataResponse.ChartPoint> chartPoints = new ArrayList<>();
        
        try {
            LocalDate endDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            int basePrice = Integer.parseInt(generateMockPrice(stockCode));
            
            for (int i = days - 1; i >= 0; i--) {
                LocalDate date = endDate.minusDays(i);
                
                int priceVariation = random.nextInt(10000) - 5000;
                int price = Math.max(1000, basePrice + priceVariation);
                int volume = random.nextInt(500000) + 50000;
                
                chartPoints.add(new ChartDataResponse.ChartPoint(
                    date.format(formatter),
                    String.valueOf(price),
                    String.valueOf(volume)
                ));
                
                basePrice = price;
            }
        } catch (Exception e) {
            for (int i = days - 1; i >= 0; i--) {
                LocalDate date = LocalDate.now().minusDays(i);
                chartPoints.add(new ChartDataResponse.ChartPoint(
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    generateMockPrice(stockCode),
                    String.valueOf(random.nextInt(500000) + 50000)
                ));
            }
        }

        return new ChartDataResponse(stockCode, chartPoints);
    }

    private String generateMockPrice(String stockCode) {
        switch (stockCode) {
            case "005930": return String.valueOf(70000 + random.nextInt(10000));
            case "000660": return String.valueOf(130000 + random.nextInt(20000));
            case "035420": return String.valueOf(180000 + random.nextInt(30000));
            case "051910": return String.valueOf(450000 + random.nextInt(50000));
            case "035720": return String.valueOf(50000 + random.nextInt(20000));
            case "005380": return String.valueOf(180000 + random.nextInt(40000));
            default: return String.valueOf(10000 + random.nextInt(50000));
        }
    }

    private StockPriceResponse generateMockStockPrice(String stockCode) {
        String name = getStockNameByCode(stockCode);
        String price = generateMockPrice(stockCode);
        int changeValue = random.nextInt(4000) - 2000;
        String change = String.valueOf(changeValue);
        String changePercent = String.format("%.2f", (changeValue / Double.parseDouble(price)) * 100);
        String volume = String.valueOf(random.nextInt(1000000) + 100000);
        boolean isUp = changeValue > 0;
        boolean isDown = changeValue < 0;

        return new StockPriceResponse(stockCode, name, price, change, changePercent, volume, isUp, isDown);
    }

    private String getStockNameByCode(String stockCode) {
        switch (stockCode) {
            case "005930": return "삼성전자";
            case "000660": return "SK하이닉스";
            case "035420": return "NAVER";
            case "051910": return "LG화학";
            case "035720": return "카카오";
            case "005380": return "현대차";
            default: return "Unknown";
        }
    }
}