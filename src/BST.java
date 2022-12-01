package bst;

import treepub.BstADT;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class BST<K,V> implements BstADT<K,V> {

    private void setRoot(BSTNode<V> root) {
        this.root = root;
    }

    private BSTNode<V>  root;
    private int size;
    private final Comparator<V> sort;
    private final BiFunction<K, V, Integer> search;


    private BST(Comparator<V> aSortFct, BiFunction<K, V, Integer> aSearchFct) {
        this.sort= aSortFct;
        this.search = aSearchFct;
        this.root = null;
        this.size = 0;
    }

    public static <K, V> BST<K, V> fromSortFctSearchFct(Comparator<V> aSortFct, BiFunction<K, V, Integer> aSearchFct){
        return new BST<>(aSortFct, aSearchFct);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public BstADT<K,V> add(V elem) {
        if(elem ==null)
            throw new NullPointerException("cannot add null element");

        var node = this.root;
        if(node == null){

            setRoot(new BSTNode<>(elem, null));
            size=1;

        } else {


            BSTNode<V> parent;
            int compare;
            do {
                parent = node;
                compare =   sort.compare( elem,node.value);

                if(compare < 0)
                    node = node.left;
                else if(compare > 0)
                    node=node.right;
            } while(node!=null);

            var n = new BSTNode<>(elem, parent);

            if(compare <0)
                parent.left = n;
            else
                parent.right = n;
            
            size++;
        }



        return this;
    }

    @Override
    public void visitInOrder(Consumer<V> visitFct) {
        inOrder(root, visitFct);
    }

    private void inOrder(BSTNode<V> node ,Consumer<V> visitFct){
        if (node == null)
            return;

        inOrder(node.left, visitFct);

        visitFct.accept(node.value);

        inOrder(node.right, visitFct);
    }

    @Override
    public void visitPreOrder(Consumer<V> visitFct) {
        preOrder(root, visitFct);
    }

    private void preOrder(BSTNode<V> node, Consumer<V> visitFct){
        if(node == null){
            return;
        }
        visitFct.accept(node.value);

        preOrder(node.left, visitFct);
        preOrder(node.right, visitFct);
    }

    @Override
    public void visitPostOrder(Consumer<V> visitFct) {
        postOrder(root, visitFct);
    }
    private void postOrder(BSTNode<V> node, Consumer<V> visitFct){
        if(node == null)
            return;

        postOrder(node.left, visitFct);

        postOrder(node.right, visitFct);

        visitFct.accept(node.getValue());
    }

    @Override
    public int height() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return root==null;
    }

    @Override
    public V search(K key) {
        var node = get(key);
        if(node == null)
            return null;
        else
            return node.value;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public void removeKey(K key) {
        var toRemove = get(key);

        if(toRemove==null){
            throw new NoSuchElementException(String.format("key: %s does not exist", key));
        }

        var parent = toRemove.parent;

        if(parent == null){
            this.root=null;

        }
        size--;

        if(toRemove.left == null && toRemove.right == null){

            assert parent != null;
            if(parent.left!=null&&parent.left.equals(toRemove)){
                parent.left = null;
            } else if(parent.right!=null&&parent.right.equals(toRemove)){
                parent.right = null;
            }
            toRemove = null;

        } else if (toRemove.right==null){

            assert parent != null;
            if(parent.left!=null&&parent.left.equals(toRemove)){
                parent.left = toRemove.left;
            }
            if(parent.right!=null&&parent.right.equals(toRemove)){
                parent.right = toRemove.left;
            }

        } else if (toRemove.left==null){

            assert parent != null;
            if(parent.left!=null &&parent.left.equals(toRemove)){
                parent.left = toRemove.right;
                parent.left.parent = parent;
            }
            if(parent.right!=null && parent.right.equals(toRemove)){
                parent.right = toRemove.right;
                parent.right.parent = parent;
            }


        } else {

            assert parent != null;
            if (parent.left!=null&&parent.left.equals(toRemove)){
                parent.left = toRemove.left;
                parent.left.parent = parent;

                // go to last on the right
                BSTNode<V> t;
                BSTNode<V> temp = parent.left;
                do{
                    t = temp;
                    temp = temp.right;

                }while(temp!=null);

                t.right = toRemove.right;
                toRemove.right.parent = t.right;

            }
            if (parent.right!=null&&parent.right.equals(toRemove)){
                parent.right = toRemove.left;
                parent.right.parent = parent;
                BSTNode<V> t;
                BSTNode<V> temp = parent.right;
                do{
                    t = temp;
                    temp = temp.right;

                }while(temp!=null);

                t.right = toRemove.right;
                toRemove.right.parent = t.right;

            }

        }
    }

    @Override
    public Iterator<V> toIterator() {

        return null;
    }

    @Override
    public Iterator<V> toPreOrderIterator() {
        return null;
    }

    private BSTNode<V> get(K key){
        if (key == null){
            throw new NullPointerException();
        }

        var node = this.root;
        while(node != null){

            int compare = search.apply(key, node.value);

            if (compare < 0)
                node = node.left;
            else if (compare > 0)
                node = node.right;
            else
                return node;

        }
        return null;
    }

    private static class BSTNode<V>{

        private BSTNode<V> left, right, parent;

        private V value;

        public BSTNode(V data, BSTNode<V> parent) {
            this.left = null;
            this.right = null;
            this.parent = parent;
            this.value = data;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V old = this.getValue();
            this.value = value;
            return old;
        }

        public String toString() {return this.value.toString();}
    }
}
