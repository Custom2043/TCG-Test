package main;

import java.net.Socket;
import java.util.ArrayList;

import carte.CarteList;
import util.CustomInputStream;
import util.CustomTimer;
import util.PacketStream;

public abstract class Account extends OtherAccount
{
	public PacketStream stream = new PacketStream();
	
	public ArrayList<Deck> decks = new ArrayList<Deck>();
	public int currentDeck;
	
	public int ping;
	
	public CustomTimer timer;//Depuis le dernier ping envoyé
	public Socket socket;
	
	public Account(String n, CustomInputStream cis, Socket s) 
	{
		super(n, cis);
		socket = s;
		try{
			int size = cis.readInt();
			for (int i=0;i<size;i++)
			{
				Deck d = new Deck();
				int ss = cis.readInt();
				for (int j=0;j<ss;j++)
					d.cards.add(CarteList.getCarteForID(cis.readInt()));
				decks.add(d);
			}
			currentDeck = cis.readInt();
		}catch(Exception e){}
		if (decks.size() == 0)
			decks.add(new Deck());
		
	}
	public Account(String n, Socket s) 
	{
		super(n);
		socket = s;
		decks.add(new Deck());
	}
}
