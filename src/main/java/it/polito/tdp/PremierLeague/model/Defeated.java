package it.polito.tdp.PremierLeague.model;

import java.util.Comparator;

public class Defeated implements Comparable <Defeated>{
	
	private double delta; //di wuanti minuti sono stati sconfitti
	private Player player;
	public Defeated( Player player,double delta) {
		super();
		this.delta = delta;
		this.player = player;
	}
	public double getDelta() {
		return delta;
	}
	public void setDelta(double delta) {
		this.delta = delta;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player pleayer) {
		this.player = pleayer;
	}
	
	@Override
	public int compareTo(Defeated o) {
		// TODO Auto-generated method stub
		return (int) (o.delta-this.delta);
	}
	
	public String toString() {
		return player+" ( "+this.delta+" )";
	}
	

}
