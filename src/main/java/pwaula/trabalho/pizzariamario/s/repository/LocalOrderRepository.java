package pwaula.trabalho.pizzariamario.s.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pwaula.trabalho.pizzariamario.s.model.LocalOrderEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LocalOrderRepository extends MongoRepository<LocalOrderEntity, String> {
    List<LocalOrderEntity> findLocalOrderEntitiesByOrderedAtTimeAfter(LocalDateTime orderedAtTime);
}
