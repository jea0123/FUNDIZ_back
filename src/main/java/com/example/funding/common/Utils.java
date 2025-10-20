package com.example.funding.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;

public class Utils {
    public static int getPercentNow(int currAmount, int goalAmount) {
        if (goalAmount == 0) {
            return 0;
        } else {
            return (int) Math.floor((currAmount * 100.0) / goalAmount);
        }
    }

    private static String normalizeMetric(String metric) {
        return "revenue".equalsIgnoreCase(metric) ? "revenue" : "qty";
    }

    public static AnalyticsWindow resolveWindow(String period, String metric, ZoneId zone) {
        int monthsInt = monthsInt(period);
        YearMonth now = YearMonth.now(zone);
        LocalDate from = null, to = null;
        if (monthsInt > 0) {
            YearMonth startYm = now.minusMonths(monthsInt - 1);
            from = startYm.atDay(1);
            to = now.atEndOfMonth();
        }
        return new AnalyticsWindow(from, to, monthsInt, normalizeMetric(metric));
    }

    public static int monthsInt(String period) {
        return switch (period) {
            case "1m" -> 1;
            case "3m" -> 3;
            case "6m" -> 6;
            case "1y" -> 12;
            case "all" -> 0;
            default -> 6;
        };
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AnalyticsWindow {
        private LocalDate from;
        private LocalDate to;
        private Integer months;
        private String metric;
    }
}
