package com.company;






import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.String;


// Thread handles of all the connected client,so server can handle multiple client at a time
public class UserThread extends Thread {



    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;


    public UserThread(Socket socket, ChatServer server){
        this.socket = socket;
        this.server = server;

    }


    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);


            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);


            String serverMessage = ("New user connected:" + userName);

            server.broadcast(serverMessage, this);

            String clientMessage;

            do {

                clientMessage = reader.readLine();
                serverMessage = " [ " + userName + "] + clientMessage";
                server.broadcast(serverMessage, this);


            } while (!clientMessage.equals("bye"));

            server.removeUser(userName, this);
            socket.close();
            serverMessage = userName + "has quit";
            server.broadcast(serverMessage, this);


        }
        catch (IOException ex) {
            System.out.println("Error in UserThread " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    void printUsers(){    //send a list of online users to newly connected users
        if(server.hasUsers()){
            writer.println("Connected users:" + server.getUserNames());
        }else{
            writer.println("No connected users");

        }
    }

    void sendMessage(String message){

        //String message;
        writer.println(message);
    }




}
