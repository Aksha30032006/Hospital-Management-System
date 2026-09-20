package jar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jar.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}