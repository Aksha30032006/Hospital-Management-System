package jar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jar.entity.Bed;

public interface BedRepository extends JpaRepository<Bed, Long> {

}