package cz.cuni.mff.java.utils;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class JsonCreatorTest {
    private JsonCreator jsonCreator;
    private Path tempQuestionFile;
    private Path tempAnswerFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create temporary files for testing
        tempQuestionFile =  Files.createTempFile("answer", ".json");
        tempAnswerFile = Files.createTempFile("answer", ".json");
//
//        // Override static file references
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
    void testCreateQuestion() throws IOException {
        jsonCreator.CreateQuestion("Test Question", "Test User", "This is a test content.");

        List<Map<String, String>> questions = jsonCreator.ListQuestionsorAnswers(true);

        assertEquals("Test Question", questions.get(0).get("question title"));
        assertEquals("Test User", questions.get(0).get("user-name"));
        assertEquals("This is a test content.", questions.get(0).get("question content"));
    }

    @Test
    void testCreateAnswer() throws IOException {
        jsonCreator.CreateAnswer("Test Question", "Test User", "This is an answer.");
        List<Map<String, String>> answers = jsonCreator.ListQuestionsorAnswers(false);


        assertEquals("Test Question", answers.get(0).get("question title"));
        assertEquals("Test User", answers.get(0).get("user-name"));
        assertEquals("This is an answer.", answers.get(0).get("answer content"));
    }

    @Test
    void testRemoveQuestion() throws IOException {
        jsonCreator.CreateQuestion("Test Question", "Test User", "This is a test content.");
        jsonCreator.RemoveList("Test Question", "Test User", true);
        List<Map<String, String>> questions = jsonCreator.ListQuestionsorAnswers(true);

        assertTrue(questions.isEmpty());
    }

    @Test
    void testRemoveAnswer() throws IOException {
        jsonCreator.CreateAnswer("Test Question", "Test User", "This is an answer.");
        jsonCreator.RemoveList("Test Question", "Test User", false);
        List<Map<String, String>> answers = jsonCreator.ListQuestionsorAnswers(false);

        assertTrue(answers.isEmpty());
    }

    @Test
    void testShowQuestion() throws IOException {
        jsonCreator.CreateQuestion("Test Question", "Test User", "This is a test content.");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream, true);

        jsonCreator.ShowQuestion(writer, "Test Question");
        String output = outputStream.toString();

        assertTrue(output.contains("Question Title: Test Question"));
        assertTrue(output.contains("user-name: Test User"));
        assertTrue(output.contains("This is a test content."));
    }

    @Test
    void testShowAnswer() throws IOException {
        jsonCreator.CreateAnswer("Test Question", "Test User", "This is an answer.");
        Map<String, String> answer = jsonCreator.ObtainListWithUserName("Test Question", "Test User", false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream, true);
        jsonCreator.ShowAnswer(writer, answer);

        String output = outputStream.toString();
        assertTrue(output.contains("Publisher: Test User"));
        assertTrue(output.contains("This is an answer."));
    }
}
