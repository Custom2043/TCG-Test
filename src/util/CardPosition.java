package util;

public class CardPosition 
{
	public final float z, y, gui;
	public final ScreenCoor coor;
	public CardPosition(ScreenCoor c, float zz, float yy, float g)
	{
		coor = c;
		z = zz;
		y = yy;
		gui = g;
	}
}
