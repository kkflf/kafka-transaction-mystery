package dk.kkflf.kafka.transaction.mystery;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Message {

    @Id
    long id;
    String name;

    public Message() {
    }

    public Message(String name, long id) {
        this.name = name;
        this.id = id;
    }
}
