package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	
	private Graph<String, Adiacenza> grafo;
	
	public Model(){
		dao = new EventsDao();
	}
	
	public void creaGrafo(String offenseCategory, Integer mese) {
		
		grafo = new SimpleWeightedGraph<>(Adiacenza.class);
		
		Graphs.addAllVertices(grafo, dao.listAllTypes(offenseCategory, mese));
		
		// archi
		
		for(String s1 : grafo.vertexSet()) {
			for(String s2 : grafo.vertexSet()) {
				if(!s1.equals(s2)) {
					if(getQuartieriComuni(offenseCategory, s1, s2, mese) > 0) {
						Adiacenza a = new Adiacenza(s1, s2, getQuartieriComuni(offenseCategory, s1, s2, mese));
						grafo.addEdge(s1, s2, a);
						// grafo.setEdgeWeight(a, getQuartieriComuni(offenseCategory, s1, s2, mese));
					}
				}
			}
		}
		
	}
	
	public Double calcolaPesoMedio(Graph<String, Adiacenza> grafo) {
		
		double media;
		int pesoTot = 0;
		
		for(Adiacenza a : grafo.edgeSet()) {
			pesoTot += a.getPeso();
		}
		
		media = pesoTot/grafo.edgeSet().size();
		
		
		return media;
	}
	
	public Graph<String, Adiacenza> getGrafo(){
		return grafo;
	}

	private int getQuartieriComuni(String offenseCategory, String s1, String s2, Integer mese) {
		return dao.getQuartieriComuni(offenseCategory, s1, s2, mese);
	}

	public List<String> listAllCategories() {
		return dao.listAllCategories();
	}

	public List<Integer> listAllMesi() {
		return dao.listAllMesi();
	}
	
	
	
}
