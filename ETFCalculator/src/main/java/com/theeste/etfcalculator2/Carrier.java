package com.theeste.etfcalculator2;

import org.joda.time.LocalDate;

/**
 * Created by ryan on 11/24/13.
 */


public abstract class Carrier {
    public abstract String getName();

    public abstract double getETF(LocalDate startDate,
                                  LocalDate endDate,
                                  boolean smartPhone);

    @Override
    public String toString() {
        return getName();
    }

    public static class Att extends Carrier {

        @Override
        public String getName() {
            return "At&t";
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
        public double getETF(LocalDate startDate,
                             LocalDate endDate,
                             boolean smartPhone) {
            return ETFCalculator.getTMobileETF(startDate, endDate, smartPhone);
        }
    }

}