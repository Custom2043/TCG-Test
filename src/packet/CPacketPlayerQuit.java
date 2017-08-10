package packet;

import java.io.IOException;
import java.util.Iterator;

import main.Client;
import main.OtherAccount;
import util.CustomInputStream;
import util.CustomOutputStream;

public class CPacketPlayerQuit extends Packet implements Packet.PacketToClient
{
	public String quitter;
	public CPacketPlayerQuit(){}
	public CPacketPlayerQuit(String s){quitter = s;}
	@Override
	public void read(CustomInputStream cis) throws IOException {
		quitter = cis.readString();
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException {
		cos.writeString(quitter);
	}

	@Override
	public void handleClient() {
			for (Iterator<OtherAccount> iter = Client.connected.iterator() ; iter.hasNext();)
				if (iter.next().name.equals(quitter))
					iter.remove();
	}

}
