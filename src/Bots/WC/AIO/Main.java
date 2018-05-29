package Bots.WC.AIO;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceWindows;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.location.navigation.basic.ViewportPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;

public class Main extends LoopingBot {

	private final String CHOP = "Chop down", TREE = "Willow";
	private Area bankArea, chopArea;
	private boolean dropping;

	public void onStart(String... args) {
		super.onStart();
		setLoopDelay(250, 401);
		System.out.println("Starting");
	}

	@Override
	public void onStop() {
		super.onStop();
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

	private void chop() {
		GameObject tree = treeSearch();
		if (tree != null) {
			Camera.turnTo(tree);
			if (!tree.isVisible()) {
				Path p = BresenhamPath.buildTo(tree);
				if (p != null) {
					p.step();
				}
			} else if (tree.interact(CHOP)) {
				Execution.delayUntil(() -> Players.getLocal().getAnimationId() != -1, 11000);
			}
		}
	}

	private void bank() {

	}

	private void walkToChopArea() {
		BresenhamPath p = BresenhamPath.buildTo(chopArea.getRandomCoordinate());
		Camera.turnTo(chopArea);
		if (p != null) {
			if (chopArea.distanceTo(Players.getLocal()) >= 8 || !ViewportPath.convert(p).step()) {  // this will attempt to walk with the viewport if the distance to the destination is < 8
				p.step();                                                                            // if it cant walk with viewport (camera not correctly set for example), step() will return false,
			}
		}
		walkDelay();
	}

	private void walkToBankArea() {
		BresenhamPath p = BresenhamPath.buildTo(bankArea.getRandomCoordinate());
		Camera.turnTo(bankArea);
		if (p != null) {
			if (bankArea.distanceTo(Players.getLocal()) >= 8 || !ViewportPath.convert(p).step()) {
				p.step();
			}
		}
		walkDelay();
	}

	private void drop() {
		if (InterfaceWindows.getInventory().isOpen()) {
			for (SpriteItem s : Inventory
					.getItems(spriteItem -> spriteItem.getDefinition().getName().toLowerCase().endsWith("log"))) {
				if (s.interact("Drop")) {
					Execution.delayUntil(() -> !s.isValid(), 2000);
				}
			}
		} else {
			InterfaceWindows.getInventory().open();
			Execution.delayUntil(() -> InterfaceWindows.getInventory().isOpen(), 500);
		}
	}

	private void walkDelay() {
		Execution.delayUntil(() -> !Players.getLocal().isMoving(), 4000, 6000);
	}

	private GameObject treeSearch() {
		return GameObjects.newQuery().names(TREE).actions(CHOP).results().nearest();
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

	private enum State {
		CHOP, WALK_TO_CHOP, BANK, WALK_TO_BANK, DROP
	}

}
