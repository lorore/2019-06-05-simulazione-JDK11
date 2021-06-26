package it.polito.tdp.crimes.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private Graph<District, DefaultWeightedEdge> graph;
	private EventsDao dao;
	
	
	public Model() {
		dao=new EventsDao();
	}
	
	public List<Year> getAnni(){
		return this.dao.getAnni();
	}
	
	public String creaGrafo(Year y) {
		String result="";
		graph=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<Integer> vertici=this.dao.getVertici(y);
		List<District> distretti=new LinkedList<>();
		for(Integer i: vertici) {
			distretti.add(new District(i));
		}
		Graphs.addAllVertices(graph, distretti);
		result="Num vertici: "+this.graph.vertexSet().size();
		for(District d: graph.vertexSet()) {
			dao.calcolaCentri(d, y);
		}
		
		for(District d1: graph.vertexSet()) {
			for(District d2: graph.vertexSet()) {
				if(graph.getEdge(d1, d2)==null && d1.getId()!=d2.getId()) {
					Double distanzaMedia = LatLngTool.distance(d1.getCentro(), 
							d2.getCentro(), 
							LengthUnit.KILOMETER);
					Graphs.addEdge(graph, d1, d2, distanzaMedia);
				}
			}
		}
		result+=" num archi: "+this.graph.edgeSet().size();
		

		return result;
		
	}
	
	public List<Vicino> getVicini(District d){
		List<Vicino> vicini=new ArrayList<>();
		List<District> nei=Graphs.neighborListOf(graph, d);
		for(District di: nei) {
			vicini.add(new Vicino(di, graph.getEdgeWeight(graph.getEdge(di, d))));
		}
		Collections.sort(vicini);
		return vicini;
	}
	
public Set<District> getV(){
	return this.graph.vertexSet();
}
	
	
}
