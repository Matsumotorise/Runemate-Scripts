package Bots.Helper;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Area.Circular;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.framework.LoopingBot;

public class Util {

	private LoopingBot bot;
	private PlayerAction pA;

	public Util(LoopingBot bot) {
		this.bot = bot;
		pA = new PlayerAction(this);
	}

	public GameObject getClosestGameObject(String name, String action) {
		return GameObjects.newQuery().names(name).actions(action).results().nearest();
	}

	public GameObject getClosestGameObject(String name) {
		return GameObjects.newQuery().names(name).results().nearest();
	}

	public Npc getClosestNPC(String name, String action) {
		return Npcs.newQuery().names(name).actions(action).results().nearest();
	}

	public Npc getClosestNPC(String name) {
		return Npcs.newQuery().names(name).results().nearest();
	}

	public Area.Circular generateAreaAroundEntity(LocatableEntity e, int radius) {
		return generateArea(e.getPosition(), radius);
	}

	public Area.Circular generateArea(Coordinate c, int radius) {
		return new Circular(c, radius);
	}

	public PlayerAction getplayerAction() {
		return pA;
	}
}
