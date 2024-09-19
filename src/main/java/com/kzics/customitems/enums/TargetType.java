package com.kzics.customitems.enums;

public enum TargetType {
    SELF("You"),
    TARGET("Target"),
    AREA("Area");


    private final String formatted;
    TargetType(String formatted){
        this.formatted = formatted;
    }

    public String getFormatted() {
        return formatted;
    }
}
