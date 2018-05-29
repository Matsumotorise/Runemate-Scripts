package Bots.WC.AIO;

import com.runemate.game.api.script.framework.LoopingBot;

/**
 * Skeleton for a State-based Script
 * Created by SlashnHax
 */

public class Main extends LoopingBot {

	private enum State{
	}

	@Override
	public void onStart(String... args){
		System.out.println("Start");
	}

	@Override
	public void onLoop() {
	}

	@Override
	public void onStop(){
	}

	private State getCurrentState(){
		return null;
	}
}
