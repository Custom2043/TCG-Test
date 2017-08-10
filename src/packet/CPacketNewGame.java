package packet;

import gui.GuiIngame;

import java.io.IOException;

import main.Client;
import main.ClientGame;
import util.CustomInputStream;
import util.CustomOutputStream;

public class CPacketNewGame extends Packet implements Packet.PacketToClient
{
	public String foe;public boolean bas;
	public CPacketNewGame(){}
	public CPacketNewGame(String s, boolean b){foe = s;bas = b;}
	@Override
	public void handleClient() 
	{
		Client.compte.against = foe;
		Client.game = new ClientGame(bas);
		Client.setScreen(new GuiIngame());
	}
	@Override
	public void read(CustomInputStream cis) throws IOException {
		foe = cis.readString();
		bas = cis.readBoolean();
	}
	@Override
	public void write(CustomOutputStream cos) throws IOException {
		cos.writeString(foe);
		cos.writeBoolean(bas);
	}

}
