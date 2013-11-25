package com.theeste.etfcalculator2;

import org.joda.time.LocalDate;

/**
 * Created by ryan on 11/24/13.
 */


public abstract class Carrier {
    public abstract String getName();

    public abstract double getETF(LocalDate contractStartDate,
                                  LocalDate etfCheckDate,
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
        public double getETF(LocalDate contractStartDate,
                             LocalDate etfCheckDate,
                             boolean smartPhone) {
            return ETFCalculator.getAttETF(contractStartDate, etfCheckDate, smartPhone);
        }
    }

    public static class Verizon extends Carrier {

        @Override
        public String getName() {
            return "Verizon";
        }

        @Override
        public double getETF(LocalDate contractStartDate,
                             LocalDate etfCheckDate,
                             boolean smartPhone) {
            return ETFCalculator.getVerizonETF(contractStartDate, etfCheckDate, smartPhone);
        }
    }

    public static class Sprint extends Carrier {

        @Override
        public String getName() {
            return "Sprint";
        }

        @Override
        public double getETF(LocalDate contractStartDate,
                             LocalDate etfCheckDate,
                             boolean smartPhone) {
            return ETFCalculator.getSprintETF(contractStartDate, etfCheckDate, smartPhone);
        }
    }

    public static class TMobile extends Carrier {

        @Override
        public String getName() {
            return "T-Mobile";
        }

        @Override
        public double getETF(LocalDate contractStartDate,
                             LocalDate etfCheckDate,
                             boolean smartPhone) {
            return ETFCalculator.getTMobileETF(contractStartDate, etfCheckDate, smartPhone);
        }
    }

}