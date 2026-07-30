package com.bank.util;

import java.util.UUID;

public class BranchCodeGenerator {

    private BranchCodeGenerator() {
    }

    public static String generate() {

        return "BR-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}