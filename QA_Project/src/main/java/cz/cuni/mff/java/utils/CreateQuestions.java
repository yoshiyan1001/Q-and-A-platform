package cz.cuni.mff.java.utils;
import java.io.*;
import java.io.IOException;

public class CreateQuestions {
    public String question;
    private String user_name;
    private JsonCreator jsonCreator = new JsonCreator();

    /**
     * Constructs a CreateQuestions instance with a specified question title and username.
     * @param question The title of the question
     * @param _user_name The username of the creator
     */
    public CreateQuestions(String question, String _user_name)
    {
        this.question = question;
        this.user_name = _user_name;

    }
    /**
     * Reads user input from a BufferedReader to create a question until "END" is entered.
     * The question is then stored in a JSON file using JsonCreator.
     *
     * @param print_writer The PrintWriter to output prompts and messages.
     * @param reader The BufferedReader to read user input.
     * @throws IOException If an input or output exception occurs.
     */
    public synchronized void MakeQuestion(PrintWriter print_writer, BufferedReader reader) throws IOException
    {
        print_writer.println("Enter question: Please write END when ended.");
        print_writer.flush();
        String content ="";
        while (true) {
            String input = reader.readLine();

            if(input.equals("END"))
            {
                System.out.println("It ended the question.");
                print_writer.println("Successfully created.");
                print_writer.flush();
                break;
            }
            content+=input+"\\n";
        }
        jsonCreator.CreateQuestion(question, user_name, content);//will fix it

    }
}
