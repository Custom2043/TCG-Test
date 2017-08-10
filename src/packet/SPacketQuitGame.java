package packet;

import java.io.IOException;

import main.Main;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;

public class SPacketQuitGame extends Packet implements Packet.PacketToServer
{
	public SPacketQuitGame(){}
	@Override
	public void read(CustomInputStream cis) throws IOException {
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException {
	}

	@Override
	public void handleServer(ServerAccount compte) {
		Main.server.removePlayer(compte.name);
	}

}
