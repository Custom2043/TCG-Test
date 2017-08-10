package packet;

import gui.GuiSalon;

import java.io.IOException;

import main.Client;
import main.Main;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;

public class DPacketDefi extends Packet implements Packet.PacketDoubleSide
{
	public String pseudo;
	
	public DPacketDefi(){}
	public DPacketDefi(String s){pseudo = s;}
	
	@Override
	public void handleClient()
	{
		if (Client.screen instanceof GuiSalon)
			((GuiSalon)Client.screen).inviteDuel(pseudo);
	}

	@Override
	public void handleServer(ServerAccount compte) 
	{
		ServerAccount s = Main.server.getAccount(pseudo);
		if (s != null)//Toujours co
		{
			if (!s.ingame)
			{
				compte.foe = s;
				new DPacketDefi(compte.name).send(s.socket);
			}
		}
		else
			new DPacketCancel().send(compte.socket);
	}

	@Override
	public void read(CustomInputStream cis) throws IOException {
		pseudo = cis.readString();
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException {
		cos.writeString(pseudo);
	}

}
