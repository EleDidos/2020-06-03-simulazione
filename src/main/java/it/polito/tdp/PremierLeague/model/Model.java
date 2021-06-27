package it.polito.tdp.PremierLeague.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private SimpleDirectedWeightedGraph< Player , DefaultWeightedEdge>graph;
	private Map <Integer, Player  > idMap;
	private PremierLeagueDAO dao;
	private List <Player> best;
	
	
	public Model() {
		idMap= new HashMap <Integer,Player  >();
		dao=new PremierLeagueDAO();
	}
	
	public void creaGrafo(Double x) {
		graph= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		dao.loadAllVertici(idMap,x);
		Graphs.addAllVertices(graph, idMap.values());
		
		for(Arco a : dao.listArchi(idMap)) //già direzionati nel dao
			Graphs.addEdgeWithVertices(graph, a.getP1(),a.getP2(),a.getPeso());
		//dal giocatore con più min a quello con meno
		
		
	}
	
	public Integer getNVertici() {
		return graph.vertexSet().size();
	}
	
	public Integer getNArchi() {
		return graph.edgeSet().size();
	}
	


	public SimpleDirectedGraph< Player , DefaultWeightedEdge> getGraph() {
		return graph;
	}
		
		
	public Player getStrongest() {
		int max=0; //avversari battuti in MIN = archi uscenti
		Player top=null;
		
		for(Player p: graph.vertexSet())
			if(graph.outgoingEdgesOf(p).size()>max) {
				max=graph.outgoingEdgesOf(p).size();
				top=p;
			}
		return top;
	}
	
	
	public List<Defeated> getBattuti(Player top){
		List<Defeated> Battuti = new ArrayList <Defeated>();
		for(Player p: Graphs.successorListOf(graph,top))
			Battuti.add(new Defeated(p, graph.getEdgeWeight( graph.getEdge(top, p) )));
		Collections.sort(Battuti);
		return Battuti;	
	}
	
	
	public void trovaGrado(Player p) {
		double grado=0.0;
		double IN=0.0;
		double OUT=0.0;
		for(DefaultWeightedEdge e: graph.outgoingEdgesOf(p))
			OUT+=graph.getEdgeWeight(e);
		for(DefaultWeightedEdge e: graph.incomingEdgesOf(p))
			IN+=graph.getEdgeWeight(e);
		grado= OUT-IN;
		p.setGrado(grado);
	}
	
	
	private Integer k;
	private double gradoDream=0.0;
	
	public List <Player> trovaDreamTeam(Integer k){
		for(Player p: graph.vertexSet()) //setto tutti i valori di titolarità dei giocatori
			this.trovaGrado(p);
		
		this.k=k;
		
		best=new ArrayList <Player>();
		List <Player> parziale = new ArrayList <Player>();
		List <Player> inseribili = new ArrayList <Player>(graph.vertexSet());
		
		this.ricorsiva(parziale, inseribili);
		return best;
	}

	
	private void ricorsiva(List <Player> parziale,List <Player> inseribili) {
		if(parziale.size()==k) {
			if(best.size()==0 || this.calcolaGradoTot(parziale)>gradoDream) {
				best=new ArrayList <Player> (parziale);
				gradoDream=this.calcolaGradoTot(parziale);
				return;
			}
		}
		else {
			for(Player p: inseribili)
				if(!parziale.contains(p)) {
					parziale.add(p);
					List <Player> remainingPlayers = new ArrayList <Player> (inseribili);
					//tolgo tutti quelli battuti dall'ultimo inserito
					remainingPlayers.removeAll(Graphs.successorListOf(graph, p));
					this.ricorsiva(parziale, remainingPlayers);
					
					parziale.remove(p);
					//non rimetto in "remaining" i giocatori eliminati perché torno
					//indietro di un liv di ricorsione
				}
		}
		

		
	}
	
	
	private double calcolaGradoTot(List<Player> parziale) {
		double grado=0.0;
		for(Player p: parziale)
			grado+=p.getGrado();
		return grado;
	}
	
	public double getGradoDream() {
		return this.gradoDream;
	}

	
}
