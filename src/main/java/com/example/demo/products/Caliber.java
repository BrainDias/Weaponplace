package com.example.demo.products;

public enum Caliber {
    AK_47("7.62x39"),
    RifleNATO("7.62x51"),
    RifleSoviet("7.62x54"),
    ACP(".45"),
    HeavyNATO(".50"),
    HeavySoviet("12.7x99"),
    PistolNATO("9x19"),
    PistolSoviet("9x18"),
    AR_NATO("5.56x45"),
    AR_Soviet("5.45x39");

    private final String caliber;

    private Caliber(String caliber) {
        this.caliber = caliber;
    }

    public String getCaliber() {
        return caliber;
    }

    @Override
    public String toString() {
        return caliber;
    }
}


