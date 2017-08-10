package main;

import java.io.IOException;

import org.lwjgl.LWJGLException;

public class Server extends Main
{
	public static void main(String[] args) throws LWJGLException, IOException
	{
		new Server().run();
	}
	
	public Server() throws IOException
	{
		super();
		Main.server = new BattleTubeServer();
	}
	@Override
	protected void tick(long dif) 
	{
		Main.server.tick(dif);
	}

	@Override
	protected void quit() 
	{
		Main.server.closeServer();
	}

	@Override
	protected boolean isClient() 
	{
		return false;
	}

}
