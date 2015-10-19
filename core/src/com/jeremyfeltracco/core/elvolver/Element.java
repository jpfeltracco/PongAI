package com.jeremyfeltracco.core.elvolver;

public class Element implements Comparable<Element> {
	public double weights[];
	public int fitness;
	
	@Override
	public int compareTo(Element elem) {
		Element e = (Element) elem;
		if (this.fitness > e.fitness) return 1;
		if (this.fitness < e.fitness) return -1;
		return 0;
	}
}
