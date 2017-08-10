package packet;

import java.io.IOException;

import main.Client;
import util.CustomInputStream;
import util.CustomOutputStream;

public class CPacketServerClosed extends Packet implements Packet.PacketToClient
{
	public CPacketServerClosed(){}
	public void read(CustomInputStream cis) throws IOException {
		
	}
	public void write(CustomOutputStream cos) throws IOException {
		
	}

	public void handleClient() {
		Client.quitServer(false);
	}

}
