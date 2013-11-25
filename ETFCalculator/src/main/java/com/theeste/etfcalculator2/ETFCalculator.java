package com.theeste.etfcalculator2;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.YearMonth;
import org.joda.time.Years;

/**
 * Created by Ryan on 11/23/13.
 */
public class ETFCalculator {

    public static final double ATT_BASE_PRICE_SMARTPHONE = 325;
    public static final double ATT_BASE_PRICE = 150;
    public static final double ATT_MONTHLY_REDUCTION_SMARTPHONE = 10;
    public static final double ATT_MONTHLY_REDUCTION = 4;

    public static final double VERIZON_BASE_PRICE_SMARTPHONE = 325;
    public static final double VERIZON_BASE_PRICE = 175;
    public static final double VERIZON_MONTHLY_REDUCTION_SMARTPHONE = 10;
    public static final double VERIZON_MONTHLY_REDUCTION = 5;

    public static final double SPRINT_BASE_PRICE_SMARTPHONE = 325;
    public static final double SPRINT_BASE_PRICE = 175;
    public static final double SPRINT_MINIMUM_SMARTPHONE = 100;
    public static final double SPRINT_MINIMUM = 50;

    public static double getVerizonETF(LocalDate startDate, LocalDate endDate,
                                       boolean smartPhone) {

        double basePrice = VERIZON_BASE_PRICE;
        double reductionAmount = VERIZON_MONTHLY_REDUCTION;

        if (smartPhone) {
            basePrice = VERIZON_BASE_PRICE_SMARTPHONE;
            reductionAmount = VERIZON_MONTHLY_REDUCTION_SMARTPHONE;
        }

        return getAttVerizonETF(startDate,
                endDate,
                basePrice,
                reductionAmount);
    }

    public static double getTMobileETF(LocalDate startDate, LocalDate endDate,
                                       boolean smartPhone) {

        Days days = Days.daysBetween(startDate, endDate);

        if (days.isGreaterThan(Days.days(180))) {
            return 200;
        }

        if (days.isGreaterThan(Days.days(90)) && days.isLessThan(Days.days(181))) {
            return 100;
        }

        return 50;
    }

    public static double getSprintETF(LocalDate startDate, LocalDate endDate,
                                      boolean smartPhone) {

        Months months = Months.monthsBetween(startDate, endDate);
        double factor = months.getMonths();
        double sprintMinimum = SPRINT_MINIMUM;

        if (smartPhone) {
            if (months.isGreaterThan(Months.months(17))) {
                return SPRINT_BASE_PRICE_SMARTPHONE;
            }
            factor = months.getMonths() * 2;
            sprintMinimum = SPRINT_MINIMUM_SMARTPHONE;
        } else if (months.isGreaterThan(Months.months(19))) {
            return SPRINT_BASE_PRICE;
        }

        if (months.isLessThan(Months.months(6))) {
            return sprintMinimum;
        }

        return factor * 10;
    }

    public static double getAttETF(LocalDate startDate, LocalDate endDate,
                                   boolean smartPhone) {

        double basePrice = ATT_BASE_PRICE;
        double reductionAmount = ATT_MONTHLY_REDUCTION;

        if (smartPhone) {
            basePrice = ATT_BASE_PRICE_SMARTPHONE;
            reductionAmount = ATT_MONTHLY_REDUCTION_SMARTPHONE;
        }

        return getAttVerizonETF(startDate,
                endDate,
                basePrice,
                reductionAmount);
    }

    private static double getAttVerizonETF(LocalDate startDate,
                                           LocalDate endDate,
                                           double basePrice,
                                           double reductionAmount) {

        Months monthsLeftOnContract = Months.monthsBetween(startDate, endDate);

        if (monthsLeftOnContract.getMonths() <= 0)
            return 0;

        int monthsIntoContract = Months.TWELVE
                .multipliedBy(2)
                .minus(monthsLeftOnContract)
                .getMonths();

        if (monthsIntoContract < 0)
            monthsIntoContract = 0;

        double result = basePrice - (monthsIntoContract * reductionAmount);

        if (result < 0) {
            result = 0;
        }

        return result;
    }
}
