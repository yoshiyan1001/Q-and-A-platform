package cz.cuni.mff.java.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class AnswerOperationTest {

    private AnswerOperation answerOperation;
    private PrintWriter writer;
    private StringWriter stringWriter;
    private BufferedReader reader;
    private JsonCreator jsonCreator;

    @BeforeEach
    void setUp() {
        // Using real JsonCreator
        jsonCreator = new JsonCreator();

        // Capture output
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter, true);
    }

    @Test
    void testMakeAnswer_NormalInput() throws IOException {
        // Simulate user input with multiple lines
        String input = "This is my answer.\nAnother line.\nEND\n";
        reader = new BufferedReader(new StringReader(input));
        String user_name = "test_user";
        String test_question = "test_question";
        answerOperation = new AnswerOperation(user_name, test_question, writer, reader);
        answerOperation.jsonCreator = jsonCreator; // Use real instance

        answerOperation.MakeAnswer();

        String output = stringWriter.toString();
        assertTrue(output.contains("Enter your Answer: Please write END when ended"));
        assertTrue(output.contains("End of answer"));
        String output_json = jsonCreator.GetAnswerOrQuestion(test_question, user_name, false);
        String expected_output = "\"question title\": \"test_question\",\"user-name\": \"test_user\",\"answer content\": \"This is my answer.\\nAnother line.\\n\"";//"Question Title: test_question user-name: test_user,answer content: This is my answer.\\nAnother line.\\n";
        assertEquals(expected_output, output_json);

    }

    @Test
    void testWriteAnswer_EmptyInput() throws IOException {

        reader = new BufferedReader(new StringReader("END\n"));
        String user_name = "test_empty";
        String test_question = "empty_question";
        answerOperation = new AnswerOperation(user_name, test_question, writer, reader);
        answerOperation.jsonCreator = jsonCreator;

        answerOperation.WriteAnswer(reader);
        String expected_output = "\"question title\": \"empty_question\",\"user-name\": \"test_empty\",\"answer content\": \"\"";
        String output = jsonCreator.GetAnswerOrQuestion(test_question, user_name, false);
        assertEquals(expected_output, output);

    }

    @Test
    void testWriteAnswer_HandlesWhitespace() throws IOException {
        reader = new BufferedReader(new StringReader("   \nHello World   \n \nEND\n"));

        answerOperation = new AnswerOperation("whitespace_user", "whitespace_question", writer, reader);
        answerOperation.jsonCreator = jsonCreator;
        String user_name = "whitespace_user";
        String test_question = "whitespace_question";
        answerOperation.WriteAnswer(reader);
        String expected_output = "\"question title\": \"whitespace_question\",\"user-name\": \"whitespace_user\",\"answer content\": \"\\nHello World\\n\\n\"";
        String output = jsonCreator.GetAnswerOrQuestion(test_question, user_name, false);
        assertEquals(expected_output, output);

    }
}
