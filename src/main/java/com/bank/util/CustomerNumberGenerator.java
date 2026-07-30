package com.bank.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for generating customer numbers.
 */
public final class CustomerNumberGenerator {

    private CustomerNumberGenerator() {
        // Prevent object creation
    }

    public static String generate() {

        String date = LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE);

        int randomNumber = ThreadLocalRandom.current()
                .nextInt(1000, 9999);

        return "CUST" + date + randomNumber;
    }

}