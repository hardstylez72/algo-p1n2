package week2;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int size = 0;
    private Node<Item> head, tail;


    private static class Node<Item> {
        Item item;
        Node<Item> next, prev;

        Node(Item element) {
            this.item = element;
        }
    }

    // construct an empty deque
    public Deque() {
        size = 0;
        head = null;
        tail = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> node = new Node<Item>(item);
        if (head == null) {
            head = node;
            tail = head;
        } else {
            head.prev = node;
            node.next = head;
            head = node;
        }

        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> node = new Node<Item>(item);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = head.item;
        if (size == 1) {
            tail = null;
            head = null;
        } else {
            Node<Item> nextHead = head.next;
            nextHead.prev = null;
            head = nextHead;
        }

        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = tail.item;
        if (size == 1) {
            tail = null;
            head = null;
        } else {
            Node<Item> tailPrev = tail.prev;
            tailPrev.next = null;
            tail = tailPrev;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new CustomIterator<Item>(head);
    }


    private static class CustomIterator<Item> implements Iterator<Item> {

        private Node<Item> head;

        public CustomIterator(Node<Item> head) {
            this.head = head;
        }

        public boolean hasNext() {
            return head != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = head.item;
            head = head.next;

            return item;

        }
    }

    // unit testing (required)
    public static void main(String[] args) {
//
//        week2.Deque<Integer> deque = new week2.Deque<>();
//        deque.addFirst(1);
//        deque.removeLast();

        Deque<Integer> deque = new Deque<Integer>();
        StdOut.println("size: " + deque.size());
        StdOut.println("isEmpty: " + deque.isEmpty());
        Integer j;

        deque.isEmpty();
        deque.addLast(2);
        deque.isEmpty();
        j = deque.removeLast();
        deque.addLast(5);
        deque.addLast(6);
        deque.addLast(7);
        deque.isEmpty();
        deque.isEmpty();
        j = deque.removeLast();

        deque.addLast(4);
        deque.addLast(5);
        deque.addLast(6);
        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);
        StdOut.println("Исходный массив");
        Iterator<Integer> iterator = deque.iterator();
        for (; iterator.hasNext(); ) {
            StdOut.println(iterator.next());
        }

        deque.removeFirst();
        deque.removeFirst();
        deque.removeLast();
        deque.removeLast();
        StdOut.println("size: " + deque.size());
        StdOut.println("isEmpty: " + deque.isEmpty());

        iterator = deque.iterator();
        while (iterator.hasNext()) {
            StdOut.println(iterator.next());
        }
    }

}