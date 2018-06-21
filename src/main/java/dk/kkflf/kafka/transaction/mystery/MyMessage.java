package dk.kkflf.kafka.transaction.mystery;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MyMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    public MyMessage() {
    }

    public MyMessage(String name) {
        this.name = name;
    }
}
