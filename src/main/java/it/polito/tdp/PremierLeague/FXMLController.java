/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Defeated;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	Double x;
    	
    	try {
    		x=Double.parseDouble(txtGoals.getText());
    		
    	}catch(NumberFormatException nfe) {
    		txtResult.setText("Inserisci un numero min di goal fatti");
    		return;
    	}catch(NullPointerException npe) {
    		txtResult.setText("Inserisci un numero min di goal fatti");
    		return;
    	}
    	
    	model.creaGrafo(x);
    	txtResult.appendText("Caratteristiche del grafo:\n#VERTICI = "+model.getNVertici()+"\n#ARCHI = "+model.getNArchi());
    	
    	
    	
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	//CONTROLLA SE ESISTE GRAFO
    	if(model.getGraph()==null) {
    		txtResult.setText("Devi prima creare il grafo!");
    		return;
    	}
    	Integer k;
    	
    	try {
    		k=Integer.parseInt(txtK.getText());
    		
    	}catch(NumberFormatException nfe) {
    		txtResult.setText("Inserisci un numero di giocatori k");
    		return;
    	}catch(NullPointerException npe) {
    		txtResult.setText("Inserisci un numero di giocatori k");
    		return;
    	}
    	
    	List <Player> dream =model.trovaDreamTeam(k);
    	txtResult.appendText("\n\n Il dream team ha un grado di titolarità di: "+model.getGradoDream()+"\nE' formato da:\n");
    	for(Player pi:dream )
    		txtResult.appendText(pi+"\n");
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	//CONTROLLA SE ESISTE GRAFO
    	if(model.getGraph()==null) {
    		txtResult.setText("Devi prima creare il grafo!");
    		return;
    	}
    	Player top = model.getStrongest();
    	txtResult.appendText("\n\nIl giocatore TOP è: \n"+top);
    	txtResult.appendText("\nI giocatori da lui battuti sono:\n ");
    	for(Defeated d: model.getBattuti(top))
    		txtResult.appendText(d+"\n");

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
