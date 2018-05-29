package Bots.Template.Normal;

import com.runemate.game.api.script.framework.LoopingBot;

public class Main extends LoopingBot {

	private enum State{

	}

	private State getCurrentState(){
		return null;
	}

	@Override
	public void onLoop() {
		setLoopDelay(250, 401);


		switch (getCurrentState()){

		}

	}

	@Override
	public void onStart(String... args){
		super.onStart();
		System.out.println("Starting");
	}

	@Override
	public void onStop(){
		super.onStop();
	}
}
