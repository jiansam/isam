package org.dasin.tools;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;

public class dPairHashMap<K1, K2, V>{
	private HashMap<SimpleEntry<K1, K2>, V> map;
	
	public dPairHashMap(){
		map = new HashMap<SimpleEntry<K1, K2>, V>();
	}
	
	public void put(K1 key1, K2 key2, V value){
		map.put(new SimpleEntry<K1, K2>(key1, key2), value);
	}
	
	public V get(K1 key1, K2 key2){
		return map.get(new SimpleEntry<K1, K2>(key1, key2));
	}
	
	public V remove(K1 key1, K2 key2){
		return map.remove(new SimpleEntry<K1, K2>(key1, key2));
	}
	
	public Set<K1> firstKeySet(){
		HashSet<K1> result = new HashSet<K1>();

		for(SimpleEntry<K1, K2> keypair : map.keySet()){
			if(!result.contains(keypair.getKey())){
				result.add(keypair.getKey());
			}
		}

		return result;
	}

	public Set<K2> secondKeySet(){
		HashSet<K2> result = new HashSet<K2>();

		for(SimpleEntry<K1, K2> keypair : map.keySet()){
			if(!result.contains(keypair.getValue())){
				result.add(keypair.getValue());
			}
		}

		return result;
	}
	
	public Collection<V> values(){
		return map.values();
	}
	
	public int size(){
		return map.size();
	}
}