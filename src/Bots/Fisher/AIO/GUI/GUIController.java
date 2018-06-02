package Bots.Fisher.AIO.GUI;

import Bots.Helper.Location;
import Bots.Helper.Util;
import com.runemate.game.api.hybrid.location.Area.Circular;
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
	private String fishHole;
	private String[] fishHoles;
	private boolean dropping, waitingForGUI;
	private Circular bankA;
	private Util util;

	public GUIController(Util util) {
		this.util = util;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fishHoles = new String[]{"net", "bait"};
		treeType.getItems().addAll(fishHoles);
		bankArea.getItems().addAll("Lumby", "Draynor");
		waitingForGUI = true;
	}

	public void initBtn() {
		rad = getRadius();
		fishHole = treeType.getSelectionModel().getSelectedItem();
		switch (bankArea.getSelectionModel().getSelectedIndex()) {
			case 0:
				bankA = (Circular) Location.LUMBRIDGE_BANK.getArea();
				break;
			case 1:
				bankA = (Circular) Location.DRAYNOR_BANK.getArea();
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

	public String getFishHoleName() {
		return fishHole;
	}

	public boolean isDropping() {
		return dropping;
	}

	public boolean isWaitingForGUI() {
		return waitingForGUI;
	}

	public Circular getBankA() {
		return bankA;
	}

	public int getMaxDelayUntilTreeDies() {
		return maxDelayUntilTreeDies;
	}
}


