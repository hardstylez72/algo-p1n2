package helloword;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {

    public static void main(String[] args) {
        int i = 0;
        String champion = "";
        String word = "";

        while (!StdIn.isEmpty()) {
            word = StdIn.readString();
            i++;

//            if (i == 1) {
//                champion = word;
//            }
            if (StdRandom.bernoulli(1/(double)i)) {
                champion = word;
            }

            if (StdIn.isEmpty()) {
                break;
            }
        }
        StdOut.println(champion);

       }


}
