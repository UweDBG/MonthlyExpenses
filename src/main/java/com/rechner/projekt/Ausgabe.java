package com.rechner.projekt;

import java.time.LocalDate;

public class Ausgabe {
	private String kategorie;
	private float betrag;
	private LocalDate datum;
	
	//Erzeuger des Objekts 
	public Ausgabe(String kategorie, float betrag, String datumStr) {
		this.kategorie = kategorie;
		this.betrag = betrag;
		this.datum = LocalDate.parse(datumStr);
	}
	
	//Getter
	public String getKategorie() {
		return kategorie;
	}
	
	public Float getBetrag() {
		return betrag;
	}
	
	public LocalDate getDatum() {
		return datum;
	}
	
	@Override
	public String toString() {
		return datum + " - " + kategorie + ": " + betrag +  " €";
	}
}
