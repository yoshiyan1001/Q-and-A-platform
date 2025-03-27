package cz.cuni.mff.java.utils;

import java.io.*;

public class AnswerOperation {//suppose each user has only one answer, should fix tomorrow

    private String question_title;
    private String user_name;
    public JsonCreator jsonCreator = new JsonCreator();
    private static PrintWriter writer;
    private static BufferedReader reader;

    public AnswerOperation(String user_name, String question_title, PrintWriter writer, BufferedReader reader) {
        this.user_name = user_name;
        this.question_title = question_title;
        this.writer = writer;
        this.reader = reader;

    }
    public void MakeAnswer() throws IOException {
        writer.println("Enter your Answer: Please write END when ended ");
        writer.flush();
        WriteAnswer(reader);
    }
    public synchronized void WriteAnswer(BufferedReader reader) throws IOException
    {
        System.out.println("Start of solution");
        String answers="";
        while (true) {
            String input = reader.readLine().trim();
            System.out.println(input);
            if (input.equals("END")) {
                writer.println("End of answer");
                writer.flush();
                System.out.println("End of answer");
                break;
            }
            answers += input + "\\n";
            System.out.println("writing"+ input);
        }
        jsonCreator.CreateAnswer(question_title, user_name, answers);//will fix it
        System.out.println("End of solution");
    }
}
