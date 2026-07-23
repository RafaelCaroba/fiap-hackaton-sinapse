package br.com.sinapse.triage.repository;

import br.com.sinapse.triage.entity.Triage;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriageRepository extends JpaRepository<Triage, UUID> {
}
