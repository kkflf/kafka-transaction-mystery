package dk.kkflf.kafka.transaction.mystery;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.transaction.ChainedKafkaTransactionManager;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@SpringBootApplication
@EnableTransactionManagement
public class KafkaTransactionMysteryApplication implements ApplicationRunner{

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(KafkaTransactionMysteryApplication.class, args);
    }

    @Bean
    @Primary
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public ChainedKafkaTransactionManager chainedTxM(JpaTransactionManager jpa, KafkaTransactionManager<?, ?> kafka) {
        return new ChainedKafkaTransactionManager(kafka, jpa);
    }

    @Autowired
    Foo foo;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        foo.findMessage();

            foo.sendToKafkaAndDB();

        System.out.println("Done executing");
//        foo.findMessage();
    }

    @Service
    public static class Foo {

        @Autowired
        private KafkaTemplate<Object, Object> template;

        @Autowired
        private MessageRepository messageRepository;

        @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "chainedTxM", rollbackFor = Throwable.class)
        public void sendToKafkaAndDB() throws Exception {
            System.out.println("Repo save: ");
            messageRepository.save(new Message("foo"));
            System.out.println("Kafka: " + this.template.send("foo", "bar"));
        }
//
//        public void findMessage(){
//            System.out.println("Repo get: " + messageRepository.findById(1l));
//        }

    }
}
