package org.sylph.everything.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceResponse {
    private String code;
    private String name;
    private String price;
    private String change;
    private String changePercent;
    private String volume;
    private boolean isUp;
    private boolean isDown;
}