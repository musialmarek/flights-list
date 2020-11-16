package pl.jgora.aeroklub.airflightslist.aircraft;

import pl.jgora.aeroklub.airflightslist.model.Aircraft;

import java.time.LocalDate;

public class AircraftTestBase {
    static AircraftBuilder builder() {
        return new AircraftBuilder();
    }

    static class AircraftBuilder {
        private Long id;
        private Boolean active;
        private Boolean engine;
        private String type;
        private String registrationNumber;
        private Integer flyingTime;
        private LocalDate arc;
        private LocalDate insurance;
        private LocalDate nextWorkDate;
        private String nextWorkDateDescription;
        private Integer nextWorkTime;
        private String nextWorkTimeDescription;

        private Integer flyingTimeHours;
        private Integer flyingTimeMinutes;
        private Integer workTimeHours;
        private Integer workTimeMinutes;

        AircraftBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        AircraftBuilder withActive(Boolean active) {
            this.active = active;
            return this;
        }

        AircraftBuilder withEngine(Boolean engine) {
            this.engine = engine;
            return this;
        }

        AircraftBuilder withType(String type) {
            this.type = type;
            return this;
        }

        AircraftBuilder withRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        AircraftBuilder withFlyingTime(Integer flyingTime) {
            this.flyingTime = flyingTime;
            return this;
        }

        AircraftBuilder withArc(LocalDate arc) {
            this.arc = arc;
            return this;
        }

        AircraftBuilder withInsurance(LocalDate insurance) {
            this.insurance = insurance;
            return this;
        }

        AircraftBuilder withNextWorkDate(LocalDate nextWorkDate) {
            this.nextWorkDate = nextWorkDate;
            return this;
        }

        AircraftBuilder withNextWorkDateDescription(String nextWorkDateDescription) {
            this.nextWorkDateDescription = nextWorkDateDescription;
            return this;
        }

        AircraftBuilder withNextWorkTime(Integer nextWorkTime) {
            this.nextWorkTime = nextWorkTime;
            return this;
        }

        AircraftBuilder withNextWorkTimeDescription(String nextWorkTimeDescription) {
            this.nextWorkTimeDescription = nextWorkTimeDescription;
            return this;
        }

        AircraftBuilder withFlyingTimeHours(Integer flyingTimeHours) {
            this.flyingTimeHours = flyingTimeHours;
            return this;
        }

        AircraftBuilder withFlyingTimeMinutes(Integer flyingTimeMinutes) {
            this.flyingTimeMinutes = flyingTimeMinutes;
            return this;
        }

        AircraftBuilder withWorkTimeHours(Integer workTimeHours) {
            this.workTimeHours = workTimeHours;
            return this;
        }

        AircraftBuilder withWorkTimeMinutes(Integer workTimeMinutes) {
            this.workTimeMinutes = workTimeMinutes;
            return this;
        }

        Aircraft build() {
            return new Aircraft(id, active, engine, type, registrationNumber, flyingTime, arc, insurance, nextWorkDate, nextWorkDateDescription, nextWorkTime, nextWorkTimeDescription, flyingTimeHours, flyingTimeMinutes, workTimeHours, workTimeMinutes);
        }

    }
}
