package com.morozec.tinkoffservicemaven.sevice;

import com.morozec.tinkoffservicemaven.exception.StockNotFoundException;
import com.morozec.tinkoffservicemaven.model.Currency;
import com.morozec.tinkoffservicemaven.model.Stock;
import dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.GetOrderBookResponse;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.core.InvestApi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {
    private final InvestApi investApi;

    @Async
    public CompletableFuture<Instrument> getInstrument(String ticker, String classCode) {
        var instrumentsService = investApi.getInstrumentsService();
        return instrumentsService.getInstrumentByTicker(ticker, classCode);
    }
    @Override
    public Stock getStockByTicker(String ticker, String classCode) {
        var cf = getInstrument(ticker, classCode);
        var instrument = cf.join();
        if (instrument == null) {
            throw new StockNotFoundException(String.format("Stock %S not found.", ticker));
        }
        return new Stock(
                instrument.getTicker(),
                instrument.getFigi(),
                instrument.getName(),
                instrument.getInstrumentType(),
                Currency.valueOf(instrument.getCurrency()),
                "TINKOFF"
        );
    }

    @Override
    public StocksDto getStocksByTickers(TickersDto tickers) {
        List<CompletableFuture<Instrument>> instruments = new ArrayList<>();
        tickers.getTickers().forEach(ticker -> instruments.add(getInstrument(ticker, "TQBR"))); // TODO
        List<Stock> stocks = instruments.stream()
                .map(CompletableFuture::join)
                .filter(instrument -> Objects.nonNull(instrument))
                .map(instrument -> new Stock(
                        instrument.getTicker(),
                        instrument.getFigi(),
                        instrument.getName(),
                        instrument.getInstrumentType(),
                        Currency.valueOf(instrument.getCurrency()),
                        "TINKOFF"
                ))
                .collect(Collectors.toList());
        return new StocksDto(stocks);
    }

    private double getDoubleFromQuotation(Quotation quotation) {
        var bd = quotation.getUnits() == 0 && quotation.getNano() == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(quotation.getUnits()).add(BigDecimal.valueOf(quotation.getNano(), 9));
        return bd.doubleValue();
    }

    @Async
    public CompletableFuture<GetOrderBookResponse> getOrderBookByFigi(String figi) {
        var orderBook = investApi.getMarketDataService();
        return orderBook.getOrderBook(figi, 1);
    }

    @Override
    public StockPricesDto getPrices(FigiesDto figiesDto) {
        List<CompletableFuture<GetOrderBookResponse>> orderBooks = new ArrayList<>();
        figiesDto.getFigies().forEach(figi -> orderBooks.add(getOrderBookByFigi(figi)));
        var prices = orderBooks.stream()
                .map(CompletableFuture::join)
                .map(orderBook -> new StockPriceDto(
                        orderBook.getFigi(),
                        getDoubleFromQuotation(orderBook.getLastPrice())
                ))
                .collect(Collectors.toList());
        return new StockPricesDto(prices);
    }
}

