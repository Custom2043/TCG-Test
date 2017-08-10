package packet;

import java.io.IOException;
import java.util.ArrayList;

import carte.CarteList;
import main.Client;
import main.Deck;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;

public class DPacketDecks extends Packet implements Packet.PacketDoubleSide
{
	public ArrayList<Deck> decks = new ArrayList<Deck>();
	int current;
	public DPacketDecks(){}
	public DPacketDecks(ArrayList<Deck> d, int c){decks = d;current = c;}

	@Override
	public void handleServer(ServerAccount s) 
	{
		s.decks = decks;
		s.currentDeck = current;
	}

	@Override
	public void read(CustomInputStream cis) throws IOException 
	{
		int size = cis.readInt();
		for (int i=0;i<size;i++)
		{
			Deck d = new Deck();
			int s = cis.readInt();
			for (int j=0;j<s;j++)
				d.cards.add(CarteList.getCarteForID(cis.readInt()));
			decks.add(d);
		}
		current = cis.readInt();
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException 
	{
		cos.writeInt(decks.size());
		for (Deck dd : decks)
		{
			cos.writeInt(dd.cards.size());
			for (CarteList cl : dd.cards)
				if (cl != null)
					cos.writeInt(cl.id);
		}
		cos.writeInt(current);
	}
	@Override
	public void handleClient() 
	{
		Client.compte.decks = decks;
		Client.compte.currentDeck = current;
	}

}
