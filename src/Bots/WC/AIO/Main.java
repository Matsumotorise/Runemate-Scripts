package Bots.WC.AIO;

import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;

public class Main extends LoopingBot {

	@Override
	public void onStart(String... args) {
		super.onStart();
		System.out.println("Starting");
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private State getCurrentState() {
		return null;
	}

	private void chop() {

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
		setLoopDelay(250, 401);
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
