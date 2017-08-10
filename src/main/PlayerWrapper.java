package main;

import carte.Carte;

public abstract class PlayerWrapper 
{
	public final Player player;
	public PlayerWrapper(Player p) 
	{
		player = p;
		p.props = this;
	}
	public abstract void piocheEffect();
	public abstract void addCarte(Carte c, int position);
	public abstract int size();
	public abstract void removeCarte(int position);
	public abstract boolean isPlaying();
	public abstract void die();
	public abstract Carte getCarte(int i);
	public abstract Game game();
}
