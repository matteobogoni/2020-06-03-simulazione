package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	Map<Integer,Player> idMap;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
		dao.listAllPlayers(idMap);
	}
	
	public void creaGrafo (double d) {
		
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, dao.getPlayerByAvg(d));
		
		//aggiungo gli archi
		for(Adiacente a : dao.getAdiacenze(idMap)) {
				if(a.peso > 0) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
				}
				else if (a.peso < 0) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), -a.getPeso());
				}
				else {
					continue;
				}
			
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
}
