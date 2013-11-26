package com.theeste.etfcalculator2;

import org.joda.time.LocalDate;

/**
 * Created by ryan on 11/24/13.
 */


public abstract class Carrier {

    public static final int CARRIER_ID_ATT = 1;
    public static final int CARRIER_ID_VERIZON = CARRIER_ID_ATT + 1;
    public static final int CARRIER_ID_SPRINT = CARRIER_ID_VERIZON + 1;
    public static final int CARRIER_ID_TMOBILE = CARRIER_ID_SPRINT + 1;

    public abstract String getName();

    public abstract int getCarrierId();

    public abstract double getETF(LocalDate startDate,
                                  LocalDate endDate,
                                  boolean smartPhone);

    @Override
    public String toString() {
        return getName();
    }

    public static Carrier getCarrierInstance(int carrier) {
        switch (carrier) {
            case CARRIER_ID_ATT:
                return new Att();
            case CARRIER_ID_VERIZON:
                return new Verizon();
            case CARRIER_ID_SPRINT:
                return new Sprint();
            case CARRIER_ID_TMOBILE:
                return new TMobile();
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
        public double getETF(LocalDate startDate,
                             LocalDate endDate,
                             boolean smartPhone) {
            return ETFCalculator.getAttETF(startDate, endDate, smartPhone);
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
        public double getETF(LocalDate startDate,
                             LocalDate endDate,
                             boolean smartPhone) {
            return ETFCalculator.getVerizonETF(startDate, endDate, smartPhone);
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
        public double getETF(LocalDate startDate,
                             LocalDate endDate,
                             boolean smartPhone) {
            return ETFCalculator.getSprintETF(startDate, endDate, smartPhone);
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
        public double getETF(LocalDate startDate,
                             LocalDate endDate,
                             boolean smartPhone) {
            return ETFCalculator.getTMobileETF(startDate, endDate, smartPhone);
        }
    }

}