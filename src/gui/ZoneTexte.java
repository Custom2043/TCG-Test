package gui;

import org.newdawn.slick.AngelCodeFont;

import util.ScreenCoor;

public class ZoneTexte extends BasicZoneTexte
{
	public ZoneTexte(String n, int i, ScreenCoor c, boolean isActiv) 
	{
		super(n, i, c, "", isActiv);
	}

	public AngelCodeFont getFont() {
		return Drawer.font;
	}
}
