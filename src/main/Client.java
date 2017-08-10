package main;

import gui.Drawer;
import gui.Gui;
import gui.GuiIngame;
import gui.GuiMainMenu;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;

import packet.DPacketPing;
import packet.DPacketPlayerConnect;
import packet.Packet;
import packet.SPacketQuitGame;
import sound.SoundManager;
import sounds.SoundSystem;
import util.CustomTimer;
import util.TimeSection;
import util.Translator;
import drawer.CustomDrawer;

public class Client extends Main
{
	public static ArrayList<OtherAccount> connected = new ArrayList<OtherAccount>();
	public static Gui screen;
	public static boolean localServer = false;
	public static ClientGame game;
	public static ClientAccount compte;
	public static boolean admin;
	
	public static int fps, prevFps=0;
	public static CustomTimer fpsCounter = new CustomTimer();
	
	public static boolean onServer = false;
	
	public static void main(String[] args) throws LWJGLException
	{
		new Client().run();
	}
	public boolean admin(){return false;}
	protected Client() throws LWJGLException 
	{
		admin = admin();
		
		Translator.loadLanguagesList("Ressources/lang/");
		
		Translator.loadLangage("Francais");
		
		Keyboard.enableRepeatEvents(true);
		
		SoundManager.init();
		
		if (admin)
			CustomDrawer.createDisplay(1280, 720, gameName);
		else
		{
			Display.setFullscreen(true);
			Display.create(new PixelFormat(0, 8, 0, 4));
		}
		
		screen = new GuiMainMenu();
		
		CustomDrawer.load2D();
		
		Drawer.init();
		
		SoundSystem.play(SoundManager.menu);
	}
	public void quit() 
	{
		if (onServer)
			quitServer(true);
		screen.quit();
		Drawer.quit();
		SoundManager.quit();
	}
	public static void quitServer(boolean send)
	{
		if (onServer && Client.localServer)
		{
			Main.server.closeServer();
			Main.server = null;
			Client.localServer = false;
			compte.socket = null;
		} 
		else
		{
			if (send)
				new SPacketQuitGame().send(compte.socket);
			try {compte.socket.close();}
			catch (IOException e) {e.printStackTrace();}
			compte.socket = null;
		}
		onServer = false;
		setScreen(new GuiMainMenu());
	}
	@Override
	public void tick(long dif) 
	{
		fps ++;
		if (fpsCounter.getDifference() > 1000)
		{
			fpsCounter.set0();
			prevFps = fps;
			fps = 0;
			TimeSection.setLast();
		}
		TimeSection.beginSection(TimeSection.PACKET);
		if (compte != null && compte.socket != null)
		{
			Packet.read(compte);
			Packet p;
			while ((p = Packet.readPacket(compte.stream)) != null)
			{
				try
				{
					if (p instanceof Packet.PacketToClient)
					{
						if (p.toWrite())
							System.out.println("Client receive packet : "+p.getClass().getName());
						((Packet.PacketToClient)p).handleClient();
					}
				}
				catch(Exception e){e.printStackTrace();}
			}
		}
		if (onServer)
		{
			if (compte.timer.getDifference() > TIMEOUT)//Server Timeout
				{quitServer(false);System.out.println("SERVEUR TIMEOUT");}
			else
			{
				if (compte.timer.getDifference() > PINGPERIOD)
				{
					new DPacketPing().send(compte.socket);
					compte.timer.set0();
				}
			}
		}
		
		if (Display.isCloseRequested())
			continu = false;
		
		TimeSection.beginSection(TimeSection.CLIENT_LOGIC);
		while (Mouse.next())
			screen.click();
		
		while (Keyboard.next())
			if (Client.admin && Keyboard.getEventKey() == Keyboard.KEY_F3 && Keyboard.getEventKeyState())
				Gui.debugMode = !Gui.debugMode;
			else
				screen.type();
		if (ingame())
			game.tick(dif);
		
		TimeSection.beginSection(TimeSection.RENDER);
		screen.draw();
		TimeSection.beginSection(TimeSection.UPDATE);
		Drawer.update();
		TimeSection.beginSection(TimeSection.CLIENT_LOGIC);
	}
	public static void setScreen(Gui s)
	{
		screen.quit();
		screen = s;
	}
	public static boolean ingame()
	{
		return screen instanceof GuiIngame;
	}
	protected boolean isClient(){return true;}
	//Local server means that this instance of the game is the server : game closed = server closed
	//A connection to localhost can be non local.
	public static void createLocalServer()
	{
		localServer = true;
		try
		{
			connected.clear();
			Main.server = new BattleTubeServer();
			compte.socket = new Socket("localhost", Main.PORT);
			compte.socket.setReceiveBufferSize(Packet.MAX_SIZE);
			compte.socket.setSendBufferSize(Packet.MAX_SIZE);
			new DPacketPlayerConnect(compte).send(compte.socket);
		}
		catch (IOException  e) {e.printStackTrace();System.out.println("BattleTube ; Can't create Server");return;}
	}
	public static void joinDistantServer(String address)
	{
		localServer = false;
		try
		{
			connected.clear();
			compte.socket = new Socket(InetAddress.getByName(address), Main.PORT);
			compte.socket.setReceiveBufferSize(Packet.MAX_SIZE);
			compte.socket.setSendBufferSize(Packet.MAX_SIZE);
			new DPacketPlayerConnect(compte).send(compte.socket);
		}
		catch (IOException  e) {e.printStackTrace();System.out.println("BattleTube ; Can't connect to Server");return;}
	}
}
class ClientAdmin extends Client
{
	protected ClientAdmin() throws LWJGLException {super();}

	public static void main(String[] args) throws LWJGLException
	{
		new ClientAdmin().run();
	}
	
	public boolean admin(){return true;}
}
