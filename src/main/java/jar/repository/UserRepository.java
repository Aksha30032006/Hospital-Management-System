package jar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jar.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}