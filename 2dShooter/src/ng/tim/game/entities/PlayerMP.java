package ng.tim.game.entities;

import java.net.InetAddress;

import ng.tim.game.level.Level;

public class PlayerMP extends Player
{

	public InetAddress ipAddress;
	public int port;
	
	//for players connecting localy local connection
	public PlayerMP(Level level, int x, int y, String username, InetAddress ipAddress, int port)
	{
		super(level, x, y, username);
		this.ipAddress = ipAddress;
		this.port = port;
	}

	@Override
	public void tick()
	{
		super.tick();
	}
}
