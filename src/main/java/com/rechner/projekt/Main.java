package com.rechner.projekt;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends Application {

	private BarChart<String, Number> kategorieDiagramm;
	private PieChart pieChart;

	// eigentliche Mainmethode
	public void start(Stage primaryStage) throws FileNotFoundException, IOException {

		// Layout erstellen
		VBox layoutVerwalten = new VBox(10);
		layoutVerwalten.setPadding(new Insets(15));
		VBox layoutAnalyse = new VBox(10);
		layoutAnalyse.setPadding(new Insets(15));

		// Tabs erstellen
		TabPane tabPane = new TabPane();

		Tab tabVerwalten = new Tab("Verwalten", layoutVerwalten);
		tabVerwalten.setClosable(false);

		Tab tabAnalyse = new Tab("Analyse", layoutAnalyse);
		tabAnalyse.setClosable(false);

		tabPane.getTabs().addAll(tabVerwalten, tabAnalyse);

		// Liste aus Dateien laden
		List<Ausgabe> bisherigeAusgaben = DateiSpeichern.ladeAusgaben();

		// ListView für Ausgabenanzeige
		ListView<Ausgabe> ausgabenListeView = new ListView<Ausgabe>();
		ausgabenListeView.getItems().addAll(bisherigeAusgaben);
		ausgabenListeView.setPrefHeight(300);
		ausgabenListeView.setFixedCellSize(26);

		// Erstellen und formatieren des Label GesamtAnalyse
		Label labelGesamtAnalyse = new Label();
		labelGesamtAnalyse.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
		labelGesamtAnalyse.setText("Gesamtausgaben: "
				+ String.format("%.2f", AusgabenManager.berechneGesamtausgaben(ausgabenListeView.getItems())) + " €\n\n");

		ComboBox<String> kategorieComboBox = new ComboBox<>();

		// Label mit Überschrift halt
		Label labelUeberschrift = new Label("Neue Ausgabe hinzufügen:\n\n");
		Label textFeldInhalt = new Label();

		// Eingabefeld für Name
		TextField nameFeld = new TextField();
		nameFeld.setPromptText("Bitte gib ein: Name  (z.B. Rewe )");

		// Eingabefeld für Betrag
		TextField betragFeld = new TextField();
		betragFeld.setPromptText("Bitte gib einen Betrag ein (z.B. 420.69)");

		// Erstellt label mit den Gesamtausgaben aus gespeichertem Txt mit der Methode
		// der Klasse AusgabenManager
		Label labelSummeAusgaben = new Label("\nFixkosten: "
				+ String.format("%.2f", AusgabenManager.berechneGesamtausgaben(ausgabenListeView.getItems())) + " €");

		// Buttons um geschriebenes hinzuzufügen und inhalte aus der liste sowie aus dem
		// textfile zu löschen
		Button hinzufuegenButton = new Button("Hinzufügen");
		Button loeschenButton = new Button("Löschen");
		Button exportButton = new Button("Als CSV Exportieren");

		// erzeugt diagramm mit dem unten aufgeführten konstruktor
		kategorieDiagramm = erstelleKategorieDiagramm(ausgabenListeView.getItems());

		// Erstellt ein Objekt der Klasse Ausgabe und schreibt dieses dann in ein Label,
		// sowie in die gespeicherte Datei wenn der Button gedrückt wird
		// Außerdem wird die Ausgabe in die bisherigen Ausgaben aufgenommen (Label++)
		hinzufuegenButton.setOnAction(e -> {
			String name = nameFeld.getText().trim();
			Float betrag = Float.parseFloat(betragFeld.getText().trim().replace(",", ".").replace("€", ""));

			if (kategorieComboBox.getValue() != null) {
				if (!name.isEmpty() && (betrag != 0)) {
					try {
						Ausgabe ausgabe = new Ausgabe(kategorieComboBox.getValue().trim(), name, betrag);
						ausgabenListeView.getItems().add(ausgabe);
						DateiSpeichern.speichereInDatei(ausgabe);

						labelSummeAusgaben
								.setText(
										"Fixkosten: "
												+ String.format("%.2f",
														AusgabenManager
																.berechneGesamtausgaben(ausgabenListeView.getItems()))
												+ " €");
						aktualisiereDiagramm(layoutVerwalten, ausgabenListeView.getItems(), labelGesamtAnalyse);
						aktualisierePieChart(layoutAnalyse, ausgabenListeView.getItems());

					} catch (NumberFormatException | IOException ex) {
						textFeldInhalt.setText("Fehler Betrag ist keine gültige Zahl");
					}

					kategorieComboBox.getSelectionModel().clearSelection();
					nameFeld.clear();
					betragFeld.clear();
				}
			} else {
				textFeldInhalt.setText("Bitte wähle eine Kategorie!");
				return;
			}
		});

		// Button der aus der Liste sowie auch aus der Textdatei löscht
		loeschenButton.setOnAction(e -> {
			Ausgabe ausgewaehlt = ausgabenListeView.getSelectionModel().getSelectedItem();

			if (ausgewaehlt != null) {
				ausgabenListeView.getItems().remove(ausgewaehlt); // aus GÙI entfernen
				labelSummeAusgaben.setText("Fixkosten: "
						+ String.format("%.2f", AusgabenManager.berechneGesamtausgaben(ausgabenListeView.getItems()))
						+ " €\n");
				aktualisiereDiagramm(layoutVerwalten, ausgabenListeView.getItems(), labelGesamtAnalyse);
				aktualisierePieChart(layoutAnalyse, ausgabenListeView.getItems());
				try {
					DateiSpeichern.ueberschreibeDatei(ausgabenListeView.getItems()); // Datei überschreiben
				} catch (IOException ex) {
					System.out.println("Fehler beim  überschreiben der Datei: " + ex.getMessage());
				}
			}
		});

		// Exportbutton
		exportButton.setOnAction(e -> {
			try {
				DateiSpeichern.exportiereAlsCSV(ausgabenListeView.getItems(), "export.csv");
				textFeldInhalt.setText("Export erfolgreich: export.csv erstellt.");
			} catch (IOException ex) {
				textFeldInhalt.setText("Fehler beim Export");
				ex.printStackTrace();
			}
		});

		// Erstelle PieChart
		pieChart = erstelleKategoriePieChart(ausgabenListeView.getItems());

		// Dropdownmenu um die kategorie des datensatzes auszuwählen
		kategorieComboBox.getItems().addAll("Miete", "Lebensmittel", "Versicherung", "Freizeit", "Sparen", "Sonstiges");
		kategorieComboBox.setPromptText("Kategorie auswählen");

		// Pastet alle GUI Elemente nacheinander in ein Layout
		layoutVerwalten.getChildren().addAll(labelUeberschrift, kategorieComboBox, nameFeld, betragFeld,
				hinzufuegenButton, textFeldInhalt, ausgabenListeView, loeschenButton, labelSummeAusgaben,
				kategorieDiagramm, exportButton);
		layoutAnalyse.getChildren().addAll(labelGesamtAnalyse, pieChart);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

		// Fenster wird erstellt
		Scene scene = new Scene(tabPane, 600, 1200);
		primaryStage.setTitle("Ausgabentracker");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	// Startet die ganze Geschichte
	public static void main(String[] args) {
		launch(args);

	}

	private BarChart<String, Number> erstelleKategorieDiagramm(List<Ausgabe> ausgaben) {
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Kategorie");

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Ausgaben (€)");

		BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
		barChart.setTitle("Ausgaben nach Kategorie");

		// Summe pro Kategorie berechnen
		Map<String, Double> kategorienSummen = new HashMap<>();
		for (Ausgabe a : ausgaben) {
			String kategorie = a.getKategorie().split(";")[0];
			kategorienSummen.put(kategorie, kategorienSummen.getOrDefault(kategorie, 0.0) + a.getBetrag());
		}

		XYChart.Series<String, Number> datenSerie = new XYChart.Series<>();
		datenSerie.setName("Gesamtausgaben");

		for (Map.Entry<String, Double> eintrag : kategorienSummen.entrySet()) {
			datenSerie.getData().add(new XYChart.Data<>(eintrag.getKey(), eintrag.getValue()));
		}

		barChart.getData().add(datenSerie);
		return barChart;
	}

	private void aktualisiereDiagramm(VBox layout, List<Ausgabe> aktuelleAusgabe, Label labelGesamtAnalyse) {
		layout.getChildren().remove(kategorieDiagramm);
		kategorieDiagramm = erstelleKategorieDiagramm(aktuelleAusgabe);
		layout.getChildren().add(kategorieDiagramm);
		
		//Gesamtsumme aktualisieren
		float summe = AusgabenManager.berechneGesamtausgaben(aktuelleAusgabe);
		labelGesamtAnalyse.setText("Gesamtausgaben: " + String.format("%.2f", summe) + " €");
	}

	private PieChart erstelleKategoriePieChart(List<Ausgabe> ausgaben) {
		Map<String, Float> kategorienSummen = new HashMap<>();
		for (Ausgabe a : ausgaben) {
			String kategorie = a.getKategorie();
			kategorienSummen.put(kategorie, kategorienSummen.getOrDefault(kategorie, (float) 0.0) + a.getBetrag());
		}

		PieChart pieChart = new PieChart();
		pieChart.setTitle("Verteilung nach Kategorien");

		for (Map.Entry<String, Float> eintrag : kategorienSummen.entrySet()) {
			pieChart.getData().add(new PieChart.Data(eintrag.getKey(), eintrag.getValue()));
		}

		return pieChart;
	}

	private void aktualisierePieChart(VBox layout, List<Ausgabe> aktuelleAusgabe) {
		layout.getChildren().remove(pieChart);
		pieChart = erstelleKategoriePieChart(aktuelleAusgabe);
		layout.getChildren().add(pieChart);
	}
}
