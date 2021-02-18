package pl.jgora.aeroklub.airflightslist.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String address;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate medicine;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate theory;
    @Column(name = "glider_practise")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate gliderPractise;
    @Column(name = "engine_practise")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate enginePractise;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sepl;
    @Column(name = "fi_s")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fis;
    @Column(name = "fi_a")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fia;
    private Boolean nativeMember;
}
