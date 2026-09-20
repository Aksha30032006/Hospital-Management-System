package jar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jar.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}