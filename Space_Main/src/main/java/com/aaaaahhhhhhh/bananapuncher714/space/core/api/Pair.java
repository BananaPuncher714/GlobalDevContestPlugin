package com.aaaaahhhhhhh.bananapuncher714.space.core.api;

public class Pair< K, V > {
	private K first;
	private V second;
	
	public Pair( K one, V other ) {
		this.first = one;
		this.second = other;
	}

	public K getFirst() {
		return first;
	}

	public void setFirst( K one ) {
		this.first = one;
	}

	public V getSecond() {
		return second;
	}

	public void setSecond( V other ) {
		this.second = other;
	}
}
