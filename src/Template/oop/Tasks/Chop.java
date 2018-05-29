package Template.oop.Tasks;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Chop extends Task {

	private final String CHOP = "Chop down", TREE = "Willow";

	@Override
	public boolean validate() {
		if (Players.getLocal().isValid()) {
			return -1 == Players.getLocal().getAnimationId() && !Inventory.isFull() && !GameObjects.newQuery().names("Willow")
					.results().isEmpty();
		}
		return false;
	}

	@Override
	public void execute() {
		GameObject tree = GameObjects.newQuery().names(TREE).actions(CHOP).results().nearest();
		if (tree != null) {
			Camera.turnTo(tree);
			if (!tree.isVisible()) {
				Path p = BresenhamPath.buildTo(tree);
				if (p != null) {
					p.step();
				}
			}

		} else if (tree.interact(CHOP)) {
			Execution.delayUntil(() -> Players.getLocal().getAnimationId() != -1, 10000);
		}
	}
}
