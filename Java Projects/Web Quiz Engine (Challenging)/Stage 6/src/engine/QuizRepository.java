package engine;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends PagingAndSortingRepository<QuizEntity, Integer> {
    Optional<QuizEntity> findById(Integer id);
    List<QuizEntity> findAll();
}
