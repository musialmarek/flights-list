package pl.jgora.aeroklub.airflightslist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "glider_flights")
@Getter
@Setter
public class GliderFlight extends AbstractFlight {
    @OneToOne
    private EngineFlight engineFlight;
    @Column(name = "start_method")
    @Enumerated(value = EnumType.STRING)
    private StartMethod startMethod;
}
