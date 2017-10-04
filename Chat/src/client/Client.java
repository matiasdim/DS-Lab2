package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.Vector;

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
            Registry registry = LocateRegistry.getRegistry(args[0]);
            service = (PresenceService) registry.lookup(name);
        } catch (Exception e) {
            System.err.println("PresenceService exception:");
            e.printStackTrace();
        }
        while (true) {
            //Registriation
            String username = args[1];
            String host = "localhost";
            int port = 1099;
            // Checks existance of optional args => host and port
            if (args.length >= 4 && args[2] != null && args[3] != null) {
                host = args[2];
                port = Integer.parseInt(args[3]);
            }

            // Startup client (registration)
            RegistrationInfo reg = new RegistrationInfo(username, host, port, true);
            Boolean startedUp = Client.startup(service, reg);
            if (startedUp) {
                String[] commandPrefixes = {"friends", "talk", "broadcast", "busy", "available", "exit"};

                Scanner reader = new Scanner(System.in);
                System.out.println("Please enter a valid command: ");
                String clInput = reader.next();
                if (clInput.equals("exit"))
                {
                    System.out.println("Exiting...");
                    System.exit(0); // or break;?

                } else if (clInput.equals("available"))
                {
                    if (reg.getStatus() == true){
                        System.out.println("You are already available.");
                    }else{
                        Client.updateUserStatus(service, reg);
                    }

                } else if (clInput.equals("busy"))
                {
                    if (reg.getStatus() == false){
                        System.out.println("You are already not available.");
                    }else{
                        Client.updateUserStatus(service, reg);
                    }

                } else if (clInput.startsWith("broadcast"))
                {

                } else if (clInput.startsWith("talk"))
                {

                } else if (clInput.equals("friends"))
                {
                    Vector<RegistrationInfo> friends = Client.listFriends(service);
                    System.out.println("List of friends:");
                    System.out.println();
                    for (RegistrationInfo user: friends) {
                        System.out.println(user.getUserName() + " (" + user.getStatus() + ")");
                    }
                }
            } else {
                System.out.println("Somebody with the given name already exists in the system. Try again!");
            }
        }
    }

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
}
