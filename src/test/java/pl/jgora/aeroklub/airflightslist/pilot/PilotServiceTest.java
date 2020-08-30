package pl.jgora.aeroklub.airflightslist.pilot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class PilotServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldReturnListOfPilotsOrderedByName() {
        //given
        List<Pilot> mockPilots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mockPilots.add(new Pilot());
        }
        PilotRepository pilotRepository = Mockito.mock(PilotRepository.class);
        PilotService testingObject = new PilotService(pilotRepository);

        Mockito.when(testingObject.findAll()).thenReturn(mockPilots);
        //when
        List<Pilot> allPilots = testingObject.findAll();
        //then
        assertThat(allPilots.size(),is(mockPilots.size()));

    }
}