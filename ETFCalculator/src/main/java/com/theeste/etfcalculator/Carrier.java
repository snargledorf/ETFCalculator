package com.theeste.etfcalculator;

import org.joda.time.LocalDate;

/**
 * Created by ryan on 11/24/13.
 */


public abstract class Carrier {

    public static final int CARRIER_ID_ATT = 1;
    public static final int CARRIER_ID_VERIZON = CARRIER_ID_ATT + 1;
    public static final int CARRIER_ID_SPRINT = CARRIER_ID_VERIZON + 1;
    public static final int CARRIER_ID_TMOBILE = CARRIER_ID_SPRINT + 1;

    public static final Carrier CARRIER_ATT = new Att();
    public static final Carrier CARRIER_VERIZON = new Verizon();
    public static final Carrier CARRIER_SPRINT = new Sprint();
    public static final Carrier CARRIER_TMOBILE = new TMobile();

    public abstract String getName();

    public abstract int getCarrierId();

    public abstract double calculateEtf(LocalDate startDate,
                                        LocalDate endDate,
                                        boolean smartPhone);

    @Override
    public String toString() {
        return getName();
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

    public static class Att extends Carrier {

        @Override
        public String getName() {
            return "At&t";
        }

        @Override
        public int getCarrierId() {
            return CARRIER_ID_ATT;
        }

        @Override
        public double calculateEtf(LocalDate startDate,
                                   LocalDate endDate,
                                   boolean smartPhone) {
            return ETFCalculator.calculateAttEtf(startDate, endDate, smartPhone);
        }
    }

    public static class Verizon extends Carrier {

        @Override
        public String getName() {
            return "Verizon";
        }

        @Override
        public int getCarrierId() {
            return CARRIER_ID_VERIZON;
        }

        @Override
        public double calculateEtf(LocalDate startDate,
                                   LocalDate endDate,
                                   boolean smartPhone) {
            return ETFCalculator.calculateVerizonEtf(startDate, endDate, smartPhone);
        }
    }

    public static class Sprint extends Carrier {

        @Override
        public String getName() {
            return "Sprint";
        }

        @Override
        public int getCarrierId() {
            return CARRIER_ID_SPRINT;
        }

        @Override
        public double calculateEtf(LocalDate startDate,
                                   LocalDate endDate,
                                   boolean smartPhone) {
            return ETFCalculator.calculateSprintEtf(startDate, endDate, smartPhone);
        }
    }

    public static class TMobile extends Carrier {

        @Override
        public String getName() {
            return "T-Mobile";
        }

        @Override
        public int getCarrierId() {
            return CARRIER_ID_TMOBILE;
        }

        @Override
        public double calculateEtf(LocalDate startDate,
                                   LocalDate endDate,
                                   boolean smartPhone) {
            return ETFCalculator.calculateTMobileEtf(startDate, endDate, smartPhone);
        }
    }

}