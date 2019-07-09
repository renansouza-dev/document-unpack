package dev.renansouza.document;

import com.github.jknack.handlebars.internal.antlr.dfa.DFA;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentFlowTest {

    @Test
    public void checkEnumValueOf() {
        assertEquals(DocumentFlow.valueOf("EMISSION"), DocumentFlow.EMISSION);
    }

    @Test
    public void checkEnumValue() {
        assertEquals(DocumentFlow.valueOf(0), DocumentFlow.EMISSION);
    }
}