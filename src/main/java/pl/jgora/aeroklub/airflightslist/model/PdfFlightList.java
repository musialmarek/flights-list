package pl.jgora.aeroklub.airflightslist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PdfFlightList {
    //TODO extend for glider flights
    private List<EngineFlight> flights = new ArrayList<>();
}
