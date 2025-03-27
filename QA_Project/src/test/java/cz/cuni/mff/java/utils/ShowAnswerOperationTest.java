package cz.cuni.mff.java.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ShowAnswerOperationTest {
    private ShowAnswerOperation showAnswerOperation;
    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream), true);
        showAnswerOperation = new ShowAnswerOperation("Test Question", writer, new BufferedReader(new StringReader("")));

    }

    @Test
    void testPrintAnswer() {
        Map<String, String> chosenAnswer = new HashMap<>();
        chosenAnswer.put("answer content", "This is a test answer.");

        showAnswerOperation.PrintAnswer(chosenAnswer);

        writer.flush();
        String output = outputStream.toString().trim();
        assertTrue(output.contains("End of solution"));
    }

    @Test
    void testShowAnswer_withNoAnswers() {
        showAnswerOperation.ShowAnswer();

        writer.flush();
        String output = outputStream.toString().trim();
        String expected_output = "Publisher: Test User\n" +
                "--------------\n" +
                "This is an answer.\n" +
                "--------------\n" +
                "End of solution\n" +
                "END\n" +
                "END";
        assertEquals(expected_output, output);
    }
}
