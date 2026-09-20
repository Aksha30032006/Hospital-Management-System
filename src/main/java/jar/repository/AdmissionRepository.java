package jar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jar.entity.Admission;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {

}