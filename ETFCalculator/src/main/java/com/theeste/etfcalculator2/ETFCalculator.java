package com.theeste.etfcalculator2;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

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

    public static double getVerizonETF(LocalDate contractStartDate, LocalDate etfCheckDate,
                                       boolean smartPhone) {

        double basePrice = VERIZON_BASE_PRICE;
        double reductionAmount = VERIZON_MONTHLY_REDUCTION;

        if (smartPhone) {
            basePrice = VERIZON_BASE_PRICE_SMARTPHONE;
            reductionAmount = VERIZON_MONTHLY_REDUCTION_SMARTPHONE;
        }

        return getAttVerizonETF(contractStartDate,
                etfCheckDate,
                basePrice,
                reductionAmount);
    }

    public static double getTMobileETF(LocalDate contractStartDate, LocalDate etfCheckDate,
                                       boolean smartPhone) {

        Days days = Days.daysBetween(contractStartDate, etfCheckDate);

        if (days.isGreaterThan(Days.days(180))) {
            return 200;
        }

        if (days.isGreaterThan(Days.days(90)) && days.isLessThan(Days.days(181))) {
            return 100;
        }

        return 50;
    }

    public static double getSprintETF(LocalDate contractStartDate, LocalDate etfCheckDate,
                                      boolean smartPhone) {

        Months months = Months.monthsBetween(contractStartDate, etfCheckDate);
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

    public static double getAttETF(LocalDate contractStartDate, LocalDate etfCheckDate,
                                   boolean smartPhone) {

        double basePrice = ATT_BASE_PRICE;
        double reductionAmount = ATT_MONTHLY_REDUCTION;

        if (smartPhone) {
            basePrice = ATT_BASE_PRICE_SMARTPHONE;
            reductionAmount = ATT_MONTHLY_REDUCTION_SMARTPHONE;
        }

        return getAttVerizonETF(contractStartDate,
                etfCheckDate,
                basePrice,
                reductionAmount);
    }

    private static double getAttVerizonETF(LocalDate contractStartDate,
                                           LocalDate etfCheckDate,
                                           double basePrice,
                                           double reductionAmount) {

        if (etfCheckDate.isBefore(contractStartDate)) {
            return 0;
        }

        Months months = Months.monthsBetween(contractStartDate, etfCheckDate);

        double result = basePrice - (months.getMonths() * reductionAmount);

        if (result < 0) {
            result = 0;
        }

        return result;
    }
}
