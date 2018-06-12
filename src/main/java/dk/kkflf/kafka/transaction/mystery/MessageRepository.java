package dk.kkflf.kafka.transaction.mystery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository

public class MessageRepository {

    @Autowired
    EntityManager em;

    @Transactional(propagation = Propagation.MANDATORY, transactionManager = "chainedTxM")
    public void save(Message message){
        em.persist(message);
    }

    public Message findById(Long id){
        return em.find(Message.class, id);
    }
}
