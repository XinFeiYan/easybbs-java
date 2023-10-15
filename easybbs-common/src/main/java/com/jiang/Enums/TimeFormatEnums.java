package com.jiang.Enums;

public enum TimeFormatEnums {
    ISO_LOCAL_DATE_TIME_REPLACET2SPACE("yyyy-MM-dd HH:mm:ss"),
    ISO_LOCAL_DATE("yyyy-MM-dd");
    private final String format;

    TimeFormatEnums(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
