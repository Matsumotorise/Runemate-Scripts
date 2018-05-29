package Template.oop;

import com.runemate.game.api.script.framework.task.TaskBot;
import Template.oop.Tasks.Chop;
import Template.oop.Tasks.Drop;

public class Main extends TaskBot {

	@Override
	public void onStart(String... args) {
		setLoopDelay(200, 450);
		add(new Chop(), new Drop());
	}

}
