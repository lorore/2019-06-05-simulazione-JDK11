package it.polito.tdp.crimes.model;

import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

public class District implements Comparable<District>{

	Integer id;
	List<LatLng> points;
	LatLng centro;
	double distanza;
	public double getDistanza() {
		return distanza;
	}
	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}
	public District(Integer id) {
		super();
		this.id = id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LatLng getCentro() {
		return centro;
	}
	public void setCentro(LatLng centro) {
		this.centro = centro;
	}
	@Override
	public int compareTo(District o) {
		return Double.compare(this.distanza, o.distanza);
	}
	@Override
	public String toString() {
		return Integer.toString(id);
	}
	
	
	
}
