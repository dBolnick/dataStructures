//DynamicArray.java

/*
	To-Dos:

	1. Search for "//TODO" tag
	2. Replace tag with your Java code
	3. You will see some code already completed to help you get started
	4. This should compile without errors (most important)
	5. It is suggested to test the results (the grading is results-based)	
*/

package dynamicList;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class DynamicArray<E> implements DynamicList<E> {
	
    //---------------------------------
    // Instance Variables	
	
	private E[] fixedArray;//sensitive ivar (access only via privateGetFixedArray and privateSetFixedArray)
	private int capacity;
	private int size;

	private int getCapacity() {
		return capacity;
	}

	private int setCapacity(int newCapacity){
		this.capacity = newCapacity;
		return capacity;
	}


	private void setSize(int size) {
		this.size = size;
	}

	//---------------------------------
    // Accessor Methods (Private)
	
	private E[] privateGetFixedArray() {
		return fixedArray;
	}
	private void privateSetFixedArray(E[] aFixedArray) {
		this.fixedArray = aFixedArray;
	}
	
    //---------------------------------
    // Public Constructors

    public static <T> DynamicArray<T> newEmpty() {
        /* Return a new empty DynamicArray
        Static factory constructor
        Usage:
            let darray = DynamicArray.newEmpty(); */
        return new DynamicArray<>();
    }
    
    public static <T> DynamicArray<T> from(T[] aFixedArray) {
        /* Return a new DynamicArray that contains all elements from the arg "fixedArray"
        Static factory constructor
        e.g.,
            let a = DynamicArray.from([10, 20, 30, 40]) */
    	DynamicArray<T> dynamic = new DynamicArray<>();
    	dynamic.addFixedArray(aFixedArray);
        return dynamic;
    }	
    
    //---------------------------------
    // Private Constructors
    
	private DynamicArray() {
		initialize();
	}

	
	//---------------------------------
	// Basic Statistics
	
	@Override
	public int size() {

		return size;
	}

	@Override
	public boolean isEmpty() {
		// Return true if we are empty (our size is zero)		
		return size() == 0;
	}
	
	@Override
	public E get(int index) {

	// Validate the index
	if ( (index < 0) || !(index < this.size()) )
		throw new RuntimeException(String.format("Index %d out of bounds", index));

	//Simply delegate (get from) our child ivar "fixed array"
	// Note we go through accessor "privateGetFixedArray" as we
	// need to be careful with this sensitive private ivar
	// Using getters is generally good practice (as opposed to
	// direct ivar access) -- it helps when debugging is needed
	// as we can get a "debugging handle" on an accessor much
	// easier than an ivar
	return this.privateGetFixedArray()[index];		
	}
	

	@Override
	public DynamicList<E> subList(int start, int stop) {
		/*
		Return a new DynamicArray containing the elements of this list
		between the given index "start" (inclusive) and
		the given index "stop" (exclusive).
		Throws exception if either passed index is invalid.
		*/
		checkRangeForSubList(start, stop, this.size());

		DynamicList<E> newList = DynamicArray.newEmpty();

		for(int i = start; i < stop; i++){
			newList.add(this.get(i));
		}
		return newList;
		


		
	}	

	@Override
	public E first() {
		/* Return first element
		Throws exception if list is empty */
		if (isEmpty())
			throw new RuntimeException("(first) Attempted to access element in empty list");
		return this.get(0);		
	}
	

	@Override
	public E last() {
		/* Return last element
		If list is empty, throws exception:
			"(last) Attempted to access element in empty list" */
		if (isEmpty())
			throw new RuntimeException("(last) Attempted to access element in empty list");

		return this.get(this.size()-1);
	}
	
	
	@Override
	public int find(Function<E, Boolean> searchFct) {
		/* Return index of first matching element (where searchFct outputs true)
		Return -1 if no match
		Example usage (first list of integers, then employees):
			matchingIndex1 = numbers.find(nextInteger => nextInteger == 10);
			matchingIndex2 = employees.find(nextEmployee => nextEmployee.getFirstName().equals("Kofi")); */

		for (int i = 0; i < this.size(); i++) {
			var elem = this.get(i);
			if(searchFct.apply(elem)){
				return i;
			}
		}
		return -1;
	}	
	
	
	//---------------------------------
	// Adding Elements	
	
	
	@Override
	public void addFirst(E newData) {
		/* Add the passed arg "newElem" to start of list */

		insert(0, newData);
	}


	@Override
	public void addLast(E newData) {

		if(privateGetFixedArray().length == this.size()){
			this.privateSetFixedArray(Arrays.copyOf(privateGetFixedArray(), setCapacity(2 * capacity)));
		}
		this.privateGetFixedArray()[size()] = newData;
		setSize(size()+1);
	}

	@Override
	public void add(E newData) {
		/* Alias for "addLast" (same functionality).
		e.g.,
		Add arg "newElem" into "this" object.
		usage:
		let aa = DynamicArray.newEmpty();
		aa.add('foo');
		println('Elem: ' + aa.get(0));
		//prints: Elem: foo */
		this.addLast(newData);
	}
	
	
	@Override
	public void addAll(DynamicList<E> other) {
		// Add all elements from "otherDynList" into "this" list
		for(int i = 0; i < other.size(); i++){
			this.add(other.get(i));
		}
	}	
		

	@Override
	public E set(int index, E newElem) {
		/* 	Insert passed arg "newElem" into position "index"
		Return previous (replaced) elem at "index"
		Valid "index" values are between 0 and "size - 1"
		If "index" is invalid, throws exception:
			"(set) Index %d is out of bounds"
		e.g.,
		let array = MyArray.from([10, 20, 30, 40])
		array.set(1, 101)
		println(array.toString()))
		//elements: [10, 101, 30, 40]
		array.set(3, 999)
		println(array.toString()))
		//elements: [10, 101, 30, 999] */
		ifOutOfBounds(index);

		var elem = privateGetFixedArray()[index];
		privateGetFixedArray()[index] = newElem;
		return elem;
	}	
	
	
	@Override
	public void insert(int insertion, E newData) {
		/* Shift to the right the element currently at "insertIndex" (if any) and all elements to the right
		Insert passed arg "newElem" into position "insertIndex"
		Valid "insertIndex" values are between 0 and "size"
		If index = "size" then it becomes a simple "add" operation
		If "insertIndex" is invalid, throws exception:
			"(insert) Index %d is out of bounds" */

		ifOutOfBounds(insertion);

		final var arr = this.privateGetFixedArray();


		final int size = size();
		if(size() == arr.length)
			this.privateSetFixedArray(Arrays.copyOf(privateGetFixedArray(), setCapacity(2 * getCapacity())));
		System.arraycopy(arr, insertion, this.privateGetFixedArray(), insertion + 1, size() - insertion);

		this.privateGetFixedArray()[insertion] = newData;
		setSize(size +1);
	}	
	
	
	//---------------------------------
	// Removing Elements

	@Override
	public void removeAll() {
		/* Reset the list so it is empty.
		If list is already empty, then do nothing
		No action need be performed on individual elements. */

		if(this.privateGetFixedArray().length != 0){
			initialize();
		}
	}

	@Override
	public E removeFirst() {
		/* Remove first element
		Return the removed element
		If list is empty (before remove), throws exception:
			"(removeFirst) Attempted to access empty list" */
		ifEmpty("removeFirst");

		//Nice easy one -- just delegate the work to another method.
		return this.removeIndex(0);
	}

	@Override
	public E removeLast() {
		/* Remove last element
		Return removed element
		If list is empty (before remove), throws exception:
			"(removeLast) Attempted to access empty list" */
		ifEmpty("removeLast");
		return this.removeIndex(size()-1);
	}


	@Override
	public E removeIndex(int index) {

		ifOutOfBounds(index);
		ifEmpty("removeIndex");

		final E oldElem = get(index);

		removeStuff(index);

		return oldElem;
	}	
	

	@Override
	public E remove(Function<E, Boolean> searchFct) {
		/* Remove first matching element (where searchFct outputs true)
		Return the removed element
		If no match, return null */

		for (int i = 0; i < this.size(); i++) {
			var elem = this.get(i);
			if(searchFct.apply(elem)){
				removeStuff(i);
				return elem;
			}
		}
		return null;
	}	
	
	
	//---------------------------------
	// Convenience	

	@Override
	public List<E> toNativeList() {
		//We can't do the next line because a() thinks it's size is 10 (capacity)
		//return new ArrayList<>(Arrays.asList(a()));
		List<E> javaList = new ArrayList<>();
		for (int i = 0; i < size; i++)
			javaList.add(get(i));
		return javaList;
	}

	@Override
	public String toString() {
		int max = 4;
		String postfix = "";
		List<E> printables;
		var size= this.size();
		if (size > max) {
			printables = subList(0, max+1).toNativeList(); 
			postfix = "... (size=" + size() + ")";			
		}
		else
			printables = toNativeList();
		return Arrays.toString(printables.toArray()) + postfix;
	}

	//---------------------------------
	// Initializing


	private void initialize() {
		int DEFAULT_CAP = 20;
		capacity = DEFAULT_CAP;
		size = 0;
		privateSetFixedArray((E[]) new Object[DEFAULT_CAP]);
	}
	
	
	//==========================================================
	//Private
	
	public void addFixedArray(E[] aFixedArray) {
		for (E nextNewElem: aFixedArray)
			this.add(nextNewElem);
	}


	// utils


	private void removeStuff(int index){

		ifOutOfBounds(index);

		final var newSize = size() -1;
		final var arr = this.privateGetFixedArray();

		if(newSize > index){
			System.arraycopy(arr, index + 1, this.privateGetFixedArray(), index, newSize - index);
		}
		this.privateGetFixedArray()[newSize] = null;
		setSize(newSize);
	}

	private void checkRangeForSubList(int startIndex, int stopIndex, int size){
		if(startIndex < 0)
			throw new RuntimeException("starting index out of bounds: " + startIndex);
		if(stopIndex > size)
			throw new RuntimeException("ending index out of bounds: " + stopIndex);
	}

	private void ifOutOfBounds(int index){
		if ( (index < 0) || !(index < this.size()) )
			throw new RuntimeException(String.format("Index %d out of bounds", index));
	}

	private void ifEmpty(String message){
		if (isEmpty())
			throw new RuntimeException(String.format("(%s) Attempted to access element in empty list", message));
	}
	

}
