package packet;

import java.io.IOException;

import main.Main;
import main.ServerAccount;
import main.ServerGame;
import util.CustomInputStream;
import util.CustomOutputStream;

public class SPacketAnswer extends Packet implements Packet.PacketToServer
{
	public String pseudo;
	public boolean answer;
	public SPacketAnswer(){}
	public SPacketAnswer(String s, boolean a){answer = a;pseudo = s;}
	
	public void handleServer(ServerAccount compte) 
	{
		ServerAccount foe = Main.server.getAccount(pseudo);
		if (foe != null && answer && foe.foe == compte)
		{
			compte.foe = foe;
			boolean b = Math.random() < 0.5f;
			new CPacketNewGame(pseudo, b).send(compte.socket);
			new CPacketNewGame(compte.name, !b).send(foe.socket);
			if (b)
				compte.setIngame(new ServerGame(compte, foe));
			else
				compte.setIngame(new ServerGame(foe, compte));
			compte.foe.setIngame(compte.getGame());
			Main.server.parties.add(compte.getGame());
		}
		if (foe != null && !answer && foe.foe.name.equals(compte.name))
		{
			new DPacketCancel().send(Main.server.getAccount(pseudo).socket);//Annuler
		}
	}

	@Override
	public void read(CustomInputStream cis) throws IOException 
	{
		answer = cis.readBoolean();
		pseudo = cis.readString();
	}

	@Override
	public void write(CustomOutputStream cos) throws IOException 
	{
		cos.writeBoolean(answer);
		cos.writeString(pseudo);
	}

}
