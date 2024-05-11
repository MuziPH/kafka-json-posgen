package io.glitchtech.kafka.jsonposgen.services.datagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.glitchtech.kafka.jsonposgen.model.DeliveryAddress;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Random;

@Service
public class AddressGenerator {
    private final Random random;
    private final DeliveryAddress[] addresses;

    public AddressGenerator() {
        final String DATAFILE = "src/main/resources/data/address.json";
        final ObjectMapper mapper;
        random = new Random();
        mapper = new ObjectMapper();
        try {
            addresses = mapper.readValue(new File(DATAFILE), DeliveryAddress[].class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private int getIndex() {
        return random.nextInt(100);
    }

    public DeliveryAddress getNextAddress() {
        return addresses[getIndex()];
    }

}
