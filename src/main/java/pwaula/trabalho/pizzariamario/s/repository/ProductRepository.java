package pwaula.trabalho.pizzariamario.s.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pwaula.trabalho.pizzariamario.s.model.ProductCategory;
import pwaula.trabalho.pizzariamario.s.model.ProductEntity;
import java.util.List;


@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, String> {
    List<ProductEntity> findProductEntitiesByProductCategory(ProductCategory productCategory);
}
