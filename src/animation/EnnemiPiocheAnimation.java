package animation;

import main.Player;
import util.CardPosition;
import util.ScreenCoor;

public class EnnemiPiocheAnimation extends Animation
{
	public EnnemiPiocheAnimation(Player p, ScreenCoor fin, float z) 
	{
		super(new CardPosition[]{
			new CardPosition(fin, z, (float)Math.PI, 1),
		}, new int[]{400});
	}
	protected void endMoveEffect() {}

}
