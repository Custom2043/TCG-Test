package main;

import java.net.Socket;

public class ClientAccount extends Account
{
	public ClientAccount(String n, Socket s) {
		super(n, s);
	}
	public String against;
}
