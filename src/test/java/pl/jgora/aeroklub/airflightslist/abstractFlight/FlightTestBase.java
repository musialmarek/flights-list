package pl.jgora.aeroklub.airflightslist.abstractFlight;

import pl.jgora.aeroklub.airflightslist.model.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlightTestBase {
    public static FlightBuilder builder(String type) {
        return new FlightBuilder(type);
    }

   public static class FlightBuilder {


        private AbstractFlight flight;

        public FlightBuilder(String type) {
            if (type.equals("glider")) {
                this.flight = new GliderFlight();
            } else if (type.equals("engine")) {
                this.flight = new EngineFlight();
            } else {
                throw new IllegalArgumentException("unknown flight type " + type);
            }
        }
        public FlightBuilder withId(Long id){
            flight.setId(id);
            return this;
        }

        public FlightBuilder withActive(Boolean active) {
            flight.setActive(active);
            return this;
        }

        public FlightBuilder withRegistrationNumber(String registrationNumber) {
            flight.setAircraftRegistrationNumber(registrationNumber);
            return this;
        }

        public FlightBuilder withPic(Pilot pic) {
            flight.setPic(pic);
            return this;
        }

        public FlightBuilder withCopilot(Pilot copilot) {
            flight.setCopilot(copilot);
            return this;
        }

        public  FlightBuilder withPicName(String picName) {
            flight.setPicName(picName);
            return this;
        }

        public FlightBuilder withCopilotName(String copilotName) {
            flight.setCopilotName(copilotName);
            return this;
        }

        public FlightBuilder withDate(LocalDate date) {
            flight.setDate(date);
            return this;
        }

        public FlightBuilder withStart(LocalTime startTime) {
            flight.setStart(startTime);
            return this;
        }

        public FlightBuilder withTouchdown(LocalTime touchdownTime) {
            flight.setTouchdown(touchdownTime);
            return this;
        }

        public FlightBuilder withTask(String task) {
            flight.setTask(task);
            return this;
        }

        public FlightBuilder withInstructor(Boolean instructor) {
            flight.setInstructor(instructor);
            return this;
        }

        public FlightBuilder withAircraft(Aircraft aircraft) {
            flight.setAircraft(aircraft);
            return this;
        }

        public FlightBuilder withAircraftType(String aircraftType) {
            flight.setAircraftType(aircraftType);
            return this;
        }

        public AbstractFlight build() {
            return this.flight;
        }
    }
}
