package dk.kkflf.kafka.transaction.mystery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@SpringBootApplication
@EnableTransactionManagement
public class KafkaTransactionMysteryApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(KafkaTransactionMysteryApplication.class, args);
        Map<String, PlatformTransactionManager> tms = ctx.getBeansOfType(PlatformTransactionManager.class);
        System.out.println(tms);
        ctx.close();
    }

    @Bean
    public ApplicationRunner runner(Foo foo) {
        return args -> foo.sendToKafkaAndDB();
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public ChainedTransactionManager chainedTxM(JpaTransactionManager jpa, KafkaTransactionManager<?, ?> kafka) {
        return new ChainedTransactionManager(kafka, jpa);
    }

    @Component
    public static class Foo {

        @Autowired
        private KafkaTemplate<Object, Object> template;

        @Autowired
        private MessageRepository messageRepository;

        @Transactional(transactionManager = "chainedTxM", rollbackFor = Exception.class)
        public void sendToKafkaAndDB() throws Exception {
            System.out.println("Repo save: " + messageRepository.save(new Message("foo", 1L)));
            System.out.println("Kafka first: " + this.template.send("foo", "bar"));
            System.out.println("Kafka second: " + this.template.send("foo", "baz"));
        }

    }
}
