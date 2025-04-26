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
	
	//Schreiben der Textdatei
	public static void speichereInDatei(Ausgabe ausgabe) throws IOException {
		
		try (PrintWriter writer = new PrintWriter(new FileWriter("ausgaben.txt", true))) {
			writer.println(ausgabe.getDatum() + ";" + ausgabe.getKategorie() + ";" + ausgabe.getBetrag());
		} catch (IOException e) {
			System.out.println("Fehler beim Schreiben in Datei; " + e.getMessage());
		}
	}
	
	//Liest die angegebene Datei aus und returnt eine Liste mit dem Inhalt
	public static List<Ausgabe> ladeAusgaben() throws FileNotFoundException, IOException {
		List<Ausgabe> ausgaben = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader("ausgaben.txt"))) {
			String zeile;
			while ((zeile = reader.readLine()) != null) {
				String[] teile = zeile.split(";");
				
				if (teile.length == 3) {
					String datumStr = teile[0];
					String kategorie = teile[1];
					float betrag = Float.parseFloat(teile[2]);
					
					Ausgabe ausgabe = new Ausgabe(kategorie, betrag, datumStr);
					ausgaben.add(ausgabe);
				}
			}
		}
		return ausgaben;
	}
}
