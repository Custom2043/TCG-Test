package util;

import carte.Carte;

public class MovementCard extends Movement
{
	public final Carte carte;
	
	public MovementCard(CardPosition cc, Carte c) 
	{
		super(cc);
		carte = c;
	}
}
