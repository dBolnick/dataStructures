package hashpub; /**
Interface: hashTable.DictionaryIdea

The idea for a dictionary (dict).

Overview

	A dictionary operates like a traditional word dictionary.
	What we call "key" is like "word" in a traditional word dictionary.
	We use "get" to look up a value at the *key*. 
	We use "put" to put a *value* into the dict (at a given *key*).
	
	We might also think of the dictionary as a collection of "pairs"
	or "associations" where a "key" is associated with a "value".
	
Data Types

	Both "key" and "value" are generic.
	Thus, types are flexible and up to the dict user.
	Examples:
	
		A dict of Employees keyed by id (an Integer/Long)
		A dict of Cars keyed by vin numbers (a String) 

NOTE WELL - All of these methods, when implemented, must be public
 	(it is convention in interfaces not to show the access modifier)	

*/

import dynamicList.DynamicList;

import java.util.function.Function;
import java.util.function.Supplier;



public interface DictionaryIdea<K, V> {

	//--------------------- Adding ---------------------

	/**
	 * If param "key" is already present
	 * 	Then put replace current value at "key" with param "value"
	 * 	Else add new association
	 * Return previous value at key (or null if none)
	 */
	V put(K key, V value);

	//--------------------- Queries ---------------------

	/**
	 * Return the number of elements in the dictionary.
	 */
	int size();

	/**
	 * Return true if dictionary contains param "key", else false
	 */
	boolean containsKey(K key);

	/**
	 * Return value at param "key" or null if key not found
	 */
	V get(K key);

	/**
	 * If key is found, return value at key
	 * Otherwise construct new value using provider (with key as arg)
	 * 	and add value to this dict, and return value
	 * Very practical convenience function, e.g.:
	 * 	color = specDict.getIfAbsentPut(name, name -> new Spec(name));
	 */
	V getIfAbsentPut(K key, Function<K, V> provider);
	
	/**
	 * Return list containing all keys (order of keys is not specified)
	 */
	DynamicList<K> keys();

	//--------------------- Removing ---------------------

	/**
	 * Remove key (association) and return previous (removed) value
	 * If key is not found throw RuntimeException
	 */
	V removeKey(K key);

	/**
	 * Remove key (association) and return previous (removed) value
	 * If key is not found, then return value obtained from param "supplier"
	 * Useful when use does not want exception thrown:
	 * 		dict.removeKeyIfAbsent(key, () -> null);  
	 */
	V removeKeyIfAbsent(K key, Supplier<V> supplier);
	
	//--------------------- Optional ---------------------
	
	/**
	 * Optional method for hashed based classes that use "buckets"
	 * Return the bucket count (i.e., the number of buckets)
	 */	
	default int bucketCount() { return -1; }

}
