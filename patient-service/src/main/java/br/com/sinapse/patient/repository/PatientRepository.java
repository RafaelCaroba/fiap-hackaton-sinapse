package br.com.sinapse.patient.repository;

import br.com.sinapse.patient.domain.Patient;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
