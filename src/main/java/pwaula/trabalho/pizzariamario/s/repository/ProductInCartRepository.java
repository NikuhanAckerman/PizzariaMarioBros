package pwaula.trabalho.pizzariamario.s.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pwaula.trabalho.pizzariamario.s.model.ProductInCartEntity;

import java.util.List;

@Repository
public interface ProductInCartRepository extends MongoRepository<ProductInCartEntity, String> {
    List<ProductInCartEntity> getProductInCartEntitiesByCartId(String cartId);
}
