package com.ryd.stockanalysis.util;

import java.util.LinkedList;

public interface SortedLinkedList<T extends Comparable<T>> {
	public boolean add(T data);
	
	public T getFrist();
	
	public T getLast();
	
	public boolean removeElement(T data);
	
	public boolean isEmpty();
	
	public Integer getSize();
	
	public boolean showList(int size);

	public LinkedList<T> getList();

	public boolean clear();
}