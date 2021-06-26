package it.polito.tdp.crimes.model;

import java.time.Year;

public class TestModel {

	public static void main(String[] args) {
		Model m=new Model();
		m.creaGrafo(Year.of(2015));
		m.avviaSim(10, 25, 5, 2015);
	}

}
