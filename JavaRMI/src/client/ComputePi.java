package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.math.BigDecimal;
import compute.Compute;
import java.util.*;

public class ComputePi {
    public static void main(String args[]) {
        Compute comp = null;
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            comp = (Compute) registry.lookup(name);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
        while (true){
            Scanner reader = new Scanner(System.in);
            System.out.println("Enter 1 to Compute Pi, 2 to compute primes or 3 to exit: ");
            int n = reader.nextInt();
            if (n == 1){
                Scanner rdr = new Scanner(System.in);
                System.out.println("Computing Pi. Enter the number to be computed: ");
                int number = rdr.nextInt();
                ComputePi.calculatePi(comp, number);
            }else if (n == 2){
                Scanner rdr = new Scanner(System.in);
                System.out.println("Computing Primes in a range. Enter the lower value: ");
                int minVal = rdr.nextInt();
                System.out.println("And now enter the upper value: ");
                int maxVal = rdr.nextInt();
                ComputePi.calculatePrimes(comp, minVal, maxVal);
            }else if (n == 3){
                System.out.println("Exiting...");
                System.exit(0);
            }
        }
    }

    public static void calculatePi(Compute comp, int val1){
        try {
            Pi task = new Pi(val1);
            BigDecimal pi = comp.executeTask(task);
            System.out.println(pi);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }

    public static void calculatePrimes(Compute comp, int minVal, int maxVal){
        try {
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