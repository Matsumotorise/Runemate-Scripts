package Bots.Fisher.AIO;

import Bots.Fisher.AIO.GUI.GUIController;
import Bots.Helper.StopWatch;
import Bots.Helper.Util;
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
	private String fishHoleAction, fishUtil, bait;
	private long loopNum;
	private int actionAnimation;
	private GUIController controller;
	private Area.Circular fishArea;
	private Area bankArea;
	private ObjectProperty<Node> botInterfaceProperty;
	private Util util;
	private StopWatch stopWatch;

	public Main() {
		actionAnimation = 879;
		loopNum = 0;
		setEmbeddableUI(this);
	}

	@Override
	public ObjectProperty<? extends Node> botInterfaceProperty() {
		if (botInterfaceProperty == null) {
			FXMLLoader loader = new FXMLLoader();
			controller = new GUIController();
			loader.setController(controller);
			try {
				Node n = loader.load(Resources.getAsStream("Bots/Fisher/AIO/GUI/WCController.fxml"));
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
		stopWatch = new StopWatch();
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

		fishArea = util.generateAreaAroundEntity(Players.getLocal(), controller.getRad());
		bankArea = controller.getBankA();
		dropping = controller.isDropping();
		fishHoleAction = controller.getFishHoleAction();
		fishUtil = controller.getFishUtil();
		bait = controller.getBait();

		System.out.println("/////////Variable details//////////");
		System.out.println("Fishing Area: " + fishArea);
		System.out.println("Banking Area: " + bankArea);
		System.out.println("Dropping: " + dropping);
		System.out.println("Fishing Action: " + fishHoleAction);
		System.out.println("Bait: " + bait);
		System.out.println("/////////Variable details//////////");
		stopWatch.start();
	}

	@Override
	public void onStop() {
		super.onStop();
		stopWatch.stop();
		System.out.println("/////////////////Stopping////////////////////");
	}

	@Override
	public void onPause() {
		super.onPause();
		stopWatch.stop();
		System.out.println("///////////////////Pausing////////////////////");
	}

	@Override
	public void onResume() {
		super.onResume();
		stopWatch.start();
		System.out.println("////////////Resuming////////////");
	}

	private void walkToChopArea() {
		util.getplayerAction().walkWithViewPort(fishArea);
	}

	private void fish() {
		if (Players.getLocal().getAnimationId() == actionAnimation) {
			fishWait();
		}

		GameObject t = util.getClosestGameObject("Fishing hole", fishHoleAction);
		if (t != null) {
			if (Math.random() < .25) {
				Camera.turnTo(t);
			}
			if (!t.isVisible()) {
				Path p = RegionPath.buildTo(t);
				if (p != null) {
					p.step();
				}
			} else if (!Players.getLocal().isMoving() && Players.getLocal().getAnimationId() != actionAnimation && t
					.isValid()) {
				System.out.println("Fishin");
				t.interact(fishHoleAction);
				fishWait();
			}
		}
	}

	private void fishWait() {
		Execution.delayUntil(() -> Players.getLocal().isMoving() || util.getplayerAction().isTargeted(), 1800);
		Execution
				.delayUntil(() -> Players.getLocal().getAnimationId() == actionAnimation || util.getplayerAction().isTargeted(),
						1800);
		Execution.delayUntil(() -> Players.getLocal().getAnimationId() == -1 || util.getplayerAction().isTargeted(),
				controller.getMaxDelayUntilTreeDies());
	}

	private void walkToBankArea() {
		util.getplayerAction().walkWithViewPort(bankArea.getRandomCoordinate());
	}

	private void bank() {
		util.getplayerAction()
				.bankAllExcept(e -> e.getDefinition().getName().equals(fishUtil) || e.getDefinition().getName().equals(bait));
	}

	private void drop() {
		util.getplayerAction().drop(e -> !e.getDefinition().getName().equals(fishUtil) || e.getDefinition().equals(bait));
	}

	@Override
	public void onLoop() {
		loopNum++;
		System.out.println("Loop Number: " + loopNum + "\t" + stopWatch.toString());
		switch (getCurrentState()) {
			case FISH:
				fish();
				break;
			case WALK_TO_FISH:
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

	//TODO logic for fishHoleAction states
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
			if (fishArea.contains(Players.getLocal())) {
				return State.FISH;
			} else {
				System.out.println("Walking to woods");
				return State.WALK_TO_FISH;
			}
		}
	}

	private enum State {
		FISH, WALK_TO_FISH, BANK, WALK_TO_BANK, DROP
	}

}
