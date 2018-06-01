package Bots.Helper;

import com.runemate.game.api.hybrid.location.Coordinate;

public enum Location {
	LUMBRIDGE_BANK(3209, 3219, 2), DRAYNOR_BANK(3094, 3516, 0);

	private Coordinate coordinate;

	Location(int x, int y, int plane) {
		coordinate = new Coordinate(x, y, plane);
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}
}
