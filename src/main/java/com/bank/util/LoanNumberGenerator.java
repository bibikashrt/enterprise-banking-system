package com.bank.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class LoanNumberGenerator {

    private static final String PREFIX = "LN";

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    private LoanNumberGenerator() {
    }

    public static String generate() {

        String date = LocalDate.now()
                .format(DATE_FORMAT);

        int randomNumber =
                ThreadLocalRandom.current()
                        .nextInt(1000, 10000);

        return PREFIX + date + randomNumber;
    }
}