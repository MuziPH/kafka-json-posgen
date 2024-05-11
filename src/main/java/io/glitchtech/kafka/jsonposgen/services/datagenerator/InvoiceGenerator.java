package io.glitchtech.kafka.jsonposgen.services.datagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.glitchtech.kafka.jsonposgen.model.DeliveryAddress;
import io.glitchtech.kafka.jsonposgen.model.LineItem;
import io.glitchtech.kafka.jsonposgen.model.PosInvoice;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Log4j2
public class InvoiceGenerator {
    private final Random invoiceIndex;
    private final Random invoiceNumber;
    private final Random numberOfItems;
    private final PosInvoice[] invoices;
    @Autowired
    AddressGenerator addressGenerator;
    @Autowired
    ProductGenerator productGenerator;

    public InvoiceGenerator() {
        String DATAFILE = "src/main/resources/data/Invoice.json";
        invoiceIndex = new Random();
        invoiceNumber = new Random();
        numberOfItems = new Random();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            invoices = objectMapper.readValue(new File(DATAFILE), PosInvoice[].class);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    private int getInvoiceIndex() {
        return invoiceIndex.nextInt(100);
    }

    private int getInvoiceNumber() {
        return invoiceNumber.nextInt(99999999) + 99999;
    }

    private int getNumberOfItems() {
        return numberOfItems.nextInt(4) + 1;
    }

    public PosInvoice getNextInvoice() {
        PosInvoice posInvoice = invoices[getInvoiceIndex()];
        posInvoice.setInvoiceNumber(Integer.toString(getInvoiceNumber()));
        posInvoice.setCreatedTime(System.currentTimeMillis());
        if (posInvoice.getDeliveryType().equalsIgnoreCase("HOME-DELIVERY")) {
            DeliveryAddress deliveryAddress = addressGenerator.getNextAddress();
            posInvoice.setDeliveryAddress(deliveryAddress);
        }
        int itemCount = getNumberOfItems();
        double totalAmount = 0.0;
        List<LineItem> items = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            LineItem item = productGenerator.getNextProduct();
            totalAmount += item.getTotalValue();
            items.add(item);
        }
        posInvoice.setNumberOfItems(itemCount);
        posInvoice.setInvoiceLineItems(items);
        posInvoice.setTotalAmount(totalAmount);
        posInvoice.setTaxableAmount(totalAmount);
        posInvoice.setCGST(totalAmount * 0.025);
        posInvoice.setSGST(totalAmount * 0.025);
        posInvoice.setCESS(totalAmount * 0.00125);
        log.debug(posInvoice);
        return posInvoice;
    }

}
