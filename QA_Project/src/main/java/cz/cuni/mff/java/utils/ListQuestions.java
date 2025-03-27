package cz.cuni.mff.java.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/*
The ListQuestions class is a simple utility that lists all available questions in the QAoperations.directory.
Here's a breakdown of its functionality:
 */
public class ListQuestions {
    private JsonCreator jsonCreator = new JsonCreator();
    public  File directory = QAoperations.directory;
    public String question;
    public ListQuestions()
    {}
    public List<Map<String, String>> ShowAllQuesion()  {
        try {
            return jsonCreator.ListQuestionsorAnswers(true);

        }
        catch (IOException e) {
            return null; //error
        }

    }
}
