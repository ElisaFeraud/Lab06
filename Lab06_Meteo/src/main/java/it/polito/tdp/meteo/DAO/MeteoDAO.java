package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	public MeteoDAO() {
		
	}
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		final String sql="SELECT * FROM situazione 	WHERE MONTH(data)=? AND localita=?"	;
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, mese);
            st.setString(2, localita);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
public String getUmiditaMedia(int mese) {
	final String sql="SELECT * FROM situazione 	WHERE MONTH(data)=?"	;
	String valori="";
	List<Rilevamento> tuttiVal = new ArrayList<>();
	LinkedHashMap<String,Rilevamento> ril = new LinkedHashMap<>();
	try {
		Connection conn = ConnectDB.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, mese);
        
		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			Rilevamento rv = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
			Double media = 0.0; 
			if(!ril.containsKey(rv.getLocalita())) {
			    ril.put(rv.getLocalita(),rv);
			   tuttiVal= getAllRilevamentiLocalitaMese(mese,rv.getLocalita());
			 
			 int num=0;
			 for(Rilevamento r: tuttiVal) {
				 media+=r.getUmidita();
				 num++;
		   }
			 media=media/num;
			 valori+= " "+ rs.getString("Localita")+" "+ media;
			//rilevamenti.add(r);
		}
		}
		conn.close();


	} catch (SQLException e) {

		e.printStackTrace();
		throw new RuntimeException(e);
	}
	

		
		return valori;
	}
public List<Citta> getCitta(){
	final String sql = "SELECT Distinct localita FROM situazione";
			List<Citta> citta = new ArrayList<Citta>();
	try {
		Connection conn = ConnectDB.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
        
		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			Citta c = new Citta(rs.getString("Localita"));
			citta.add(c);
		}

		conn.close();
		return citta;

	} catch (SQLException e) {

		e.printStackTrace();
		throw new RuntimeException(e);
	}

	
}



}
