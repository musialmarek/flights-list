package pl.jgora.aeroklub.airflightslist.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "notes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"payer", "account"})
@EqualsAndHashCode(of = "id")

public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String number;
    @ManyToOne
    private Pilot payer;
    @ManyToOne
    private Account account;
    private String payerData;
    private Boolean paid;
    private Boolean active;
    private LocalDate date;
    private BigDecimal value;
    private String description;

    @PrePersist
    @PreUpdate
    private void prePersist() {
        if (payer != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(payer.getName()).append("\n").append(payer.getAddress());
            this.payerData = sb.toString();
        }
    }
}

