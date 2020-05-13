package org.insa.graphs.model;

public class LabelStar extends Label {
	
	private double distanceToDestination;
	
	public LabelStar(Node sommetCourant, Node sommetDestination) {
		super(sommetCourant);
		this.distanceToDestination = sommetCourant.getPoint().distanceTo(sommetDestination.getPoint());
	}
	
	public LabelStar(Node sommetCourant, Node sommetDestination, boolean marque, double cout, Arc pere) {
		super(sommetCourant, marque, cout, pere);
		this.distanceToDestination = sommetCourant.getPoint().distanceTo(sommetDestination.getPoint());
	}
	
	public void setCost(double cout) {
		this.cout = cout + this.distanceToDestination; 
	}
}
