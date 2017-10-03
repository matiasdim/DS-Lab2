package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.math.BigDecimal;
import compute.Compute;

public class ComputePrimes {
    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            Compute comp = (Compute) registry.lookup(name);
            int minVal = Integer.parseInt(args[1]);
            int maxVal = Integer.parseInt(args[2]);
            Primes task = new Primes(minVal, maxVal);
            int[] primes = comp.executeTask(task);
            System.out.println("The prime numbers between " + minVal + " and " + maxVal + " are: ");
            for (int i = 0; i < primes.length; i++){
                System.out.println(primes[i]);
            }

        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }
}
