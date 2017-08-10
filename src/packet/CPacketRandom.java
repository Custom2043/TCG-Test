package packet;

import java.io.IOException;

import main.Client;
import util.CustomInputStream;
import util.CustomOutputStream;

public class CPacketRandom extends Packet implements Packet.PacketToClient
{
	public boolean random;
	public CPacketRandom(){}
	public CPacketRandom(boolean b){random = b;}
	@Override
	public void handleClient() 
	{
		Client.game.setNextRandom(random);
	}

	@Override
	public void read(CustomInputStream cis) throws IOException 
	{
		random = cis.readBoolean();
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException 
	{
		cos.writeBoolean(random);
	}
}
