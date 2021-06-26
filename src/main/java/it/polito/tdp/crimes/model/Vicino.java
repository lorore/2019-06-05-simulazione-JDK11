package it.polito.tdp.crimes.model;

public class Vicino implements Comparable<Vicino>{

	District d;
	double distanza;
	public Vicino(District d, double distanza) {
		super();
		this.d = d;
		this.distanza = distanza;
	}
	public District getD() {
		return d;
	}
	public void setD(District d) {
		this.d = d;
	}
	public double getDistanza() {
		return distanza;
	}
	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}
	@Override
	public String toString() {
		return "Vicino [d=" + d + ", distanza=" + distanza + "]";
	}
	@Override
	public int compareTo(Vicino o) {
		return Double.compare(this.distanza, o.distanza);
	}
	
	
}
