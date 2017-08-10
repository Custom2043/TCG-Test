package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import packet.CPacketPlayerQuit;
import packet.CPacketServerClosed;
import packet.CPacketWin;
import packet.Packet;

public class BattleTubeServer 
{
	public PlayerAccepter acc;
	public List<ServerAccount> connected = Collections.synchronizedList(new ArrayList<ServerAccount>());
	public List<ServerGame> parties = new ArrayList<ServerGame>();
	
	public BattleTubeServer() throws IOException
	{
		acc = new PlayerAccepter(this);
	}
	public void sendPacketToAllPlayers(Packet p)
	{
		for (Account h : connected)
			p.send(h.socket);
	}
	public void sendPacketToAllPlayersExcept(Packet p, String name)
	{
		for (Account h : this.connected)
			if (!h.name.equals(name))
				p.send(h.socket);
	}
	public void closeServer()
	{
		acc.quit();
		this.sendPacketToAllPlayers(new CPacketServerClosed());
		for (Iterator<ServerAccount> iter = this.connected.iterator();iter.hasNext();)
		{
			ServerAccount sa = iter.next();
			iter.remove();
			removePlayer(sa);
		}
	}
	public void removePlayer(ServerAccount pl)
	{
		pl.save();
		System.out.println(pl.name +" a quitté la partie");
		try {pl.socket.close();}
		catch (IOException e) {e.printStackTrace();}
		Main.server.sendPacketToAllPlayers(new CPacketPlayerQuit(pl.name));
		if (pl.ingame)
		{
			new CPacketWin(true).send(pl.foe.socket);
			pl.foe.setNogame();
		}
	}
	public void removePlayer(String name)
	{
		for (Iterator<ServerAccount> iter = this.connected.iterator();iter.hasNext();)
		{
			ServerAccount pl = iter.next();
			if (pl.name.equals(name))
			{
				iter.remove();
				removePlayer(pl);
			}
		}
	}
	public ServerAccount getAccount(String nom)
	{
		for (ServerAccount h : this.connected)
			if (h.name.equals(nom))
				return h;
		return null;
	}
	public void tick(long dif)
	{
		for (int i=0;i<this.connected.size();i++)
		{
			ServerAccount a = this.connected.get(i);
			if (a.timer.getDifference() > Main.TIMEOUT)
				{removePlayer(a.name);System.out.println(a.name + " A QUITTE LA PARTIE TABERNAK");}
			else 
			{
				
				Packet.read(a);
				Packet p;
				while ((p = Packet.readPacket(a.stream)) != null)
				{
					try
					{
						if (p instanceof Packet.PacketToServer)
						{
							if (p.toWrite())
								System.out.println("Server receive packet : "+p.getClass().getName());
							((Packet.PacketToServer)p).handleServer(a);
						}
					}
					catch(Exception e){e.printStackTrace();}
				}
			}
		}
	}
}
