/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
    	//String goalStr;
    	float goal; //con il PUNTO
    	try {
    		//goalStr=txtGoals.getText();
    		goal=Float.parseFloat(txtGoals.getText());
    	} catch(NumberFormatException e) {
    		txtResult.setText("Inserire un numero minimo di goal a partita!");
    		return;
    	} catch (NullPointerException npe) {
    		txtResult.setText("Inserire un numero minimo di goal a partita!");
    		return;
    	}
    	
    	this.model.creaGrafo(goal);
    	
    	txtResult.appendText(String.format("#VERTICI = %d\n#ARCHI = %d\n", model.nVertici(),model.nArchi()));
    	txtResult.appendText("ARCHI:\n");
    	txtResult.appendText(model.getArchi().toString());
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	txtResult.appendText("\n");
    	
    	Integer k; //n giocatori
    	
    	try {
    		k = Integer.parseInt(txtK.getText());
    		
    	} catch(NumberFormatException e) {
    		txtResult.setText("Inserire un numero minimo di goal a partita!");
    		return;
    	} catch (NullPointerException npe) {
    		txtResult.setText("Inserire un numero minimo di goal a partita!");
    		return;
    	}
    	
    	txtResult.appendText("Il DreamTeam è composto da:\n");
    	List <Player> best = model.getDreamTeam(k);
    	String dreamTeam="";
		for(Player p: best)
			dreamTeam+=p.getName()+"\n";
    	txtResult.appendText(dreamTeam);
    	txtResult.appendText("Il GRADO di titolarità è di: "+model.getGradoTot(best));
    	
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	txtResult.appendText("\n");
    	Player top = model.getTopPlayer();
    	
    	if(top==null) {
    		txtResult.appendText("Crea prima il GRAFO!\n");
    		return;
    	}
    	
    	txtResult.appendText("Il top player è: "+top+"\n");
    	txtResult.appendText("Ha superato i seguenti giocatori in MINUTI giocati:\n");
    	txtResult.appendText(model.getSconfitti(top).toString());

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
