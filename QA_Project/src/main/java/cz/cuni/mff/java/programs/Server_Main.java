package cz.cuni.mff.java.programs;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.io.*;

/*
This class represents the main server program that listens for incoming client connections, handles client requests,
and processes commands related to a question-answering system. The server accepts client connections, manages communication
with each client, and performs operations based on commands received from the client.
 */
import cz.cuni.mff.java.utils.*;
public class Server_Main
{
    static int port = 6067;
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    /*
    This class implements Runnable and is responsible for handling the communication with a single client.
    Each client is handled in a separate thread to allow concurrent communication.
     */
    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter writer;
        private BufferedReader reader;
        private QAoperations operator;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        /*
        void OperateCommands(String command): Processes the given command and calls the corresponding method
        from the QAoperations class based on the command. The commands include operations such as listing, creating,
        editing, and deleting questions or answers.
         */
        public void OperateCommands(String command)throws IOException
        {
            switch (command.toLowerCase())
            {
                case "list questions":
                    operator.ActionListQuestion();
                    break;
                case "create question":
                    operator.ActionCreateQuestion();
                    break;
                case "create answer":
                    operator.ActionCreateAnswer();
                    break;
                case "show answers":
                    operator.ActionShowAnswers();
                    break;
                case "edit question":
                    operator.ActionEdit(true);
                    break;
                case "edit answer":
                    operator.ActionEdit(false);
                    break;
                case "delete question":
                    operator.ActionDelete(true);
                    break;
                case "delete answer":
                    operator.ActionDelete(false);
                    break;
                case "view question":
                    operator.ActionViewQuestion();
                    break;
                default:
                    break;
            }
        }
        /*
        run(): The main entry point for the ClientHandler thread. It handles the client communication by reading client input,
        executing commands, and responding to the client. The client is prompted to send commands, which are then processed.
        If the command is "exit", the connection is closed.
         */
        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);

                UserOperation user_operator = new UserOperation(writer, reader);
                user_operator.UserManagement();
                System.out.println("Server started" + user_operator.user_name);
                operator = new QAoperations(user_operator.user_name,reader, writer);
                while(true)
                {
                    writer.println("Send command:"); writer.flush();
                    System.out.println("Waiting for user command...");
                    String line = reader.readLine();
                    System.out.println(line);
                    if(line.equals("exit"))
                    {
                        break;
                    }
                    else
                    {
                        OperateCommands(line.trim());
                    }
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + socket);
            } finally {
                    closeConnection();
            }
        }
        /*
        closeConnection(): Closes the client socket and removes the client handler from the active clients list.
        After closing the connection, it checks if the server should stop by calling checkAndStopServer().
         */
    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientHandlers.remove(this); // Remove from active clients
        checkAndStopServer(); // Check if server should close
    }

    }
    /*
    checkAndStopServer(): Checks if there are any active client connections. If there are no active clients,
    the server shuts down by calling System.exit(0).
     */
    public static synchronized void checkAndStopServer() {
        if (clientHandlers.isEmpty()) {
            System.out.println("All clients disconnected. Shutting down server...");
            System.exit(0); // Terminate server
        }
    }
    /*
    The main method that starts the server. It creates a ServerSocket and waits for incoming client connections.
    When a client connects, it creates a new ClientHandler, adds it to the list of active clients, and starts a new thread
    to handle the client's requests.
     */
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket);

            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandlers.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }
}