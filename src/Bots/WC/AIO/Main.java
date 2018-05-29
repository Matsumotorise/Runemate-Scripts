package Bots.WC.AIO;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceWindows;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;

public class Main extends LoopingBot {

	private Area bankArea, chopArea;
	private boolean dropping;
	private final String CHOP = "Chop down", TREE = "Willow";

	@Override
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
		} else {
			if (chopArea.contains(Players.getLocal())) {
				return State.CHOP;
			} else {
				return State.WALK_TO_CHOP;
			}
		}
		return null;
	}

	private void chop() {
		GameObject tree = GameObjects.newQuery().names(TREE).actions(CHOP).results().nearest();
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

	private void walkToChop() {

	}

	private void walkToBank() {

	}

	private void bank() {

	}

	private void drop() {

	}

	private void walkDelay() {
		Execution.delayUntil(() -> !Players.getLocal().isMoving(), 4000, 6000);
	}

	@Override
	public void onLoop() {
		switch (getCurrentState()) {
			case CHOP:
				chop();
				break;
			case WALK_TO_CHOP:
				walkToChop();
				break;
			case BANK:
				bank();
				break;
			case WALK_TO_BANK:
				walkToBank();
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
