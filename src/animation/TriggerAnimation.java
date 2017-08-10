package animation;

import main.Client;
import util.CardPosition;
import util.MovementCard;
import carte.CimetiereWrapper;
import carte.ClientObjectWrapper;
import carte.Piege;

public class TriggerAnimation extends SlotAnimation<ClientObjectWrapper>
{
	public final Piege pp;
	public TriggerAnimation(ClientObjectWrapper cw, Piege p) 
	{
		super(cw, new CardPosition[]{cw.movement.getLastCardPosition(), cw.movement.getLastCardPosition()}
		,new int[]{400, 100});
		pp = p;
	}

	@Override
	protected void endMoveEffect() 
	{
		if (getId() == 1)
		{
			CimetiereWrapper cw = Client.game.getCim(slot.slot.possesseur.bas).prop;
			MovementCard mc = new MovementCard(slot.movement.getLastCardPosition(), slot.slot.getCarte());
			mc.addAnim(new DeathAnimation(slot));
			cw.cartes.add(mc);
		}
	}
}