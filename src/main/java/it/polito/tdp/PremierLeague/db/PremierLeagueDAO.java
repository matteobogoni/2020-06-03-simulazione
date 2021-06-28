package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacente;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(Map<Integer,Player> idMap){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				idMap.put(res.getInt("PlayerID"), player);
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
	
	public List<Player> getPlayerByAvg (double d){
		
		String sql = "SELECT p.PlayerID as id, p.Name "
				+ "FROM actions a, players p "
				+ "WHERE a.PlayerID = p.PlayerID "
				+ "GROUP BY p.PlayerID, p.Name "
				+ "HAVING AVG(a.Goals) > ? ";
		
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, d);
			ResultSet res = st.executeQuery();
			
			if (res.next()) {
				
				Player player = new Player(res.getInt("id"), res.getString("p.Name"));
				
				result.add(player);
				
			}
			conn.close();
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacente> getAdiacenze (Map<Integer,Player> idMap){
		
		String sql = "SELECT p1.PlayerID, p2.PlayerID, (a1.TimePlayed - a2.TimePlayed) AS peso "
				+ "FROM players p1, players p2, actions a1, actions a2 "
				+ "WHERE p1.PlayerID > p2.PlayerID AND p1.PlayerID = a1.PlayerID AND p2.PlayerID = a2.PlayerID AND "
				+ "		a1.TeamID > a2.TeamID AND a1.`Starts`= 1 AND a1.`Starts` = a2.`Starts` AND a1.MatchID = a2.MatchID "
				+ "GROUP BY p1.PlayerID, p2.PlayerID";
		List<Adiacente> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Adiacente a = new Adiacente(idMap.get(res.getInt("p1.PlayerID")), idMap.get(res.getInt("p2.PlayerID")), res.getInt("peso"));
				
				result.add(a);
			}
			conn.close();
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
