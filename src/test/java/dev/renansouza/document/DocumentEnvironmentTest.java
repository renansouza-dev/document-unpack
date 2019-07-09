package dev.renansouza.document;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentEnvironmentTest {

    @Test
    public void checkEnumValueOf() {
        assertEquals(DocumentEnvironment.valueOf("TEST"), DocumentEnvironment.TEST);
    }

    @Test
    public void checkEnumValue() {
        assertEquals(DocumentEnvironment.valueOf(0), DocumentEnvironment.TEST);
    }
}