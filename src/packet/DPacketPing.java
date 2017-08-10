package packet;

import java.io.IOException;

import main.Client;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;

public class DPacketPing extends Packet implements Packet.PacketDoubleSide
{
	public DPacketPing(){}
	public void read(CustomInputStream cis) throws IOException {}
	public void write(CustomOutputStream cos) throws IOException {}
	public void handleClient() 
	{
		Client.compte.ping = (int)Client.compte.timer.getDifference()/2;
	}
	@Override
	public void handleServer(ServerAccount compte)
	{
		new DPacketPing().send(compte.socket);
		compte.timer.set0();
	}
	public boolean toWrite()
	{
		return false;
	}
}
