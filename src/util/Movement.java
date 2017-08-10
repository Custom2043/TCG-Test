package util;

import java.util.ArrayList;

import animation.Animation;
import animation.BasicAnimation;

public class Movement 
{
	private ArrayList<Animation> anims = new ArrayList<Animation>();
	private Animation current = null;
	
	private CardPosition from, to, last;
	
	private CustomTimer timer = new CustomTimer();
	private int temps; // En milli
	private float lastDif;
	
	public Movement(CardPosition c)
	{
		to = from = c;
		temps = 0;
	}
	
	private void to(CardPosition cc, CardPosition c, int m)
	{
		from = cc;
		to = c;
		temps = m;
		timer.set0();
	}
	
	public void to(CardPosition c, int m)
	{
		addAnim(new BasicAnimation(c, m));
	}
	
	public void toCancel(CardPosition c, int m)
	{
		anims.clear();
		current = null;
		to(getCardPosition(), c, m);
	}
	
	public boolean ended (){return temps == 0;}
	public void reset(){timer.set0();}
	
	public CardPosition getPositionTo(){return to;}
	
	public CardPosition getLastCardPosition(){return last;}
	public float getLastDif(){return lastDif;}
	
	public void update()
	{
		float p;
		if (anims.size() > 0 && current == null)
		{
			current = anims.remove(0);
			if (!current.isOver())
				to(from, current.getNextPos(), current.getNextTime());
		}
		do
		{
			int dif = (int)timer.getDifference();
			if (dif >= temps)
			{
				from = to;
				temps = 0;
				
				if (current != null)
				{
					current.endMove();
					if (current.isOver())
					{
						if (anims.size() > 0)
						{
							current = anims.remove(0);
							if (!current.isOver())
								to(from, current.getNextPos(), current.getNextTime());
						}
						else
							current = null;
					}
					else
						to(from, current.getNextPos(), current.getNextTime());
				}
				p = 1;
			}
			else
				p = dif / ((float)temps);
		}while (inAnim() && p == 1);
		lastDif = p;
		last = new CardPosition(
				ScreenCoor.screenGuiFlat(
					from.coor.xScreen+(to.coor.xScreen-from.coor.xScreen)*p, 
					from.coor.yScreen+(to.coor.yScreen-from.coor.yScreen)*p, 
					from.coor.wScreen+(to.coor.wScreen-from.coor.wScreen)*p, 
					from.coor.hScreen+(to.coor.hScreen-from.coor.hScreen)*p, 
					from.coor.xGui+(to.coor.xGui-from.coor.xGui)*p, 
					from.coor.yGui+(to.coor.yGui-from.coor.yGui)*p, 
					from.coor.wGui+(to.coor.wGui-from.coor.wGui)*p, 
					from.coor.hGui+(to.coor.hGui-from.coor.hGui)*p, 
					from.coor.xFlat+(to.coor.xFlat-from.coor.xFlat)*p, 
					from.coor.yFlat+(to.coor.yFlat-from.coor.yFlat)*p, 
					from.coor.wFlat+(to.coor.wFlat-from.coor.wFlat)*p, 
					from.coor.hFlat+(to.coor.hFlat-from.coor.hFlat)*p),
				from.z + (to.z - from.z)*p,
				from.y + (to.y - from.y)*p,
				from.gui + (to.gui - from.gui)*p);
	}
	
	public CardPosition getCardPosition()
	{
		update();
		return last;
	}
	
	public float getDif()
	{
		update();
		return lastDif;
	}
	
	public void addAnim(Animation a){anims.add(a);}
	public void endAnim(){current = null;}
	public boolean inAnim(){return current != null;}
	public Animation getAnim(){return current;}
}
