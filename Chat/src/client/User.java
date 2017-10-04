package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.PresenceService;
import server.RegistrationInfo;

public class User {
    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "PresenceService";
            Registry registry = LocateRegistry.getRegistry(args[0]);

            String username = args[1].toString();
            String host = "localhost";
            int port = 1099;
            boolean isAvailabe = true;
            // Checks existance of optional args => host and port
            if (args.length >= 4 && args[2] != null && args[3] != null){
                host = args[2].toString();
                port = Integer.parseInt(args[3]);
            }

            PresenceService service = (PresenceService) registry.lookup(name);
            RegistrationInfo task = new RegistrationInfo(username, host, port, isAvailabe);
            boolean registered = service.register(task);

            System.out.println("User Registered?" + registered);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }
}
