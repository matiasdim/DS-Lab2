package engine;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import compute.Compute;
import compute.Task;



public class ComputeEngine implements Compute{

    public ComputeEngine() {
        super();
    }

    public <T> T executeTask(Task<T> t) {
        return t.execute();
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Compute engine = new ComputeEngine();
            Compute stub =
                    (Compute) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }
}

/*
SERVER:
java -cp /Users/matiasdim/Documents/Maestria/Fall-2017/656-Distributed\ Systems/Labs/Lab2/JavaRMI/src:/Users/matiasdim/Documents/Maestria/Fall-2017/656-Distributed\ Systems/Labs/Lab2/JavaRMI/src/compute.jar -Djava.rmi.server.codebase=file:///Users/matiasdim/Documents/Maestria/Fall-2017/656-Distributed\ Systems/Labs/Lab2/JavaRMI/src/compute.jar -Djava.rmi.server.useCodebaseOnly=false -Djava.rmi.server.hostname=localhost -Djava.security.policy=/Users/matiasdim/Documents/Maestria/Fall-2017/656-Distributed\ Systems/Labs/Lab2/JavaRMI/src/engine/server.policy engine.ComputeEngine
CLIENT:
java -cp /Users/matiasdim/Documents/Maestria/Fall-2017/656-Distributed\ Systems/Labs/Lab2/JavaRMI/src:/Users/matiasdim/Documents/Maestria/Fall-2017/656-Distributed\ Systems/Labs/Lab2/JavaRMI/src/compute.jar -Djava.rmi.server.codebase=http://localhost/classes/compute.jar -Djava.security.policy=/Users/matiasdim/Documents/Maestria/Fall-2017/656-Distributed\ Systems/Labs/Lab2/JavaRMI/src/client/client.policy client.ComputePi localhost 45
 */

