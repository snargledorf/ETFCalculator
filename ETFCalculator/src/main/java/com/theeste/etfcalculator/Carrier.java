package com.theeste.etfcalculator;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

/**
 * Created by ryan on 11/24/13.
 */


public enum Carrier {
    ATT("At&t", 150, 325, 4, 10) {
        @Override
        public double calculateEtf(LocalDate endDate, boolean smartPhone) {
            double basePrice = getBasePrice();
            double reductionAmount = getMonthlyReduction();

            if (smartPhone) {
                basePrice = getBasePriceSmartphone();
                reductionAmount = getMonthlyReductionSmartphone();
            }

            return calculateAttVerizonETF(endDate, basePrice, reductionAmount);
        }
    },
    VERIZON("Verizon", 175, 325, 5, 10) {
        @Override
        public double calculateEtf(LocalDate endDate, boolean smartPhone) {
            double basePrice = getBasePrice();
            double reductionAmount = getMonthlyReduction();

            if (smartPhone) {
                basePrice = getBasePriceSmartphone();
                reductionAmount = getMonthlyReductionSmartphone();
            }

            return calculateAttVerizonETF(endDate, basePrice, reductionAmount);
        }
    },
    SPRINT("Sprint", 175, 325, 0, 0) {

        private static final double SPRINT_MINIMUM_SMARTPHONE = 100;
        private static final double SPRINT_MINIMUM = 50;

        @Override
        public double calculateEtf(LocalDate endDate, boolean smartPhone) {
            Months months = Months.monthsBetween(LocalDate.now(), endDate);
            double factor = months.getMonths();
            double sprintMinimum = SPRINT_MINIMUM;

            if (smartPhone) {
                if (months.isGreaterThan(Months.months(17))) {
                    return getBasePriceSmartphone();
                }
                factor = months.getMonths() * 2;
                sprintMinimum = SPRINT_MINIMUM_SMARTPHONE;
            } else if (months.isGreaterThan(Months.months(19))) {
                return getBasePrice();
            }

            if (months.isLessThan(Months.months(6))) {
                return sprintMinimum;
            }

            return factor * 10;
        }
    },
    TMOBILE("T-Mobile", 200, 200, 0, 0) {
        @Override
        public double calculateEtf(LocalDate endDate, boolean smartPhone) {
            Days days = Days.daysBetween(LocalDate.now(), endDate);

            if (days.isGreaterThan(Days.days(180))) {
                return 200;
            }

            if (days.isGreaterThan(Days.days(90)) && days.isLessThan(Days.days(181))) {
                return 100;
            }

            return 50;
        }
    };

    private final String name;
    private final int basePrice;
    private final int basePriceSmartphone;
    private final int monthlyReduction;
    private final int monthlyReductionSmartphone;

    public abstract double calculateEtf(LocalDate endDate, boolean smartPhone);

    private Carrier(String name,
                    int basePrice,
                    int basePriceSmartphone,
                    int monthlyReduction,
                    int monthlyReductionSmartphone) {
        this.name = name;
        this.basePrice = basePrice;
        this.basePriceSmartphone = basePriceSmartphone;
        this.monthlyReduction = monthlyReduction;
        this.monthlyReductionSmartphone = monthlyReductionSmartphone;
    }

    public String getName() {
        return name;
    }

    private static double calculateAttVerizonETF(LocalDate endDate,
                                                 double basePrice,
                                                 double reductionAmount) {

        Months monthsLeftOnContract = Months.monthsBetween(LocalDate.now(), endDate);

        if (monthsLeftOnContract.getMonths() < 0)
            return 0;

        int monthsIntoContract = Months.TWELVE
                .multipliedBy(2)
                .minus(monthsLeftOnContract)
                .minus(1) // Compensate for the first month
                .getMonths();

        if (monthsIntoContract < 0)
            monthsIntoContract = 0;

        double result = basePrice - (monthsIntoContract * reductionAmount);

        if (result < 0) {
            result = 0;
        }

        return result;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public int getBasePriceSmartphone() {
        return basePriceSmartphone;
    }

    public int getMonthlyReduction() {
        return monthlyReduction;
    }

    public int getMonthlyReductionSmartphone() {
        return monthlyReductionSmartphone;
    }

    @Override
    public String toString() {
        return getName();
    }
}