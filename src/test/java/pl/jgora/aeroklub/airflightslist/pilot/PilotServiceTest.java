package pl.jgora.aeroklub.airflightslist.pilot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class PilotServiceTest {
    private PilotService testingObject;
    private PilotRepository pilotRepository;

    @BeforeEach
    void setUp() {
        pilotRepository = mock(PilotRepository.class);
        testingObject = new PilotService(pilotRepository);
    }

    @Test
    void shouldFindingInDatabaseAllPilotsOrderByName() {
        testingObject.findAll();

        verify(pilotRepository, times(1)).findPilotsByOrderByName();
    }

    @Test
    void shouldFindingInDatabaseOnePilotById() {
        long aLong = anyLong();
        testingObject.findById(aLong);

        verify(pilotRepository, times(1)).findFirstById(aLong);

    }

    @Test
    void shouldSetActiveFalseAndSaveToDatabaseWhenActiveArgIsFalse() {
        //Given
        Pilot activePilot = PilotTestBase.builder().withActive(true).build();
        //When
        testingObject.activationUpdate(activePilot, false);
        //Then
        assertThat(activePilot.getActive()).isFalse();
        verify(pilotRepository, times(1)).save(activePilot);
    }

    @Test
    void shouldSetActiveTrueAndSaveToDatabaseWhenActiveArgIsTrue() {
        //Given
        Pilot inactivePilot = PilotTestBase.builder().withActive(false).build();
        //When
        testingObject.activationUpdate(inactivePilot, true);
        //Then
        assertThat(inactivePilot.getActive()).isTrue();
        verify(pilotRepository, times(1)).save(inactivePilot);
    }

    @Test
    void shouldFindingPilotByIdInDataBaseWhenUpdate() {
        //Given
        Pilot pilot = PilotTestBase.builder().build();
        //When
        testingObject.update(pilot);
        //Then
        verify(pilotRepository, times(1)).findFirstById(pilot.getId());
    }

    @Test
    void shouldGetPilotByIdFromDataBaseAndSaveWhenUpdateAndIdEquals() {
        //Given
        Pilot editedPilot = PilotTestBase.builder().withEngineInstructor(false).build();
        Pilot toEditPilot = PilotTestBase.builder().build();
        long id = toEditPilot.getId();
        Mockito.when(pilotRepository.findFirstById(id)).thenReturn(toEditPilot);
        //When
        testingObject.update(editedPilot);
        //then
        verify(pilotRepository, times(1)).save(toEditPilot);
    }

    @Test
    void shouldNotSaveWhenUpdateAndIdNotEquals() {
        //Given
        Pilot editedPilot = PilotTestBase.builder().withId(2L).withEngineInstructor(false).build();
        Pilot toEditPilot = PilotTestBase.builder().withId(3L).build();
        long id = toEditPilot.getId();
        Mockito.when(pilotRepository.findFirstById(id)).thenReturn(toEditPilot);
        //When
        testingObject.update(editedPilot);
        //then
        verify(pilotRepository, never()).save(toEditPilot);
    }


    @Test
    void shouldChangeDataInPilotToEditWhenUpdateAndIdNotEquals() {
        //Given
        Pilot editedPilot = PilotTestBase.builder()
                .withActive(false)
                .withEngineInstructor(false)
                .withFia(null)
                .withGliderPractise(LocalDate.now().plusYears(1))
                .withFis(LocalDate.now().plusYears(3))
                .withEnginePractise(LocalDate.now().plusYears(1))
                .withSepl(LocalDate.now().plusYears(2))
                .build();
        Pilot toEditPilot = PilotTestBase.builder().build();
        long id = toEditPilot.getId();
        Mockito.when(pilotRepository.findFirstById(id)).thenReturn(toEditPilot);
        //When
        testingObject.update(editedPilot);
        //then
        assertThat(editedPilot.getActive()).isEqualTo(toEditPilot.getActive());
        assertThat(editedPilot.getEngineInstructor()).isEqualTo(toEditPilot.getEngineInstructor());
        assertThat(editedPilot.getFia()).isEqualTo(toEditPilot.getFia());
        assertThat(editedPilot.getGliderPractise()).isEqualTo(toEditPilot.getGliderPractise());
        assertThat(editedPilot.getFis()).isEqualTo(toEditPilot.getFis());
        assertThat(editedPilot.getEnginePractise()).isEqualTo(toEditPilot.getEnginePractise());
        assertThat(editedPilot.getSepl()).isEqualTo(toEditPilot.getSepl());
    }

    @Test
    void filteredPilots() {
    }

    @Test
    void getEnginePilots() {
    }

    @Test
    void getEngineInstructors() {
    }

    @Test
    void getTowPilots() {
    }

    @Test
    void getGliderInstructors() {
    }

    @Test
    void getGliderPilots() {
    }
}