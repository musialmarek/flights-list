package pl.jgora.aeroklub.airflightslist.engineFlight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

        verify(engineFlightRepository,times(1)).getDistinctByDateOrderByStart(date);
    }

    @Test
    void save() {
    }

    @Test
    void getById() {
    }

    @Test
    void update() {
    }

    @Test
    void getDatesAndActives() {
    }

    @Test
    void getByPilot() {
    }

    @Test
    void getFilteredEngineFlightsByPilot() {
    }

    @Test
    void getFilteredFlightsByAircraft() {
    }

    @Test
    void getAllByAircraft() {
    }
}