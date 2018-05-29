package Bots.WC.AIO.GUI;

import Bots.WC.AIO.Main;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class GUIController implements Initializable {

	private Main bot;

	@FXML
	private Button init;

	@FXML
	private TextField radius;

	@FXML
	private ComboBox<String> treeType, bankArea;

	@FXML
	private CheckBox drop;

	public GUIController(Main m) {
		bot = m;
	}

	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		treeType.getItems().addAll("Tree", "Oak Tree", "Willow Tree", "Yew Tree");
		bankArea.getItems().addAll("Lumby", "Draynor");
		bot.setChopArea(bot.getPlayerArea(getRadius()));
		bot.setDropping(drop.isSelected());

	}

	public void initBtn(){
		switch (treeType.getSelectionModel().getSelectedIndex()) {
			case 0:
				bot.setTree("Tree");
				break;
			case 1:
				bot.setTree("Oak Tree");
				break;
			case 2:
				bot.setTree("Willow Tree");
				break;
			case 3:
				bot.setTree("Yew Tree");
				break;
		}

		Coordinate c = null;
		switch (bankArea.getSelectionModel().getSelectedIndex()) {
			case 0:
				c = new Coordinate(3221, 3218, 0);
				break;
			case 1:
				c = new Coordinate(3094, 3516, 0);
				break;
		}
		bot.setBankArea(new Area.Circular(c, getRadius()));

		bot.setWaitingForGUI(false);
	}



	private int getRadius() {
		try {
			return Integer.parseInt(radius.getText());
		} catch (NumberFormatException error) {
			error.printStackTrace();
		}
		return 10;
	}
}

