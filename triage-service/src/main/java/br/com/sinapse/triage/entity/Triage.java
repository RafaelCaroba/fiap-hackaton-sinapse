package br.com.sinapse.triage.entity;

import br.com.sinapse.triage.enums.Priority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SourceType;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "triages", schema = "triage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Triage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false, length = 11)
    private String cpf;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<String> symptoms;

    @Column(nullable = false)
    private Integer systolicPressure;

    @Column(nullable = false)
    private Integer diastolicPressure;

    @Column(nullable = false)
    private Integer heartRate;

    @Column(nullable = false)
    private Integer respiratoryRate;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Integer oxygenSaturation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @CreationTimestamp(source = SourceType.VM)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
