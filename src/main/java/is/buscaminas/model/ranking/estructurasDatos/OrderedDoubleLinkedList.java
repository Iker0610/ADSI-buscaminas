package is.buscaminas.model.ranking.estructurasDatos;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedDoubleLinkedList<T extends Comparable<? super T>> {

    // Atributos
    private Node<T> first; // apuntador al primero
    private int count;

    // Constructor
    public OrderedDoubleLinkedList ()
    {
        first = null;
        count = 0;
    }

    public boolean isEmpty ()
    //Determina si la lista está vacía
    {
        return first == null;
    }

    public int size ()
    //Determina el número de elementos de la lista
    {
        return count;
    }

    public void add (T elem)
    {
        //Añade ordenadamente el nuevo elemento
        //COSTE O(n)
        Node<T> newElem = new Node<>(elem);
        count++;
        if (isEmpty()) {
            first = newElem;
            newElem.next = newElem;
            newElem.prev = newElem;
        }
        else {
            Node<T> elemAct = this.first;
            boolean menor;
            do {
                menor = elemAct.data.compareTo(elem) < 0;
                elemAct = elemAct.next;
            } while (elemAct != this.first && !menor);

            //Si se ha encontrado un elemento menor -> se pondra antes que este
            if (menor) {
                elemAct = elemAct.prev;
                Node<T> elemPrev = elemAct.prev;
                newElem.next = elemAct;
                newElem.prev = elemPrev;
                elemAct.prev = newElem;
                elemPrev.next = newElem;
                if (elemAct.equals(first)) {
                    first = newElem;
                }
            }
            //Si no se encuentra ninguno mayor -> se situa al final
            else {
                newElem.prev = first.prev;
                newElem.next = first;
                first.prev.next = newElem;
                first.prev = newElem;
            }
        }
    }

    public Object[] getTop10 ()
    {
        int numPosiciones = -1;
        Iterator<T> itr = getIterator();
        Object[] top10 = new Object[10];

        while (numPosiciones < 9 && itr.hasNext()) top10[++numPosiciones] = itr.next();
        return top10;
    }

    /**
     * Return an iterator to the stack that iterates through the items .
     */
    private Iterator<T> getIterator ()
    {
        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<T> {
        Node<T> elemAct = first;

        //Constructora
        private ListIterator ()
        {
        }

        //Métodos
        @Override
        public boolean hasNext ()
        {
            //O(1)
            return (elemAct != null);
        }

        @Override
        public T next ()
        {
            //O(1)
            if (!hasNext()) throw new NoSuchElementException();
            T next = elemAct.data;
            elemAct = elemAct.next == first ? null : elemAct.next;
            return next;
        }

        @Override
        public void remove ()
        {
            throw new UnsupportedOperationException();
        }

    } // private class
}