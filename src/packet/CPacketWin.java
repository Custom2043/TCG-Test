package packet;

import gui.GuiEndGame;

import java.io.IOException;

import main.Client;
import util.CustomInputStream;
import util.CustomOutputStream;

public class CPacketWin extends Packet implements Packet.PacketToClient
{
	public boolean win;
	public CPacketWin(){}
	public CPacketWin(boolean b){win = b;}
	@Override
	public void read(CustomInputStream cis) throws IOException {
		win = cis.readBoolean();
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException {
		cos.writeBoolean(win);
	}
	@Override
	public void handleClient() 
	{
		Client.setScreen(new GuiEndGame(win));
	}

}
