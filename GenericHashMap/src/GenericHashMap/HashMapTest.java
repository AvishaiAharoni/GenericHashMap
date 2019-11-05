package GenericHashMap;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HashMapTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Test
	void testIsEmpty() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		assertSame(hm.isEmpty(), true);
		
		hm.put("evya", 31);
		
		assertSame(hm.isEmpty(), false);
		
		hm.remove("evya");
		
		assertSame(hm.isEmpty(), true);
	}
	
	@Test
	void testSize() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		assertSame(hm.size(), 0);
		
		hm.put("evya", 31);
		
		assertSame(hm.size(), 1);
		
		hm.put("evy", 30);
		
		assertSame(hm.size(), 2);
		
		hm.remove("evy");
		
		assertSame(hm.size(), 1);
	}
	
	@Test
	void testGet() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		
		hm.put("evya", age);
		
		assertSame(age, hm.get("evya"));
		
		hm.put("evy", age2);
		
		assertSame(age2, hm.get("evy"));
	}
	
	@Test
	void testGetNotExist() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		
		hm.put("evya", age);
		
		assertSame(null, hm.get("evy"));
	}
	
	@Test
	void testPutRetValue() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 32;
		
		assertSame(null, hm.put("evya", age));
		
		assertSame(age, hm.put("evya", age2));
	}
	
	@Test
	void testContainsKey() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		
		hm.put("evya", age);
		
		hm.put("evy", age2);
		
		assertSame(true, hm.containsKey("evya"));
		assertSame(true, hm.containsKey("evy"));
		assertSame(false, hm.containsKey("ev"));
	}
	
	@Test
	void testRemove() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		Integer ret;
		Integer retNull;
		
		hm.put("evya", age);
		
		hm.put("evy", age2);
				
		ret = hm.remove("evy");
		
		retNull = hm.remove("evy");
		
		assertSame(ret, age2);
		assertSame(retNull, null);
	}
	
	@Test
	void testContainsValue() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		
		hm.put("evya", age);
		
		hm.put("evy", age2);
		
		assertSame(true, hm.containsValue(age2));
				
		hm.remove("evy");
		
		assertSame(true, hm.containsValue(age));
		assertSame(false, hm.containsValue(age2));
	}
	
	@Test
	void testEntrySetSize() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		
		hm.put("evya", age);
		
		hm.put("evy", age2);
		
		Set<Entry<String, Integer>> s = hm.entrySet();
		
		assertSame(s.size(), 2);
	}

	@Test
	void testEntrySet() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		Integer sum = 0;
		
		hm.put("evya", age);
		
		hm.put("evy", age2);
		
		Set<Entry<String, Integer>> s = hm.entrySet();
		
		assertSame(2, s.size());
		
		for (Entry<String, Integer> gp : s) {
			sum += gp.getValue();
		}
		
		assertSame(sum.intValue(), 61);
	}
	
	@Test
	void testValues() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		Integer sum = 0;
		
		hm.put("evya", age);
		
		hm.put("evy", age2);
		
		Collection<Integer> s = hm.values();
		
		assertSame(2, s.size());
		
		for (Integer i : s) {
			sum += i;
		}
		
		assertSame(sum.intValue(), 61);
	}
	
	
	@Test
	void testKeys() {
		GenericHashMap<Integer, String> hm = new GenericHashMap<Integer, String>(10);

		Integer age = 31;
		Integer age2 = 30;
		Integer sum = 0;
		
		hm.put(age, "evy");
		
		hm.put(age2, "evya");
		
		Set<Integer> s = hm.keySet();
		
		assertSame(2, s.size());
		
		for (Integer i : s) {
			sum += i;
		}
		
		assertSame(sum.intValue(), 61);
	}
	
	@Test 
	void testKeyModification() {
		GenericHashMap<Integer, String> hm = new GenericHashMap<Integer, String>(10);

		Integer age = 31;
		Integer age2 = 30;
		Integer age3 = 33;
		
		hm.put(age, "evy");
		
		hm.put(age2, "evya");
		
		Set<Integer> s = hm.keySet();
		
		Iterator<Integer> iter = s.iterator();
		
		boolean cond = false;
		
		iter.next();
		
		hm.put(age3, "evya");
		
		try{
			iter.next();
			
		} catch (ConcurrentModificationException e) { 
			cond = true;
		}
		
		assertSame(true, cond);

	}
	
	@Test 
	void testValueModification() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		Integer age3 = 33;
		
		hm.put("evya", age);
		
		hm.put("evy", age2);
		
		boolean cond = false;
		
		Collection<Integer> s = hm.values();
		
		Iterator<Integer> iter = s.iterator();
		
		iter.next();
		
		hm.put("evy", age3);
		
		try{
			iter.next();
				 
		} catch (ConcurrentModificationException e) { 
			cond = true;
		}
		assertSame(true, cond);

	}
	
	@Test 
	void testEntrysetModification() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		Integer age3 = 33;
		
		hm.put("evya", age);
		
		hm.put("evy", age2);
		
		boolean cond = false;
		
		Collection<Entry<String, Integer>> s = hm.entrySet();
		
		Iterator<Entry<String, Integer>> iter = s.iterator();
		
		iter.next();
		
		hm.put("evy", age3);
		
		try{
			iter.next();
			
		} catch (ConcurrentModificationException e) { 
			cond = true;
		}
		assertSame(true, cond);
	}
	
	@Test 
	void testClear() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		
		hm.put("evya", age);
		
		hm.put("evy", age2);
		
		hm.clear();
		
		assertSame(hm.size(), 0);
	}
	
	@Test 
	void testClearModification() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		
		hm.put("evya", age);
		
		hm.put("evy", age2);
		
		boolean cond = false;
		
		Collection<Entry<String, Integer>> s = hm.entrySet();
		
		Iterator<Entry<String, Integer>> iter = s.iterator();
		
		iter.next();

		hm.clear();
		
		try{
			iter.next();
			
		} catch (ConcurrentModificationException e) { 
			cond = true;
		}
		assertSame(true, cond);
	}
	
	@Test 
	void testPutAll() {
		GenericHashMap<String, Integer> hm = new GenericHashMap<String, Integer>(10);
		
		GenericHashMap<String, Integer> hm1 = new GenericHashMap<String, Integer>(10);

		Integer age = 31;
		Integer age2 = 30;
		
		Integer age3 = 35;
		Integer age4 = 30;
		
		Integer sum = 0;
		
		hm.put("evya", age);
		hm.put("evy", age2);
		
		hm1.put("evyat", age3);
		hm1.put("ev", age4);
				
		hm.putAll(hm1);
		
		Collection<Integer> s = hm.values();
		
		for (Integer i : s) {
			sum += i;
		}
		
		assertSame(hm.size(), 4);
		assertSame(sum.intValue(), 126);
		
	}
}