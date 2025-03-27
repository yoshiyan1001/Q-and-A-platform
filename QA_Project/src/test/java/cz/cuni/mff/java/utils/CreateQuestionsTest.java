package cz.cuni.mff.java.utils;
import org.junit.jupiter.api.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class CreateQuestionsTest {
    private CreateQuestions createQuestions;
    private ByteArrayOutputStream outputStream;
    private PrintWriter printWriter;
    private BufferedReader reader;

    @BeforeEach
    void setUp() {
        createQuestions = new CreateQuestions("Test Question", "TestUser");
        outputStream = new ByteArrayOutputStream();
        printWriter = new PrintWriter(outputStream, true);
    }

    @Test
    void testMakeQuestion() throws IOException {
        String input = "This is a test question.\nEND\n";
        reader = new BufferedReader(new StringReader(input));

        createQuestions.MakeQuestion(printWriter, reader);

        String output = outputStream.toString();
        assertTrue(output.contains("Enter question: Please write END when ended."));
        assertTrue(output.contains("Successfully created."));
    }
}
