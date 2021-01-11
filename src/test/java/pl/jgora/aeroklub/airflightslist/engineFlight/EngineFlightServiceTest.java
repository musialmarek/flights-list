package pl.jgora.aeroklub.airflightslist.engineFlight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.jgora.aeroklub.airflightslist.abstractFlight.FlightTestBase;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.pilot.PilotTestBase;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

class EngineFlightServiceTest {
    private EngineFlightService testingObject;
    private EngineFlightRepository engineFlightRepository;

    @BeforeEach
    void setUp() {
        engineFlightRepository = mock(EngineFlightRepository.class);
        testingObject = new EngineFlightService(engineFlightRepository);
    }

    static Stream<Arguments> searchingFlightsByDateArgumentsProvider() {
        return Stream.of(
                arguments(LocalDate.of(2020, 11, 15)),
                arguments(LocalDate.of(2019, 8, 16)),
                arguments(LocalDate.of(2018, 5, 22)),
                arguments(LocalDate.of(2017, 2, 28)),
                arguments(LocalDate.of(2021, 1, 1))
        );
    }

    @ParameterizedTest(name = "should searching in date {0}")
    @MethodSource("searchingFlightsByDateArgumentsProvider")
    void shouldSearchingFlightsByDate(LocalDate date) {
        testingObject.getByDate(date);

        verify(engineFlightRepository, times(1)).getDistinctByDateOrderByStart(date);
    }

    @Test
    void shouldSaveFlight() {
        //given
        Pilot pilot = PilotTestBase.builder().withId(1L).build();
        EngineFlight flight = (EngineFlight) FlightTestBase
                .builder("engine")
                .withPic(pilot)
                .withInstructor(false)
                .build();
        //when
        testingObject.save(flight);
        //then
        verify(engineFlightRepository, times(1)).save(flight);

    }

    //TODO parametrized test
    void shouldFindById() {
        //given
        Long id = 1L;
        //when
        testingObject.getById(id);
        //then
        verify(engineFlightRepository, times(1)).findFirstById(id);
        //TODO
    }

    @Test
    void update() {
        //TODO
    }

    @Test
    void getDatesAndActives() {
        //TODO
    }

    @Test
    void getByPilot() {
        //TODO
    }

    @Test
    void getFilteredEngineFlightsByPilot() {
        //TODO
    }

    @Test
    void getFilteredFlightsByAircraft() {
        //TODO
    }

    @Test
    void getAllByAircraft() {
        //TODO
    }
}