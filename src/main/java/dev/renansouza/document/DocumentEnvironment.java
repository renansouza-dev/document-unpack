package dev.renansouza.document;

import java.util.Arrays;

enum DocumentEnvironment {
    TEST(0), PROD(1);

    private final int value;

    DocumentEnvironment(int value) {
        this.value = value;
    }

    static DocumentEnvironment valueOf(int value) {
        return Arrays.stream(values())
                .filter(env -> env.value == value)
                .findFirst().get();
    }
}