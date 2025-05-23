package pwaula.trabalho.pizzariamario.s.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pwaula.trabalho.pizzariamario.s.model.PizzaEntity;


@Repository
public interface PizzaRepository extends MongoRepository<PizzaEntity, String> {
}
