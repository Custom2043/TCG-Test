package main;

import java.util.ArrayList;

import carte.Carte;
import carte.MainEffet;

public class Player
{
	public PlayerWrapper props;
	public final boolean bas;
	
	public ArrayList<MainEffet> effets = new ArrayList<MainEffet>();
	
	private int vie = 30;
	private int mana = 1;
	
	public Player(boolean b)
	{		
		bas = b;
	}
	public int getVie()
	{
		return vie;
	}
	public void setMana(int m)
	{
		mana = m;
		if (mana < 0)
			mana = 0;
		if (mana > 10)
			mana = 10;
	}
	public void addMana(int m)
	{
		setMana(mana + m);
	}
	public int mana(){return mana;}
	public void damage(int damage)
	{
		for (MainEffet e : effets)
			damage = e.damage(damage);
		vie -= damage;
		if (vie <= 0)
			props.die();
	}
	public void heal(int heal)
	{
		vie += heal;
		if (vie > 30)
			vie = 30;
	}
	public void pioche()
	{
		props.piocheEffect();
	}
	public void addCarte(Carte c){props.addCarte(c, props.size());}
	public void setVie(int v){vie = v;}
}
