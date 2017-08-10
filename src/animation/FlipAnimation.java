package animation;

import util.CardPosition;
import carte.ClientObjectWrapper;
import carte.ClientSlotWrapper;
import carte.ObjectSlot;
import carte.Piege;

public class FlipAnimation extends SlotAnimation
{
	public static final int TRIGGER = 1;
	final int suite;
	public FlipAnimation(ClientSlotWrapper cw){this(cw,0);}
	public FlipAnimation(ClientSlotWrapper cw, int s) 
	{
		super(cw, new CardPosition[]{
				new CardPosition(cw.movement.getLastCardPosition().coor, cw.movement.getLastCardPosition().z, (float)Math.PI, cw.movement.getLastCardPosition().gui), 
				new CardPosition(cw.movement.getLastCardPosition().coor, cw.movement.getLastCardPosition().z, 0, cw.movement.getLastCardPosition().gui), 
				new CardPosition(cw.movement.getLastCardPosition().coor, cw.movement.getLastCardPosition().z, 0, cw.movement.getLastCardPosition().gui)
				},new int[]{0, 800, 100});
		
		if (cw instanceof ClientObjectWrapper && ((ClientObjectWrapper)slot).slot.faceCache)
			((ObjectSlot)slot.slot).faceCache = false;
		
		suite = s;
	}

	@Override
	protected void endMoveEffect() 
	{
		if (getId() == 2)
			if (suite == TRIGGER)
				slot.movement.addAnim(new TriggerAnimation((ClientObjectWrapper)slot, (Piege)slot.slot.getCarte()));
	}
}
