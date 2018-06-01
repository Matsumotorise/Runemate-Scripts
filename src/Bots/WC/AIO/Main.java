package Bots.WC.AIO;

import Bots.Helper.Util;
import Bots.WC.AIO.GUI.GUIController;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.GameEvents.OSRS;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.AbstractBot;
import com.runemate.game.api.script.framework.AbstractBot.State;
import com.runemate.game.api.script.framework.LoopingBot;
import java.io.IOException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class Main extends LoopingBot implements EmbeddableUI {

	private boolean dropping;
	private String chop, tree;

	private GUIController controller;
	private Area.Circular bankArea, chopArea;
	private ObjectProperty<Node> botInterfaceProperty;
	private Util util;

	public Main() {
		chop = "Chop down";
		tree = "Tree";
		setEmbeddableUI(this);
	}

	@Override
	public ObjectProperty<? extends Node> botInterfaceProperty() {
		if (botInterfaceProperty == null) {
			FXMLLoader loader = new FXMLLoader();
			controller = new GUIController(util);
			loader.setController(controller);
			try {
				Node n = loader.load(Resources.getAsStream("Bots/WC/AIO/GUI/WCController.fxml"));
				botInterfaceProperty = new SimpleObjectProperty<>(n);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return botInterfaceProperty;
	}

	public void onStart(String... args) {
		super.onStart();
		OSRS.LOGIN_HANDLER.disable();
		util = new Util(this);
		setLoopDelay(250, 401);

		System.out.println("GUI starting");
		try {
			Execution.delay(1000);
			while (controller.isWaitingForGUI()) {
				Execution.delay(400, 500);
				System.out.println("Waiting for information");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Starting");

		chopArea = util.generateAreaAroundEntity(Players.getLocal(), controller.getRad());
		bankArea = controller.getBankA();
		dropping = controller.isDropping();
		tree = controller.getTree();
		System.out.println("/////////Variable details//////////");
		System.out.println(chopArea);
		System.out.println(bankArea);
		System.out.println(dropping);
		System.out.println(tree);
		System.out.println("/////////Variable details//////////");
	}

	@Override
	public void onStop() {
		super.onStop();
		System.out.println("/////////////////Stopping////////////////////");
	}

	@Override
	public void onPause() {
		super.onPause();
		System.out.println("///////////////////Pausing////////////////////");
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("////////////Resuming////////////");
	}

	private void walkToChopArea() {
		util.getplayerAction().walkWithViewPort(chopArea);
	}

	private void chop() {
		GameObject t = util.closestGameObject(tree, chop);
		if (t != null) {
			if (!t.isVisible()) {
				//TODO fix path to make roundabout path
				Path p = BresenhamPath.buildTo(t);
				if (p != null) {
					p.step();
					if (Math.random() < .25) {
						Camera.turnTo(t);
					}
				}
			} else if (!Players.getLocal().isMoving() && Players.getLocal().getAnimationId() != 879 && t.isValid()) {
				t.interact(chop);
				Execution.delayUntil(() -> Players.getLocal().isMoving(), 1800);
				Execution.delayUntil(() -> Players.getLocal().getAnimationId() == 879, 1800);
				Execution.delayUntil(() -> Players.getLocal().getAnimationId() == -1, controller.getMaxDelayUntilTreeDies());
			}
		}
	}
	private void walkToBankArea() {
		util.getplayerAction().walkWithViewPort(bankArea.getRandomCoordinate());
	}

	//TODO fix banking
	private void bank() {
		util.getplayerAction().bankAllExcept("axe");
	}
	private void drop() {
		util.getplayerAction().drop("log");
	}

	@Override
	public void onLoop() {
		System.out.println("Looping");
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
					System.out.println("Banking");
					return State.BANK;
				} else {
					System.out.println("Walking to bank");
					return State.WALK_TO_BANK;
				}
			} else {
				System.out.println("Dropping");
				return State.DROP;
			}
		} else {
			if (chopArea.contains(Players.getLocal())) {
				System.out.println("Choppin");
				return State.CHOP;
			} else {
				System.out.println("Walking to woods");
				return State.WALK_TO_CHOP;
			}
		}
	}

	private enum State {
		CHOP, WALK_TO_CHOP, BANK, WALK_TO_BANK, DROP
	}

}
