package engine;

import server.PresenceService;
import server.RegistrationInfo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class Engine implements PresenceService {

    Vector<RegistrationInfo> users = new Vector<>();

    public Engine() {
        super();
    }
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "PresenceService";
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
        boolean status = true;
        for (RegistrationInfo register: users) {
            if (register.getUserName().equals(reg.getUserName())){
                status = false;
                break;
            }
        }
        if (status){
            this.users.add(reg);
        }
        return status;
    }

    @Override
    public boolean updateRegistrationInfo(RegistrationInfo reg) throws RemoteException {
        boolean status = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserName().equals(reg.getUserName())){
                status = true;
                this.users.set(i, reg);
                break;
            }
        }
        return status;
    }

    @Override
    public RegistrationInfo lookup(String name) throws RemoteException {
        for (RegistrationInfo register: users) {
            if (register.getUserName().equals(name)){
                return register;
            }
        }
        return null;
    }

    @Override
    public void unregister(String userName) throws RemoteException {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserName().equals(userName)){
                users.remove(i);
            }
        }
    }

    @Override
    public Vector<RegistrationInfo> listRegisteredUsers() throws RemoteException {
        return users;
    }

}
