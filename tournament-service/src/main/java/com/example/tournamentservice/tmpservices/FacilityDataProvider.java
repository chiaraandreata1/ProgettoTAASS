package com.example.tournamentservice.tmpservices;

public class FacilityDataProvider {

    public static class FacilityData {

        private int openingHour, closingHour;

        public FacilityData(int openingHour, int closingHour) {
            this.openingHour = openingHour;
            this.closingHour = closingHour;
        }

        public int getOpeningHour() {
            return openingHour;
        }

        public int getClosingHour() {
            return closingHour;
        }
    }

    public static final FacilityData FACILITY_DATA = new FacilityData(15, 24);
}
