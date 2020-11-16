package pl.jgora.aeroklub.airflightslist.aircraft;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@Slf4j
class AircraftServiceTest {
    private static AircraftService testingObject;
    private static AircraftRepository aircraftRepository;

    @BeforeEach
    void setUp() {
        aircraftRepository = mock(AircraftRepository.class);
        testingObject = new AircraftService(aircraftRepository);
    }

    @Test
    void shouldFindingInDatabaseAllAircraftsOrderByType() {
        testingObject.findAll();

        verify(aircraftRepository, times(1)).findAircraftsByOrderByType();
    }

    @Test
    void shouldFindingInDatabaseOneAircraftById() {
        long aLong = anyLong();
        testingObject.findById(aLong);

        verify(aircraftRepository, times(1)).findFirstById(aLong);
    }

    @Test
    void shouldSetActiveFalseAndSaveToDatabaseWhenActiveArgIsFalse() {
        //Given
        Aircraft activeAircraft = AircraftTestBase.builder().withActive(true).build();
        //When
        testingObject.activationUpdate(activeAircraft, false);
        //Then
        assertThat(activeAircraft.getActive()).isFalse();
        verify(aircraftRepository, times(1)).save(activeAircraft);
    }

    @Test
    void shouldSetActiveTrueAndSaveToDatabaseWhenActiveArgIsTrue() {
        //Given
        Aircraft inactiveAircraft = AircraftTestBase.builder().withActive(false).build();
        //When
        testingObject.activationUpdate(inactiveAircraft, true);
        //Then
        assertThat(inactiveAircraft.getActive()).isTrue();
        verify(aircraftRepository, times(1)).save(inactiveAircraft);
    }

    @Test
    void shouldFindingAircraftByIdInDataBaseWhenUpdate() {
        //Given
        Aircraft aircraft = AircraftTestBase.builder().build();
        //When
        testingObject.update(aircraft);
        //Then
        verify(aircraftRepository, times(1)).findFirstById(aircraft.getId());
    }

    @Test
    void shouldGetAircraftByIdFromDataBaseAndSaveWhenUpdateAndIdEquals() {
        //Given
        Aircraft editedAircraft = AircraftTestBase.builder().build();
        Aircraft toEditAircraft = AircraftTestBase.builder().build();
        long id = toEditAircraft.getId();
        Mockito.when(aircraftRepository.findFirstById(id)).thenReturn(toEditAircraft);
        //When
        testingObject.update(editedAircraft);
        //then
        verify(aircraftRepository, times(1)).save(toEditAircraft);
    }

    @Test
    void shouldNotSaveWhenUpdateAndIdNotEquals() {
        //Given
        Aircraft editedAircraft = AircraftTestBase.builder().withId(2L).build();
        Aircraft toEditAircraft = AircraftTestBase.builder().withId(3L).build();
        long id = toEditAircraft.getId();
        Mockito.when(aircraftRepository.findFirstById(id)).thenReturn(toEditAircraft);
        //When
        testingObject.update(editedAircraft);
        //then
        verify(aircraftRepository, never()).save(toEditAircraft);
    }


    @Test
    void shouldChangeDataInAircraftToEditWhenUpdateAndIdEquals() {
        //Given
        Aircraft editedAircraft = AircraftTestBase.builder()
                .withActive(false)
                .withArc(LocalDate.now().plusDays(365))
                .withNextWorkDate(LocalDate.now().plusDays(150))
                .withFlyingTime(1000)
                .withInsurance(LocalDate.now().plusDays(300))
                .withNextWorkTime(9000)
                .withNextWorkTimeDescription("50h")
                .build();
        Aircraft toEditAircraft = AircraftTestBase.builder().build();
        long id = toEditAircraft.getId();
        Mockito.when(aircraftRepository.findFirstById(id)).thenReturn(toEditAircraft);
        //When
        testingObject.update(editedAircraft);
        //then
        assertThat(editedAircraft.getActive()).isEqualTo(toEditAircraft.getActive());
        assertThat(editedAircraft.getArc()).isEqualTo(toEditAircraft.getArc());
        assertThat(editedAircraft.getNextWorkDate()).isEqualTo(toEditAircraft.getNextWorkDate());
        assertThat(editedAircraft.getFlyingTime()).isEqualTo(toEditAircraft.getFlyingTime());
        assertThat(editedAircraft.getInsurance()).isEqualTo(toEditAircraft.getInsurance());
        assertThat(editedAircraft.getNextWorkTime()).isEqualTo(toEditAircraft.getNextWorkTime());
        assertThat(editedAircraft.getNextWorkTimeDescription()).isEqualTo(toEditAircraft.getNextWorkTimeDescription());
    }

    @Test
    void shouldGetSetOfEngineAircraftsUsingAircraftRepository() {
        testingObject.getEngineAircrafts();

        verify(aircraftRepository, times(1)).findByEngineTrueOrderByType();
    }

    @Test
    void shouldGetSetOfGlidersUsingAircraftRepository() {
        testingObject.getGliders();

        verify(aircraftRepository, times(1)).findByEngineFalseOrderByType();
    }
}