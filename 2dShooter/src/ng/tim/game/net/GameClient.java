package ng.tim.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ng.tim.game.Game;
import ng.tim.game.entities.PlayerMP;
import ng.tim.game.net.packets.Packet;
import ng.tim.game.net.packets.Packet.PacketTypes;
import ng.tim.game.net.packets.Packet00Login;
import ng.tim.game.net.packets.Packet01Disconnect;
import ng.tim.game.net.packets.Packet02Move;

public class GameClient extends Thread
{
	private InetAddress ipAddress;//ip address of the server we are connecting to
	private DatagramSocket socket;//the socket
	private Game game;
	
	public GameClient(Game game, String ipAddress)
	{
		this.game = game;
		try
		{
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		while(true)
		{
			byte[] data = new byte[1024]; // the array of bytes of data that will be sent to and from the server
			DatagramPacket packet = new DatagramPacket(data, data.length); //the actual packet that is sent. Puts the data in the packet
			try
			{
				socket.receive(packet);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

			//String message = new String(packet.getData());
			//System.out.println("SERVER > " + message);
		}
	}
	
	//deals with all of the packets
	private void parsePacket(byte[] data, InetAddress address, int port)
	{
		String message = new String(data).trim();
		
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		
		switch(type)
		{
		default:
			case INVALID:
				break;
			case LOGIN:
				packet = new Packet00Login(data);
				handleLogin((Packet00Login)packet, address, port);
				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect)packet).getUsername() + " has left the world...");
				game.level.removePlayerMP(((Packet01Disconnect)packet).getUsername());
				break;
			case MOVE:
				packet = new Packet02Move(data);
		}
			
	}
	
	public void sendData(byte data[])
	{
		if(!game.isApplet)
		{
			DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
			try
			{
				socket.send(packet);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void handleLogin(Packet00Login packet, InetAddress address, int port)
	{
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has joined the game...");
		PlayerMP player = new PlayerMP(game.level, packet.getX(), packet.getY(), packet.getUsername(), address, port);
		game.level.addEntity(player);
	}
	
}
