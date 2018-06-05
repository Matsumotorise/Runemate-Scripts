package Bots.WC.AIO;

import Bots.Helper.Util;
import Bots.WC.AIO.GUI.GUIController;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
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

	private boolean dropping;
	private String chop, tree;
	private long loopNum;

	private GUIController controller;
	private Area.Circular chopArea;
	private Area bankArea;
	private ObjectProperty<Node> botInterfaceProperty;
	private Util util;

	public Main() {
		chop = "Chop down";
		tree = "Tree";
		loopNum = 0;
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
		//OSRS.LOGIN_HANDLER.disable();
		util = new Util(this);
		setLoopDelay(250, 401);
		System.out.println("GUI starting");
		try {
			Execution.delayUntil(() -> {
				System.out.println("Waiting for information");
				Execution.delay(2000);
				return !controller.isWaitingForGUI();
			}, 1200000, 1290000);
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
		if (Players.getLocal().getAnimationId() == 879) {
			chopWait();
		}

		GameObject t = util.getClosestGameObject(tree, chop);
		if (t != null) {
			if (Math.random() < .25) {
				Camera.turnTo(t);
			}
			if (!t.isVisible()) {
				Path p = RegionPath.buildTo(t);
				if (p != null) {
					p.step();
				}
			} else if (!Players.getLocal().isMoving() && Players.getLocal().getAnimationId() != 879 && t.isValid()) {
				System.out.println("Choppin");
				t.interact(chop);
				chopWait();
			}
		}
	}

	private void chopWait() {
		Execution.delayUntil(() -> Players.getLocal().isMoving() || util.getplayerAction().isTargeted(), 1800);
		Execution.delayUntil(() -> Players.getLocal().getAnimationId() == 879 || util.getplayerAction().isTargeted(), 1800);
		Execution.delayUntil(() -> Players.getLocal().getAnimationId() == -1 || util.getplayerAction().isTargeted(),
				controller.getMaxDelayUntilTreeDies());
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
		loopNum++;
		System.out.println("Loop Number: " + loopNum);
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
		if (Inventory.isFull() || util.getplayerAction().isTargeted()) {
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
