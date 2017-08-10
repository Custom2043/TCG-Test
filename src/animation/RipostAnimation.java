package animation;

import util.CardPosition;
import carte.ClientSlotWrapper;

public class RipostAnimation extends SlotAnimation
{
	public RipostAnimation(ClientSlotWrapper f) 
	{
		super(f, new CardPosition[]{
				/*new CardPosition(f.movement.getCardPosition().coor, 0, 0, 2),
				new CardPosition(f.movement.getCardPosition().coor, 0, 0, .5f),
				new CardPosition(f.movement.getCardPosition().coor, 0, 0, 1f),*/
		}, new int[]{/*250, 250, 250*/});
	}

	@Override
	protected void endMoveEffect() 
	{

	}
	public void pousse()
	{
	}
}
