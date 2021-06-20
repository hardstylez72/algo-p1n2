package p2w1;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class WordNet {

    private final HashMap<Integer, String> nounIdToStringMap;
    private final HashMap<String, SET<Integer>> nounToIdMap;
    private final SET<String> NounSet;
    private final Digraph digraph;
    private final SAP sap;

    private static class Synset {
        public int id;
        public String[] synsets;
        public String dict;

        public Synset() {
        }
    }

    private static class Hypernym {
        public int id;
        public int[] rel;

        public Hypernym() {
        }
    }

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {



        this.nounToIdMap = new HashMap<String, SET<Integer>>();
        this.nounIdToStringMap = new HashMap<Integer, String>();
        this.NounSet = new SET<String>();

        Map<Integer, Synset> synsetMap = new HashMap<Integer, Synset>();
        for (Synset synset : parseSynsets(synsets)) {
            synsetMap.put(synset.id, synset);
            for (String s : synset.synsets) {
                if (this.nounToIdMap.containsKey(s)) {
                    SET<Integer> integers = this.nounToIdMap.get(s);
                    integers.add(synset.id);
                    this.nounToIdMap.put(s, integers);
                } else {
                    SET<Integer> integers = new SET<>();
                    integers.add(synset.id);
                    this.nounToIdMap.put(s, integers);
                }
                this.nounIdToStringMap.put(synset.id, s);
                this.NounSet.add(s);
            }
        }

        Map<Integer, Hypernym> hypernymMap = new HashMap<Integer, Hypernym>();
        for (Hypernym hypernym : parseHypernyms(hypernyms))
            hypernymMap.put(hypernym.id, hypernym);

        this.digraph = new Digraph(hypernymMap.size());

        Collection<Hypernym> hypernyms1 = hypernymMap.values();

        for (Iterator<Hypernym> h = hypernyms1.iterator(); h.hasNext(); ) {
            Hypernym hh = h.next();
            for (int id : hh.rel)
                this.digraph.addEdge(hh.id, id);
        }

        if (new DirectedCycle(this.digraph).hasCycle()) throw new IllegalArgumentException();

        this.sap = new SAP(this.digraph);
    }

    private Synset[] parseSynsets(String synsets) {
        In in = new In(synsets);
        String[] lines = in.readAllLines();
        Synset[] out = new Synset[lines.length];

        int i = 0;
        for (String s : lines) {

            Synset synset = new Synset();


            String[] parts = s.split(",");
            if (parts.length >= 3) {
                synset.id = Integer.parseInt(parts[0]);
                synset.synsets = parts[1].split(" ");


                StringBuilder dict = new StringBuilder();
                int cnt = 0;
                for (int j = 2; j < parts.length; j++) {
                    dict.append(parts[j]);
                }

                synset.dict = dict.toString();
                out[i++] = synset;
            } else {
                StdOut.println(out[i]);
            }
        }

        return out;
    }


    private Hypernym[] parseHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        String[] lines = in.readAllLines();
        Hypernym[] out = new Hypernym[lines.length];

        int i = 0;
        for (String s : lines) {

            Hypernym hypernym = new Hypernym();

            String[] parts = s.split(",");
            if (parts.length >= 1) {
                hypernym.id = Integer.parseInt(parts[0]);

                int[] rel = new int[parts.length - 1];
                int cnt = 0;
                for (int j = 1; j < parts.length; j++) {
                    rel[cnt++] = Integer.parseInt(parts[j]);
                }
                hypernym.rel = rel;

                out[i++] = hypernym;
            } else {
                StdOut.println(1);
            }
        }

        return out;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nounToIdMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return this.NounSet.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        SET<Integer> ai = this.nounToIdMap.get(nounA);
        SET<Integer> bi = this.nounToIdMap.get(nounB);

        int dist = sap.length(ai, bi);

        return dist;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        SET<Integer> ai = this.nounToIdMap.get(nounA);
        SET<Integer> bi = this.nounToIdMap.get(nounB);
        Integer id = this.sap.ancestor(ai, bi);

        return this.nounIdToStringMap.get(id);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        wordnet.nouns();
    }
}