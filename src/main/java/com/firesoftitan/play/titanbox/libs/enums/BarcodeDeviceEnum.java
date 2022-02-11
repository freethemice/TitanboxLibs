package com.firesoftitan.play.titanbox.libs.enums;

public enum BarcodeDeviceEnum {
    NONE(0, "", false),
    INVALID(1, "Invalid", false),
    DUPPED(2, "Dupped", false),
    VALID(3, "Valid", true);

    private final String name;
    private final Integer value;
    private final Boolean good;
    BarcodeDeviceEnum(int value, String name, Boolean good) {
        this.name = name;
        this.value = value;
        this.good = good;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean isGood() {
        return good;
    }
}
