package Bots.Helper;

import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceWindows;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.location.navigation.basic.ViewportPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import java.util.function.Predicate;

public class PlayerAction {

	public PlayerAction() {
	}

	public void bank(Predicate<SpriteItem> filter) {
		bankAllExcept(filter.negate());
	}

	public void bankAllExcept(String endsWith) {
		bankAllExcept((e -> e.getDefinition().getName().toLowerCase().endsWith(endsWith)));
	}

	public void bankAllExcept(Predicate<SpriteItem> filter) {
		if (Bank.isOpen()) {
			Bank.depositAllExcept(filter);
		} else {
			Bank.open();
			Execution.delayUntil(() -> Bank.isOpen(), 1000);
		}
	}

	public void withdraw(Predicate<SpriteItem> filter, int amt) {
		if (Bank.isOpen()) {
			Bank.withdraw(filter, amt);
		} else {
			Bank.open();
			Execution.delayUntil(() -> Bank.isOpen(), 1000);
		}
	}

	public void drop(Predicate<SpriteItem> filter) {
		if (InterfaceWindows.getInventory().isOpen()) {
			for (SpriteItem s : Inventory.getItems(filter)) {
				if (s.interact("Drop")) {
					Execution.delayUntil(() -> !s.isValid(), 2000, 3000);
				}
			}
		} else {
			InterfaceWindows.getInventory().open();
			Execution.delayUntil(() -> InterfaceWindows.getInventory().isOpen(), 500);
		}
	}

	public void drop(String endsWith) {
		drop(spriteItem -> spriteItem.getDefinition().getName().toLowerCase().endsWith(endsWith));
	}

	public void dropAllExcept(Predicate<SpriteItem> exclusion) {
		drop(exclusion.negate());
	}

	public void walkWithViewPort(Area location) {
		BresenhamPath p = BresenhamPath.buildTo(location.getRandomCoordinate());
		Camera.turnTo(location);
		if (p != null) {
			if (location.distanceTo(Players.getLocal()) >= 8 || !ViewportPath.convert(p)
					.step()) {  // this will attempt to walk with the viewport if the distance to the destination is < 8
				p.step();                                                                            // if it cant walk with viewport (camera not correctly set for example), step() will return false,
			}
		}
		walkDelay();
	}

	public void walkWithViewPort(Coordinate location) {
		BresenhamPath p = BresenhamPath.buildTo(location);
		Camera.turnTo(location);
		if (p != null) {
			if (location.distanceTo(Players.getLocal()) >= 8 || !ViewportPath.convert(p)
					.step()) {  // this will attempt to walk with the viewport if the distance to the destination is < 8
				p.step();                                                                            // if it cant walk with viewport (camera not correctly set for example), step() will return false,
			}
		}
		walkDelay();

	}

	private void walkDelay() {
		Execution.delayUntil(() -> Players.getLocal().isMoving(), 200, 400);
		Execution.delayUntil(() -> !Players.getLocal().isMoving(), 4000, 6000);
	}

}