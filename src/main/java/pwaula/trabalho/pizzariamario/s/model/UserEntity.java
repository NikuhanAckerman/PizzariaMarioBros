package pwaula.trabalho.pizzariamario.s.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Document(collection = "UserEntity")
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

    private String cartId;

    private List<String> ordersDoneId = new ArrayList<>();

    private String roles;

}
