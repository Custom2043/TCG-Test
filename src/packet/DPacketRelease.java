package packet;

import gui.GuiIngame;

import java.io.IOException;

import main.Client;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;

public class DPacketRelease extends Packet implements Packet.PacketDoubleSide
{

	@Override
	public void handleClient() 
	{
		if (Client.screen instanceof GuiIngame)
			((GuiIngame)Client.screen).releasePicked();
	}

	@Override
	public void handleServer(ServerAccount compte) 
	{
		if (compte.getGame().getPlayingPlayerWrapper().compte == compte)
		{
			new DPacketRelease().send(compte.foe.socket);
			if (compte.getGame().suspend != null)
			{
				compte.getGame().getPlayingPlayer().addCarte(compte.getGame().suspend);
				compte.getGame().suspend = null;
			}
		}
	}

	@Override
	public void read(CustomInputStream cis) throws IOException {
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException {
	}

}
