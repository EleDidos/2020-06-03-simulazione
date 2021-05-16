package it.polito.tdp.PremierLeague.db;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** RIEMPIO ID MAP CON VERTICI
	 * = GIOCATORI con una media di almeno x goal per partita
	* da inserire in mappa
	*/
	public void vertex(Map <Integer,Player> idMap, float x){
		
		String sql = "SELECT p.PlayerID, p.Name "
				+ "FROM Players AS p, Actions AS a "
				+ "WHERE p.PlayerID=a.PlayerID "
				+ "GROUP BY p.PlayerID, p.Name "
				+ "HAVING AVG(a.Goals)>?";
	
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setFloat(1, x);
			ResultSet res = st.executeQuery();
		
			while (res.next()) {
			
				if(!idMap.containsKey(res.getInt("PlayerID")))
					idMap.put(res.getInt("PlayerID"), new Player(res.getInt("PlayerID"), res.getString("Name")));
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	/**
	 * IN OGNI RIGA VEDO solo TITOLARI che si
	 * sono incontrati in una o più partite, ma sono di squadre diverse
	 * con la rispettiva somma di minuti giocati in quelle partite
	 * per direzionare l'arco
	 *  
	 * @return
	 */
	public List <Adiacenza> getAdiacenze(){
		String sql = "SELECT a1.PlayerID AS player1, SUM(a1.TimePlayed) AS time1, a2.PlayerID AS player2, SUM(a2.TimePlayed) AS time2 "
				+ "FROM Actions AS a1, ACTIONS AS a2 "
				+ "WHERE a1.Starts=1 AND a2.Starts=1 AND a1.MatchID=a2.MatchID AND a1.PlayerID>a2.PlayerID AND a1.TeamID>a2.TeamID "
				+ "GROUP BY a1.PlayerID, a2.PlayerID";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				//CREO ADIACENZA: è la classe stessa a metterli in ordine di minuti
				Adiacenza a = new Adiacenza (res.getInt("player1"),res.getInt("time1"),res.getInt("player2"),res.getInt("time2") );
				
				result.add(a);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
