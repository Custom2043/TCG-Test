package animation;

import util.CardPosition;
import util.ScreenCoor;
import carte.ClientSlotWrapper;
import carte.Posable;

public class PoseAnimation extends SlotAnimation
{
	public final ScreenCoor base;
	public final Posable p;
	public PoseAnimation(ClientSlotWrapper f, ScreenCoor coor, Posable pp) {
		super(f, new CardPosition[]
			{
				new CardPosition(coor, 0, 0, 1.3f),
				new CardPosition(pp == null ? f.coor.addYGui(-100/12f) : f.coor.addYGui(-788/12f+1034/12f), 0, 0, 1.4f),
				new CardPosition(pp == null ? f.coor.addYGui(-100/12f) : f.coor.addYGui(-788/12f+1034/12f), 0, 0, 1.4f),
				new CardPosition(pp == null ? f.coor : f.coor.addYGui(-688/12f+1034/12f), 0, 0, 1f),
				new CardPosition(f.coor, 0, 0, 1f),
				new CardPosition(f.coor, 0, 0, 1f),
			}, new int[]{0, 800, 100, 250, 0, 300});
		base = coor;
		p = pp;
	}

	protected void endMoveEffect() 
	{
		if (p != null)
			if (getId() == 3)
				p.endPose();
	}
}
