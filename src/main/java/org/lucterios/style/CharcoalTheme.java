package org.lucterios.style;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

/**
 * This class describes a theme using gray colors.
 * 
 * 1.7 01/23/03
 * 
 * @author Steve Wilson
 */
public class CharcoalTheme extends DefaultMetalTheme {

	public String getName() {
		return "Charcoal";
	}

	private final ColorUIResource primary1 = new ColorUIResource(66, 33, 66);

	private final ColorUIResource primary2 = new ColorUIResource(90, 86, 99);

	private final ColorUIResource primary3 = new ColorUIResource(99, 99, 99);

	private final ColorUIResource secondary1 = new ColorUIResource(0, 0, 0);

	private final ColorUIResource secondary2 = new ColorUIResource(90, 90, 90);

	private final ColorUIResource secondary3 = new ColorUIResource(175, 175,
			175);

	private final ColorUIResource black = new ColorUIResource(250, 250, 250);

	private final ColorUIResource white = new ColorUIResource(0, 0, 0);

	protected ColorUIResource getPrimary1() {
		return primary1;
	}

	protected ColorUIResource getPrimary2() {
		return primary2;
	}

	protected ColorUIResource getPrimary3() {
		return primary3;
	}

	protected ColorUIResource getSecondary1() {
		return secondary1;
	}

	protected ColorUIResource getSecondary2() {
		return secondary2;
	}

	protected ColorUIResource getSecondary3() {
		return secondary3;
	}

	protected ColorUIResource getBlack() {
		return black;
	}

	protected ColorUIResource getWhite() {
		return white;
	}

}
