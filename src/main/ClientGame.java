package main;

public class ClientGame extends Game
{
	public final ClientPlayerWrapper basProps = new ClientPlayerWrapper(bas), 
									 hautProps = new ClientPlayerWrapper(haut);
	private Boolean random = null;
	public ClientGame(boolean start) 
	{
		super(start);
	}
	public ClientPlayerWrapper getPlayingPlayerWrapper()
	{
		return basTurn ? basProps : hautProps;
	} 
	public ClientPlayerWrapper getNotPlayingPlayerWrapper()
	{
		return basTurn ? hautProps : basProps;
	}
	public boolean random(){boolean b = random;random = null;return b;}
	public void setNextRandom(boolean b){random = b;}
}
