package com.rechner.projekt;

import java.util.List;

public class AusgabenManager {

	//rechnet die Kosten aus dem gepseicherten Txt-File zusammen
	public static float berechneGesamtausgaben(List<Ausgabe> ausgabenListe) {

		float summe = 0;

		for (Ausgabe a : ausgabenListe) {
			summe += a.getBetrag();
		}
		
		return summe;
	}
}
