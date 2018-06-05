package Bots.Fisher.AIO.GUI;

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
	public ComboBox<String> fishUtilField, bankArea;

	@FXML
	public CheckBox drop;

	private int rad, maxDelayUntilTreeDies;
	private String[] fishUtils;
	private String fishUtil, fishHoleAction, bait;
	private boolean dropping, waitingForGUI;
	private Area bankA;

	public GUIController() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bait = "";
		fishUtils = new String[]{"Small Net", "Fishing rod", "Fly fishing rod", "Harpoon", "Lobster pot"};
		fishUtilField.getItems().addAll(fishUtils);
		bankArea.getItems().addAll("Lumby", "Draynor");
		waitingForGUI = true;
	}

	public void initBtn() {
		rad = getRadius();
		fishUtil = fishUtilField.getSelectionModel().getSelectedItem();

		int i = fishUtilField.getSelectionModel().getSelectedIndex();
		switch (i) {
			case 0:
				fishHoleAction = "Small net";
				break;
			case 1:
				fishHoleAction = "Bait";
				bait = "fishing bait";
				break;
			case 2:
				fishHoleAction = "Lure";
				bait = "Feathers";
				break;
			case 3:
				fishHoleAction = "Harpoon";
				break;
			case 4:
				fishHoleAction = "Cage";
				break;
		}

		i = bankArea.getSelectionModel().getSelectedIndex();
		switch (i) {
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

	public String getFishHoleAction() {
		return fishHoleAction;
	}

	public String getFishUtil() {
		return fishUtil;
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

	public String getBait() {
		return bait;
	}
}


