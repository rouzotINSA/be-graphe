package org.insa.graphs.model;

public class Label {
	
	private Node sommetCourant;
	private boolean marque;
	private double cout;
	private Arc pere;
	
	
	public Label(Node sommetCourant) {
		this.sommetCourant = sommetCourant;
		this.marque = false;
	}
	
	public Label(Node sommetCourant, boolean marque, double cout, Arc pere) {
		this.sommetCourant = sommetCourant;
		this.marque = marque;
		this.cout = cout;
		this.pere = pere;
	}
	
	
	public double getCost() {
		return this.cout;
	}
	
	public void setCost(double cout) {
		this.cout = cout;
	}
	
	public void marquer() {
		this.marque = true;
	}
	
	public boolean estMarque() {
		return this.marque;
	}
	
	public void setPere(Arc pere) {
		this.pere = pere;
	}
	
	public Arc getPete() {
		return this.pere;
	}
}
