package com.rechner.projekt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main extends Application {
	
	//eigentliche Mainmethode
    public void start(Stage primaryStage) throws FileNotFoundException, IOException
    {
    	//Pastet Liste mit biherigen Eingaben aus gespeicherter Textdatei in Label
    	List<Ausgabe> bisherigeAusgaben = DateiSpeichern.ladeAusgaben();
    	Label textBisherigeAusgaben = new Label();
    	for (Ausgabe a : bisherigeAusgaben) {
    		textBisherigeAusgaben.setText(textBisherigeAusgaben.getText() + a.toString() + "\n");
    		System.out.println(a);
    	}
    	
        Label label = new Label("Was willst du hinzufügen?\n\n");
        Label textFeldInhalt = new Label();
    	
    	// Eingabefeld für Eingabe
    	TextField eingabeFeld = new TextField();
    	eingabeFeld.setPromptText("Bitte gib ein: Kategorie Betrag (z.B. Miete 750)");
    	
    	Button hinzufuegenButton = new Button("Hinzufügen");
    	
    	//Erstellt ein Objekt der Klasse Ausgabe und schreibt dieses dann in ein Label, sowie in die gespeicherte Datei wenn der Button gedrückt wird
    	hinzufuegenButton.setOnAction(e -> {
    		String text = eingabeFeld.getText().trim();
    		
    		if (!text.isEmpty()) {
				String[] teile = text.split(" ", 2);
				
				if (teile.length == 2) {
					try {
						String kategorie = teile[0];
						float betrag = Float.parseFloat(teile[1]);
						String datumStr = LocalDate.now().toString();
						
						Ausgabe ausgabe = new Ausgabe(kategorie, betrag, datumStr);
						textFeldInhalt.setText("Du hast eingegeben: " + ausgabe);
						DateiSpeichern.speichereInDatei(ausgabe);
					} catch (NumberFormatException | IOException ex) {
						textFeldInhalt.setText("Fehler Betrag ist keine gültige Zahl");
					}
				} else {
					textFeldInhalt.setText("Bitte gib ein: Kategorie Betrag (z.B. Miete 750)");
				}
				
				eingabeFeld.clear();
			}
    	});
    	
    	//Pastet alle GUI Elemente nacheinander in ein Layout 
    	VBox layout = new VBox(10);
    	layout.getChildren().addAll(label, eingabeFeld, textFeldInhalt, hinzufuegenButton, textBisherigeAusgaben);
    	
    	//Fenster wird erstellt
    	Scene scene = new Scene(layout, 400, 400);
    	primaryStage.setTitle("Ausgabentracker");
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	
    }
    
    //Startet die ganze Geschichte
    public static void main(String[] args) {
    	launch(args);
    }
}
