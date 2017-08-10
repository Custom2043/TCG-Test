package animation;

import util.CardPosition;

public class BasicAnimation extends Animation
{
	public BasicAnimation(CardPosition cp, int t) 
	{
		super(new CardPosition[]{cp}, new int[]{t});
	}
	protected void endMoveEffect() {}
}
