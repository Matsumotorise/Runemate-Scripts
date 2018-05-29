package Template.oop.Tasks;

import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceWindows;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Drop extends Task {

	@Override
	public boolean validate() {
		return Inventory.isFull();
	}

	@Override
	public void execute() {
		if (InterfaceWindows.getInventory().isOpen()) {
			for (SpriteItem s : Inventory
					.getItems(spriteItem -> spriteItem.getDefinition().getName().toLowerCase().endsWith("log"))) {
				if (s.interact("Drop")) {
					Execution.delayUntil(() -> !s.isValid(), 1000);
				}
			}
		} else {
			InterfaceWindows.getInventory().open();
			Execution.delayUntil(() -> InterfaceWindows.getInventory().isOpen(), 500);
		}
	}
}
