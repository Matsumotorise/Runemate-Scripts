package Bots.WC.AIO.GUI;

import Bots.Helper.Location;
import Bots.Helper.Util;
import com.runemate.game.api.hybrid.location.Area;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class GUIController implements Initializable {

	@FXML
	public Button init;

	@FXML
	public TextField radius;

	@FXML
	public ComboBox<String> treeType, bankArea;

	@FXML
	public CheckBox drop;


	private int rad, maxDelayUntilTreeDies;
	private String tree;
	private boolean dropping, waitingForGUI;
	private Area bankA;
	private Util util;


	public GUIController(Util util) {
		this.util = util;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		treeType.getItems().addAll("Tree", "Oak Tree", "Willow Tree", "Yew Tree");
		bankArea.getItems().addAll("Lumby", "Draynor");
		waitingForGUI = true;
	}

	public void initBtn() {
		rad = getRadius();

		switch (treeType.getSelectionModel().getSelectedIndex()) {
			case 0:
				tree = "Tree";
				maxDelayUntilTreeDies = 30000;
				break;
			case 1:
				tree = "Oak";
				maxDelayUntilTreeDies = 40000;
				break;
			case 2:
				tree = "Willow";
				maxDelayUntilTreeDies = 50000;
				break;
			case 3:
				tree = "Yew";
				maxDelayUntilTreeDies = 60000;
				break;
		}

		switch (bankArea.getSelectionModel().getSelectedIndex()) {
			case 0:
				bankA = Location.LUMBRIDGE_BANK.getArea();
				break;
			case 1:
				bankA = Location.DRAYNOR_BANK.getArea();
				break;
		}

		dropping = drop.isSelected();

		waitingForGUI = false;
	}

	private int getRadius() {
		try {
			return Integer.parseInt(radius.getText());
		} catch (NumberFormatException error) {
			error.printStackTrace();
		}
		return 10;
	}

	public int getRad() {
		return rad;
	}

	public String getTree() {
		return tree;
	}

	public boolean isDropping() {
		return dropping;
	}

	public boolean isWaitingForGUI() {
		return waitingForGUI;
	}

	public Area getBankA() {
		return bankA;
	}

	public int getMaxDelayUntilTreeDies() {
		return maxDelayUntilTreeDies;
	}
}


