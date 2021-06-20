import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    // constructor takes a WordNet object
    private WordNet wordNet = null;

    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {

        Double max = 0.0;
        Integer maxi = 0;
        for (int i = 0; i < nouns.length; i++) {
            String n = nouns[i];
            Double sum = 0.0;
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) continue;
                String cmp = nouns[j];
                sum += this.wordNet.distance(n, cmp);
            }
            if (sum > max) {
                maxi = i;
                max = sum;
            }
        }

        return nouns[maxi];

    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}