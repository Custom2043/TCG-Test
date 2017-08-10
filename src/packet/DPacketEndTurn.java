package packet;

import gui.GuiIngame;

import java.io.IOException;

import main.Client;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;

public class DPacketEndTurn extends Packet implements Packet.PacketDoubleSide
{
	public DPacketEndTurn(){}
	@Override
	public void handleClient() 
	{
		((GuiIngame)Client.screen).picked = null;
		Client.game.passTurn();
		
	}

	@Override
	public void handleServer(ServerAccount compte) 
	{
		if (compte.getGame().getPlayingPlayerWrapper().compte == compte)
		{
			compte.getGame().passTurn();
			compte.getGame().getPlayingPlayer().pioche();
			new DPacketEndTurn().send(compte.socket);
			new DPacketEndTurn().send(compte.foe.socket);
			if (compte.getGame().suspend != null)
			{
				compte.getGame().getPlayingPlayer().addCarte(compte.getGame().suspend);
				compte.getGame().suspend = null;
			}
		}
	}

	@Override
	public void read(CustomInputStream cis) throws IOException {}

	@Override
	public void write(CustomOutputStream cos) throws IOException {}
}
