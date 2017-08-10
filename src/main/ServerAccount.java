package main;

import java.io.File;
import java.net.Socket;

import carte.CarteList;
import util.CustomInputStream;
import util.CustomOutputStream;

public class ServerAccount extends Account
{
	public ServerAccount(String n, Socket s) 
	{
		super(n, s);
	}
	public ServerAccount(String n, CustomInputStream cis, Socket s) 
	{
		super(n, cis, s);
	}

	private ServerGame game;
	public void setIngame(ServerGame sg){game = sg;ingame = true;}
	public void setNogame(){game = null;ingame = false;}
	public ServerGame getGame(){return game;}
	public ServerAccount foe;
	
	public void save()
	{
		try
		{
			if (!new File("Saves/Players/").exists())
				new File("Saves/Players/").mkdirs();
			
			CustomOutputStream cos = new CustomOutputStream(new File("Saves/Players/"+name+".bts"));
			cos.writeInt(decks.size());
			for (Deck dd : decks)
			{
				cos.writeInt(dd.cards.size());
				for (CarteList cl : dd.cards)
					cos.writeInt(cl.id);
			}
			cos.writeInt(currentDeck);
			cos.close();
		}catch(Exception e){e.printStackTrace();}
	}
}
