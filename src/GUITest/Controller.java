package GUITest;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;

public class Controller implements Initializable {

	@FXML
	public Button start;
	public ToggleGroup t;

	public void start(ActionEvent actionEvent) {
		System.out.println("Click");
		if(start.getText().equals("Stopped")){
			start.setText("Started");
		} else if(start.getText().equals("Started")){
			start.setText("Stopped");
		} else {
			start.setText("Started");
		}

		System.out.println(t.getSelectedToggle().toString());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Loading");
	}

}
