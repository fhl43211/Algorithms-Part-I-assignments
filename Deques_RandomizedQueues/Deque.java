
/**
 * MIT License
 * 
 * Copyright (c) 2021 Hongliang Fan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node _first;
    private Node _last;
    private int _size;

    // construct an empty deque
    public Deque() {
        _size = 0;
        _first = null;
        _last = null;
    }

    private class Node {
        private final Item _value;
        private Node _next;
        private Node _prev;

        Node(Item item) {
            if (item == null) {
                throw new IllegalArgumentException("Cannot add null item");
            }
            _value = item;
        }
    }

    // is the deque empty?
    public boolean isEmpty() {
        return _size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return _size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        Node oldFirst = _first;
        _first = new Node(item);
        _first._next = oldFirst;
        if (oldFirst != null)
            oldFirst._prev = _first;
        ++_size;
        if (_size <= 1) {
            _last = _first;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (isEmpty()) {
            addFirst(item);
            return;
        }
        Node newNode = new Node(item);
        _last._next = newNode;
        newNode._prev = _last;
        _last = newNode;
        ++_size;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Remove from the empty deque");
        }
        Item firstItem = _first._value;
        _first = _first._next;
        if (_first != null)
            _first._prev = null;
        --_size;
        if (_size <= 1) {
            _last = _first;
        }
        return firstItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (_size <= 1) {
            return removeFirst();
        }
        Item lastItem = _last._value;
        _last = _last._prev;
        _last._next = null;
        --_size;
        return lastItem;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node _pointer = _first;

        public boolean hasNext() {
            return _pointer != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Cannot call next() if there is no next element available");
            }
            Item item = _pointer._value;
            _pointer = _pointer._next;
            return item;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> test = new Deque<Integer>();
        test.addFirst(1);
        System.out.println(test.size());
        System.out.println("pop " + test.removeLast());
        System.out.println(test.size());
        test.addFirst(1);
        System.out.println(test.size());
        System.out.println("pop " + test.removeFirst());
        System.out.println(test.size());
        test.addLast(1);
        System.out.println(test.size());
        System.out.println("pop " + test.removeFirst());
        System.out.println(test.size());
        test.addLast(1);
        System.out.println(test.size());
        System.out.println("pop " + test.removeLast());
        System.out.println(test.size());
        test.addLast(1);
        test.addFirst(2);
        System.out.println(test.size());
        System.out.println("pop " + test.removeFirst());
        System.out.println("pop " + test.removeFirst());
        System.out.println(test.size());
        test.addFirst(1);
        test.addLast(2);
        test.addFirst(0);
        test.addLast(3);
        System.out.println(test.size());
        for (int v : test) {
            System.out.println("for each " + v);
        }
        System.out.println("pop " + test.removeLast());
        System.out.println("pop " + test.removeLast());
        System.out.println("pop " + test.removeLast());
        Iterator<Integer> iterator = test.iterator();
        System.out.println(iterator.hasNext());
        System.out.println(iterator.next());
        System.out.println(iterator.hasNext());
        System.out.println("pop " + test.removeLast());
        System.out.println(test.size());
        iterator = test.iterator();
        System.out.println(iterator.hasNext());
    }
}