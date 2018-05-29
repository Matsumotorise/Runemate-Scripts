package Bots.WC.AIO;

import com.runemate.game.api.script.framework.LoopingBot;

public class Main extends LoopingBot {

	@Override
	public void onStart(String... args){
		super.onStart();
		System.out.println("Starting");
	}

	@Override
	public void onStop(){
		super.onStop();
	}

	private enum State{
		CHOP, WALK_TO_CHOP, BANK, WALK_TO_BANK, DROP
	}

	private State getCurrentState(){
		return null;
	}


	private void chop(){

	}

	private void walkToChop(){

	}

	private void bank(){

	}

	private void walkToBank(){

	}

	private void drop(){

	}

	@Override
	public void onLoop() {
		setLoopDelay(250, 401);
		switch (getCurrentState()){
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

}
