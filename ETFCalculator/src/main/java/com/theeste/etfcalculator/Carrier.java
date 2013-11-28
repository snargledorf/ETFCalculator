package com.theeste.etfcalculator;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

/**
 * Created by ryan on 11/24/13.
 */


public abstract class Carrier {

    public static final int CARRIER_ID_ATT = 1;
    public static final int CARRIER_ID_VERIZON = CARRIER_ID_ATT + 1;
    public static final int CARRIER_ID_SPRINT = CARRIER_ID_VERIZON + 1;
    public static final int CARRIER_ID_TMOBILE = CARRIER_ID_SPRINT + 1;

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

    public abstract String getName();
    public abstract int getCarrierId();
    public abstract double calculateEtf(LocalDate endDate, boolean smartPhone);

    @Override
    public String toString() {
        return getName();
    }

    public static final Carrier CARRIER_ATT = new Carrier() {
        @Override
        public String getName() {
            return "At&t";
        }

        @Override
        public int getCarrierId() {
            return CARRIER_ID_ATT;
        }

        @Override
        public double calculateEtf(LocalDate endDate, boolean smartPhone) {

            double basePrice = ATT_BASE_PRICE;
            double reductionAmount = ATT_MONTHLY_REDUCTION;

            if (smartPhone) {
                basePrice = ATT_BASE_PRICE_SMARTPHONE;
                reductionAmount = ATT_MONTHLY_REDUCTION_SMARTPHONE;
            }

            return getAttVerizonETF(endDate, basePrice, reductionAmount);
        }
    };

    public static final Carrier CARRIER_VERIZON = new Carrier() {

        @Override
        public String getName() {
            return "Verizon";
        }

        @Override
        public int getCarrierId() {
            return CARRIER_ID_VERIZON;
        }

        @Override
        public double calculateEtf(LocalDate endDate, boolean smartPhone) {

            double basePrice = VERIZON_BASE_PRICE;
            double reductionAmount = VERIZON_MONTHLY_REDUCTION;

            if (smartPhone) {
                basePrice = VERIZON_BASE_PRICE_SMARTPHONE;
                reductionAmount = VERIZON_MONTHLY_REDUCTION_SMARTPHONE;
            }

            return getAttVerizonETF(endDate, basePrice, reductionAmount);
        }
    };

    public static final Carrier CARRIER_SPRINT = new Carrier() {

        @Override
        public String getName() {
            return "Sprint";
        }

        @Override
        public int getCarrierId() {
            return CARRIER_ID_SPRINT;
        }

        @Override
        public double calculateEtf(LocalDate endDate, boolean smartPhone) {

            Months months = Months.monthsBetween(LocalDate.now(), endDate);
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
    };
    public static final Carrier CARRIER_TMOBILE = new Carrier() {

        @Override
        public String getName() {
            return "T-Mobile";
        }

        @Override
        public int getCarrierId() {
            return CARRIER_ID_TMOBILE;
        }

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

    private static double getAttVerizonETF(LocalDate endDate,
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

    public static Carrier getCarrier(int carrier) {
        switch (carrier) {
            case CARRIER_ID_ATT:
                return CARRIER_ATT;
            case CARRIER_ID_VERIZON:
                return CARRIER_VERIZON;
            case CARRIER_ID_SPRINT:
                return CARRIER_SPRINT;
            case CARRIER_ID_TMOBILE:
                return CARRIER_TMOBILE;
            default:
                throw new IllegalArgumentException("Invalid carrier id: " + carrier);
        }
    }
}