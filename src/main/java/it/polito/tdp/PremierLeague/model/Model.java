package it.polito.tdp.PremierLeague.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private SimpleDirectedWeightedGraph <Player, DefaultWeightedEdge> graph;
	private Map <Integer,Player> idMap;
	private PremierLeagueDAO dao;
	
	private List <Player> inseribili; //vertici per RICORSIONE
	private List <Player> tolti; //lo aggiorno di volta in volta
	
	public Model() {
		idMap= new HashMap <Integer,Player>();
		dao=new PremierLeagueDAO();
		
		
	}
	
	public void creaGrafo(float x) {
		graph = new SimpleDirectedWeightedGraph <> (DefaultWeightedEdge.class);
		
		//riempio ID MAP
		dao.vertex(idMap, x);
		
		//aggiungo vertici
		Graphs.addAllVertices(graph, idMap.values());
		
		//aggiungo archi
		// un'adiacenza diventa arco solo se quei giocatori 
		// sono nella mappa e quindi hanno la media giusta
		
		for(Adiacenza a : dao.getAdiacenze()) {
			
			//se entrambi esistono nella mappa e il peso>0
			if(idMap.get(a.getPlayerID1())!=null && idMap.get(a.getPlayerID2())!=null) {
				if(a.getPeso()>0.0)
					Graphs.addEdgeWithVertices(graph, idMap.get(a.getPlayerID1()), idMap.get(a.getPlayerID2()), a.getPeso());
				else if(a.getPeso()<0.0) //arco in senso contrario, perché peso negativo che moltiplico *(-1)
					Graphs.addEdgeWithVertices(graph, idMap.get(a.getPlayerID2()), idMap.get(a.getPlayerID1()), (a.getPeso()*(-1)));
				else //peso=0 non ci deve essere
					continue;
			}
		}//for
	}

	public int nVertici() {
		int n= this.graph.vertexSet().size();
		return n;
	}
	

	public int nArchi() {
		
		return this.graph.edgeSet().size();
	}
	
	public StringBuilder getArchi() {
		StringBuilder sb = new 	StringBuilder();
		for(DefaultWeightedEdge e: graph.edgeSet()) {
    		sb.append(graph.getEdgeSource(e)+" ---> " +graph.getEdgeTarget(e)+ "---> peso= "+ graph.getEdgeWeight(e)+"\n");
    	}
		return sb;
	}
	
	/**
	 * Usando funzione OUTGOINGEDGESOF cerco
	 * il giocatore che ha più archi uscenti
	 * quindi che ha superato più avversari in min giocati
	 */
	public Player getTopPlayer() {
		
		//se grafo==null --> sta provando a chiedermi il top player
		//senza prima aver creato il grafo
		if(graph==null)
			return null;
		
		int max=0; //avversari superati=archi uscenti
		Player top=null;
		boolean found=false;
		
		for(Player p: graph.vertexSet()) {
			if(graph.outgoingEdgesOf(p).size()>max) {
				max=graph.outgoingEdgesOf(p).size();
				top=p;
				found=true;
			}	
		}
		if(found)
			return top;
		else
			return null;
	}
	
	/**
	 * Stampo con StringBuilder avversari sconfitti a liv di minuti
	 * con OUTGOINGEDGESOF
	 */
	public StringBuilder getSconfitti(Player p) {
		StringBuilder sb = new StringBuilder();
		
		//in ordine di peso decrescente per chiave
					//peso DELTA, sconfitto 
		LinkedHashMap <Double, Player> sconfitti = new LinkedHashMap <Double, Player>();
		
		for(DefaultWeightedEdge e: graph.outgoingEdgesOf(p)) {
			sconfitti.put(graph.getEdgeWeight(e), graph.getEdgeTarget(e));
		}
		
		for(Double d: sconfitti.keySet()) {
			sb.append(sconfitti.get(d)+" con "+d+" minuti di distacco\n");
		}
			
		return sb;
	}
	
	public LinkedHashMap <Double, Player> getSconfittiMappa(Player p) {
		
		//in ordine di peso decrescente per chiave
					//peso DELTA, sconfitto 
		LinkedHashMap <Double, Player> sconfitti = new LinkedHashMap <Double, Player>();
		
		for(DefaultWeightedEdge e: graph.outgoingEdgesOf(p)) {
			sconfitti.put(graph.getEdgeWeight(e), graph.getEdgeTarget(e));
		}
			
		return sconfitti;
	}
	
	/**
	 * Dato un giocatore trovare GRADO DI TITOLARITA'
	 * = differenza di peso tra somma archi uscenti
	 * ed entranti
	 */
	public float getGrado(Player p) {
		float grado=0;
		float pesoOUT=0;
		float pesoIN=0;
		
		for(DefaultWeightedEdge e: graph.outgoingEdgesOf(p)) {
			pesoOUT+=graph.getEdgeWeight(e);
		}
		for(DefaultWeightedEdge e: graph.incomingEdgesOf(p)) {
			pesoIN+=graph.getEdgeWeight(e);
		}
		grado=pesoOUT-pesoIN;
		return grado;
	}
	
	
	/**
	 * GRADO TOT di un'intera lista di giocatori
	 * @param lista
	 * @return
	 */
	public float getGradoTot (List <Player> lista) {
		float gradoTot=0;
		for(Player p: lista) {
			gradoTot+=this.getGrado(p);
		}
		return gradoTot;
	}
	
	
	/**
	 * PROCEDURA PUBBLICA DI RICORSIONE
	 * @param k
	 * @return
	 */
	public List<Player> getDreamTeam(Integer k) {
		
		//tutti i giocatori che possono essere provati
		//all'inizio tutti i vertici
		inseribili = new ArrayList <Player>();
		for(Player p: idMap.values())
			inseribili.add(p);
		
		List <Player> best = new ArrayList <Player>();
		List <Player> parziale = new ArrayList <Player>();
		
		ricorsiva(parziale,best,k);
		
		return best;
	}
	
	
	/**
	 * PROCEDURA DI RICORSIONE PRIVATA
	 * @param parziale
	 * @param livello
	 * @param best
	 */
	private void ricorsiva (List<Player>parziale,List<Player> best, int k) {
		
		//CONDIZIONE DI TERMINAZIONE
		if(parziale.size()==k) {
			//controllo se è migliore di best
			if(best.size()==0 || this.getGradoTot(parziale)>this.getGradoTot(best)) {
				best= new ArrayList<Player>(parziale);
			}
			return;
		}
		else {
			for(Player prova: inseribili) {
				if(!parziale.contains(prova)) {
					parziale.add(prova);
					this.togliSconfitti(prova);
					ricorsiva(parziale,best,k);
				
					//tolgo ultimo elemento - torno indietro di un liv
					//provo con nuovo inseribile
					//riaggiungo quelli che avevo tolto
					parziale.remove(parziale.size()-1);
					for(Player p: tolti)
						inseribili.add(p);
				}//if
			}
			
			
		} //else
		
		
	}//ricorsiva
	
	
	/**
	 * Tiene Traccia dei giocatori tolti da inseribili
	 * perché sconfitto da quello di prova
	 * se poi devo fare BACKTRACKING
	 * devo sapere chi rinserire
	 * 
	 * @param prova
	 * @return
	 */
	private void togliSconfitti(Player prova) {
		tolti = new ArrayList<Player>(); // i giocatori tolti da inseribili
							
		for(Player p: inseribili)
			//se p è tra gli sconfitti di "prova"
			//lo tolgo da inseribili
			if(this.getSconfittiMappa(prova).values().contains(p)) {
				inseribili.remove(p);
				tolti.add(p);
			}
	}

}
