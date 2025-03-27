package cz.cuni.mff.java.utils;

import java.io.IOException;
import java.io.PrintWriter;
/**
 * The {@code DeleteOperation} class provides functionality for deleting questions or answers
 * from a JSON-based storage system. It also allows viewing the content before deletion.
 */
public class DeleteOperation {
    private String question_title;
    private String user_name;
    private boolean isQuestion;
    private PrintWriter writer;
    private JsonCreator jsonCreator = new JsonCreator();
    /**
     * Constructs a {@code DeleteOperation} instance.
     *
     * @param user_name the name of the user who created the question or answer
     * @param question_title the title of the question or the related question for an answer
     * @param writer a {@code PrintWriter} to output messages
     * @param isQuestion {@code true} if deleting a question, {@code false} if deleting an answer
     */
    public DeleteOperation(String user_name, String question_title, PrintWriter writer, boolean isQuestion)
    {
        this.user_name = user_name;
        this.question_title = question_title;
        this.writer = writer;
        this.isQuestion = isQuestion;
    }
    /**
     * Executes the delete action. If the specified question or answer exists, it will be removed.
     * Otherwise, an error message is printed.
     *
     * @throws IOException if an I/O error occurs during file operations
     */
    public void DeleteAction() throws IOException
    {

            if (!jsonCreator.isFoundQuestionOrAnswer(question_title, user_name, isQuestion)) {
                System.out.println("Question not found");
                writer.println("Delete failed.");
                writer.flush();
                return;
            }
            jsonCreator.RemoveList(question_title, user_name, isQuestion);
        writer.println("Successfully deleted.");
        writer.flush();
        System.out.println("You deleted this question.");
    }
    /**
     * Displays the content of the question or answer before deletion.
     * If the content does not exist, an appropriate message is displayed.
     *
     * @throws IOException if an I/O error occurs while retrieving content
     */

    public void SeeContent() throws IOException
    {
        if(isQuestion)
        {
            jsonCreator.ShowQuestion(writer, question_title);
        }
        else {
            var answer =  jsonCreator.ObtainListWithUserName(question_title, user_name, false);
            if(answer != null) {
                System.out.println("Found your answer.");
                jsonCreator.ShowAnswer(writer, answer);
            }
            else
            {
                System.out.println("No answer found.");
                writer.println("No answer found.");
                writer.println("END");
                writer.flush();
            }
        }
    }
}