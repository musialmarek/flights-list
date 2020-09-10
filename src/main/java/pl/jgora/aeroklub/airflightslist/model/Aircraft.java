package pl.jgora.aeroklub.airflightslist.model;

import jdk.jfr.Unsigned;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "aircrafts")
@Getter
@Setter
@NoArgsConstructor
@Slf4j
@ToString
@EqualsAndHashCode(of = {"id"})
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean active;
    @NotNull
    private Boolean engine;
    private String type;
    @Column(name = "registration_number", unique = true)
    private String registrationNumber;
    @Positive
    @Column(name = "flying_time")
    @Unsigned
    private Integer flyingTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arc;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate insuring;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "next_work_date")
    private LocalDate nextWorkDate;
    @Column(name = "next_work_date_description")
    private String nextWorkDateDescription;
    @Column(name = "next_work_time")
    @Unsigned
    private Integer nextWorkTime;
    @Column(name = "next_work_time_description")
    private String nextWorkTimeDescription;
    @Transient
    private Integer flyingTimeHours;
    @Transient
    private Integer flyingTimeMinutes;
    @Transient
    private Integer workTimeHours;
    @Transient
    private Integer workTimeMinutes;

    @PrePersist
    @PreUpdate
    private void parseTime() {
        flyingTime = flyingTimeHours * 60 + flyingTimeMinutes;
        nextWorkTime = workTimeHours * 60 + workTimeMinutes;
    }

    @PostLoad
    private void reParseTime() {
        flyingTimeHours = flyingTime / 60;
        flyingTimeMinutes = flyingTime % 60;
        workTimeHours = nextWorkTime / 60;
        workTimeMinutes = nextWorkTime % 60;
    }
}
