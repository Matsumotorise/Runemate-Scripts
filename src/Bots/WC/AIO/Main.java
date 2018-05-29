package Bots.WC.AIO;

import Bots.Helper.Util;
import Bots.WC.AIO.GUI.GUIController;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Area.Circular;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;
import java.io.IOException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class Main extends LoopingBot implements EmbeddableUI {

	private String chop = "Chop down", tree = "Willow";
	private Area.Circular bankArea, chopArea;
	private boolean dropping, waitingForGUI = true;

	private ObjectProperty<Node> botInterfaceProperty;

	private Util util;

	public Main() {
		setEmbeddableUI(this);
	}

	@Override
	public ObjectProperty<? extends Node> botInterfaceProperty() {
		if (botInterfaceProperty == null) {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(new GUIController(this));
			try {
				Node n = loader.load(Resources.getAsStream("Bots/WC/AIO/GUI/WCController.fxml"));
				botInterfaceProperty = new SimpleObjectProperty<>(n);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return botInterfaceProperty;
	}

	public void onStart(String ... args) {
		super.onStart();
		setLoopDelay(250, 401);

		System.out.println("GUI starting");
		while (waitingForGUI) {
			Execution.delay(100, 200);
		}
		util = new Util(this);
		System.out.println("Starting");
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private void chop() {
		GameObject t = util.closestGameObject(tree, chop);
		if (t != null) {
			Camera.turnTo(t);
			if (!t.isVisible()) {
				Path p = BresenhamPath.buildTo(t);
				if (p != null) {
					p.step();
				}
			} else if (t.interact(chop)) {
				Execution.delayUntil(() -> Players.getLocal().getAnimationId() != -1, 11000);
			}
		}
	}

	private void bank() {
		util.bankAllExcept("axe");
	}

	private void walkToChopArea() {
		util.walkToArea(chopArea);
	}

	private void walkToBankArea() {
		util.walkToArea(bankArea);
	}

	private void drop() {
		util.drop("log");
	}

	@Override
	public void onLoop() {
		switch (getCurrentState()) {
			case CHOP:
				chop();
				break;
			case WALK_TO_CHOP:
				walkToChopArea();
				break;
			case BANK:
				bank();
				break;
			case WALK_TO_BANK:
				walkToBankArea();
				break;
			case DROP:
				drop();
				break;
		}
	}

	private State getCurrentState() {
		if (Inventory.isFull()) {
			if (!dropping) {
				if (bankArea.contains(Players.getLocal())) {
					return State.BANK;
				} else {
					return State.WALK_TO_BANK;
				}
			} else {
				return State.DROP;
			}
		} else {
			if (chopArea.contains(Players.getLocal())) {
				return State.CHOP;
			} else {
				return State.WALK_TO_CHOP;
			}
		}
	}

	public void setBankArea(Area.Circular bankArea) {
		this.bankArea = bankArea;
	}

	public void setChopArea(Area.Circular chopArea) {
		this.chopArea = chopArea;
	}

	public void setDropping(boolean dropping) {
		this.dropping = dropping;
	}

	public Circular getPlayerArea(int rad) {
		return new Circular(Players.getLocal().getPosition(), rad);
	}

	public void setTree(String tree) {
		this.tree = tree;
	}

	public void setWaitingForGUI(boolean waitingForGUI) {
		this.waitingForGUI = waitingForGUI;
	}

	private enum State {
		CHOP, WALK_TO_CHOP, BANK, WALK_TO_BANK, DROP
	}

}
