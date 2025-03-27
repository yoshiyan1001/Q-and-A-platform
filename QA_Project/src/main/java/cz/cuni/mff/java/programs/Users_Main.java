package cz.cuni.mff.java.programs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
/*
   This class implements a client-side application that allows a user to interact with a server over a socket connection.
   It provides a command-line interface to manage questions and answers through the following actions: listing, creating,
    editing, and deleting questions and answers. The client communicates with the server using a predefined protocol over TCP sockets.

* */
public class Users_Main {
    static int port = 6067;
    public static String user_name;
    public static String password;
    public static Scanner scanner = new Scanner(System.in);
    public static PrintWriter writer;
    public static BufferedReader reader;

    private static String[] list_commands = {"List Questions", "View Question","Create Question", "Create Answer",
                                                "Show Answers", "Edit question", "Edit answer", "Delete question", "Delete answer", "exit"};

    /*
     OperateActions() Reads and processes server responses to determine if an operation was successful.
    */
    public static boolean OperateActions() throws IOException
    {
        String response = reader.readLine();
        System.out.println(response);
        if(response.trim().equals("Accepted your question."))
        {
            return true;
        }
        else{
            return false;
        }
    }
    /*
    main(String[] args) initializes the socket connection to the server and manages user authentication and command interaction.
     */
    public static void main(String[] args) throws IOException {

        String host = "localhost";

        Socket socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        scanner = new Scanner(System.in);
        System.out.println("Connected to Server");
        UserManager();
        CommandOperator();
        socket.close();
    }
    private static String SendCommand() throws IOException
    {
        System.out.print(reader.readLine());//send the command:
        return scanner.nextLine();
    }
    /*
    CommandOperator() continuously prompts the user for a command and executes the appropriate action.
     */
    private static void CommandOperator() throws IOException
    {
        while(true)
        {
            System.out.println("------------List of Commands------------");
            PrintAllCommands();

            String line = SendCommand();
            writer.println(line);

            if(line.equals("exit"))
            {
                break;
            }
            writer.flush();
            switch(line.trim().toLowerCase())
            {
                case "list questions":
                    ActionListQuestions();
                    break;
                case "create question":
                    CreateQuestionOperation();
                    break;
                case "create answer":
                    CreateAnswerOperation();
                    break;
                case "show answers":
                    ShowAnswersOperation();
                    break;
                case "edit question":
                    EditOperation(true);
                    break;
                case "edit answer":
                    EditOperation(false);
                    break;
                case "delete question":
                    DeleteOperation(true);
                    break;
                case "delete answer":
                    DeleteOperation(false);
                    break;
                case "view question":
                    ViewQuestionOperation();
                    break;
                default:
                    System.out.println("Unknown command: " + line);
                    break;
            }
        }
    }
    /*
    CreateQuestionOperation(), CreateAnswerOperation(): Allows the user to create questions and answers
    by interacting with the server.
     */
    private static String WriteReadName() throws IOException
    {
        System.out.println(reader.readLine());//user name
        String _user_name = scanner.nextLine();
        writer.println(_user_name);         //send user name
        writer.flush();
        return _user_name;
    }
    private static String WriteReadPassword() throws IOException
    {
        System.out.println(reader.readLine());//password
        String _password = scanner.nextLine();   //write password
        writer.println(_password);         //write password
        writer.flush();
        return _password;
    }
    private static boolean isValidUserName(String response, String part)
    {
        if(response.toLowerCase().trim().equals(part+" failed") || response.toLowerCase().trim().equals("Invalid Command"))
        {
            return false;
        }
        return true;
    }
    private static boolean isValidPassword(String response, String part, String _password, String _user_name)
    {
        if(response.toLowerCase().trim().equals(part.toLowerCase()+" successful"))
        {
            user_name = _user_name;
            password = _password;
            return true;
        }
        return false;
    }
    /*
     IsValidUserPart(String part): Handles a user's user-name and password.
     */
    private static boolean IsValidUserPart(String part) throws IOException
    {
        String _user_name = WriteReadName();       //write user name
        String response = reader.readLine();      //read the validity
        System.out.println(response);

        if(!isValidUserName(response,part))
        {
            return false;
        }
        String _password = WriteReadPassword();
        response = reader.readLine();        //success or not
        System.out.println(response);
        if(isValidPassword(response,part,_password,_user_name))
        {
            return true;
        }
        System.out.println("error");
        return false;
    }
    private static boolean IsValidUserInfo(String sign) throws IOException
    {
        return IsValidUserPart(sign);
    }
    private static void UserManager() throws IOException
    {
        while(true) {
            System.out.println(reader.readLine());//sign up or in
            String response = scanner.nextLine();
            writer.println(response);
            writer.flush();
            String isValid = reader.readLine();
            System.out.println(isValid);
            if(!isValid.trim().equals("Invalid Command")) {
                if (IsValidUserInfo(response)) {
                    return;
                }
            }
        }
    }
    /*
    CreateQuestionOperation(), CreateAnswerOperation(): Allows the user to create questions and answers
    by interacting with the server.
     */
    private static void CreateQuestionOperation() throws IOException
    {
        System.out.println(reader.readLine());
        PostQuestion();
        if(OperateActions())
        {
            EnterContent();
        }
    }
    private static void EnterContent() throws IOException
    {
        System.out.println(reader.readLine()); //enter question
        while(true)
        {
            String input = scanner.nextLine();
            writer.println(input);//writing question
            if(input.equals("END"))
            {
                System.out.println(reader.readLine());
                break;
            }
        }
        writer.flush();
    }
    private static void PrintContent(String ending_word) throws IOException
    {
        while(true)
        {
            String input = reader.readLine().trim();

            if(input.equals(ending_word))
            {
                break;
            }
            System.out.println(input);
        }
    }
    private static void ViewQuestionOperation() throws IOException
    {
        System.out.println(reader.readLine()); //write question
        PostQuestion();
        if(OperateActions())
        {
            PrintContent("END");
            System.out.println(reader.readLine()); //yes or no
            String answer = scanner.nextLine();
            writer.println(answer);//yes or no
            if(answer.equals("yes"))
            {
                if(OperateActions()) {
                    WriteAnswer();
                }
            }
            else
            {
                writer.println(reader.readLine()); //no accepted
            }
        }
    }
    private static void WriteAnswer() throws IOException
    {
        EnterContent();
    }
    private static void CreateAnswerOperation() throws IOException
    {
        System.out.println(reader.readLine()); //write question
        PostQuestion();
        if(OperateActions())
        {
            WriteAnswer();
        }
    }
    /*
    ShowAnswersOperation(): Handles the operation to show answers.
     */
    private static void ShowAnswersOperation() throws IOException
    {
        System.out.println(reader.readLine()); //write question
        PostQuestion();
        if(OperateActions())
        {
            PrintContent("END");
        }
    }
    /*
    EditOperation(boolean isQuestion): Handles the modification of existing questions or answers.
     */
    private static void EditOperation(boolean isQuestion) throws IOException {
        System.out.println(reader.readLine()); //write question
        PostQuestion();
        if (OperateActions()) {
            PrintContent("END");
            if(!isQuestion)
            {
                PrintContent("END");
            }
            if(WriteYesOrNo())
            {
                EnterContent();
            }
            PrintContent("end of edit");//fail or not
        }
        else {
            System.out.println("Error");
        }
    }
    private static void PostQuestion()
    {
        String question = scanner.nextLine();
        writer.println(question);
    }

    private static void ActionListQuestions() throws IOException
    {
        String response;
        while(!(response = reader.readLine()).equals("END"))
        {
            System.out.println("・" + response);
        }
    }
    /*
    DeleteOperation(boolean isQuestion): Handles the deletion of questions or answers.
     */
    private static boolean WriteYesOrNo() throws IOException
    {
        System.out.println(reader.readLine()); //yes or no
        String a = scanner.nextLine();
        writer.println(a); //yes or no
        writer.flush();
        return a.trim().equals("yes");

    }

    private static void DeleteOperation(boolean isQuestion) throws IOException
    {
        ActionListQuestions();;
        System.out.println(reader.readLine());//write question
        PostQuestion();
        if(OperateActions())
        {
            if(isQuestion)
            {
                PrintContent("END");
            }
            else
            {
                PrintContent("END");
            }
            WriteYesOrNo();
            System.out.println(reader.readLine()); //success or not
            System.out.println("finished deleting it.");
        }
        else{
            System.out.println(reader.readLine());//no exit
        }
    }
    public static void PrintAllCommands()
    {
        for(String command: list_commands)
        {
            System.out.println("・"+command);
        }
    }
}
