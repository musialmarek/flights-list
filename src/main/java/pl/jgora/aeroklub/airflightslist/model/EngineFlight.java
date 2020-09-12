package pl.jgora.aeroklub.airflightslist.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "engine_flights")
@Getter
@Setter
public class EngineFlight extends AbstractFlight {
    private String crew;
    private Boolean tow;
}
