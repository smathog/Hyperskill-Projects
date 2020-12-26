package platform;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeRepository extends CrudRepository<CodeEntity, Integer> {
    Optional<List<CodeEntity>> findAllByOrderByDateAsc();
}
