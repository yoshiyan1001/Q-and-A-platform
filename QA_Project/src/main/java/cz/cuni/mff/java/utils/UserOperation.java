package cz.cuni.mff.java.utils;

import java.io.*;

/*
The UserOperation class handles user authentication by providing sign-in and sign-up functionalities.
It manages user usernames and passwords stored in separate text files.
Usernames File (UserNames.txt) → Stores registered usernames.
Passwords File (UserPasswords.txt) → Stores corresponding user passwords.
 */
public class UserOperation {
    public static File user_info_directory = new File("src/main/java/cz/cuni/mff/java/UserInformation/");
    public static File user_name_file = new File("src/main/java/cz/cuni/mff/java/UserInformation/UserNames.txt");
    public static File user_passwards_file = new File("src/main/java/cz/cuni/mff/java/UserInformation/UserPasswords.txt");
    private PrintWriter writer;
    private BufferedReader reader;
    public String user_name;
    public String user_password;

    public UserOperation(PrintWriter writer, BufferedReader reader) throws IOException
    {
        this.writer = writer;
        this.reader = reader;

        initialize();
    }
    private void initialize() throws IOException
    {
        if(!user_info_directory.exists())
        {
            user_info_directory.mkdir();
            user_name_file.createNewFile();
            user_passwards_file.createNewFile();
        }
    }
    /*
    IsFoundUserName(String _user_name): Checks if a username exists in UserNames.txt.
    Returns the line number if found, or Integer.MIN_VALUE if not found.
     */
    private synchronized int IsFoundUserName(String _user_name) throws IOException
    {
        BufferedReader _reader = new BufferedReader(new FileReader(user_name_file));
        String user = "";
        int count = 0;
        while((user = _reader.readLine()) != null)
        {
            if(user.equals(_user_name))
            {
                user_name = _user_name;
                System.out.println("Found the user name.");
                return count;
            }
            count++;
        }
        System.out.println("The user name was not found.");
        return Integer.MIN_VALUE;//not found
    }
    private synchronized void WriteFile(File file, String line) throws IOException
    {
        FileWriter _writer = new FileWriter(file, true);
        _writer.write(line+"\n");
        _writer.close();
    }
    /*
    isFoundPassword(String _password, int num_line): Checks if the given password matches the one stored
    at the corresponding line in UserPasswords.txt.
     */
    private synchronized boolean isFoundPassword(String _password, int num_line) throws IOException
    {
        BufferedReader _reader = new BufferedReader(new FileReader(user_passwards_file));
        int count = 0;
        String user = "";
        while((user = _reader.readLine()) != null)
        {
            if(num_line == count)
            {
                if(user.equals(_password))
                {
                    user_password = _password;
                    return true;
                }
                else
                {

                    return false;
                }
            }
            count++;
        }
        return  false;
    }
    /*
    UserSignIn(): Prompts the user to enter a username and password.
    Verifies the username exists and checks if the password matches.
    Returns true if authentication is successful; otherwise, false.
     */
    private boolean UserSignIn() throws IOException
    {
        writer.println("User Name:");
        String _user_name = reader.readLine();
        System.out.println("User name received: " + _user_name);
        int num_line = IsFoundUserName(_user_name);
        if(num_line == Integer.MIN_VALUE)
        {
            return false;
        }
        System.out.println("User name is valid.");
        writer.println("User name is valid.");
        writer.println("Password:");
        String _password = reader.readLine();
        System.out.println("Password received: " + _password);
        if(isFoundPassword(_password, num_line))
        {
            return true;
        }
        return false;
    }
    /*
    UserSignUp(): Prompts the user to enter a username.
    it checks if the username is already taken.
    If not, it prompts for a password and writes both credentials to UserNames.txt and UserPasswords.txt.
    it returns true if the sign-up is successful.

     */
    private boolean UserSignUp() throws IOException
    {
        writer.println("User Name:");
        String _user_name = reader.readLine();
        System.out.println("User name received: " + _user_name);
        if(IsFoundUserName(_user_name) == Integer.MIN_VALUE)
        {
            System.out.println("User name is valid.");
            writer.println("User name is valid.");
            user_name = _user_name;
            writer.println("Password:");
            user_password = reader.readLine();
            System.out.println("Password received: " + _user_name);
            WriteFile(user_name_file, user_name);
            WriteFile(user_passwards_file, user_password);
            return true;
        }
        return false;
    }
    /*
    UserManagement(): Continuously prompts the user for authentication. it offers Sign In or Sign Up options.
    it calls the appropriate methods based on user input.
     */
    public void UserManagement() throws IOException {

       // System.out.println(count);
        while (true) {
            writer.println("Do you want to sign in or sign up? (Write sign in or sign up)");
            writer.flush();
            String response = reader.readLine();
            System.out.println("Processing " + response);
            switch (response.toLowerCase()) {
                case "sign in":
                    writer.println("Sign in process ...");
                    writer.flush();
                    if (UserSignIn()) {
                        writer.println("Sign in successful");
                        writer.flush();
                        return;
                    } else {
                        writer.println("Sign in failed");
                        writer.flush();
                        break;
                    }
                case "sign up":
                    writer.println("Sign up process ...");
                    writer.flush();
                    if(UserSignUp())
                    {
                        writer.println("Sign up successful");
                        writer.flush();
                        return;
                    }
                    else
                    {
                        writer.println("Sign up failed");
                        writer.flush();
                        break;
                    }
                default:
                    writer.println("Invalid Command");
                    writer.flush();
                    break;
            }
            System.out.println("still processing");
        }
    }
}
