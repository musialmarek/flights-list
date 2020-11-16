package pl.jgora.aeroklub.airflightslist.aircraft;

import pl.jgora.aeroklub.airflightslist.model.Aircraft;

import java.time.LocalDate;

public class AircraftTestBase {
    static AircraftBuilder builder() {
        return new AircraftBuilder();
    }

    static class AircraftBuilder {
        private Long id = 0L;
        private Boolean active = true;
        private Boolean engine = true;
        private String type = "PZL-1";
        private String registrationNumber = "SP-AAA";
        private Integer flyingTime = 0;
        private LocalDate arc = LocalDate.now().plusDays(100);
        private LocalDate insurance = LocalDate.now().plusDays(100);
        private LocalDate nextWorkDate = LocalDate.now().plusDays(100);
        private String nextWorkDateDescription = "";
        private Integer nextWorkTime = 6000;
        private String nextWorkTimeDescription = "";

        private Integer flyingTimeHours = flyingTime / 60;
        private Integer flyingTimeMinutes = flyingTime % 60;
        private Integer workTimeHours = nextWorkTime / 60;
        private Integer workTimeMinutes = nextWorkTime % 60;

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
            this.flyingTimeHours = flyingTime / 60;
            this.flyingTimeMinutes = flyingTime % 60;
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
            this.workTimeHours = nextWorkTime / 60;
            this.workTimeMinutes = nextWorkTime % 60;
            return this;
        }

        AircraftBuilder withNextWorkTimeDescription(String nextWorkTimeDescription) {
            this.nextWorkTimeDescription = nextWorkTimeDescription;
            return this;
        }

        Aircraft build() {
            return new Aircraft(id, active, engine, type, registrationNumber, flyingTime, arc, insurance, nextWorkDate, nextWorkDateDescription, nextWorkTime, nextWorkTimeDescription, flyingTimeHours, flyingTimeMinutes, workTimeHours, workTimeMinutes);
        }

    }
}
