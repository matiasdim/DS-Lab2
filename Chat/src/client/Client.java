package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.Vector;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


import server.PresenceService;
import server.RegistrationInfo;

public class Client {

    public static void main(String args[]) {
        PresenceService service = null;
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "PresenceService";

            String host = "localhost";
            int port = 1099;
            // Checks existance of optional args => host and port
            if (args.length >= 2 && args[1] != null) {
                host = args[1];
            }
            if (args.length >= 3 && args[2] != null){
                port = Integer.parseInt(args[2]);
            }
            Registry registry = LocateRegistry.getRegistry(host, port);
            service = (PresenceService) registry.lookup(name);
        } catch (Exception e) {
            System.err.println("PresenceService exception:");
            e.printStackTrace();
        }

        /*
         * Registriation section
         */
        String username = args[0]; // Save username coming from start process
        // Sockets!
        // -------- To listen "socket"
        ServerSocket serverSocket = null;
        Socket socket = null; // This one is to receive messages
        String userHost = "localhost"; // default user host
        int userPort = 9999; // default user port
        try {
            //Getting the local IP
            InetAddress ip = InetAddress.getLocalHost();
            userHost = ip.getHostAddress(); // update to host to local IP
            serverSocket = new ServerSocket(0);
            userPort = serverSocket.getLocalPort(); // Update port to serversocket port
            // Start listening for messages
            Thread thread = new Thread(new TextListener(serverSocket, username));
            thread.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("User host: " + userHost);
        System.out.println("User port: " + userPort);
        // Startup client (registration)
        RegistrationInfo reg = new RegistrationInfo(username, userHost, userPort, true);
        Boolean startedUp = Client.startup(service, reg);
        if (startedUp) {
            System.out.println("Welcome " + username + ".");
            System.out.print((char)27 + "[38;5;118m"+ username + " ➜ " + (char)27 + "[0m");
            while (true) {
                // Read inputs to Run chat
                Scanner reader = new Scanner(System.in);
                String clInput = reader.nextLine();
                String[] inputParts = clInput.split("\\s");
                // Verify first word of the input to know what to do...
                switch (inputParts[0]) {
                    case "exit":
                        System.out.println("Exiting...");
                        Client.removeUser(service, reg);
                        System.exit(0);
                        break;
                    case "available":
                        if (reg.getStatus() == true) {
                            System.out.println("You are already available.");
                        } else {
                            Client.updateUserStatus(service, reg);
                        }
                        System.out.print((char)27 + "[38;5;118m"+ username + " ➜ " + (char)27 + "[0m");
                        break;
                    case "busy":
                        if (reg.getStatus() == false) {
                            System.out.println("You are already not available.");
                        } else {
                            Client.updateUserStatus(service, reg);
                        }
                        System.out.print((char)27 + "[38;5;118m"+ username + " ➜ " + (char)27 + "[0m");
                        break;
                    case "broadcast":
                        if (inputParts.length < 2) {
                            System.out.println("Command to send message seems to be malformed. Try again.");
                        } else {
                            Vector<RegistrationInfo> friends = Client.listFriends(service);
                            Vector<RegistrationInfo> friendsAvailable = new Vector<>();
                            for (RegistrationInfo user : friends) {
                                if (user.getStatus() && !user.getUserName().equals(reg.getUserName())) {
                                    String message = Client.joinString(1, inputParts);//clInput.substring(clInput.indexOf(' ')+1); // Removing first word from input to only get message
                                    message = reg.getUserName() + " says ➜ " + message;
                                    Client.sendMessage(user.getHost(), user.getPort(), message);
                                }
                            }
                            System.out.print((char)27 + "[38;5;118m"+ username + " ➜ " + (char)27 + "[0m");
                        }
                        break;
                    case "talk":
                        if (inputParts.length < 3) {
                            System.out.println("Command to send message seems to be malformed. Try again.");
                        } else {
                            RegistrationInfo user = Client.lookForAUser(service, inputParts[1]);
                            if (user == null) {
                                System.out.println("User does not exists.");
                            } else {
                                if (user.getUserName().equals(reg.getUserName()) || !user.getStatus()) {
                                    System.out.println("User is not available");
                                } else {
                                    String message = Client.joinString(2, inputParts); // Removing first two words from input to only get message
                                    message = reg.getUserName() + " says ➜ " + message;
                                    Client.sendMessage(user.getHost(), user.getPort(), message);
                                }
                            }
                        }
                        System.out.print((char)27 + "[38;5;118m"+ username + " ➜ " + (char)27 + "[0m");
                        break;
                    case "friends":
                        Vector<RegistrationInfo> onlineFriends = Client.listFriends(service);
                        System.out.println((char)27 + "[38;5;251mOnline friends:" + (char)27 + "[0m");
                        System.out.println((char)27 + "[38;5;251m--------------------" + (char)27 + "[0m");
                        String status = "";
                        for (RegistrationInfo user : onlineFriends) {
                            if (user.getStatus()) {
                                System.out.println((char)27 + "[38;5;84m✓ " + user.getUserName() + " (Available)" + (char)27 + "[0m");
                            } else {
                                System.out.println((char)27 + "[38;5;94m✗ " + user.getUserName() + " (Busy)" + (char)27 + "[0m");
                            }

                        }
                        System.out.print((char)27 + "[38;5;118m"+ username + " ➜ " + (char)27 + "[0m");
                        break;
                    default:
                        System.out.println((char)27 + "[38;5;88mCommand not detected. Try again!" + (char)27 + "[0m");
                        System.out.print((char)27 + "[38;5;118m"+ username + " ➜ " + (char)27 + "[0m");
                        break;
                }
            }
        } else {
            System.out.println("Somebody with the given name already exists in the system. Exiting...");
        }
    }

    // Auxiliar function to format message before send it.
    public static String joinString(int index, String[] array){
        String text = "";
        for (int i = index; i < array.length; i++){
            text += array[i] + " ";
        }
        return text;
    }

    /*
     * Socket
     * Snippet to send message to a user via socket.
     */
    public static void sendMessage(String userHost, int userPort, String message){
        try{
            // -------- To talk "clientSocket"
            Socket clientSocket = new Socket(userHost, userPort);
            PrintStream os;
            os = new PrintStream(clientSocket.getOutputStream());
            os.println(message);
            clientSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /*
     * Functions to comunicate with Presence Service using RMI
     */
    // User registration upon startup
    public static Boolean startup(PresenceService service, RegistrationInfo reg){
        boolean isRegistered = false;
        try {
            isRegistered = service.register(reg);
        } catch (Exception e) {
            System.err.println("RegistrationInfo exception:");
            e.printStackTrace();
        }
        return isRegistered;
    }

    // Update status
    public static boolean updateUserStatus(PresenceService service, RegistrationInfo reg){
        boolean updated = false;
        try {
            reg.setStatus(!reg.getStatus());
            updated = service.updateRegistrationInfo(reg);
        } catch (Exception e) {
            System.err.println("updateRegistrationInfo exception:");
            e.printStackTrace();
        }
        return updated;
    }

    // List all users connected
    public static Vector<RegistrationInfo> listFriends(PresenceService service){
        Vector<RegistrationInfo> friends = new Vector<>();
        try {
            friends = service.listRegisteredUsers();
        } catch (Exception e) {
            System.err.println("listRegisteredUsers exception:");
            e.printStackTrace();
        }
        return friends;
    }

    // TO remove a particular user
    public static void removeUser(PresenceService service, RegistrationInfo reg){
        try {
            service.unregister(reg.getUserName());
        } catch (Exception e) {
            System.err.println("unregisterUsers exception:");
            e.printStackTrace();
        }
    }

    // Looking for a particular user
    public static RegistrationInfo lookForAUser(PresenceService service, String username){
        RegistrationInfo reg = null;
        try {
            reg = service.lookup(username);
        } catch (Exception e) {
            System.err.println("lookup exception:");
            e.printStackTrace();
        }
        return reg;
    }
}
