package io.glitchtech.kafka.jsonposgen.services.datagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.glitchtech.kafka.jsonposgen.model.LineItem;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Random;

@Service
public class ProductGenerator {
    private final Random random;
    private final Random qty;
    private final LineItem[] products;

    public ProductGenerator() {
        String DATAFILE = "src/main/resources/data/products.json";
        ObjectMapper objectMapper = new ObjectMapper();
        // Generate random item from the array
        random = new Random();
        // Generate random quantity
        qty = new Random();
        try {
            // load the json product array into LineItem array
            products = objectMapper.readValue(new File(DATAFILE), LineItem[].class);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    private int getIndex() {
        return random.nextInt(100);
    }

    private int getQuantity() {
        return qty.nextInt(2) + 1;
    }

    public LineItem getNextProduct() {
        LineItem lineItem = products[getIndex()];
        lineItem.setItemQty(getQuantity());
        lineItem.setTotalValue(lineItem.getItemPrice() * lineItem.getItemQty());
        return lineItem;
    }
}
