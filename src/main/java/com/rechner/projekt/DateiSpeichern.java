package com.rechner.projekt;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DateiSpeichern {
	
	//Export als CSV
	public static void  exportiereAlsCSV(List<Ausgabe> ausgaben, String pfad) throws IOException {
		try   (FileWriter writer =  new FileWriter(pfad)) {
			writer.write("Kategorie;Name;Betrag\n");
			for (Ausgabe a : ausgaben) {
				writer.write(a.getKategorie() + ";" + a.getName() + ";" + a.getBetrag() + "\n");
			}
		}
	}
	
	//Schreiben der Textdatei
	public static void speichereInDatei(Ausgabe ausgabe) throws IOException {	
		try (PrintWriter writer = new PrintWriter(new FileWriter("ausgaben.txt", true))) {
			writer.println(ausgabe.getKategorie() + ";" + ausgabe.getName() + ";" + ausgabe.getBetrag());
		} catch (IOException e) {
			System.out.println("Fehler beim Schreiben in Datei; " + e.getMessage());
		}
	}
	
	//Überschreiben der Textdatei nachdem gelöscht wurde
	public static void ueberschreibeDatei(List<Ausgabe> ausgaben) throws IOException {
	    try (PrintWriter writer = new PrintWriter(new FileWriter("ausgaben.txt"))) {
	        for (Ausgabe a : ausgaben) {
	            writer.println(a.getKategorie() + ";" + a.getName() + ";" + a.getBetrag());
	        }
	    }
	}
	
	//Liest die angegebene Datei aus und returnt eine Liste mit dem Inhalt
	public static List<Ausgabe> ladeAusgaben() throws FileNotFoundException, IOException {
		List<Ausgabe> ausgabenListe = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader("ausgaben.txt"))) {
			String zeile;
			while ((zeile = reader.readLine()) != null) {
				String[] teile = zeile.split(";");
				
				if (teile.length == 3) {
					String kategorie = teile[0];
					String name = teile[1];
					float betrag = Float.parseFloat(teile[2]);
					
					Ausgabe ausgabe = new Ausgabe(kategorie, name, betrag);
					ausgabenListe.add(ausgabe);
				}
			}
		}
		return ausgabenListe;
	}
}
