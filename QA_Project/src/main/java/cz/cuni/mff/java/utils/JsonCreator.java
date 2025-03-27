package cz.cuni.mff.java.utils;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
The JsonCreator class is responsible for handling JSON-based question and answer storage in a file system.
It allows creating, retrieving, and deleting questions and answers while maintaining proper JSON formatting.
 */
public class JsonCreator {
    public static File question_file = new File("/Users/yoshi/Documents/QA_Project/src/main/java/cz/cuni/mff/java/QABox/question.json");//TODO change the file path
    public static File answer_file = new File("/Users/yoshi/Documents/QA_Project/src/main/java/cz/cuni/mff/java/QABox/answer.json");//TODO change the file path
    private FileWriter writer;
    private String start ="[";
    private String end ="]";
    public JsonCreator() {
    }
    /*
    WriteContent() writes content to a given PrintWriter, replacing \n with @ and splitting lines accordingly.
     */
    public synchronized void WriteContent(PrintWriter writer, String content)
    {
        String[] lines = content.replace("\\n", "@").split("@");
        for (String line : lines) {
            writer.println(line);
        }
    }
    /*
    ShowAnswer() displays an answer on the provided PrintWriter.
     */
    public synchronized void ShowAnswer(PrintWriter writer, Map<String, String> answer) throws IOException
    {
        writer.println("Publisher: "+answer.get("user-name"));
        writer.println("--------------");
        String answer_content = answer.get("answer content");
        WriteContent(writer, answer_content);
        writer.println("--------------");
        writer.println("End of solution");
        writer.println("END");
        writer.flush();
    }
    /*
    RemoveList removes a question or answer from the corresponding JSON file based on the title and username.
     */
    public void RemoveList(String question_title, String user_name, boolean isQuestion) throws IOException
    {
        List<Map<String, String>> lists = ListQuestionsorAnswers(isQuestion);
        List<Map<String,String>> selected_lists = new ArrayList<>();

        for (Map<String, String> list : lists) {
            if(!(list.get("question title").equals(question_title) && list.get("user-name").equals(user_name))) {
                if(isQuestion)
                {
                    selected_lists.add(list);

                }
                else
                {
                    selected_lists.add(list);

                }
            }
            else {System.out.println("Removing" +list.get("question title") +", " + list.get("user-name") );}
        }
        if(isQuestion)
        {
            WriteAllQuestions(selected_lists);
        }
        else
        {
            WriteAllAnswers(selected_lists);
        }
    }
    /*
    ShowQuestion() displays a question's details to a PrintWriter.
     */
    public void ShowQuestion(PrintWriter writer, String question) throws IOException//might with user
    {
        Map<String, String> _question = ObtainQuestion(question);
        writer.println("Question Title: " + _question.get("question title") + " user-name: " + _question.get("user-name"));
        writer.println("--------------");
        String question_content = _question.get("question content");
        WriteContent(writer, question_content);
        writer.println("--------------");
        System.out.println("End viewing question: " + question);
        writer.println("END");
        writer.flush();
    }
    /*
    GetAnswerOrQuestion() retrieves a question or answer in JSON format.
     */
    public String GetAnswerOrQuestion(String question_title, String user_name, boolean isQuestion) throws IOException
    {
        List<Map<String, String>> lists = ListQuestionsorAnswers(isQuestion);
        for (Map<String, String> question : lists) {
            if (question_title.equals(question.get("question title")) && question.get("user-name").equals(user_name)) {
                if(isQuestion)
                {
                    return "\"question title\": \"" + question_title +"\"," + "\"user-name\": \"" + user_name+ "\","+ "\"question content\": \"" + question.get("question content") +"\"";
                }else {
                    return "\"question title\": \"" + question_title + "\"," + "\"user-name\": \"" + user_name + "\"," + "\"answer content\": \"" + question.get("answer content") + "\"";//return "Question Title: " + question.get("question title") + " user-name: " + question.get("user-name");
                }
                }
        }

        return null;

    }
    /*
    isFoundQuestionOrAnswer() checks if a specific question or answer exists in the respective JSON file.
     */
    public boolean isFoundQuestionOrAnswer(String title, String user_name, boolean isQuestion) throws IOException
    {
        List<Map<String, String>> lists = ListQuestionsorAnswers(isQuestion);

        for (Map<String, String> question : lists) {
            if (title.equals(question.get("question title"))) {
                if(user_name==null)
                {
                    return true;
                }
                else {
                    if(question.get("user-name").equals(user_name))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /*
    ObtainListWithUserName() finds a question or answer posted by a specific user.
     */
    public Map<String, String> ObtainListWithUserName(String question_title, String user_name, boolean isQuestion) throws IOException
    {
        List<Map<String, String>> lists = ListQuestionsorAnswers(isQuestion);
        for(Map<String, String> list : lists)
        {
            if (list.get("user-name").equals(user_name) && list.get("question title").equals(question_title)) {
                return list;
            }
        }
        return null;
    }
    /*
    ObtainQuestion() finds and returns a question by title.
     */
    public Map<String, String> ObtainQuestion(String question_title) throws IOException
    {
        List<Map<String, String>> questions = ListQuestionsorAnswers(true);
        for (Map<String, String> question : questions) {
            if (question_title.equals(question.get("question title"))) {
                return question;
            }
        }
        return null;
    }
    /*
    ListQuestionsorAnswers() reads and parses questions or answers from the corresponding JSON file.
     */
    public synchronized List<Map<String, String>> ListQuestionsorAnswers(boolean isQuestion) throws IOException {//fix it

        List<Map<String, String>> questions = new ArrayList<>();
        BufferedReader _reader = null;
        if(isQuestion) {
            _reader = new BufferedReader(new FileReader(question_file));
        }
        else
        {
           _reader = new BufferedReader(new FileReader(answer_file));
        }
        try {
            String line;
            while ((line = _reader.readLine()) != null) {
                //System.out.println(line);
                if (line.equals(start) || line.equals(end) || line.trim().isEmpty()) {
                } else {
                    Pattern pattern = Pattern.compile("\"(.*?)\"\\s*:\\s*\"(.*?)\"");
                    Matcher matcher = pattern.matcher(line);
                    List<String> question = new ArrayList<>();
                    while (matcher.find()) {
                        String key = matcher.group(1);
                        question.add(key);
                        String value = matcher.group(2);
                        question.add(value);
                    }
                    if(isQuestion) {
                        questions.add(QuestionDict(question.get(1), question.get(3), question.get(5)));
                    }
                    else
                    {
                        questions.add(AnswerDict(question.get(1), question.get(3), question.get(5)));
                    }
                }
            }
            _reader.close();

            return questions;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
    /*
    AddQuestion() create json question.
     */
    private synchronized void AddQuestion(String question_title, String question_content, String user_name, FileWriter _writer) throws IOException
    {
        _writer.write("{");
        _writer.write("\"question title\": \"" + question_title +"\"," + "\"user-name\": \"" + user_name+ "\","+ "\"question content\": \"" + question_content +"\"");
        _writer.write("}");
    }
    /*
    QuestionDict() make the dictionary based on the parameters to create a json format.
     */
    private Map<String, String> QuestionDict(String question_title, String user_name, String question_content) throws IOException
    {
        Map<String, String> dictionary = new HashMap<>();
        dictionary.put("question title", question_title);
        dictionary.put("user-name",  user_name);
        dictionary.put("question content", question_content);
        return dictionary;
    }
    /*
    Make the dictionary for answer json format.
     */
    private Map<String, String> AnswerDict(String question_title,String user_name, String answer_content)
    {
        Map<String, String> dictionary = new HashMap<>();

        dictionary.put("question title", question_title);
        dictionary.put("user-name",  user_name);
        dictionary.put("answer content", answer_content);
        return dictionary;
    }
    /*
    WriteAllAnswers and WriteAllQuestions write all questions or answers to the files.
     */
    public synchronized void WriteAllAnswers(List<Map<String, String>> answers)throws IOException
    {
        writer = new FileWriter(answer_file, false);
        System.out.println("Writing answers...");
        writer.write(start+"\n");
        for(int i = 0; i < answers.size(); i++)//(Map<String, String> question : questions)
        {
            AddAnswer(answers.get(i).get("question title"), answers.get(i).get("user-name"), answers.get(i).get("answer content"),writer);
            if(i != answers.size()-1)
            {
                writer.write(",\n");
            }
        }
        writer.write("\n");
        writer.write(end);
        writer.close();
    }
    public synchronized void WriteAllQuestions(List<Map<String, String>> questions)throws IOException
    {
        writer = new FileWriter(question_file, false);
        System.out.println("Writing questions...");
        writer.write(start+"\n");
        for(int i = 0; i < questions.size(); i++)//(Map<String, String> question : questions)
        {
            AddQuestion(questions.get(i).get("question title"), questions.get(i).get("question content"), questions.get(i).get("user-name"), writer);
            if(i != questions.size()-1)
            {
                writer.write(",\n");
            }
        }
        writer.write("\n");
        writer.write(end);
        writer.close();
    }
    private synchronized void AddAnswer(String question_tile, String user_name, String answer_content, FileWriter _writer) throws IOException
    {
        _writer.write("{");
        _writer.write("\"question title\": \"" + question_tile +"\"," + "\"user-name\": \"" + user_name+ "\","+ "\"answer content\": \"" + answer_content +"\"");
        _writer.write("}");

    }
    /*
    We create answer and question to write the parameters to the files.
     */
    public synchronized void CreateAnswer(String question_tile, String user_name, String answer_content) throws IOException
    {
        if(!answer_file.exists())
        {
            writer = new FileWriter(answer_file, false);
            writer.write(start+"\n");
            AddAnswer(question_tile, user_name, answer_content, writer);
            writer.write(end);
            writer.close();
            System.out.println("Answer created");
        }
        else {
            List<Map<String, String>> answers = ListQuestionsorAnswers(false);
            answers.add(AnswerDict(question_tile, user_name, answer_content));
            WriteAllAnswers(answers);
        }
    }
    public synchronized void CreateQuestion(String question_tile, String user_name, String question_content) throws IOException {
        if(!question_file.exists())
        {
            writer = new FileWriter(question_file, false);
            writer.write(start+"\n");
            AddQuestion(question_tile, question_content, user_name, writer);

            writer.write(end);
            writer.close();
        }
        else {
            List<Map<String, String>> questions = ListQuestionsorAnswers(true);

                questions.add(QuestionDict(question_tile, user_name, question_content));
                WriteAllQuestions(questions);
            }

    }
}

