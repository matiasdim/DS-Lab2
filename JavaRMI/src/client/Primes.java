package client;

import compute.Task;
import java.io.Serializable;
import java.util.ArrayList;

public class Primes implements Task<int[]>, Serializable {

    private final int minor;
    private final int major;
    public Primes(int minor, int major){
        this.minor = minor;
        this.major = major;
    }
    public int[] execute() {
        return computePrimes(minor, major);
    }

    public static int[] computePrimes(int minVal, int maxVal){
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = minVal; i <= maxVal; i++ ){
            int j;
            for (j = 2; j < i; j++){
                if (i % j == 0){
                    break;
                }
            }
            if(i == j){
                list.add(i);
            }
            System.out.println();
        }
        // To cast ArrayList of Integer to array of ints
        int[] primes = new int[list.size()];
        for (int i=0; i < primes.length; i++)
        {
            primes[i] = list.get(i);
        }
        return primes;
    }
}
