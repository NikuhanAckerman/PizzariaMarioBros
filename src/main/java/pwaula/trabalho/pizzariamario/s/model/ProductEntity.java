package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "ProductEntity")
@Getter @Setter
public class ProductEntity {

    @Id
    private String id;

    private String name;

    private String description;

    private BigDecimal price;

    private String imageUrl;

    private ProductCategory productCategory;

    private boolean availableForProduction;

    public ProductEntity(String name,
                         String description,
                         BigDecimal price,
                         String imageUrl,
                         ProductCategory productCategory,
                         boolean availableForProduction) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.productCategory = ProductCategory.valueOf(productCategory.name());
        this.availableForProduction = availableForProduction;
    }

}
