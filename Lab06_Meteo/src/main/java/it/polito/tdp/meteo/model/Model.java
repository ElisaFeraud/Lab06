package it.polito.tdp.meteo.model;

import java.util.List;
import java.util.ArrayList;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	List<Citta> allCitta;
	List<Citta> risultato;

	public Model() {
     MeteoDAO DAO = new MeteoDAO();
     this.allCitta= DAO.getCitta();
	}

	public List<Citta> getCitta(){
		return allCitta ;
	}
	
	public String getUmiditaMedia(int mese) {
		MeteoDAO dao = new MeteoDAO();
	 
		return dao.getUmiditaMedia(mese);
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		this.risultato=null;
		MeteoDAO dao= new MeteoDAO();
		for(Citta c: allCitta) {
			
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		List<Citta> parziale= new ArrayList<>();
		recursive(parziale,0);
		return risultato; 
	}
	private void recursive(List<Citta> parziale,int numeroGiorni) {
		if(numeroGiorni==NUMERO_GIORNI_TOTALI) {
			//caso terminale
		
			double costoParziale= calcolaCosto(parziale);
			if(risultato==null || costoParziale<=calcolaCosto(risultato))
				risultato=new ArrayList<>(parziale);
						
		}
		else {
			for(Citta c : allCitta) {
			if(isCorrect(c,parziale)) {
			  parziale.add(c);
			  recursive(parziale,numeroGiorni+1);
			  parziale.remove(parziale.size()-1);
			  }
			
				
			}
			
		}
	}


	private boolean isCorrect(Citta citta, List<Citta> parziale) {
		// TODO Auto-generated method stub		
		int num=0;
		for(Citta c: parziale) {
			
			if(c.equals(citta))
				num++;
		}
		if(num>NUMERO_GIORNI_CITTA_MAX) {
		
				return false;}
		
		
		if(parziale.size()==0) // se è vuoto, va bene qualsiasi città
			return true;
		//se è di dimensione 1 o 2, la città deve essere la stessa MILANO,MILANO
		if(parziale.size()<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
               return parziale.get(parziale.size()-1).equals(citta);
               }		
	   //controllo se la città
		
		if(parziale.get(parziale.size()-1).equals(citta))
				return true;
		for(int i=0; i<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN-1;i++) {
			if(!parziale.get(parziale.size()-(i+1)).equals(parziale.get(parziale.size()-(i+2)))) {
					return false;
			}
		}
		
			
			
		return true;
		
		
		
	}

	private double calcolaCosto(List<Citta> parziale) {
		// TODO Auto-generated method stub
		double costo=0.0;
		for(int i=1; i<NUMERO_GIORNI_TOTALI; i++) {
		   Citta ci = parziale.get(i-1);
		   costo+=ci.getRilevamenti().get(i-1).getUmidita();
		
		}
	    for(int i=2; i<NUMERO_GIORNI_TOTALI; i++){
	    	if(!parziale.get(i).equals(parziale.get(parziale.size()-(i-1))))
	    			costo+=COST;
	    }
		return costo;
	}
	

}
