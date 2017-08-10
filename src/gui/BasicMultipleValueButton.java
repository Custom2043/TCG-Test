package gui;

import org.newdawn.slick.Color;

import drawer.CustomDrawer;
import util.ScreenCoor;
import util.Translator;
import value.MultipleStateValue;

public class BasicMultipleValueButton extends CustomMultipleStateBouton
{
	public BasicMultipleValueButton(String n, int i, ScreenCoor c,
			MultipleStateValue o) {
		super(n, i, c, o);
	}
	
	protected void keyTyped(char carac, int keyCode) {}

	protected void draw() 
	{
		CustomDrawer.drawRect(this.coor, Color.white);
		CustomDrawer.drawString(this.coor.getMiddleX(), this.coor.getMiddleY(), true, true, options.noms[options.value], BasicBouton.font, Color.black);
	}
	@Override
	public void clickOn() 
	{
		Translator.loadLangage(options.noms[options.value]);
	}

}
