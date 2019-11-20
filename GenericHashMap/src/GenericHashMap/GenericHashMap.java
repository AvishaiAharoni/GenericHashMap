package GenericHashMap;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import GenericPairs.GenericPair;;

/**
 * @author Avishai
 *
 * @param <K> - key
 * @param <V> - value
 */
public class GenericHashMap<K, V> implements Map<K, V> {	
	private List<ArrayList<GenericPair<K, V>>> myHash;										
	private int size;										
	private int modCount;
	private final int capacity;
											
	/**
	 * constructor
	 * @param capacity for the size of the hash map
	 */
	public GenericHashMap(int capacity) {
		this.myHash = new ArrayList<>(capacity);
		this.capacity = capacity;
		
		// init the hash map with empty array lists
		for (int i = 0; i < this.capacity; ++i) {
			this.myHash.add(new ArrayList<>());
		}	
	}
	
	/**
	 * class for the iterator of the value type.
	 * it can be used to do an iteration all over the values
	 */
	class ValueType extends AbstractCollection<V> {										

		/* 
		 * to create a new iterator for the value type
		 * @see java.util.AbstractCollection#iterator()
		 */
		@Override								
		public Iterator<V> iterator() {								
			return new ValueIterator();							
		}								
										
		 /* 
		 * to return the size of the elements in the hash map
		 * @see java.util.AbstractCollection#size()
		 */
		@Override								
		public int size() {								
			return GenericHashMap.this.size;							
		}								
										
		class ValueIterator implements Iterator<V> {								
			private Iterator<Map.Entry<K, V>> iter;
							
			private ValueIterator() {
				this.iter = entrySet().iterator();
			}

			@Override							
			public boolean hasNext() {							
				return this.iter.hasNext();
			}							
										
			@Override							
			public V next() {							
				return this.iter.next().getValue();						
			}							
		}								
	}										
										
										
	/**
	 * class for the iterator of the key type.
	 * it can be used to do an iteration all over the keys
	 */
	class KeyType extends AbstractSet<K> {										
		
		/* 
		 * to create a new iterator for the key type
		 * @see java.util.AbstractCollection#iterator()
		 */
		@Override										
		public Iterator<K> iterator() {										
			return new KeyIterator();										
		}										
												
		/* 
		 * to return the size of the elements in the hash map
	   	 * @see java.util.AbstractCollection#size()
		 */
		@Override										
		public int size() {										
			return GenericHashMap.this.size;										
		}										
		
		class KeyIterator implements Iterator<K>{										
			private Iterator<Map.Entry<K, V>> iter;
			
			private KeyIterator() {
				this.iter = entrySet().iterator();
			}

			@Override
			public boolean hasNext() {
				return this.iter.hasNext();
			}
										
			@Override							
			public K next() {							
				return this.iter.next().getKey();						
			}							
		}								
	}										
	
	
	/**
	 * class for the iterator of the EntrySet.
	 * it can be used to do an iteration all over the values
	 */
	class EntrySetType extends AbstractSet<Map.Entry<K,V>> {

		/* 
		 * to create a new iterator for the EntrySet
		 * @see java.util.AbstractCollection#iterator()
		 */
		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return new EntrySetIterator();
		}										
												
		/* 
		 * to return the size of the elements in the hash map
	   	 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return GenericHashMap.this.size;
		}
												
		class EntrySetIterator implements Iterator<Map.Entry<K, V>> {
			private int modCounterChecker;
			private int idxInHash;
			private int idxInList;

			/**
			 * constructor for the iterator
			 * init the index of the hash map to the first item.
			 */
			EntrySetIterator() {
				this.modCounterChecker = GenericHashMap.this.modCount;
				this.idxInHash = findNextIdxInHash(0);
			}
			
			/* 
			 * to check if the iterator has a next item
			 * @see java.util.Iterator#hasNext()
			 * @return true if has next.
			 */
			@Override
			public boolean hasNext() {
				return this.idxInHash < GenericHashMap.this.capacity;
			}							
										
			/* 
			 * to move the iterator to the next item.
			 * @see java.util.Iterator#next()
			 * @return the previous of the GenericPair.
			 * @throw ConcurrentModificationException if there was a modification while the iteration.
			 * @throw NoSuchElementException if there isn't next to this item.
			 */
			@Override
			public Map.Entry<K, V> next() {
				// check the flag modCount and modCountChecker
				if (this.modCounterChecker != GenericHashMap.this.modCount) {
					throw new ConcurrentModificationException();
				}
				
				// if there is no next to the current iterator
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				
				// the old GenericPair (before the move)
				GenericPair<K, V> oldGp = GenericHashMap.this.myHash.get(this.idxInHash).get(this.idxInList);
				
				// there is another item in this list, but in another idxInHash
				if (++this.idxInList >= GenericHashMap.this.myHash.get(this.idxInHash).size()) {
					this.idxInList = 0;
					
					idxInHash = findNextIdxInHash(++idxInHash);
				}
				
				return oldGp;
			}							
		}
		
		/**
		 * help method to find the next index that not empty and has items
		 * @param idxInHash - the index to search items
		 * @return the next index in the hash that has items
		 */
		private int findNextIdxInHash(int idxInHash) {
			while ((idxInHash < GenericHashMap.this.capacity) && 
				   (GenericHashMap.this.myHash.get(idxInHash).isEmpty())) {
				++idxInHash;
			}
			
			return idxInHash;
		}
	}										
											
	/* 
	 * to clear all the array lists in the hash map.
	 * also update the size to 0.
	 * @see java.util.Map#clear()
	 */
	@Override										
	public void clear() {
		for (ArrayList<GenericPair<K, V>> al : this.myHash) {
			al.clear();
		}

		this.size = 0;
		++this.modCount;
	}										
											
	/* 
	 * to check if a given key is in the hash map.
	 * @param key - the key to search
	 * @see java.util.Map#containsKey(java.lang.Object)
	 * @return true if the key exists.
	 */
	@Override										
	public boolean containsKey(Object key) {
		final int idxInList = idxOfKeyInList(idxOfKeyInHash(key), key);

		return idxInList != -1;
	}

	/* 
	 * to check if a given value is in the hash map.
	 * @param value - the key to search
	 * @see java.util.Map#containsValue(java.lang.Object)
	 * @return true if the value exists.
	 */
	@Override
	public boolean containsValue(Object value) {
		final Collection<V> coll = values();
		
		for (V val : coll) {
			if (val.equals(value)) {
				return true;
			}
		}
		
		return false;										
	}
										
	/* 
	 * to get a new EntrySet
	 * @see java.util.Map#entrySet()
	 */
	@Override										
	public Set<Entry<K, V>> entrySet() {
		return new EntrySetType();										
	}										

	/* 
	 * to get the value from a given key in the hash map.
	 * @param key - the key to search
	 * @see java.util.Map#get(java.lang.Object)
	 * @return the value (if removed the Pair) or null (if the key didn't exist).
	 */
	@Override										
	public V get(Object key) {										
		final int idxInHash = idxOfKeyInHash(key);
		final int idxInList = idxOfKeyInList(idxInHash, key);

		return (idxInList != -1) ? getVal(idxInHash, idxInList) : null;
	}										
											
	/* 
	 * to check if the hash map has elements.
	 * @see java.util.Map#isEmpty()
	 * @return true if it has no elements
	 */
	@Override										
	public boolean isEmpty() {										
		return this.size == 0;										
	}										
											
	/* 
	 * to get a new key set
	 * @see java.util.Map#keySet()
	 */
	@Override										
	public Set<K> keySet() {
		return new KeyType();										
	}										
	
	/* 
	 * to insert a new key and value to the hash map.
	 * if the key is already in the hash map - updates the value to the new one,
	 * if not - insert a new GenericPair.
	 * @param key - the key to search
	 * @param value - the new value to update or insert
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 * @return the old value (if updated the value) or null (if insertes new Pair).
	 */
	@Override										
	public V put(K key, V value) {										
		V retVal = null;
		final int idxInHash = idxOfKeyInHash(key);
		final int idxInList = idxOfKeyInList(idxInHash, key);
		
		++this.modCount;

		// found the key in the array list - need to update the value
		if (idxInList != -1) {
			retVal = getVal(idxInHash, idxInList);
			getPairFromHash(idxInHash, idxInList).setValue(value);
		}
		// need to insert new GenericPair
		else {
			this.myHash.get(idxInHash).add(GenericPair.of(key, value));
			++this.size;
		}
		
		return retVal;
	}										
											
	/* 
	 * to insert a group of new keys and values to the hash map.
	 * if the key is already in the hash map - updates the value to the new one,
	 * if not - insert a new GenericPair.
	 * @param m - the map with all the keys and values
	 * @see java.util.Map#putAll(java.util.Map)
	 * has the same effact like loop put all over the map
	 */
	@Override										
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> me : map.entrySet()) {
			put(me.getKey(), me.getValue());
		}
	}										
											
	/* 
	 * to remove a given key from the hash map.
	 * @param key - the key to search
	 * @see java.util.Map#remove(java.lang.Object)
	 * @return the old value (if removed the Pair) or null (if the key didn't exist).
	 */
	@Override										
	public V remove(Object key) {										
		V retVal = null;
		final int idxInHash = idxOfKeyInHash(key);
		final int idxInList = idxOfKeyInList(idxInHash, key);

		if (idxInList != -1) {
			retVal = getVal(idxInHash, idxInList);
			this.myHash.get(idxInHash).remove(idxInList);
			--this.size;
			++this.modCount;
		}
		
		return retVal;
	}										
											
	/* 
	 * to return the size of the elements in the hash map
	 * @see java.util.Map#size()
	 */
	@Override										
	public int size() {
		return this.size;										
	}
											
	/* 
	 * to get a collection of the values
	 * @see java.util.Map#values()
	 */
	@Override										
	public Collection<V> values() {
		return new ValueType();
	}
	
	
	/***************************************************************************/
	/*****************************help methods**********************************/
	/***************************************************************************/
	
	/**
	 * to find a given key in an array list in a given index of the hash map
	 * @param idxOfArrayList - the index in the hash map to search
	 * @param key - the key to search
	 * @return the index in the array list of the key that found.
	 * 			if not found - return -1.
	 */
	private int idxOfKeyInList(int idxInHash, Object key) {
		int idx = 0;
		
		for (Map.Entry<K, V> m : myHash.get(idxInHash)) {
			if (m.getKey().equals(key)) {
				return idx;
			}
			++idx;
		}
		
		// didn't find
		return -1;
	}
	
	/**
	 * to find a given key in the hash map
	 * @param key - the key to search
	 * @return the index in the hash map of the key that found.
	 */
	private int idxOfKeyInHash(Object key) {
		return key.hashCode() % this.capacity;
	}
	
	/**
	 * to get the value in given indexes
	 * @param idxInHash
	 * @param idxInList
	 * @return the value of the GenericPair
	 */
	private V getVal(int idxInHash, int idxInList) {
		return getPairFromHash(idxInHash, idxInList).getValue();
	}
	
	/**
	 * to get the GenericPair in the hash map fom given indexes
	 * @param idxInHash
	 * @param idxInList
	 * @return the GenericPair
	 */
	private GenericPair<K, V> getPairFromHash(int idxInHash, int idxInList) {
		return this.myHash.get(idxInHash).get(idxInList);
	}
}