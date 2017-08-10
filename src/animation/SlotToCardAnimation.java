package animation;

import main.Client;
import util.CardPosition;
import util.MovementCard;
import carte.CimetiereWrapper;
import carte.ClientObjectWrapper;
import carte.ClientSlotWrapper;
import carte.Piege;

public class SlotToCardAnimation extends SlotAnimation
{
	public static final int MORT = 1, TRIGGER = 2, FLIPTRIGGER = 3;
	private final int suite; boolean cache;
	public SlotToCardAnimation(ClientSlotWrapper f, boolean toCache){this(f, 0, toCache);}
	public SlotToCardAnimation(ClientSlotWrapper f, int s, boolean toCache)
	{
		super(f, new CardPosition[]{
			new CardPosition(toCache ? f.coor : f.coor.addYGui(-688/12f+1034/12f), 0, 0, 1f),
			new CardPosition(toCache ? f.coor.addYGui(-100/12f) : f.coor.addYGui(-788/12f+1034/12f), 0, 0, 1.3f),
			new CardPosition(toCache ? f.coor.addYGui(-100/12f) : f.coor.addYGui(-788/12f+1034/12f), 0, 0, 1.3f),
		},
		new int[]{0, 500, 300});
		suite = s;
		cache = toCache;
	}

	@Override
	protected void endMoveEffect()
	{
		if (getId() == 2)
		{
			if (suite == MORT)
			{				
				CimetiereWrapper cw = Client.game.getCim(slot.slot.possesseur.bas).prop;
				MovementCard mc = new MovementCard(slot.movement.getLastCardPosition(), slot.slot.getCarte());
				mc.addAnim(new DeathAnimation(slot));
				cw.cartes.add(mc);
			}
			else if (suite == TRIGGER)
				slot.movement.addAnim(new TriggerAnimation((ClientObjectWrapper)slot, (Piege)slot.slot.getCarte()));
			else if (suite == FLIPTRIGGER)
				slot.movement.addAnim(new FlipAnimation(slot, FlipAnimation.TRIGGER));
		}
	}
}
