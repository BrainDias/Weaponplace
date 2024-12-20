package org.weaponplace.products;

public enum Caliber {
    AK_47("7.62x39"),
    RIFLE_NATO("7.62x51"),
    RIFLE_SOVIET("7.62x54"),
    ACP(".45"),
    HEAVY_NATO(".50"),
    HEAVY_SOVIET("12.7x99"),
    PISTOL_NATO("9x19"),
    PISTOL_SOVIET("9x18"),
    AR_NATO("5.56x45"),
    AR_SOVIET("5.45x39");

    private final String caliber;

    Caliber(String caliber) {
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


