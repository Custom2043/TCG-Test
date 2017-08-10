package animation;

import util.CardPosition;
import carte.ClientSlotWrapper;

public class AttackAnimation extends SlotAnimation
{
	private final ClientSlotWrapper attacked;
	private final RipostAnimation ripost;
	public AttackAnimation(ClientSlotWrapper f, ClientSlotWrapper a) 
	{
		super(f, new CardPosition[]{
			/*new CardPosition(GuiIngame.retourneIf(ScreenCoor.screen(a.coor.xScreen, 0.466666666f, 0.084375f, 0.2111111f)), 0, 0, 1),	
			new CardPosition(f.movement.getCardPosition().coor, 0, 0, 1),*/
			new CardPosition(f.coor, 0, 0, 1),
		},
		new int[]{/*200, 250,*/ 00});
		attacked = a;
		ripost = new RipostAnimation(attacked);
		attacked.movement.addAnim(ripost);
	}
	public void endMoveEffect() 
	{
		if (getId() == 0)
			slot.slot.getCarte().use(attacked.slot);
	}
}
