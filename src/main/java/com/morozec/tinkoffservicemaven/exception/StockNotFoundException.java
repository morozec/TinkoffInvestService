package com.morozec.tinkoffservicemaven.exception;

public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(String message) {super(message);}
}
