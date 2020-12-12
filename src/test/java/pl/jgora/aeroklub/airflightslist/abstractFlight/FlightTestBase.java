package pl.jgora.aeroklub.airflightslist.abstractFlight;

import pl.jgora.aeroklub.airflightslist.model.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlightTestBase {
    static FlightBuilder builder(String type) {
        return new FlightBuilder(type);
    }

    static class FlightBuilder {


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

        FlightBuilder withActive(Boolean active) {
            flight.setActive(active);
            return this;
        }

        FlightBuilder withRegistrationNumber(String registrationNumber) {
            flight.setAircraftRegistrationNumber(registrationNumber);
            return this;
        }

        FlightBuilder withPic(Pilot pic) {
            flight.setPic(pic);
            return this;
        }

        FlightBuilder withCopilot(Pilot copilot) {
            flight.setCopilot(copilot);
            return this;
        }

        FlightBuilder withPicName(String picName) {
            flight.setPicName(picName);
            return this;
        }

        FlightBuilder withCopilotName(String copilotName) {
            flight.setCopilotName(copilotName);
            return this;
        }

        FlightBuilder withDate(LocalDate date) {
            flight.setDate(date);
            return this;
        }

        FlightBuilder withStart(LocalTime startTime) {
            flight.setStart(startTime);
            return this;
        }

        FlightBuilder withTouchdown(LocalTime touchdownTime) {
            flight.setTouchdown(touchdownTime);
            return this;
        }

        FlightBuilder withTask(String task) {
            flight.setTask(task);
            return this;
        }

        FlightBuilder withInstructor(Boolean instructor) {
            flight.setInstructor(instructor);
            return this;
        }

        FlightBuilder withAircraft(Aircraft aircraft) {
            flight.setAircraft(aircraft);
            return this;
        }

        FlightBuilder withAircraftType(String aircraftType) {
            flight.setAircraftType(aircraftType);
            return this;
        }

        AbstractFlight build() {
            return this.flight;
        }
    }
}
