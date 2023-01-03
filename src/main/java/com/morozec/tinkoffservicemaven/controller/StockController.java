package com.morozec.tinkoffservicemaven.controller;

import com.morozec.tinkoffservicemaven.model.Stock;
import com.morozec.tinkoffservicemaven.sevice.StockService;
import dto.FigiesDto;
import dto.StockPricesDto;
import dto.StocksDto;
import dto.TickersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker) {
        return stockService.getStockByTicker(ticker, "TQBR"); // TODO
    }

    @PostMapping("/stocks/getStocksByTickers")
    public StocksDto getStocksByTicker(@RequestBody TickersDto tickersDto) {
        return stockService.getStocksByTickers(tickersDto);
    }

    @PostMapping("/stocks/prices")
    public StockPricesDto getPrices(@RequestBody FigiesDto figiesDto) {
        return stockService.getPrices(figiesDto);
    }
}