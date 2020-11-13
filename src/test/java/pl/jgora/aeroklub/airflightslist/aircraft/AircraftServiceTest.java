package pl.jgora.aeroklub.airflightslist.aircraft;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

@Slf4j
class AircraftServiceTest {
    private static AircraftService testingObject;
    private static AircraftRepository aircraftRepository;

    @BeforeAll
    static void setUp() {
        aircraftRepository = mock(AircraftRepository.class);
        testingObject = new AircraftService(aircraftRepository);
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void activationUpdate() {
    }

    @Test
    void update() {
    }

    @Test
    void filteredAircrafts() {
    }

    @Test
    void getEngineAircrafts() {
    }

    @Test
    void getGliders() {
    }
}