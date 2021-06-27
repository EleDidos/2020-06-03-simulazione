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
import it.polito.tdp.PremierLeague.model.Arco;
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
	
	
	public void loadAllVertici(Map <Integer, Player> idMap, Double x){
		
		
		String sql = "SELECT p.PlayerID, p.Name "
				+ "from Players as p, Actions as a "
				+ "WHERE p.PlayerID=a.PlayerID "
				+ "GROUP BY p.PlayerID, p.Name "
				+ "HAVING AVG(a.Goals)>?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setDouble(1, x);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(!idMap.containsKey(res.getInt("p.PlayerID"))) {
					Player p = new Player(res.getInt("p.PlayerID"),res.getString("p.Name"));
					idMap.put(res.getInt("p.PlayerID"),p );
					System.out.println(p);
				}//if
				
			}//while
			
			conn.close();
			return  ;

		} catch (SQLException e) {
			e.printStackTrace();
			return  ;
		}

	}


public List <Arco> listArchi(Map <Integer, Player> idMap){
	
	String sql = "SELECT a1.PlayerID as id1, a2.PlayerID as id2, SUM(a1.TimePlayed) as time1, SUM(a2.TimePlayed) as time2 "
			+ "from Actions as a1, Actions as a2, Matches as m1, Matches as m2 "
			+ "WHERE a1.MatchID=m1.MatchID and a2.MatchID=m2.MatchID and a1.TeamID>a2.TeamID and a1.Starts=1 and a2.Starts=1 and a1.MatchID=a2.MatchID "
			+ "GROUP BY a1.PlayerID, a2.PlayerID";

	Connection conn = DBConnect.getConnection();
	List <Arco> archi = new ArrayList <Arco>();

	try {
		PreparedStatement st = conn.prepareStatement(sql);
		ResultSet res = st.executeQuery();
		while (res.next()) {

			Player p1 = idMap.get(res.getInt("id1"));
			Player p2 = idMap.get(res.getInt("id2"));
			Arco a;
			
			if(p1!=null & p2!=null) {
				if(res.getInt("time1")>res.getInt("time2"))
					a = new Arco(p1,p2,res.getInt("time1")-res.getInt("time2"));
				else if(res.getInt("time1")<res.getInt("time2"))
					a = new Arco(p2,p1,res.getInt("time2")-res.getInt("time1"));
				else //uguali
					continue;
				archi.add(a);
				System.out.println(a);
			}
		

		}
		conn.close();
		return archi;
		
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	}
}


}
