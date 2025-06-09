package pwaula.trabalho.pizzariamario.s.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pwaula.trabalho.pizzariamario.s.model.ClientEntity;

@Repository
public interface ClientRepository extends MongoRepository<ClientEntity, String> {
    ClientEntity findClientEntityByUserId(String userId);
}
