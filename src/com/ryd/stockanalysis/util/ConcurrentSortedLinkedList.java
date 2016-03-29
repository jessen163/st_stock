package com.ryd.stockanalysis.util;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class  ConcurrentSortedLinkedList<T extends Comparable<T>> implements SortedLinkedList<T> {
	private LinkedList<T> list;

	public ConcurrentSortedLinkedList(){
		init();
	}

	public ConcurrentSortedLinkedList(T t){
		init();
		list.add(t);
	}

	private synchronized void init() {
		if (list==null) {
			list = new LinkedList<T>();
		}
	}

	/**
	 *
	 * <p>Title: 添加对象到集合</p>
	 * <p>Description: </p>
	 *
	 * @param
	 * @return    设定文件
	 * boolean    返回类型
	 * @throws
	 *
	 * @Author：chenji
	 * @Date：2016-3-28 上午11:04:14
	 */
	public synchronized boolean add(T data) {
		list.add(data);
		Collections.sort(list);//自动调用compareTo
		return true;
	}

	/**
	 *
	 * <p>Title: 获取集合中第一个元素</p>
	 * <p>Description: </p>
	 *
	 * @return    设定文件
	 * T    返回类型
	 * @throws
	 *
	 * @Author：yl
	 * @Date：2016-3-28 上午11:05:04
	 */
	public synchronized T getFrist() {
		if(!list.isEmpty()) {
			return list.getFirst();
		}
		return null;
	}

	/**
	 *
	 * <p>Title:  删除该元素</p>
	 * <p>Description: TODO(这里用一句话描述这个方法的作用)</p>
	 *
	 * @param t
	 * @return    设定文件
	 * boolean    返回类型
	 * @throws
	 *
	 * @Author：yl
	 * @Date：2016-3-28 上午11:05:49
	 */
	@Override
	public synchronized boolean removeElement(T t) {
		return list.remove(t);
	}

	@Override
	public synchronized T getLast() {
		// TODO Auto-generated method stub
		if(!list.isEmpty()) {
			return list.getLast();
		}
		return null;
	}

	@Override
	public synchronized boolean isEmpty() {
		// TODO Auto-generated method stub
		if(list.size()>0){
			return false;
		}
		return true;
	}

	@Override
	public Integer getSize() {
		// TODO Auto-generated method stub
		return list.size();
	}

	/**
	 *
	 * <p>Title: 测试获取集合</p>
	 * <p>Description: TODO(仅供测试)</p>
	 *
	 * @return    设定文件
	 * LinkedList<T>    返回类型
	 * @throws
	 *
	 * @Author：yl
	 * @Date：2016-3-28 上午11:07:15
	 */
	public LinkedList<T> getList() {
		return list;
	}

	/**
	 *
	 * <p>Title: 显示一定数量的记录</p>
	 * <p>Description: TODO(这里用一句话描述这个方法的作用)</p>
	 *
	 * @param size
	 * @return    设定文件
	 * boolean    返回类型
	 * @throws
	 *
	 * @Author：yl
	 * @Date：2016-3-28 上午11:48:45
	 */
	public boolean showList(int size) {
		if (list==null) {
			System.out.println("ConcurrentSortedLinkedList is Empty!");
		}
		for (T t : list) {
			System.out.println("t:" + t);
		}
		return true;
	}

	/**
	 * 清除所有
	 * @return
	 */
	public synchronized boolean clear(){
		list.clear();
		return true;
	}
}