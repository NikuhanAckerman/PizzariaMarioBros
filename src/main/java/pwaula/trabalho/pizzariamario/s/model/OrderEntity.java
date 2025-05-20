package pwaula.trabalho.pizzariamario.s.model;

import ch.qos.logback.core.net.server.Client;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "OrderEntity")
@Getter @Setter
public class OrderEntity {

    @Id
    private String id;

    private ClientEntity client;

    private LocalDateTime timeOrderFinished;

}
