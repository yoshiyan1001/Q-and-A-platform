package cz.cuni.mff.java.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DeleteOperation class.
 */
class DeleteOperationTest {
    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;
    private JsonCreator jsonCreator;
    private DeleteOperation deleteOperation;
    private Path tempQuestionFile;
    private Path tempAnswerFile;

    @BeforeEach
    void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream, true);
        tempQuestionFile =  Files.createTempFile("answer", ".json");
        tempAnswerFile = Files.createTempFile("answer", ".json");

        JsonCreator.question_file = tempQuestionFile.toFile();
        JsonCreator.answer_file = tempAnswerFile.toFile();
        jsonCreator = new JsonCreator();
    }
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempQuestionFile);
        Files.deleteIfExists(tempAnswerFile);
    }

    @Test
    void testDeleteAction_SuccessfulDeletion() throws IOException {
        // Simulate existing question
        String questionTitle = "Test Question";
        String userName = "User1";
        deleteOperation = new DeleteOperation(userName, questionTitle, writer, true);

        // Assume the question exists
        jsonCreator.CreateQuestion(questionTitle, userName, "Sample content");

        deleteOperation.DeleteAction();

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Successfully deleted."));
    }

    @Test
    void testDeleteAction_Failure_NoQuestionFound() throws IOException {
        deleteOperation = new DeleteOperation("User1", "Nonexistent Question", writer, true);
        deleteOperation.DeleteAction();

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Delete failed."));
    }

    @Test
    void testSeeContent_ShowQuestion() throws IOException {
        String questionTitle = "Test Question";
        String userName = "User1";
        deleteOperation = new DeleteOperation(userName, questionTitle, writer, true);

        jsonCreator.CreateQuestion(questionTitle, userName, "Sample question content");
        deleteOperation.SeeContent();

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Question Title: Test Question"));
    }

    @Test
    void testSeeContent_ShowAnswer() throws IOException {
        String questionTitle = "Test Question";
        String userName = "User1";
        deleteOperation = new DeleteOperation(userName, questionTitle, writer, false);

        jsonCreator.CreateAnswer(questionTitle, userName, "Sample answer content");
        deleteOperation.SeeContent();

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Publisher: User1"));
    }

    @Test
    void testSeeContent_NoAnswerFound() throws IOException {
        deleteOperation = new DeleteOperation("User1", "Test Question", writer, false);
        deleteOperation.SeeContent();

        String output = outputStream.toString().trim();
        assertTrue(output.contains("No answer found."));
    }
}
