package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.crimes.model.Event.EventType;

public class Simulator {
	
	private Model m;

	private PriorityQueue<Event> queue;
	private int N;
	private int gg;
	private int mm;
	private int y;
	Map<District, Integer> agenti;
	Map<Integer, District> distretti;
	int giorni;
	
	
	

	public Simulator(Model m, int n, int gg, int mm, int y) {
		super();
		this.m=m;
		N = n;
		this.gg = gg;
		this.mm = mm;
		this.y = y;
	}





	public void init() {
		queue=new PriorityQueue<>();
		queue.addAll(this.m.getEventiGiorno(y, mm, gg));
		agenti=new HashMap<>();
		distretti=new HashMap<>();
		
		for(District d: this.m.getV()) {
			agenti.put(d, 0);
			distretti.put(d.getId(), d);
		}
		
	District migliore=distretti.get(this.m.getMigliore(y));
	agenti.put(migliore, N);
	giorni=0;
	}
	
	
	public void sim() {
		while(this.queue.peek()!=null) {
			Event e=this.queue.poll();
			processEvent(e);
		}
		System.out.println(giorni);
	}
	
	private void processEvent(Event e) {
	switch(e.getType()){
		case INCIDENTE:
		//è avvenuto un incidente, devo trovare poliziotto libero
		District d=distretti.get(e.getDistrict_id());
		List<Vicino> vicini=this.m.getVicini(d);
		boolean trovato=false;
		int i=0;
		while(trovato==false && i<vicini.size()) {
		Vicino v=vicini.get(i);
		if(agenti.get(v.getD())>0) {
			//ho trovato l'agente più vicino
			trovato=true;
			double tempoRaggiungimento=v.getDistanza()/60; //espresso in ore
			if(tempoRaggiungimento>0.25) {
				giorni++;
				//ha impiegato più di 15 minuti (25% di un'ora), evento quindi mal gestito
			}
			agenti.put(v.getD(), agenti.get(v.getD())-1);
			//tolgo un agente perchè non è più in centrale
			
			//da capire quanto tempo è occupato
			double tempoIntervento=0.0;
			if(e.getOffense_category_id().equals("all_other_crimes")) {
				
				if(Math.random()<=0.5) {
					tempoIntervento=1;
				}else
					 tempoIntervento=2;
			}else
				tempoIntervento=2;
			System.out.println(e.getReported_date());
			System.out.println((tempoIntervento+tempoRaggiungimento));
			LocalDateTime ora=e.getReported_date().plusMinutes((long) ((tempoRaggiungimento+tempoIntervento)*60.0));
			System.out.println(ora);
			queue.add(new Event(ora, EventType.LIBERATOAGENTE, e.getDistrict_id()));
			
			
		}
		i++;
		
		}
		if(trovato==false) {
			giorni++;
			//considero mal gestito anche se non si trova un agente da mandare
		}
	break;
	
		case LIBERATOAGENTE:
			District di=distretti.get(e.getDistrict_id());
			agenti.put(di, agenti.get(di)+1);
			//si è liberato e ora rimane sul posto
			
	}
	}

}
