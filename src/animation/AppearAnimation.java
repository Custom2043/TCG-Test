package animation;

import util.CardPosition;
import carte.ClientSlotWrapper;

public class AppearAnimation extends SlotAnimation
{
	public AppearAnimation(ClientSlotWrapper f) 
	{
		super(f, new CardPosition[]{new CardPosition(f.movement.getCardPosition().coor, 0, 0, 1)}, new int[]{200});
	}

	@Override
	protected void endMoveEffect() 
	{
		
	}
}
