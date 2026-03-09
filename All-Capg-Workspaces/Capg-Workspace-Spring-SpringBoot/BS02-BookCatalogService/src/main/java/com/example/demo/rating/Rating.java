package com.example.demo.rating;

public class Rating {
private int rId;
private int brating;
public int getrId() {
	return rId;
}
public void setrId(int rId) {
	this.rId = rId;
}
public int getBrating() {
	return brating;
}
public void setBrating(int brating) {
	this.brating = brating;
}
public Rating(int rId, int brating) {
	super();
	this.rId = rId;
	this.brating = brating;
}

}
