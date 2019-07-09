package dev.renansouza.document;

import java.util.Arrays;

enum DocumentFlow {
    EMISSION(0), RECEIPT(1);

    private final int value;

    DocumentFlow(int value) {
        this.value = value;
    }

    static DocumentFlow valueOf(int value) {
        return Arrays.stream(values())
                .filter(flow -> flow.value == value)
                .findFirst().get();
    }
}