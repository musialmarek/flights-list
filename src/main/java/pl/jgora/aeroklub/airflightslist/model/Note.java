package pl.jgora.aeroklub.airflightslist.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "note")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")

public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String number;
    @ManyToOne
    private Pilot payer;
    private Boolean paid;
    private Boolean active;
    private LocalDate date;
    private BigDecimal value;
}
