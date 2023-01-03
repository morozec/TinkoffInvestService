package dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class StockPriceDto {
    private String figi;
    private Double price;
}
