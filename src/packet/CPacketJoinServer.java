package packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.Client;
import main.ClientAccount;
import main.OtherAccount;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;
import util.CustomTimer;

public class CPacketJoinServer extends Packet implements Packet.PacketToClient
{
	public ArrayList<OtherAccount> acc = new ArrayList<OtherAccount>();
	public CPacketJoinServer(){}
	public CPacketJoinServer(List<ServerAccount> a)
	{
		for (OtherAccount aa : a)
			acc.add(aa);
	}
	@Override
	public void read(CustomInputStream cis) throws IOException 
	{
		int j = cis.readInt();
		for (int i=0;i<j;i++)
			acc.add(new ClientAccount(cis.readString(), null));
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException 
	{
		cos.writeInt(acc.size());
		for (OtherAccount a : acc)
			cos.writeString(a.name);
	}

	@Override
	public void handleClient() 
	{
		Client.onServer = true;
		for (OtherAccount j : acc)
		{
			System.out.println(j.name+" est connecté");
			Client.connected.add(j);
		}
		Client.compte.timer = new CustomTimer();
	}

}
