package com.company;



import java.io.IOException;
import java.util.Set;

import  java.net.Socket;
import java.net.ServerSocket;
import java.util.HashSet;



public class ChatServer {


    private int port;
    private Set<String> userNames = new HashSet< >();
    private  Set<UserThread>  userThreads = new HashSet< >();

    public ChatServer(int port){
        this.port = port;
    }
    public void execute(){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("chat server is listening on port" + port);


            while (true){
                Socket socket = serverSocket.accept();

                System.out.println("New User Connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();





            }


        }catch (IOException ex){

            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }

    }


    void broadcast(String message,UserThread excludeUser){     // delivers message from one user to others(broadcasting)
        for(UserThread aUser: userThreads){
            if(aUser != excludeUser){
                aUser.sendMessage(message);
            }
        }

    }
    void addUserName(String userName){      // stores the username of new connected usert
        userNames.add(userName);
    }



    void removeUser(String userName,UserThread aUser){

        boolean removed = userNames.remove(userName);
        if(removed){
            userThreads.remove(aUser);
            System.out.println("The user" + userName + " quited");

        }

    }







    public static void main(String[] args) {

	// write your code here


        if(args.length<1){
            System.out.println("syntax: java ChatServer <port-number> ");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);
        ChatServer server = new ChatServer(port);
        server.execute();


    }
    Set <String> getUserNames() {

        return this.userNames;

    }





    boolean hasUsers(){   // returns true if there  other users connected(not count the current connected users)
        return !this.userNames.isEmpty();
    }



    }

