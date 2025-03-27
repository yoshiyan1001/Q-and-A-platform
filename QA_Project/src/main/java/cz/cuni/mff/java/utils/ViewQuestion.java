package cz.cuni.mff.java.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ViewQuestion {
    private String user_name;
    private String question;
    private PrintWriter writer;
    private static BufferedReader reader;
    private JsonCreator jc = new JsonCreator();
    public ViewQuestion(String user_name, String question, PrintWriter writer, BufferedReader reader) {
        this.user_name = user_name;
        this.question = question;
        this.writer = writer;
        this.reader = reader;
    }
    public void ShowQuestion() throws IOException
    {
        System.out.println("Start viewing question: " + question);
        jc.ShowQuestion(writer, question);
    }
    public boolean GoNextStep() throws IOException
    {
        System.out.println("asking if the user will answer the question.");
        writer.println("Do you want to answer it; write yes or no");
        String answer = reader.readLine();
        return answer.equalsIgnoreCase("yes");
    }

}
