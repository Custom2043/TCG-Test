package animation;

import main.Player;
import util.CardPosition;
import carte.ClientSlotWrapper;

public class AttackPlayerAnimation extends SlotAnimation
{
	private final Player player;
	public AttackPlayerAnimation(ClientSlotWrapper f, Player p) 
	{
		super(f, new CardPosition[]{
				//new CardPosition(GuiIngame.retourneIf(p.bas, ScreenCoor.screen(.95f, .8f, 0.084375f, 0.2111111f)), 0, 0, 2),
				//new CardPosition(GuiIngame.retourneIf(p.bas, ScreenCoor.screen(.95f, .8f, 0.084375f, 0.2111111f)), 0, 0, 1),
		}, new int[]{/*250, 200*/});
		player = p;
	}
	@Override
	protected void endMoveEffect() 
	{
		if (getId() == 0)
		{
			slot.slot.getCarte().use(player);
		}
	}
}
