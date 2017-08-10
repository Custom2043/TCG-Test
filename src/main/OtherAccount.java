package main;

import util.CustomInputStream;

public class OtherAccount 
{
	public boolean ingame = false;
	
	public String name;
	
	public OtherAccount(String n, CustomInputStream save)
	{
		name = n;
	}
	
	public OtherAccount(String n)
	{
		name = n;
	}
}
