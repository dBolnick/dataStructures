package dynamicList;


import java.util.*;
import java.util.function.Function;

public class LinkedList<E> implements DynamicList<E>{

    private Node<E> head;
    private Node<E> tail;

    private int size;

    private Node<E> getHead() {
        return head;
    }
    private void setHead(Node<E> first) {
        this.head = first;
    }

    private Node<E> getTail() {
        return tail;
    }
    private void setTail(Node<E> last) {
        this.tail = last;
    }

    private int getSize() {
        return size;
    }
    private void setSize(int size) {
        this.size = size;
    }

    private LinkedList(){
        initialize();
    }

    public static <T> LinkedList<T> newEmpty(){
        return new LinkedList<>();
    }

    private void initialize(){
        this.setSize(0);
        this.setHead(null);
        this.setTail(null);
    }

    //
    @Override
    public int size() {
        return this.getSize();
    }

    //
    @Override
    public boolean isEmpty() {
        return this.getHead()==null;
    }

    //
    @Override
    public E get(int index) {
        return getNode(index).getData();
    }

    //
    private Node<E> getNode(int index){
        {
            if(isEmpty() || isOutOfBounds(index)){
                throw new IndexOutOfBoundsException();
            }
            //check if closer to head of tail
            // if closer to head
            var finish = this.getSize()-1;
            var distanceFromEnd = finish -index;
            Node<E> node;

            if(index < distanceFromEnd){

                //start from zero to get to index

                node = this.getHead();

                for(int i = 0; i < index; i++){

                    node = node.getNext();

                }

                // if closer to tail, start iteration from tail to index
            } else {

                node = this.getTail();

                for (int i = this.getSize() -1; i > index; i--){

                    node = node.getPrevious();

                }

            }
            return node;
        }
    }

    //
    @Override
    public E first() {
        var h = getHead();
        if(h == null){
            throw new NoSuchElementException();
        }
        return h.getData();
    }

    //
    @Override
    public E last() {
        var t = getTail();
        if(t==null){
            throw new NoSuchElementException();
        }
        return t.getData();
    }

    //
    @Override
    public void addFirst(E newElem) {
        final var first = getHead();
        final var n = new Node<>(newElem, null, first);

        this.setHead(n);

        if(first == null){
            setTail(n);
        } else {
            first.setPrevious(n);
        }

        var size = getSize();
        this.setSize(++size);
    }

    //
    @Override
    public void addLast(E newElem) {

        final var last = getTail();
        final var n = new Node<>(newElem, last, null);

        this.setTail(n);
        if(last == null){
            setHead(n);
        }else{
            last.setNext(n);
        }
        var size = getSize();
        this.setSize(++size);
    }

    //
    @Override
    public void add(E newElem) {
        addLast(newElem);
    }

    //
    @Override
    public E set(int index, E newElem) {

        ///////////////check in bounds
        if (isOutOfBounds(index)){
            throw new IndexOutOfBoundsException(String.format("(set) out of bounds at %d", index));
        }

        var node = getNode(index);
        var old = node.data;

        node.setData(newElem);

        return old;
    }

    //
    @Override
    public void insert(int insertIndex, E newElem) {
        if(insertIndex> getSize() || insertIndex < 0){
            throw new IndexOutOfBoundsException(String.format("(insert) out of bounds at index %d", insertIndex));
        }

        if(insertIndex == getSize()){
            add(newElem);
        } else if(insertIndex ==0){
            addFirst(newElem);
        }
        else {

            var indexNode = getNode(insertIndex);
            var prevNode = indexNode.getPrevious();

            var newNode = new Node<>(newElem, prevNode, indexNode);
            indexNode.setPrevious(newNode);

            if(prevNode == null){
                setHead(newNode);
            } else {
                prevNode.setNext(newNode);
            }
            var s = getSize();
            setSize(++s);
        }

    }

    //
    @Override
    public E removeFirst() {
        if(this.isEmpty()){
            throw new NoSuchElementException("list empty");
        }

        var first = getHead();
        var next = first.getNext();

        if(getSize()>1){

            next.setPrevious(null);
            setHead(next);

        } else {

            setHead(null);
            setTail(null);
        }

        var s = getSize();
        setSize(--s);

        return first.getData();
    }

    //
    @Override
    public E removeLast() {

        if(this.isEmpty()){
            throw new NoSuchElementException("list empty");
        }

        var last = getTail();
        var prev = last.getPrevious();

        if(getSize()>1){
            prev.setNext(null);
            setTail(prev);

        } else {
            setHead(null);
            setTail(null);
        }

        var s = getSize();
        setSize(--s);

        return last.getData();
    }

    //
    @Override
    public void removeAll() {
        initialize();
    }

    //
    @Override
    public E removeIndex(int index) {


        if(isOutOfBounds(index)){
            throw new IndexOutOfBoundsException(String.format("out of bounds at index: %d", index));
        }

        var n = getNode(index);

        removeNode(n);
        return n.getData();
    }

    private void removeNode(Node<E> n){

        var prev = n.getPrevious();
        var next = n.getNext();

        if(prev==null){
            removeFirst();
        } else if (next==null){
            removeLast();
        } else {
            prev.setNext(next);
            next.setPrevious(prev);
            var s = getSize();
            setSize(--s);
        }
    }

    @Override
    public E remove(Function<E, Boolean> searchFct) {
        var n = getHead();

        while(n!=null){
            if(searchFct.apply(n.getData())){

                removeNode(n);
                return n.getData();
            }
            else{
                n=n.getNext();
            }
        }

        return null;
    }

    //
    @Override
    public DynamicList<E> subList(int start, int stop) {
        if(stop > getSize()){
            throw new IndexOutOfBoundsException(String.format("index out of bounds at index: %d", stop));
        }
        if (start < 0){
            throw new IndexOutOfBoundsException(String.format("index out of bounds at index: %d", start));
        }
        LinkedList<E> d = new LinkedList<>();

        var n  = getNode(start);


        var index = start;
        while(index < stop) {
            d.add(n.getData());
            index++;
            n=n.getNext();
        }

        return d;
    }


    @Override
    public int find(Function<E, Boolean> searchFct) {
        var n= getHead();
        var index = 0;
        while (n!=null){
            if(searchFct.apply(n.getData())){
                return index;
            }
            else{
                n = n.getNext();
                index++;
            }
        }
        return -1;
    }

    @Override
    public List<E> toNativeList() {

        List<E> javaList = new ArrayList<>();
        for (int i = 0; i < getSize(); i++) {
            javaList.add(get(i));
        }
        return javaList;
    }

    @Override
    public void addAll(DynamicList<E> otherDynList) {
        for (E elem:otherDynList) {
            this.add(elem);
        }
    }

    @Override
    public String toString() {
         var l = toNativeList().toString() + "\n";
        return l + "Size: " + getSize();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            Integer index = 0;
            Node<E> node = getHead();


            @Override
            public boolean hasNext() {
                return index < getSize();
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                var current = node;
                index++;
                node = node.getNext();
                return current.getData();
            }
        };
    }

    private boolean isOutOfBounds(int index){
        return index >= this.getSize() || index <= -1;
    }

    private static class Node<E>{
        private E data;
        private Node<E> previous, next;

        public E getData() {
            return data;
        }
        public void setData(E data) {
            this.data = data;
        }
        public Node<E> getPrevious() {
            return previous;
        }
        public void setPrevious(Node<E> previous) {
            this.previous = previous;
        }
        public Node<E> getNext() {
            return next;
        }
        public void setNext(Node<E> next) {
            this.next = next;
        }


        Node(E data, Node<E> previous, Node<E> next){
            this.data = data;
            this.previous = previous;
            this.next = next;
        }
    }

}
