package io.glitchtech.kafka.jsonposgen;

import io.glitchtech.kafka.jsonposgen.services.KafkaProducerService;
import io.glitchtech.kafka.jsonposgen.services.datagenerator.InvoiceGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class JsonposgenApplication implements ApplicationRunner {
    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    private InvoiceGenerator invoiceGenerator;
    @Value("${application.configs.invoice.count}")
    private int INVOICE_COUNT;

    public static void main(String[] args) {
        SpringApplication.run(JsonposgenApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < INVOICE_COUNT; i++) {
            kafkaProducerService.sendMessage(invoiceGenerator.getNextInvoice());
            Thread.sleep(1000);
        }
    }
}
