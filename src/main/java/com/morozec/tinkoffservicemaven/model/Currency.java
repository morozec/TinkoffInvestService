package com.morozec.tinkoffservicemaven.model;

public enum Currency {
    rub("rub"),
    usd("usd"),
    eur("eur"),
    gbr("gbr"),
    hkd("hkd"),
    chf("chf"),
    jpy("jpy"),
    cny("cny");
//    try("try");

    private String currency;

    Currency(String currency) {this.currency = currency;}
    }

