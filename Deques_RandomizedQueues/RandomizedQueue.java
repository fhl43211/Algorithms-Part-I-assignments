
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

import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    // initial capacity of underlying resizing array
    private static final int INIT_CAPACITY = 8;
    private Item[] _vec;
    private int _endLoc;

    // construct an empty randomized queue
    public RandomizedQueue() {
        _vec = (Item[]) new Object[INIT_CAPACITY];
        _endLoc = 0;
    }

    /**
     * \brief Resize and reset the front loc to one;
     * 
     * @param newCapacity
     * @param offset
     */
    private void resize(int newCapacity) {
        assert newCapacity >= _endLoc;
        Item[] newVec = (Item[]) new Object[newCapacity];
        for (int i = 0; i < _endLoc; ++i) {
            newVec[i] = _vec[i];
        }
        _vec = newVec;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        Item[] iteratorCopy;
        int current = 0;

        private RandomizedQueueIterator() {
            iteratorCopy = (Item[]) new Object[_endLoc];
            for (int i = 0; i < _endLoc; ++i) {
                iteratorCopy[i] = _vec[i];
            }
            StdRandom.shuffle(iteratorCopy);
        }

        public boolean hasNext() {
            return current < _endLoc;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Cannot call next() if there is no next element available");
            }
            return iteratorCopy[current++];
        }
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return _endLoc == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return _endLoc;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot enqueue a null input");
        }
        // In case it goes past the capacity, move it to the front if it is not at and
        // double the capacity.
        if (_endLoc >= _vec.length) {
            resize(_vec.length * 2);
        }
        _vec[_endLoc++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot dequeue from an empty queue");
        }
        if (_endLoc < _vec.length / 4 && _vec.length >= INIT_CAPACITY) {
            resize(_vec.length / 2);
        }
        int chosen = StdRandom.uniform(_endLoc);
        --_endLoc;
        Item temp = _vec[chosen];
        _vec[chosen] = _vec[_endLoc];
        return temp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot sample from an empty queue");
        }
        return _vec[StdRandom.uniform(_endLoc)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> test = new RandomizedQueue<Integer>();
        test.enqueue(1);
        test.enqueue(2);
        test.enqueue(3);
        test.enqueue(4);
        test.enqueue(5);
        for (var a : test) {
            for (var b : test)
                System.out.print(a + "-" + b + " ");
            System.out.println();
        }
        System.out.println(test.sample());
        System.out.println(test.sample());
        System.out.println(test.sample());
        System.out.println(test.size());
        System.out.println(test.isEmpty());
        System.out.println("Dequeue ");
        System.out.println(test.dequeue());
        System.out.println(test.dequeue());
        System.out.println(test.dequeue());
        System.out.println(test.dequeue());
        System.out.println(test.dequeue());
        System.out.println(test.size());
        System.out.println(test.isEmpty());
    }

}