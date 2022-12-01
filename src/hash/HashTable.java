package hash;

import dynamicList.DynamicArray;
import dynamicList.DynamicList;
import dynamicList.LinkedList;
import hashpub.DictionaryIdea;

import java.util.function.Function;
import java.util.function.Supplier;

public class HashTable<k,v> implements DictionaryIdea<k,v> {

    //instance variables
    private static final int DEFAULT_CAP = 16;
    private static final double DEFAULT_MAX_LOAD = .75;
    private final double maxLoadRatio;

    private int size;
    private DynamicArray<LinkedList<Entry<k, v>>> buckets;


    //constructors

    private HashTable(int initCapacity, double maxLoadRatio){
        if(initCapacity < 0)
            throw new IllegalArgumentException("capacity must be greater than zero");
        if(maxLoadRatio < 0)
            throw new IllegalArgumentException("maxLoadRatio must be greater than zero");

        this.buckets = initBuckets(initCapacity);
        this.size = 0;
        this.maxLoadRatio = maxLoadRatio;
    }

    public static <k,v>HashTable<k,v> newEmpty(){
        return fromCapacityMaxLoadRatio(DEFAULT_CAP, DEFAULT_MAX_LOAD);
    }

    public static <k,v> HashTable<k,v> fromCapacityMaxLoadRatio(int initCapacity, double maxLoadRatio){
        return new HashTable<>(initCapacity, maxLoadRatio);
    }

    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public v put(k key, v value) {

        if(value == null){
            throw new NullPointerException("value cannot be equal to null");
        }
        if(key == null){
            throw new NullPointerException("value cannot be equal to null");
        }

        var ratio = (double)size()/bucketCount();

        if(ratio >= maxLoadRatio){
            rehash();
        }
        var index = hashToIndex(hash(key));

        var table = buckets;
        var bucket = table.get(index);

        var bucketIndex = bucket.find(kvEntry -> kvEntry.key.equals(key));

        if(bucketIndex == -1){
            bucket.add(new Entry<>(key, value));
            size++;
        } else {
            var e = bucket.get(bucketIndex);
            var old = e.value;
            e.value = value;

            return old;
        }


        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(k key) {

        var index = hashToIndex(hash(key));
        var bucket = buckets.get(index);

        for (int i = 0; i < bucket.size(); i++) {
            if (bucket.get(i).key.equals(key))
                return true;
        }

        return false;
    }

    @Override
    public v get(k key) {
        v w = null;
        var s = hashToIndex(hash(key));
        var table = buckets;
        var bucket = table.get(s);
            for (int i = 0; i <bucket.size() ; i++) {

                var current = bucket.get(i);
                if(current.key.equals(key)){
                    w = current.getValue();
                    break;
                }
            }

        return w;
    }


    @Override
    public v getIfAbsentPut(k key, Function<k, v> provider) {

        var g = get(key);

        if(g == null){
            var value = provider.apply(key);
            put(key, value);

            return value;
        } else return g;
    }

    @Override
    public DynamicList<k> keys() {
        DynamicList<k> d = DynamicArray.newEmpty();

        for (int i = 0; i < buckets.size(); i++) {
            if(!buckets.get(i).isEmpty())
                for (int j = 0; j < buckets.get(i).size(); j++) {
                    d.add(buckets.get(i).get(j).key);
                }
        }

        return d;
    }

    private v remove(k key) {
        var index = hashToIndex(hash(key));
        v v = null;

        var bucket = buckets.get(index);
        for (int i = 0; i < bucket.size(); i++) {
            var current = bucket.get(i);
            if(current.key.equals(key)){
                v = current.value;

                bucket.removeIndex(i);
                size--;
            }

        }
        return v;
    }

    @Override
    public v removeKey(k key) {
        v v = remove(key);

        if(v == null){

            throw new RuntimeException(String.format("key: %s does not exist", key));
        }
        return v;
    }


    @Override
    public v removeKeyIfAbsent(k key, Supplier<v> supplier) {
        var j = remove(key);

        if(j == null){

            j = supplier.get();

        }

        return j;
    }

    @Override
    public int bucketCount() {
        return buckets.size();
    }

    public String toString(){

        if (size == 0){
            return "{}";
        }

        StringBuilder sb = new StringBuilder();

        sb.append('{');

        for (int i = 0; i < bucketCount(); i++) {

            var bucket = buckets.get(i);

            if (!bucket.isEmpty()){
                for (int j = 0; j < bucket.size(); j++) {
                    sb.append(bucket.get(j).toString()).append(", ");
                }
            }
        }

        return sb.append('}').toString();
    }

    //utils
    private DynamicArray<LinkedList<Entry<k,v>>> initBuckets(int length){
        DynamicArray<LinkedList<Entry<k,v>>> newBuckets = DynamicArray.newEmpty();
        for (int i = 0; i < length; i++) {
            newBuckets.add(LinkedList.newEmpty());
        }
        return newBuckets;
    }

    private int hash(k key){
        int hash;
        if (key instanceof Integer){
            var h = (Integer)key;
            hash = Integer.hashCode(h);
        } else {
            hash = key.hashCode();
        }

        return hash;
    }

    private int hashToIndex(int hash){
        hash = Math.abs(hash);

        return hash % bucketCount();
    }

    private void rehash(){
        int oldSize = bucketCount();
        var oldTable = buckets;

        int newSize = (oldSize << 1);

        buckets = initBuckets(newSize);

        size = 0;

        for (int i = 0; i < oldSize; i++) {
            for (int j = 0; j < oldTable.get(i).size(); j++) {
                var elem = oldTable.get(i).get(j);

                put(elem.key, elem.value);
            }
        }
    }


    //entry
    private static class Entry<k,v> {

        final k key;
        v value;

        public k getKey() {
            return key;
        }
        public v getValue() {
            return value;
        }

        public v setValue(v value) {
            if(value == null){
                throw new NullPointerException();
            }

            v old = this.value;

            this.value = value;

            return old;
        }

        Entry(k key, v value){
            this.key = key;
            this.value = value;
        }

        public String toString() {return key.toString() + " = " + value.toString();}
    }
}
