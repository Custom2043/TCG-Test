package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.commons.io.IOUtils;

import packet.CPacketJoinServer;
import packet.CPacketServerClosed;
import packet.DPacketDecks;
import packet.DPacketPlayerConnect;
import packet.Packet;
import util.CustomInputStream;
import util.CustomTimer;
import util.PacketStream;

public class PlayerAccepter extends Thread
{
	public final BattleTubeServer server;
	public final ServerSocket ss;
	public PlayerAccepter(BattleTubeServer hs) throws IOException
	{
		this.server = hs;
		this.ss = new ServerSocket(Main.PORT);
		System.out.println(ss.getInetAddress());
		System.out.println(ss.getLocalPort());
		this.start();
	}
	public void run()
	{
		try
		{
			while (true)
				new ConnectWaiter(this.ss.accept(), this.server).start();
		}
		catch (IOException e)
		{
			System.out.println("PlayerAccepter ; Socket IO");
		}
	}
	public void quit()
	{
		try {this.ss.close();}
		catch (IOException e) {e.printStackTrace();}
	}
	public class ConnectWaiter extends Thread
	{
		public final BattleTubeServer server;
		public final Socket socket;
		public ConnectWaiter(Socket s, BattleTubeServer hs){this.socket = s;this.server = hs;}
		public void run()
		{
			try {
				this.socket.setReceiveBufferSize(Packet.MAX_SIZE);
				this.socket.setSendBufferSize(Packet.MAX_SIZE);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
			PacketStream ps = new PacketStream();
			DPacketPlayerConnect p = Packet.waitConnectPacket(this.socket, ps);
			if (p != null)
			{
				if (this.isPlayerAlreadyCo(p.acc.name))
				{
					System.out.println("PlayerAccepter : Le joueur "+p.acc.name+" est déjà  connecté");
					new CPacketServerClosed().send(this.socket);
				}
				else
				{
					CustomInputStream cis = null;
					ServerAccount h;
					try
					{
						cis = new CustomInputStream(new FileInputStream(new File("Saves/Players/"+p.acc.name+".bts")));
						System.out.println("Player Accepter : Player found in datas : "+p.acc.name);

						h = new ServerAccount(p.acc.name, cis, this.socket);
					}
					catch (Exception e) {System.out.println("PlayerAccepter ; No datas found about "+p.acc.name);
					h = new ServerAccount(p.acc.name, this.socket);}
					finally {if (cis != null)IOUtils.closeQuietly(cis);}
					
					new CPacketJoinServer(this.server.connected).send(socket);
					new DPacketDecks(h.decks, h.currentDeck).send(socket);
					h.stream = ps;
					h.timer = new CustomTimer();
					this.server.connected.add(h);
					
					System.out.println("PlayerAccepter : Connexion de "+h.name);
					this.server.sendPacketToAllPlayersExcept(new DPacketPlayerConnect(h), h.name);
				}
			}
			else
			{
				System.out.println("PlayerAccepter : Echec de la connexion");
				try {this.socket.close();}
				catch (IOException e) {e.printStackTrace();}
			}
		}
		public boolean isPlayerAlreadyCo(String nom)
		{
			for (OtherAccount h : this.server.connected)
				if (h.name.equals(nom))
					return true;
			return false;
		}
	}
}
