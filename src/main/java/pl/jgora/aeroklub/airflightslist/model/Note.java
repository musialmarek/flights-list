package pl.jgora.aeroklub.airflightslist.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @Column(name = "payer_data")
    private String payerData;
    private Boolean paid;
    private Boolean active;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(name = "date_limit")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateLimit;
    private BigDecimal value;
    private BigDecimal paidValue;
    private String description;
    private String annotation;
    private NoteCategory category;

    @PrePersist
    @PreUpdate
    private void prePersist() {
        if (payer != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(payer.getName()).append("\n").append(payer.getAddress());
            this.payerData = sb.toString();
        }
        if (paidValue == null) {
            paidValue = new BigDecimal(0);
        }
        if (value.compareTo(paidValue) <= 0) {
            paid = true;
        }
    }
}

