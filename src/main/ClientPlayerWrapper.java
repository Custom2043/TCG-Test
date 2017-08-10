package main;

import gui.GuiEndGame;
import gui.GuiIngame;

import java.util.ArrayList;

import util.CardPosition;
import util.MovementCard;
import animation.EnnemiPiocheAnimation;
import animation.PiocheAnimation;
import carte.Carte;

public class ClientPlayerWrapper extends PlayerWrapper
{
	public ArrayList<MovementCard> main = new ArrayList<MovementCard>();
	
	public ClientPlayerWrapper(Player p) {
		super(p);
	}
	
	public void removeCarte(int position)
	{
		main.remove(position);
		for (int i=0;i<main.size();i++)
			main.get(i).toCancel(new CardPosition(GuiIngame.getMainCarteScreenCoor(player.bas, main.size(), i), GuiIngame.getZRotMain(player.bas, main.size(), i), getAngle(), 1),TIME);
	}
	public static int TIME = 400;
	public void addCarte(Carte c)
	{
		addCarte(c, main.size());
	}
	public void addCarte(Carte c, int position)
	{
		addCarte(new MovementCard(new CardPosition(GuiIngame.getDeckCoor(player.bas), 0, (float)Math.PI, 1), c));
	}
	public void addCarte(MovementCard c)
	{
		addCarte(c, main.size());
	}
	public float getAngle(){return player.bas ? 0 : (float)Math.PI;}
	public void addCarte(MovementCard c, int position)
	{
		if (player.bas)
			c.addAnim(new PiocheAnimation(this.player, GuiIngame.getMainCarteScreenCoor(player.bas, main.size()+1, position), GuiIngame.getZRotMain(player.bas, main.size()+1, position)));
		else
			c.addAnim(new EnnemiPiocheAnimation(this.player, GuiIngame.getMainCarteScreenCoor(player.bas, main.size()+1, position), GuiIngame.getZRotMain(player.bas, main.size()+1, position)));
		main.add(position, c);
		for (int i=0;i<main.size();i++)
			if (i != position)
				main.get(i).toCancel(new CardPosition(GuiIngame.getMainCarteScreenCoor(player.bas, main.size(), i), GuiIngame.getZRotMain(player.bas, main.size(), i), getAngle(),1),TIME);
	}

	public void piocheEffect() {}
	
	@Override
	public int size() 
	{
		return main == null ? 0 : main.size();
	}

	@Override
	public boolean isPlaying() 
	{
		return Client.game.getPlayingPlayer() == this.player;
	}

	@Override
	public void die() 
	{
		Client.setScreen(new GuiEndGame(!player.bas));
	}

	@Override
	public Carte getCarte(int i) {
		return main.get(i).carte;
	}

	@Override
	public Game game() 
	{
		return Client.game;
	}
}
