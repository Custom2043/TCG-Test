package animation;

import util.CardPosition;

public abstract class Animation 
{
	private int id = 0;
	private CardPosition[] pos; private int times[];
	public Animation(CardPosition[] p, int[] t)
	{
		pos = p; times = t;
	}
	protected abstract void endMoveEffect();
	public CardPosition getNextPos(){return pos[id];}
	public int getNextTime(){return times[id];}
	
	public final void endMove()
	{
		endMoveEffect();
		id ++;
	}
	public int getId(){return id;}
	public boolean isOver(){return id >= pos.length;}
}
