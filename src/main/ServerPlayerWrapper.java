package main;

import java.util.ArrayList;
import java.util.List;

import packet.CPacketPioche;
import packet.CPacketWin;
import carte.Carte;
import carte.CarteList;

public class ServerPlayerWrapper extends PlayerWrapper
{
	public ArrayList<Carte> main = new ArrayList<Carte>();
	
	public Deck deck = new Deck();
	
	public final ServerAccount compte;
	
	public ServerPlayerWrapper(Player p, ServerAccount c) {
		super(p);
		compte = c;
		for (CarteList cl : compte.decks.get(compte.currentDeck).cards)
			deck.cards.add(cl);
		
		if (deck.cards.size() == 0) // TODO ?
		while (deck.cards.size() < 30)
		{
			int i = (int)(Math.random() * 151) - 1;
			CarteList cl = CarteList.getCarteForID(i);
			if (cl != null)
				if (deck.numberOf(cl) < 2)
					deck.cards.add(cl);
		}
		
		for (int i=0;i<5;i++)
			p.pioche();
	}
	
	public void removeCarte(int position)
	{
		main.remove(position);
	}
	
	public List getMain() {
		return main;
	}

	@Override
	public void addCarte(Carte c, int position) {
		main.add(position, c);
		
	}
	public void piocheEffect()
	{
		if (deck.cards.size() > 0 && size() < 8)
		{
			int i = (int)(Math.random() * deck.cards.size());
			Carte c = deck.cards.remove(i).instance(player);
			player.addCarte(c);
			new CPacketPioche(c, true, false).send(compte.foe.socket);
			new CPacketPioche(c, false, false).send(compte.socket);
		}
	}

	@Override
	public int size() 
	{
		return main == null ? 0 : main.size();
	}

	@Override
	public boolean isPlaying() 
	{
		return compte.getGame().getPlayingPlayer() == this.player;
	}

	@Override
	public void die() 
	{
		new CPacketWin(false).send(compte.socket);
		new CPacketWin(true).send(compte.foe.socket);
		compte.setNogame();
		compte.foe.setNogame();
	}

	@Override
	public Carte getCarte(int i) {
		return main.get(i);
	}

	@Override
	public Game game() {
		return compte.getGame();
	}
}
