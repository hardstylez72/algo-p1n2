package week2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private int lastIndexCounter;
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        items = (Item[]) new Object[1];
    }


    private void resize(int oldSize, int newSize) {

        if (oldSize == newSize) {
            return;
        }


    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (items.length < (lastIndexCounter + 1)) {
            int j = 0;
            Item[] newItems = (Item[]) new Object[items.length * 2];
            for (Item item1 : items) {
                if (item1 != null) {
                    newItems[j] = item1;
                    j++;
                }
            }
            size = j;
            items = newItems;
            lastIndexCounter = j;
        }


        items[lastIndexCounter] = item;
        lastIndexCounter++;
        size++;
    }

    private int getRandomNotNullIndex() {
        int index = 0;
        Item item = null;
        while (item == null) {
            index = StdRandom.uniform(items.length);
            item = items[index];
        }

        return index;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = getRandomNotNullIndex();
        Item item = items[index];


        items[index] = null;
        size--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = items[getRandomNotNullIndex()];

        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new CustomIterator(items, size);
    }

    private static class CustomIterator<Item> implements Iterator<Item> {

        private Item[] items;
        private int itemsLeft;

        public CustomIterator(Item[] items, int size) {

            int j = 0;
            Item[] newItems = (Item[]) new Object[size];
            for (Item item : items) {
                if (item != null) {
                    newItems[j] = item;
                    j++;
                }
            }

            StdRandom.shuffle(newItems);
            this.items = newItems;
            this.itemsLeft = j;
        }

        public boolean hasNext() {
            return itemsLeft > 0;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = items[itemsLeft - 1];
            itemsLeft--;
            return item;

        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue();
        Integer k = 0;
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
//        randomizedQueue.enqueue(3);
//        randomizedQueue.enqueue(4);
//        randomizedQueue.enqueue(5);
//        randomizedQueue.enqueue(6);
//        randomizedQueue.enqueue(7);
        k = randomizedQueue.size();
        k = randomizedQueue.dequeue();
        randomizedQueue.enqueue(4);
        k = randomizedQueue.dequeue();
//        k = randomizedQueue.dequeue();
        k = randomizedQueue.size();
        StdOut.println("isEmpty: " + randomizedQueue.isEmpty());
        StdOut.println("sample: " + randomizedQueue.sample());

        Iterator<Integer> iterator = randomizedQueue.iterator();
        for (; iterator.hasNext(); ) {
            StdOut.println(iterator.next());
        }
    }

}