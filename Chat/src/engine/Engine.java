package engine;

import server.PresenceService;
import server.RegistrationInfo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class Engine implements PresenceService {
    public Engine() {
        super();
    }
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Engine";
            PresenceService service = new Engine();
            PresenceService stub = (PresenceService) UnicastRemoteObject.exportObject(service, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Engine bound");
        } catch (Exception e) {
            System.err.println("Engine exception:");
            e.printStackTrace();
        }
    }

    @Override
    public boolean register(RegistrationInfo reg) throws RemoteException {
        return false;
    }

    @Override
    public boolean updateRegistrationInfo(RegistrationInfo reg) throws RemoteException {
        return false;
    }

    @Override
    public RegistrationInfo lookup(String name) throws RemoteException {
        return null;
    }

    @Override
    public void unregister(String userName) throws RemoteException {

    }

    @Override
    public Vector<RegistrationInfo> listRegisteredUsers() throws RemoteException {
        return null;
    }
}
