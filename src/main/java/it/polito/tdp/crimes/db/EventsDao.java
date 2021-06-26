package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.crimes.model.District;
import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	
	
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
	public List<Year> getAnni(){
		String sql="SELECT DISTINCT YEAR(e.reported_date) as anno "
				+ "FROM EVENTS e";
		List<Year> anni=new LinkedList<>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				anni.add(Year.of(res.getInt("anno")));
			}
			
			conn.close();
			return anni ;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public List<Integer> getVertici(Year y){
		String sql="SELECT DISTINCT e.district_id AS d "
				+ "FROM EVENTS e "
				+ "WHERE YEAR(e.reported_date)=? "
				+ "ORDER BY e.district_id ";
		List<Integer> result=new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, y.getValue());
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getInt("d"));
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public void calcolaCentri(District d, Year y) {
		String sql="SELECT e.incident_id AS id, e.geo_lon AS lon, e.geo_lat AS lat "
				+ "FROM EVENTS e "
				+ "WHERE e.district_id=? AND YEAR(e.reported_date)=? ";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, d.getId());
			st.setInt(2, y.getValue());
			
			ResultSet res = st.executeQuery() ;
			double latitude=0;
		    double longitude=0;
		    int n=0;
			while(res.next()) {
				 
				    n++;
				    LatLng point=new LatLng(res.getDouble("lat"), res.getDouble("lon"));
				   
				    
				    
				    	
				        latitude += point.getLatitude();
				        longitude += point.getLongitude();
				    

				    
			}
			
			conn.close();
			d.setCentro(new LatLng(latitude/n, longitude/n));

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Integer getMigliore(Year y) {
		String sql="SELECT e.district_id as id, COUNT(DISTINCT e.incident_id) AS n "
				+ "FROM EVENTS e "
				+ "WHERE YEAR(e.reported_date)=? "
				+ "GROUP BY e.district_id "
				+ "ORDER BY n ";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, y.getValue());
			
			
			ResultSet res = st.executeQuery() ;
			
			if(res.first()) {
				conn.close();
				return res.getInt("id");
			}
			
			conn.close();
			return null;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	
	public List<Event> getEventiDelGiorno(int y, int m, int d){
		String sql="SELECT * "
				+ "FROM EVENTS e "
				+ "WHERE YEAR(e.reported_date)=? AND MONTH(e.reported_date)=? AND DAY(e.reported_date)=? "
				+ "ORDER BY e.reported_date ";
		List<Event> list = new ArrayList<>() ;

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, y);
			st.setInt(2, m);
			st.setInt(3, d);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
}
