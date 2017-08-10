package animation;

import main.Player;
import util.CardPosition;
import util.ScreenCoor;

public class PiocheAnimation extends Animation
{
	public PiocheAnimation(Player p, ScreenCoor fin, float z) 
	{
		super(new CardPosition[]{
			/*new CardPosition(GuiIngame.retourneIf(p.bas, ScreenCoor.screenGui(.77f,.65f,0,0,0,0,744/6f, 1034/6f)), 0, (float)Math.PI/5, 1.3f),
			new CardPosition(GuiIngame.retourneIf(p.bas, ScreenCoor.screenGui(.7f,.65f,0,0,0,0,744/6f, 1034/6f)), z/5f, 0, 1.3f),*/
			new CardPosition(fin, z, 0, 1),
		}, new int[]{400});
	}
	protected void endMoveEffect() {}

}
