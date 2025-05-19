package pwaula.trabalho.pizzariamario.s.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pwaula.trabalho.pizzariamario.s.model.PizzaEntity;


public interface PizzaRepository extends MongoRepository<PizzaEntity, String> {

}
