package util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * */
public  class List<T> {
	
	/***/
	public T head;
	
	/***/
	public List<T> tail;
	
	/***/
	private int size;
	
	/**
	 * 
	 * */
	public List(){
		head = null;
		tail = null;
		size = 0;
	}
	
	/**
	 * 
	 * */
	public List(T head, List<T> tail){
		if(head != null){
			this.head = head;
			this.tail = tail;
			if(tail != null) size = tail.size+1;
			else size = 1;
		}
	}
	
	/**
	 * 
	 * */
	public T get(int i){
		List<T> t = this;
		for(int j = 0; j < i; j++){
			if(t != null){
				t = t.tail;
			}	
		}
		return t.head;
	}
	
	/**
	 * 
	 * */
	public void set(int i, T elem){
		List<T> l = this;
		for(int j = 0; j < i; j++ ){
			if(l.tail != null)l = l.tail;
		}
		if(l != null){
			l.head = elem;
		}
	}
	
	/**
	 * 
	 * */
	public void  add(T elem){
		tail = new List<T>(head, tail);
		head = elem;
		size++;
	}
	
	/**
	 * 
	 * */
	public int size(){
		return size;
	}
	
	/**
	 * 
	 * */
	public boolean contains(T elem){
		if(elem == null) return false;
		List<T> t = tail;
		if(head != null && head.equals(elem)) return true;
		if(t != null) return t.contains(elem);
		return false;
	}
	
	/**
	 * 
	 * 
	 * */
	public boolean equal(List<T> l){
		Set<T> l1 = new HashSet<T>();
		Set<T> l2 = new HashSet<T>();
		List<T> aux1 = this;
		List<T> aux2 = l;
		for(;aux1!= null; aux1 = aux1.tail){
			if(aux1.head != null)
				l1.add(aux1.head);
		}
		for(;aux2!= null; aux2 = aux2.tail){
			if(aux2.head != null)
				l2.add(aux2.head);
		}
		return l1.equals(l2);
	}
	
	/**
	 * 
	 * */
	private List<T> remove(T elem) {
		if (head.equals(elem)) return tail;
		else {
			if(tail != null) return new List<T>(head,tail.remove(elem));
			else return this;
		}
	}
	
	/**
	 * 
	 * */
	public Iterator<T> iterator(){
		final List<T> owner = this;
		return new Iterator<T>(){
			List<T> l = owner;
			@Override
			public boolean hasNext() {
				return l != null;
			}
			@Override
			public T next() {
				T elem = l.head;
				l = l.tail;
				return elem;
			}
			@Override
			public void remove() {}
		};	
	}
	
	
}
