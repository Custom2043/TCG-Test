package animation;

import util.CardPosition;
import carte.ClientSlotWrapper;

public abstract class SlotAnimation<T extends ClientSlotWrapper> extends Animation
{
	protected final T slot;
	public SlotAnimation(T f, CardPosition[] p, int[] t)
	{
		super(p, t);
		slot = f;
	}
}
