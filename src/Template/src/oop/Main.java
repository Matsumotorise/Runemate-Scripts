package oop;

import com.runemate.game.api.script.framework.task.TaskBot;
import oop.Tasks.Chop;
import oop.Tasks.Drop;

public class Main extends TaskBot {

	@Override
	public void onStart(String... args) {
		setLoopDelay(200, 450);
		add(new Chop(), new Drop());
	}

}
