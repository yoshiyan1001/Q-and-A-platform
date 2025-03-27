package cz.cuni.mff.java.utils;

import java.io.*;
import java.util.List;
import java.util.Map;

public class ShowAnswerOperation {
    private JsonCreator jsonCreator = new JsonCreator();
    private PrintWriter writer;
    private String question_title;
    public ShowAnswerOperation(String question_title, PrintWriter writer, BufferedReader reader)
    {
        this.question_title = question_title;
        this.writer = writer;
    }
    /*
    synchronized void PrintAnswer(String answer_with_user): Prints the content of a specific solution.
    The method reads the content of the answer file line by line and outputs it.
    If the file doesn't exist, it informs the user that no answer was accepted and ends the process.
     */
    public synchronized void PrintAnswer(Map<String, String> chosen_answer)
    {
        System.out.println("start of solution");
        try {
            jsonCreator.ShowAnswer(writer, chosen_answer);
        } catch (IOException e) {
        }
        System.out.println("End of solution");
    }
    /*
     Displays all available answers for the specific question. It checks if any solutions exist, and if they do,
     it lists them one by one and prints their content. If no solutions are found, it simply prints END.
     */
    public void ShowAnswer()
    {
        try {
            jsonCreator.ListQuestionsorAnswers(false);
            List<Map<String, String>> all_answers =jsonCreator.ListQuestionsorAnswers(false);;
            for(Map<String, String> answer : all_answers)
            {
                if(answer.get("question title").equals(question_title))
                {
                   PrintAnswer(answer);
                }
                else {
                    System.out.println("No answers found.");
                    writer.println("END");
                    writer.flush();

                }
            }
////            writer.println("END");
//            writer.flush();
        }
        catch (Exception e) {
            System.out.println(e);
            writer.println("END");
            writer.flush();
        }
    }
}
