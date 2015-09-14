package MainPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class client {
	String serverIP = "127.0.0.1";
	int port = 1234;
	static Socket socket;
	public int UserID = 0;
	public String clientName;
	OutputStream os;
	InputStream is;
	DataInputStream in;
	DataOutputStream out;
 	
	client()
	{
	}
	public void setUserName(String name)
	{
		this.clientName = name;
	}
	private void connectToServer() throws ConnectException
	{
		try {
			socket = new Socket(serverIP, port);
			
			os = socket.getOutputStream();
			is = socket.getInputStream();
			
			out = new DataOutputStream(os);
			in = new DataInputStream(is);
			
		} catch (UnknownHostException e) {
			//e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String CheckUser(String login, String password) throws IOException
	{
		connectToServer();
		out.writeInt(1);
		out.writeUTF(login);
		out.writeUTF(password);
		int n = in.readInt();
		disconnectFromServer();
		switch(n)
		{
		case 1:
			return "Successfully entered.";
		case 2:
			return "You are registred as " + login + ". Use this account for next enter.";
		case 3:
			return "Incorrect password.";
		}
		return null;
	}
	public void getUserID(String login) throws IOException
	{
		connectToServer();
		out.writeInt(7);
		out.writeUTF(login);
		
		UserID = in.readInt();
		disconnectFromServer();
	}
	public void addFilms(String cinema, String cinemaName, String[] filmNames, String[] films, 
							int filmCount, String[][] actors, int[] actorCount) throws IOException, InterruptedException
	{
		connectToServer();
		out.writeInt(2);
		out.writeUTF(cinemaName);
		int n = in.readInt();
		if (n == 1)
		{
			out.writeUTF(cinema);
		}
		out.writeInt(filmCount);
		for (int i = 0; i < filmCount; ++i)
		{
			out.writeUTF(filmNames[i]);
			n = in.readInt();
			if (n == 1)
			{
				out.writeUTF(films[i]);
			}
			out.writeInt(actorCount[i]);
			for (int j = 0; j < actorCount[i]; ++j)
			{

				out.writeUTF(actors[i][j]);
			}
			//JOptionPane.showMessageDialog(new JFrame(), "Film ¹ " + (i+1) + " imported");
		}
		disconnectFromServer();
	}
	public ArrayList<String[]> countCinema() throws IOException
	{
		connectToServer();
		out.writeInt(3);
		ArrayList<String[]> cinemas = new ArrayList<String[]>();
		
		int n = in.readInt();
		System.out.println(n);
		for (int i = 0; i < n; ++i)
		{
			String[] cinema = new String[3];
			cinema[0] = in.readUTF();
			cinema[1] = in.readUTF();
			cinema[2] = in.readUTF();
			
			cinemas.add(cinema);
		}
		disconnectFromServer();
		return cinemas;
	}
	public ArrayList<String[]> getFilms(String name) throws IOException
	{
		connectToServer();
		out.writeInt(4);
		ArrayList<String[]> cinemas = new ArrayList<String[]>();
		out.writeUTF(name);
		int n = in.readInt();
		//JOptionPane.showConfirmDialog(new JFrame(), n);
		for (int j = 0; j < n; ++j)
		{
			String[] film = new String[5];
			for (int i = 0; i < 5; ++i)
			{
				film[i] = in.readUTF();
				System.out.println(film[i]);
			}
			System.out.println();
			cinemas.add(film);
		}
		disconnectFromServer();
		return cinemas;
	}
	public ArrayList<String> getActorsFilm(String name) throws IOException
	{
		connectToServer();
		out.writeInt(5);
		ArrayList<String> actors = new ArrayList<String>();
		out.writeUTF(name);
		int n = in.readInt();
		System.out.println(name);
		for (int j = 0; j < n; ++j)
		{
			String actor;
			actor = in.readUTF();
			System.out.println(actor);
			actors.add(actor);
		}
		disconnectFromServer();
		return actors;
		
	}
	public boolean isVisible(String comment, String autorID) throws IOException
	{
		connectToServer();
		out.writeInt(10);
		out.writeInt(UserID);
		out.writeUTF(comment);
		out.writeUTF(autorID);
		String isVis = in.readUTF();
		disconnectFromServer();
		if (isVis.equals("NO"))
		{
			return true;
		}
		return false;
	}
	public ArrayList<String[]> getComments(String filmName) throws IOException
	{
		connectToServer();
		out.writeInt(6);
		ArrayList<String[]> comments = new ArrayList<String[]>();
		out.writeUTF(filmName);
		int n = in.readInt();
		for (int i = 0; i < n; ++i)
		{
			String[] comment = new String[7];
			for (int j = 0; j < 6; ++j)
			{
				comment[j] = in.readUTF();
			}
			comments.add(comment);
		}
		out.writeInt(9);
		out.writeInt(n);
		for (int i = 0; i < n; ++i)
		{
			out.writeUTF(comments.get(i)[0]);
			comments.get(i)[6] = in.readUTF();
		}
		disconnectFromServer();
		return comments;
	}
	public void addComment(String filmName, String comment, String rating) throws IOException
	{
		connectToServer();
		JOptionPane.showMessageDialog(new JFrame(), rating);
		out.writeInt(8);
		out.writeInt(UserID);
		out.writeUTF(filmName);
		out.writeUTF(comment);
		out.writeUTF(rating);
		disconnectFromServer();
	}
	
	public void disconnectFromServer() throws IOException
	{
		out.writeInt(13);
	}
	public void addCommentIntoDB(String commentID, int i) throws IOException {
		connectToServer();
		out.writeInt(11);
		out.writeInt(Integer.parseInt(commentID));
		out.writeInt(UserID);
		out.writeInt(i);
		disconnectFromServer();
	}
}
