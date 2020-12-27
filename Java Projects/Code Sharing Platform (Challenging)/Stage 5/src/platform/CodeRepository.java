package platform;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeRepository extends CrudRepository<CodeEntity, Integer> {
    List<CodeEntity> findAllByOrderByDateDescCodeDesc();
    List<CodeEntity> findAll();
    Optional<CodeEntity> findByUUID(String UUID);
    void deleteByUUID(String UUID);
}
