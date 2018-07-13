package dk.kkflf.kafka.transaction.mystery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
public class MessageRepository {

    @Autowired
    EntityManager em;

    //Mandatory to make sure this is never executed outside a transaction
    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void save(MyMessage message) {
//        em.persist(message);
        em.merge(message);
        System.out.println("Msg id: " + message.id);
    }

    public MyMessage findById(Long id) {
        return em.find(MyMessage.class, id);
    }
}
