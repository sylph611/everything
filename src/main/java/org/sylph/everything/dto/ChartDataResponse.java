package org.sylph.everything.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataResponse {
    private String code;
    private List<ChartPoint> data;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartPoint {
        private String date;
        private String price;
        private String volume;
    }
}