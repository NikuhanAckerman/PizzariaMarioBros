package pwaula.trabalho.pizzariamario.s.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientUpdateDTO {

    private String userId;

    private String name;

    private String password;

    private String email;

    private String phone;

    private String address;

    private String cpf;

}
