package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "ClientEntity")
@Getter @Setter
public class UserEntity {

    @Id
    private String id;

    private String name;

    private String email;

    private String password;

    private String phone;

    private String address;

    private String cpf;

    private CartEntity cart;

    private List<OrderEntity> ordersDone = new ArrayList<>();

    private String roles;

}
