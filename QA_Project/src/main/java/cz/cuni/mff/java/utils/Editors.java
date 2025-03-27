package cz.cuni.mff.java.utils;
import java.io.*;
import java.util.Map;
/*
The Editors class allows users to view and edit questions or answers.
It interacts with the JsonCreator class to retrieve and update stored data.
The class supports displaying content and modifying existing questions or
answers based on user permissions.
 */
public class Editors {
    public String question_title;
    public String answer;
    public boolean isQuestionEdit;
    private PrintWriter writer;
    private BufferedReader reader;
    private String user_name;
    private JsonCreator jsonCreator = new JsonCreator();
/*
Initializes the Editors instance with the provided user details, question title, and I/O streams.
 */
    public Editors(String _user_name,String question_title,  boolean isQuestionEdit, PrintWriter writer, BufferedReader reader)
    {
        user_name = _user_name;
        this.isQuestionEdit = isQuestionEdit;
        this.writer = writer;
        this.reader = reader;
        this.question_title = question_title;

    }

    public void ShowContent() throws IOException
    {

        jsonCreator.ShowQuestion(writer, question_title); //might be with user

        if(!isQuestionEdit)
        {
            Map<String, String> answer = jsonCreator.ObtainListWithUserName(question_title, user_name, false);
            if(answer==null)
            {
                System.out.println("No answer for question "+question_title);
            }
            else
            {
                System.out.println("Answer for question "+question_title);
                jsonCreator.ShowAnswer(writer, answer);
            }
        }

    }
/*
Allows the user to edit a question or an answer. If the user has permission:
The existing entry is removed from the JSON data.
A new question is created using CreateQuestions (if editing a question).
A new answer is created using AnswerOperation (if editing an answer).
If the user lacks permission, an error message is printed.
 */
    public void EditContent() throws IOException
    {
        if(isQuestionEdit)
        {
           Map<String, String> selected_question = jsonCreator.ObtainListWithUserName(question_title, user_name, true);

           if(CanEditIt(selected_question))
           {
               System.out.println("Can edit it");
               jsonCreator.RemoveList(selected_question.get("question title"), selected_question.get("user-name"), true);
               CreateQuestions createQuestions = new CreateQuestions(question_title, user_name);
               createQuestions.MakeQuestion(writer, reader);
           }
           else {
               System.out.println("Can't edit it");
               writer.println("Can't edit it");
           }
        }
        else {
            Map<String, String> selected_answer = jsonCreator.ObtainListWithUserName(question_title, user_name, false);
            if(CanEditIt(selected_answer)) {
                jsonCreator.RemoveList(selected_answer.get("question title"), selected_answer.get("user-name"), false);
                System.out.println("Can edit it");
                AnswerOperation answerOperation = new AnswerOperation(user_name, question_title, writer, reader);
                answerOperation.MakeAnswer();
            }
            else {
                System.out.println("Can't edit it");
                writer.println("Can't edit it");
            }
        }
        System.out.println("end of edit");
    }
    /*
    Checks whether the user has permission to edit a question or answer.
    If the selected_list is null, editing is denied and an error message is printed.
     */
    public boolean CanEditIt( Map<String, String> selected_list)
    {
        if(selected_list==null)
        {
            System.out.println("No permission for edit.");
            writer.println("You are not allowed to edit this question.");
            return false;
        }
        return true;
    }
}
