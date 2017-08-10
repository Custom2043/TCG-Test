package packet;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import main.Account;
import main.ServerAccount;
import util.CustomInputStream;
import util.CustomOutputStream;
import util.CustomTimer;
import util.PacketStream;

public abstract class Packet
{
	public static int MAX_SIZE = 65536;//Non utilisé
	public static ArrayList<Packet> toSend = new ArrayList<Packet>();
	public abstract void read(CustomInputStream cis) throws IOException;
	public abstract void write(CustomOutputStream cos) throws IOException;
	public boolean toWrite(){return false;}
	public void send(Socket s)
	{
		try
		{
			if (this.toWrite())
				System.out.println("Envoyé : "+this.getClass().getName());
			CustomOutputStream cos = new CustomOutputStream(s.getOutputStream());
			cos.writeInt(PacketList.getIDForPacket(this.getClass()));
			this.write(cos);
			cos.flush();
		}
		catch (IOException e) {e.printStackTrace();}
	}
	public static DPacketPlayerConnect waitConnectPacket(Socket s, PacketStream ps)
	{
		CustomTimer timer = new CustomTimer();Packet p = null;
		while (p == null && timer.getDifference() < 5000)
		{
			read(s, ps);
			while ((p = readPacket(ps)) != null)
			{
				if (p instanceof DPacketPlayerConnect)
					return (DPacketPlayerConnect)p;
			}
			p = null;
		}
		return null;
	}
	public static void read(Account compte){read(compte.socket, compte.stream);}
	public static void read(Socket socket, PacketStream stream)//Read all datas in the socket
	{
		try
		{
			int i = socket.getInputStream().available();
			if (i > 0)
			{
				byte b[] = new byte[i];
				socket.getInputStream().read(b);
				stream.addPacket(b);
			}
		}
		catch (IOException e) {e.printStackTrace();}
	}
	public static Packet readPacket(PacketStream ps)//Extract a packet from the stream
	{
		try
		{
			CustomInputStream cis = new CustomInputStream(ps);
			while (ps.available() >= 4)
			{
				int i = cis.readInt();
				try 
				{
					Packet p = PacketList.getPacketForID(i).newInstance();
					try
					{
						p.read(cis);
					}
					catch(IOException e){ps.reset();return null;/*Packet read fail, no more bytes*/}
					ps.delete();
					return p;
				}
				catch (InstantiationException | IllegalAccessException e) {System.out.println("Constructor error for packet id "+i);}
				catch(NullPointerException e){System.out.println("Unknown id "+i);}
				ps.delete();
			}
		}catch(IOException e){System.out.println("De fuk ?");e.printStackTrace();}
		return null;
	}
	public static interface PacketToClient{public abstract void handleClient();}
	public static interface PacketToServer{public abstract void handleServer(ServerAccount s);}
	static interface PacketDoubleSide extends PacketToClient, PacketToServer{}
}
