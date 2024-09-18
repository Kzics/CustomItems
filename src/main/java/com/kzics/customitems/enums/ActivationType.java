package com.kzics.customitems.enums;

public enum ActivationType {
    RIGHT_CLICK("Right-Click"),
    HIT("Hit"),
    LEFT_CLICK("Left-Click");

    private final String formatted;
    ActivationType(final String formatted){
        this.formatted = formatted;
    }

    public String getFormatted() {
        return formatted;
    }
}
