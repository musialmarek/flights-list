package pl.jgora.aeroklub.airflightslist.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "glider_flights")
@Getter
@Setter
public class GliderFlight extends AbstractFlight {
    @OneToOne
    @JoinColumn(unique = true)
    private EngineFlight engineFlight;
    @Column(name = "start_method")
    @Enumerated(value = EnumType.STRING)
    private StartMethod startMethod;
}
