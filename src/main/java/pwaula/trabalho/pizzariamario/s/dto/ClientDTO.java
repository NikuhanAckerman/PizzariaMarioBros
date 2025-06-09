package pwaula.trabalho.pizzariamario.s.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private String userId;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String cpf;

}
