package com.example.tournamentservice.tmpservices;

public class SportDataProvider {

    public static class SportData {

        private int hoursPerMatch;
        private int availableCourts;

        public SportData(int hoursPerMatch, int availableCourts) {
            this.hoursPerMatch = hoursPerMatch;
            this.availableCourts = availableCourts;
        }

        public int getHoursPerMatch() {
            return hoursPerMatch;
        }

        public int getAvailableCourts() {
            return availableCourts;
        }
    }

    public static SportData getData(String sport) {
        return new SportData(3, 3);
    }
}
