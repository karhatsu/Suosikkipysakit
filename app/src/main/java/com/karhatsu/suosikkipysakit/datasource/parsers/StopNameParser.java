package com.karhatsu.suosikkipysakit.datasource.parsers;

public class StopNameParser {
    public static String parse(String mode, String shortName) {
        switch (mode) {
            case "FERRY":
                return "⛴";
            case "SUBWAY":
                return "🚇";
            default:
                return shortName;
        }
    }
}
