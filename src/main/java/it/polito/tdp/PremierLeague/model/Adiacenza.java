package it.polito.tdp.PremierLeague.model;

/** TUTTI gli SCONTRI tra due persone partite da titolari
 * 
 */
public class Adiacenza {
	
	private int PlayerID1;
	private int Time1; 
	private int PlayerID2;
	private int Time2;
	private float peso; //diff min giocati
	
	public Adiacenza(int playerID1, int timePlayed1, int playerID2, int timePlayed2) {
		
		if(timePlayed1>timePlayed2) {
			this.PlayerID1=playerID1;
			this.Time1 = timePlayed1;
			this.PlayerID2=playerID2;
			this.Time2 = timePlayed2;
		} else {
			this.PlayerID1=playerID2;
			this.Time1 = timePlayed2;
			this.PlayerID2=playerID1;
			this.Time2 = timePlayed1;
		}
		
		peso=(float)(this.Time1-this.Time2);
		
	}

	public int getPlayerID1() {
		return PlayerID1;
	}

	public void setPlayerID1(int playerID1) {
		PlayerID1 = playerID1;
	}

	public int getTime1() {
		return Time1;
	}

	public void setTime1(int time1) {
		Time1 = time1;
	}

	public int getPlayerID2() {
		return PlayerID2;
	}

	public void setPlayerID2(int playerID2) {
		PlayerID2 = playerID2;
	}

	public int getTime2() {
		return Time2;
	}

	public void setTime2(int time2) {
		Time2 = time2;
	}

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}
	
	
	
	
	

}
