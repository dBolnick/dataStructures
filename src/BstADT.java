package treepub;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 Interface: BstADT - Binary Search Tree Abstract Data Type

 The terms "elem" and "data" are used interchangeably

 Adding data -- data should be added into the tree like this:

 If newData is less than data at an existing node,
 - The newData should go to the left
 - Otherwise to the right

 Duplicates are allowed

 Visit (implementing):

 The param to the visit methods is a Consumer
 It is a simple function (without return value).
 To execute it (e.g. to "visit") you would just do this:

 visitFct.accept(data);

 Where "data" is the data for the current (visited) node.

 Visit (using/testing):

 Here is how simple it is to use the visit function.
 Here we print the tree in order:

 bst.visitInOrder(data -> System.out.println("" + data));

 Where "bst" is an instance of the BST tree
 Where "data" is the data (or elem) for the visited node

 Trick for printing nicely formatted tree:

 Nice print to console in formatted tree format
 Reverse your inorder traversal
 I.e. [traverse right]-[visit current]-[traverse left]
 Then, at each level, first indent, then print

 Generics: E is elem (data) type, K - key type
 */

public interface BstADT<K, V> {

    //------------------------ Queries (Core) -------------------------

    /*
    Return number of elements in this tree. Return int type.
    */
    int size();

    //--------------------- Adding ---------------------

    /**
     * Add elem (data) into tree in correct position (see above)
     * This method should also support "method cascading". All that
     * means is the return value should be tree itself (e.g. "return this").
     * Method cascading just makes our tree more user friendly.
     * So we can (optionally) use this pattern:
     bst
     .add(employee1)
     .add(employee2);
     * More on that -- https://en.wikipedia.org/wiki/Method_cascading
     * You do not need to worry about the details of "method cascading"
     */
    BstADT<K, V> add(V elem);

    //--------------------- Traversing ---------------------

    /**
     * Visit the tree using "in-order"
     * traversal
     */
    void visitInOrder(Consumer<V> visitFct);

    /**
     * Visit the tree using "pre-order"
     * traversal
     */
    void visitPreOrder(Consumer<V> visitFct);


    /**
     * Visit the tree using "post-order"
     * traversal
     */
    void visitPostOrder(Consumer<V> visitFct);

    //--------------------- Queries ---------------------

    /**
     * Return height of tree (*max* number of node levels)
     * Special cases:
     If tree is empty, height is zero (0).
     If tree only has root, height is one (1).
     */
    int height();

    /**
     * Return true if tree is empty,
     * otherwise false
     */
    boolean isEmpty();

    /**
     * Return the first match
     * If no match found, return null
     */
    V search(K key);

    /**
     * Return true if the tree contains the
     * key
     */
    boolean containsKey(K key);

    //-------------------------------------------------
    //Remove

    /** Remove the key */
    void removeKey(K key);

    //--------------------- Optional ---------------------

    /**
     * Return iterator for tree using
     * "in-order" traversing After
     * construction, the first "next" sent
     * to the iterator should return the
     * first node relative to "in-order"
     */
    Iterator<V> toIterator();

    /**
     * Return iterator for tree using
     * "pre-order" traversing After
     * construction, the first "next" sent
     * to the iterator should return the
     * first node relative to "pre-order"
     */
    Iterator<V> toPreOrderIterator();


    /**
     * This is real handy for debugging.
     * Do a nice print to console in
     * formatted tree format
     */
    default void printAsTree() {
        throw notImplemented();
    }

    //------------------------------------------------
    //Private

    private static RuntimeException notImplemented() {
        return new RuntimeException("Not Implemented");
    }



}
