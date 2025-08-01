package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class OptimizedHeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of element-priority pairs.
     */
    private final List<PriorityNode<E>> elements;
    /**
     * {@link Map} of each element to its associated index in the {@code elements} heap.
     */
    private final Map<E, Integer> elementsToIndex;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        elements = new ArrayList<>();
        elementsToIndex = new HashMap<>();
        elements.add(null);
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public OptimizedHeapMinPQ(Map<E, Double> elementsAndPriorities) {
        this();
        for (E element : elementsAndPriorities.keySet()) {
            elements.add(new PriorityNode<>(element, elementsAndPriorities.get(element)));
            elementsToIndex.put(element, size());
        }
        for (int i = elements.size() / 2; i >= 1; i--) {
            sink(i);
        }
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        elements.add(new PriorityNode<E>(element, priority));
        elementsToIndex.put(element, size());
        swim(size());
    }

    @Override
    public boolean contains(E element) {
        return elementsToIndex.containsKey(element);
    }

    @Override
    public double getPriority(E element) {
        if (contains(element)) {
            int index = elementsToIndex.get(element);
            return elements.get(index).getPriority();
        }
        return 0;
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return elements.get(1).getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        E element = elements.get(1).getElement();
        exch(1, elements.size() - 1);

        elements.remove(elements.size() - 1);
        elementsToIndex.remove(element);
        sink(1);
        return element;
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        int index = elementsToIndex.get(element);
        elements.get(index).setPriority(priority);
        sink(index);
        swim(index);
    }

    @Override
    public int size() {
        return elements.size() - 1;
    }

    private void sink(int index) {
        while (2 * index < elements.size()) {
            int smallestChild = 2 * index;
            if (smallestChild + 1 < elements.size() && greater(smallestChild, smallestChild + 1)) {
                smallestChild++;
            }
            if (!greater(index, smallestChild)) {
                break;
            }
            exch(index, smallestChild);
            index = smallestChild;
        }
    }

    private void swim(int index) {
        while (index > 1 && greater(index / 2, index)) {
            exch(index / 2, index);
            index = index / 2;
        }
    }

    private boolean greater(int child, int other) {
        return elements.get(child).getPriority() > elements.get(other).getPriority();
    }

    private void exch(int i, int j) {
        E elementI = elements.get(i).getElement();
        E elementJ = elements.get(j).getElement();
        PriorityNode<E> swap = elements.get(i);
        elements.set(i, elements.get(j));
        elements.set(j, swap);
        elementsToIndex.put(elementI, j);
        elementsToIndex.put(elementJ, i);
    }
}
