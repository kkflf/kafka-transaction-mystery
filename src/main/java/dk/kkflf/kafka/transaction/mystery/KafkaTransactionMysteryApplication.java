package dk.kkflf.kafka.transaction.mystery;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.transaction.ChainedKafkaTransactionManager;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@EnableTransactionManagement
public class KafkaTransactionMysteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaTransactionMysteryApplication.class, args);
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public ChainedKafkaTransactionManager chainedTxM(JpaTransactionManager jpa, KafkaTransactionManager<?, ?> kafka) {
        kafka.rol
        return new ChainedKafkaTransactionManager(kafka, jpa);
    }

    @KafkaListener(topics = "trans-topic")
    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "chainedTxM", rollbackFor = Exception.class)
    public void listen(ConsumerRecord record) throws Exception {
        System.out.println(record.value());
        if (true) {
            throw new Exception("Force rollback");
        }
    }
}
