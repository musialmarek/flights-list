package pl.jgora.aeroklub.airflightslist.abstractFlight;

import org.junit.jupiter.api.Test;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.pilot.PilotTestBase;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class AbstractFlightServiceTest {

    @Test
    void shouldUpdateFlightDataToDataFromNewFlight() {
//TODO create parametrized test with more data
        //given
        EngineFlight engineFlight = (EngineFlight) FlightTestBase.builder("engine")
                .withActive(true)
                .withPicName("Żwirko Franciszek")
                .withCopilotName("Wigura Stanisław")
                .withDate(LocalDate.of(1932, 8, 15))
                .withStart(LocalTime.of(12, 55))
                .withTouchdown(LocalTime.of(15, 33))
                .withRegistrationNumber("SP-AHN")
                .withAircraftType("RWD-6")
                .withTask("Chalange")
                .withInstructor(false)
                .build();
        EngineFlight newEngineFlight = (EngineFlight) FlightTestBase.builder("engine").withActive(false)
                .withPicName("Wigura stanisław")
                .withCopilotName("Żwirko Franciszak")
                .withDate(LocalDate.of(1932, 8, 12))
                .withStart(LocalTime.of(10, 33))
                .withTouchdown(LocalTime.of(16, 17))
                .withRegistrationNumber("SP-AHL")
                .withAircraftType("RWD6")
                .withTask("CHAL")
                .withInstructor(true)
                .build();
        //when
        AbstractFlightService.updateFlight(engineFlight, newEngineFlight);
        //then
        assertThat(engineFlight.getActive()).isEqualTo(newEngineFlight.getActive());
        assertThat(engineFlight.getDate()).isEqualTo(newEngineFlight.getDate());
        assertThat(engineFlight.getStart()).isEqualTo(newEngineFlight.getStart());
        assertThat(engineFlight.getTouchdown()).isEqualTo(newEngineFlight.getTouchdown());
        assertThat(engineFlight.getAircraftRegistrationNumber()).isEqualTo(newEngineFlight.getAircraftRegistrationNumber());
        assertThat(engineFlight.getAircraftType()).isEqualTo(newEngineFlight.getAircraftType());
        assertThat(engineFlight.getPicName()).isEqualTo(newEngineFlight.getPicName());
        assertThat(engineFlight.getCopilotName()).isEqualTo(newEngineFlight.getCopilotName());
        assertThat(engineFlight.getTask()).isEqualTo(newEngineFlight.getTask());
    }

    @Test
    void shouldReplacePilotsWhenGivenAnInstructorFlight() {
        //given
        Pilot instructor = PilotTestBase.builder().withId(1L).build();
        Pilot student = PilotTestBase.builder().withId(2L).build();
        GliderFlight gliderFlight = (GliderFlight) FlightTestBase.builder("glider")
                .withPic(student)
                .withCopilot(instructor)
                .withInstructor(true)
                .build();
        //when
        AbstractFlightService.replacePilots(gliderFlight);
        //then
        assertThat(gliderFlight.getPic()).isEqualTo(instructor);
        assertThat(gliderFlight.getCopilot()).isEqualTo(student);
    }

    @Test
    void shouldNotReplacePilotsIfGivenNotInstructorFlight() {
        //given
        Pilot pic = PilotTestBase.builder().withId(1L).build();
        Pilot copilot = PilotTestBase.builder().withId(2L).build();
        EngineFlight engineFlight = (EngineFlight) FlightTestBase.builder("engine")
                .withInstructor(false)
                .withPic(pic)
                .withCopilot(copilot)
                .build();
        //when
        AbstractFlightService.replacePilots(engineFlight);
        //then
        assertThat(engineFlight.getPic()).isEqualTo(pic);
        assertThat(engineFlight.getCopilot()).isEqualTo(copilot);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfGivenInstructorFlightsAndThereIsNoOneOFPilots() {
        //given
        Pilot instructor = PilotTestBase.builder().withId(1L).build();
        EngineFlight engineFlight = (EngineFlight) FlightTestBase.builder("engine")
                .withInstructor(true)
                .withCopilot(instructor)
                .build();
        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AbstractFlightService.replacePilots(engineFlight));
    }
}