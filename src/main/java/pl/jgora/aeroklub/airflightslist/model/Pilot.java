package pl.jgora.aeroklub.airflightslist.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pilots")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = {"id"})
public class Pilot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String licence;
    private Boolean active;
    @Column(name = "glider_pilot")
    private Boolean gliderPilot;
    @Column(name = "glider_instructor")
    private Boolean gliderInstructor;
    @Column(name = "engine_pilot")
    private Boolean enginePilot;
    @Column(name = "engine_instructor")
    private Boolean engineInstructor;
    private Boolean tow;
    private LocalDate medicine;
    private LocalDate theory;
    @Column(name = "glider_practise")
    private LocalDate gliderPractise;
    @Column(name = "engine_practise")
    private LocalDate enginePractise;
    private LocalDate SEPL;
    @Column(name = "fi_s")
    private LocalDate FiS;
    @Column(name = "fi_a")
    private LocalDate FiA;

}
