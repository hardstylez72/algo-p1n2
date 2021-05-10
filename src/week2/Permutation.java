package week2;

import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    public static void main(String[] args) {
        int k ;
        if (args == null) {
            throw new IllegalArgumentException();
        }
        if (args.length <= 0) {
            throw new IllegalArgumentException();
        }
        try {
            k = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException();
        }

        if (k < 0 || k > args.length - 1) {
            throw new IllegalArgumentException();
        }

        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();

        for (int i = 0; i < k; i++) {
            randomizedQueue.enqueue(args[i + 1]);
        }

        for (Object o : randomizedQueue) {
            StdOut.println(o);
        }
    }
}