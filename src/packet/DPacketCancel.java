package packet;

import gui.GuiSalon;

import java.io.IOException;

import main.Client;
import main.ServerAccount;
import packet.Packet.PacketDoubleSide;
import util.CustomInputStream;
import util.CustomOutputStream;

public class DPacketCancel extends Packet implements PacketDoubleSide
{
	public DPacketCancel(){}
	
	@Override
	public void handleClient() 
	{
		if (Client.screen instanceof GuiSalon)
			((GuiSalon)Client.screen).cancel();
	}

	@Override
	public void handleServer(ServerAccount compte) 
	{
		if (compte.foe != null)
			new DPacketCancel().send(compte.foe.socket);
		compte.foe = null;
	}

	@Override
	public void read(CustomInputStream cis) throws IOException {}

	@Override
	public void write(CustomOutputStream cos) throws IOException {}
}
