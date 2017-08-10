package gui;

import main.Client;

import org.newdawn.slick.Color;

import util.TimeSection;
import drawer.CustomDrawer;

public abstract class Gui extends CustomGui
{
	public static boolean debugMode = false;
	public abstract void quit();
	@Override
	public void draw()
	{
		this.drawBeforeButtons();
		this.drawButtons();
		this.drawAfterButtons();
		if (debugMode)
			this.drawDebugMode();
	}
	protected abstract void drawBeforeButtons();
	protected void drawAfterButtons(){}
	public void drawDebugMode()
	{
		CustomDrawer.drawString(5, 5, false, false, "FPS : "+Client.prevFps, Drawer.font, Color.gray);
		for (int i=0;i<TimeSection.SECTION_NUMBER;i++)
			CustomDrawer.drawString(5, 50 + 25*i, false, false, TimeSection.sectionNames[i]+" : "+TimeSection.last[i],Drawer.font, TimeSection.getColor(i));
	}
}
