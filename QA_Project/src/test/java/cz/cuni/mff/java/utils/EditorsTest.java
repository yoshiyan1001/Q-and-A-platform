package cz.cuni.mff.java.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EditorsTest {
    private Editors editors;
    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;
    private BufferedReader reader;
    private JsonCreator jsonCreator;
    private Path tempQuestionFile;
    private Path tempAnswerFile;

    @BeforeEach
    void setUp()  throws IOException {
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream, true);
        reader = new BufferedReader(new StringReader("New content\nEND\n"));
        tempQuestionFile =  Files.createTempFile("answer", ".json");
        tempAnswerFile = Files.createTempFile("answer", ".json");

        JsonCreator.question_file = tempQuestionFile.toFile();
        JsonCreator.answer_file = tempAnswerFile.toFile();
        jsonCreator = new JsonCreator();

        editors = new Editors("test_user", "test_question", true, writer, reader);
    }
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempQuestionFile);
        Files.deleteIfExists(tempAnswerFile);
    }

    @Test
    void testEditContentWhenUserHasPermission() throws IOException {
        Map<String, String> selectedQuestion = new HashMap<>();
        selectedQuestion.put("question title", "test_question");
        selectedQuestion.put("user-name", "test_user");

        assertTrue(editors.CanEditIt(selectedQuestion));
    }

    @Test
    void testEditContentWhenUserHasNoPermission() throws IOException {
        assertFalse(editors.CanEditIt(null));
        assertTrue(outputStream.toString().contains("You are not allowed to edit this question."));
    }
}
