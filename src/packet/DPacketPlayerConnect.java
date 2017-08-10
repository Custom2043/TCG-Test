package packet;

import java.io.IOException;

import main.Client;
import main.OtherAccount;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;

public class DPacketPlayerConnect extends Packet implements Packet.PacketDoubleSide
{
	public OtherAccount acc;
	
	public DPacketPlayerConnect(){}
	public DPacketPlayerConnect(OtherAccount a){acc = a;}
	
	@Override
	public void read(CustomInputStream cis) throws IOException 
	{
		acc = new OtherAccount(cis.readString());
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException 
	{
		cos.writeString(acc.name);
	}

	@Override
	public void handleClient() {
		System.out.println(acc.name+" se connecte");
		Client.connected.add(acc);
	}
	@Override
	public void handleServer(ServerAccount compte) {}

}
