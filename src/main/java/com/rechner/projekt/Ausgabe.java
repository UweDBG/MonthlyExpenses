package com.rechner.projekt;

public class Ausgabe {
	private String kategorie;
	private String name;
	private float betrag;
	
	//Erzeuger des Objekts 
	public Ausgabe(String kategorie, String name, float betrag) {
		this.kategorie = kategorie;
		this.name = name;
		this.betrag = betrag;
	}
	
	//Getter
	public String getKategorie() {
		return kategorie;
	}
	
	public String getName() {
		return name;
	}
	
	public Float getBetrag() {
		return betrag;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s, %s €", kategorie, name, String.format("%.2f", betrag));
	}
}
