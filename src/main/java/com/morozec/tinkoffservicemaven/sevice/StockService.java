package com.morozec.tinkoffservicemaven.sevice;

import com.morozec.tinkoffservicemaven.model.Stock;
import dto.*;

public interface StockService {
    Stock getStockByTicker(String ticker, String classCode);
    StocksDto getStocksByTickers(TickersDto tickers);
    StockPricesDto getPrices(FigiesDto figiesDto);
}

