package Bots.Helper;

import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;

public enum Location {
	LUMBRIDGE_BANK(3209, 3219, 3209, 3219, 2), DRAYNOR_BANK(3092, 3242, 3092, 3245, 0);

	private Area area;

	Location(int x1, int y1, int x2, int y2, int plane) {
		area = new Area.Rectangular(new Coordinate(x1, y1, plane), new Coordinate(x2, y2, plane));
	}

	public Area getArea() {
		return area;
	}
}
