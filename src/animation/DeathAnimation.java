package animation;

import gui.GuiIngame;
import main.Client;
import util.CardPosition;
import carte.Carte;
import carte.CimetiereWrapper;
import carte.ClientSlotWrapper;

public class DeathAnimation extends SlotAnimation<CimetiereWrapper>
{
	public Carte c;
	public DeathAnimation(ClientSlotWrapper f)
	{
		super(Client.game.getCim(f.slot.possesseur.bas).prop, new CardPosition[]{
			new CardPosition(GuiIngame.getCimSlotCoor(f.slot.possesseur.bas), 0, 0, 1.3f),
			new CardPosition(GuiIngame.getCimSlotCoor(f.slot.possesseur.bas), 0, 0, 1f),
		},
		new int[]{700, 150});
		f.slot.carte = null;
	}
	public void endMoveEffect() 
	{
		
	}
}
