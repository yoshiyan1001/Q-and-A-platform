package cz.cuni.mff.java.utils;

import java.io.*;
import java.util.List;
import java.util.Map;
/*
The QAoperations class handles various operations related to questions and answers in a Q&A platform.
It provides functionalities to create, view, edit, delete, and list questions and answers,
ensuring proper user interaction and data management.
 */
public class QAoperations {
    public String question;
    private String user_name;
    private PrintWriter writer;
    private BufferedReader reader;
    private JsonCreator jsonCreator = new JsonCreator();
    public static File directory = new File("/Users/yoshi/Documents/QA_Project/src/main/java/cz/cuni/mff/java/QABox"); //TODO change the path
/*
This constructor initializes an instance of QAoperations with a given username, input reader, and output writer. Ensures that the required directory exists.
 */
    public QAoperations(String _user_name,BufferedReader reader, PrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
        this.user_name = _user_name;
        if(!directory.exists()) {
            directory.mkdir();
        }
    }
/*
GetQuestion() gets the question user wrote.
 */
    public String GetQuestion() throws IOException {
        String question = reader.readLine();//scanner.nextLine();
        return question;
    }

    private void WriteAccepted()//write accept question to a user
    {
        writer.println("Accepted your question.");
        writer.flush();
    }
    private void WriteNoAccepted()// write not accepted question to a user
    {
        writer.println("No accepted your question.");
        writer.flush();

    }
    private boolean IsFoundAnswerWithUser(String question, String user_name) throws IOException // If we find the answer with the same user name, it returns true.
    {
        return jsonCreator.isFoundQuestionOrAnswer(question, user_name, false);
    }
    private boolean IsFoundQuestion(String question, String user_name) throws IOException//If we find the question in json file, it returns true.
    {
        return jsonCreator.isFoundQuestionOrAnswer(question, user_name, true);
    }
    /*
    ActionViewQuestion() prompts the user to enter a question title and attempts to retrieve and display the question if it exists.
     */
    public void ActionViewQuestion() throws IOException {
        System.out.println("view question command");
        writer.println("Write your question.");
        String question = GetQuestion();
        if(IsFoundQuestion(question, null)) {
            WriteAccepted();
            OperateViewQuestionAction(question);
        }
        else
        {
            WriteNoAccepted();
        }
    }
    /*
     ActionListQuestion() lists all available questions along with the username of their authors.
     */
    public void ActionListQuestion()throws IOException
    {
        ListQuestions list_q = new ListQuestions();
        List<Map<String, String>> questions = list_q.ShowAllQuesion();
        for (Map<String, String> question_with_user : questions)
        {
            System.out.println(question_with_user);
            String question_title = question_with_user.get("question title");
            String user_name = question_with_user.get("user-name");
            writer.println("Question: " + question_title + " , Publisher: " + user_name);
        }
        writer.println("END");
        writer.flush();
    }
    /*
    ActionCreateQuestion() prompts the user to enter a new question title and stores it if it is not already present.
     */
    public void ActionCreateQuestion() throws IOException {

        writer.println("Write your question title.");
        String question = GetQuestion();
        if(IsFoundQuestion(question, null))
        {
            WriteNoAccepted();
        }
        else {
            WriteAccepted();
            OperateQuestionActions(question);
        }
    }
    /*
    ActionCreateAnswer() prompts the user to enter a question title and allows them to provide an answer if the question exists and they have not answered it before.
     */
    public void ActionCreateAnswer() throws IOException
    {
        writer.println("Write your question title you want to answer.");
        question = GetQuestion();
        System.out.println(question);
        if(IsFoundQuestion(question, null) && !IsFoundAnswerWithUser(question, user_name))
        {
            WriteAccepted();
            OperateAnswerActions(question);
        }
        else {
            WriteNoAccepted();
        }
    }
    /*
    ActionShowAnswers() prompts the user to enter a question title and retrieves all answers associated with that question.
     */
    public void ActionShowAnswers() throws IOException
    {
        writer.println("Write your question title for which you want to see answers.");

        question = GetQuestion();//might be fixed
        System.out.println(question);
        if(IsFoundQuestion(question, null))
        {
            WriteAccepted();
            OperateShowAnswerActions(question);
        }
        else
        {
            WriteNoAccepted();
        }
    }
/*
ActionEdit() allows the user to edit either a question or an answer, based on the provided boolean parameter.
 */
    public void ActionEdit(boolean isQuestion) throws IOException
    {
        String temp_user_name = null;
        if(isQuestion)
        {
            temp_user_name = user_name;
            writer.println("Write your question which you want to edit.");
            writer.flush();
            question = GetQuestion();
            System.out.println(question+","+temp_user_name);
            if(IsFoundQuestion(question, temp_user_name))
            {
                System.out.println("Found the valid question.");
                WriteAccepted();
                OperateEditAction(question, isQuestion);
                return;
            }

        }
        else
        {
            writer.println("Write your question title for which you want to edit the answer.");
            writer.flush();
            question = GetQuestion();
            if(IsFoundQuestion(question, temp_user_name) && IsFoundAnswerWithUser(question, user_name))
            {
                System.out.println("Found the valid question and answer");
                WriteAccepted();
                OperateEditAction(question, isQuestion);
                return;
            }
        }
        WriteNoAccepted();
    }
    /*
    ActionDelete() allows the user to delete a question or answer after confirmation.
     */
    public void ActionDelete(boolean isQuestion) throws IOException
    {
        ActionListQuestion();
        if(isQuestion)
        {
            writer.println("Write a question you want to delete.");
        }
        else
        {
            writer.println("Write a question for which you want to delete a answer.");
        }
        question = GetQuestion();
        System.out.println(question);
        if(IsFoundQuestion(question, null))
        {
            WriteAccepted();
            OperateDeleteAction(question, isQuestion);
        }
        else
        {
            WriteNoAccepted();
            writer.println("It does not exit.");
        }

    }
    private void OperateViewQuestionAction(String question) throws IOException
    {
        ViewQuestion a = new ViewQuestion(user_name, question, writer, reader);
        a.ShowQuestion();
        if(a.GoNextStep() && !IsFoundAnswerWithUser(question, user_name))
        {
            System.out.println("You will answer this question.");
            WriteAccepted();
            OperateAnswerActions(question);
        }
        else
        {
            System.out.println("The question might be already answered.");
            WriteNoAccepted();
        }
    }
    private void OperateQuestionActions(String question) throws IOException
    {
        CreateQuestions a = new CreateQuestions(question, user_name);
        a.MakeQuestion(writer, reader);
    }
    private void OperateAnswerActions(String question) throws IOException
    {
        AnswerOperation a = new AnswerOperation(user_name, question, writer, reader);
        a.MakeAnswer();
    }
    private void OperateShowAnswerActions(String question)
    {
        ShowAnswerOperation a = new ShowAnswerOperation(question, writer, reader);
        a.ShowAnswer();
    }
    private void OperateEditAction(String question_title, boolean isQuestion) throws IOException
    {
        Editors editor = new Editors(user_name,question_title, isQuestion, writer, reader);

        editor.ShowContent();
            writer.println("Do you want to edit it? : Write yes or no");
            writer.flush();
            String response = reader.readLine();
            System.out.println(response);
            if (response.trim().equals("yes")) {
                editor.EditContent();

            } else {
                System.out.println("Failed to edit it.");
                writer.println("Failed to edit it.");
            }
        writer.println("end of edit");
        writer.flush();
    }

    private void OperateDeleteAction(String _question, boolean isQuestion) throws IOException
    {
        DeleteOperation deleteOperation = new DeleteOperation(user_name,_question, writer, isQuestion);
        deleteOperation.SeeContent();
        writer.println("Do you want to delete it? : Write yes or no");
        writer.flush();
        String response = reader.readLine();
        System.out.println(response);
        if(response.trim().equals("yes"))
        {
            deleteOperation.DeleteAction();

        }
        else{
            System.out.println("Failed to delete it.");
            writer.println("Failed to delete it.");
            writer.flush();
        }
        System.out.println("finishing the delete action.");
    }
}
