package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Server {
	ServerSocket ssocket;
	int port = 1234;
	
	Connection connect;
	Statement statement;
	ResultSet ResSet;
	
	InputStream is;
	OutputStream os;
	DataInputStream in;
	DataOutputStream out;
	
	void waitForConnections(Socket socket) throws IOException
	{
		socket = ssocket.accept();
		
		is = socket.getInputStream();
		os = socket.getOutputStream();
		
		in = new DataInputStream(is);
		out = new DataOutputStream(os);
	}
	Server() throws IOException, SQLException, InterruptedException
	{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/cinemaBD",
																"root", "qwerty");
			statement = (Statement) connect.createStatement();
		} catch (Exception e)
		{
			System.out.println(e);
		}
		ssocket = new ServerSocket(port);
		System.out.println("Server is running");
		Socket socket = ssocket.accept();
		
		is = socket.getInputStream();
		os = socket.getOutputStream();
		
		in = new DataInputStream(is);
		out = new DataOutputStream(os);
		Integer n ;
		String selectTable;
		int id = -1;
		while ( (n = in.readInt()) != null)
		{
			switch (n)
			{
			case 1:
				String login = in.readUTF();
				String password = in.readUTF();
				System.out.println(login + "    " + password);
				selectTable = "select password from user where user.login = \"" +login + "\"";
				System.out.println(selectTable);
				try {
					ResSet = statement.executeQuery(selectTable);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				boolean isFound = false;
				String passSQL = null;
				while (ResSet.next())
				{
					System.out.println("i'm here");
					isFound = true;
					passSQL = ResSet.getString("password");
				}
				if (!isFound)
				{
					selectTable =  "insert into user (login, password) values (\"" + login + "\", \"" + password + "\")";
					System.out.println(selectTable);
					statement.executeUpdate(selectTable);
					System.out.println(selectTable);
					out.writeInt(2);
				} else
				{
					if (passSQL.equals(password))
					{
						out.writeInt(1);
					}
					else
					{
						out.writeInt(3);
					}
				} 
				break;
			case 2:
				String cinemaName = in.readUTF();
				int cinemaID = -1;
				System.out.println(cinemaName);
				selectTable = "select id from cinema where cinema.name = \"" + cinemaName + "\"";
				System.out.println(selectTable);
				try {
					ResSet = statement.executeQuery(selectTable);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while(ResSet.next())
				{
					cinemaID = ResSet.getInt("id");
					System.out.println(cinemaID);
				}
				if (cinemaID == -1)
				{
					out.writeInt(1);
					String cinema = in.readUTF();
					selectTable = "insert into cinema (name, address) values " + cinema;
					
					System.out.println(selectTable);
					
					statement.executeUpdate(selectTable);
					
					selectTable = "select id from cinema where cinema.name = \"" + cinemaName + "\"";
					System.out.println(selectTable);
					try {
						ResSet = statement.executeQuery(selectTable);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while(ResSet.next())
					{
						cinemaID = ResSet.getInt("id");
					}
				} else out.writeInt(0);
				selectTable = "delete cinema_film from cinema_film where cinema_id =" + cinemaID;
				statement.executeUpdate(selectTable);
				int filmCount = in.readInt();
				System.out.println(filmCount);
				String filmName;
				for (int i = 0; i < filmCount; ++i)
				{
					filmName = in.readUTF();
					selectTable = "select id from film where film.name = \"" + filmName + "\"";
					try {
						ResSet = statement.executeQuery(selectTable);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					id = -1;
					while(ResSet.next())
					{
						id = ResSet.getInt("id");
						System.out.println(filmName + " " + id);
					}
					
					if (id == -1)
					{
						out.writeInt(1);
						String film = in.readUTF();
						
						selectTable = "insert into film (name,rating,description,genre,date) values " + film;
						System.out.println(selectTable);
						statement.executeUpdate(selectTable);
						
						selectTable = "select id from film where film.name = \"" + filmName + "\"";
						try {
							ResSet = statement.executeQuery(selectTable);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						while(ResSet.next())
						{
							
							id = ResSet.getInt("id");
						}
						selectTable = "insert into cinema_film(cinema_id, film_id) values (" + cinemaID + "," + id + ")";
						System.out.println(selectTable);
						statement.executeUpdate(selectTable);	
					}
					else
					{
						out.writeInt(2);
						int cinema_film_id = 0;
						selectTable = "select cinema_film_id from cinema_film where film_id = " + id + " and cinema_id = " + cinemaID;
						
						try {
							ResSet = statement.executeQuery(selectTable);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						while(ResSet.next())
						{
							cinema_film_id = ResSet.getInt("cinema_film_id");
						}
						if (cinema_film_id == 0)
						{
							selectTable = "insert into cinema_film(cinema_id, film_id) values (" + cinemaID + "," + id + ")";
							System.out.println(selectTable);
							statement.executeUpdate(selectTable);
							
						}
						
					}
					int actorCount = in.readInt();
					System.out.println(actorCount);
					
					for (int j = 0; j < actorCount; ++j)
					{
						int actorID = -1;
						String actorName = in.readUTF();
						
						selectTable = "select id from actor where actor.name = \"" + actorName + "\"";
						System.out.println(selectTable);
						try {
							ResSet = statement.executeQuery(selectTable);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						while(ResSet.next())
						{
							actorID = ResSet.getInt("id");
						}
						if (actorID == -1)
						{
							System.out.println("i'm here");
							selectTable = "insert into actor(name) values (\"" + actorName + "\")";
							System.out.println(selectTable);
							statement.executeUpdate(selectTable);
							
							selectTable = "select id from actor where actor.name = \"" + actorName + "\"";
							try {
								ResSet = statement.executeQuery(selectTable);
							} catch (SQLException e) {
								e.printStackTrace();
							}
							while(ResSet.next())
							{
								actorID = ResSet.getInt("id");
							}
							selectTable = "insert into film_actor(actor_id, film_id) values (" + actorID + "," + id + ")";
							System.out.println(selectTable);
							statement.executeUpdate(selectTable);
						} else 
						{
							int actor_film_id = 0;
							selectTable = "select film_actor_id from film_actor where film_id = " + id + " and actor_id = " + actorID;
							
							try {
								ResSet = statement.executeQuery(selectTable);
							} catch (SQLException e) {
								e.printStackTrace();
							}
							while(ResSet.next())
							{
								actor_film_id = ResSet.getInt("film_actor_id");
							}
							if (actor_film_id == 0)
							{
								selectTable = "insert into film_actor(actor_id, film_id) values (" + actorID + "," + id + ")";
								System.out.println(selectTable);
								statement.executeUpdate(selectTable);
								
							}
						}
					}
				}
				out.writeInt(0);
				break;
			case 3:
				String select = "select count(*) from cinema";
				ResSet = statement.executeQuery(select);
				while (ResSet.next())
					n = ResSet.getInt("count(*)");
				System.out.println(n);
				out.writeInt(n);
				select = "select * from cinema";
				ResSet = statement.executeQuery(select);
				while (ResSet.next())
				{
					String resId = ResSet.getString("id");
					String resName = ResSet.getString("name");
					String resAddress = ResSet.getString("address");
					
					out.writeUTF(resId);
					out.writeUTF(resName);
					out.writeUTF(resAddress);
				}
				break;
			case 4:
				String name = in.readUTF();
				cinemaID = 0;
				select = "select id from cinema where cinema.name = \"" + name + "\"";
				try
				{
					ResSet = statement.executeQuery(select);
				}catch(SQLException e)
				{
					e.printStackTrace();
				}
				while(ResSet.next())
				{
					cinemaID = ResSet.getInt("id");
				}
				select = "select count(*) from film where film.id in (Select film_id from cinema_film where cinema_id =" + cinemaID + ")";
				try
				{
					ResSet = statement.executeQuery(select);
				}catch(SQLException e)
				{
					e.printStackTrace();
				}
				int count = 0;
				while (ResSet.next())
				{
					count = ResSet.getInt("count(*)");
				}
				out.writeInt(count);
				
				select = "select * from film where film.id in (Select film_id from cinema_film where cinema_id =" + cinemaID + ")";
				try
				{
					ResSet = statement.executeQuery(select);
				}catch(SQLException e)
				{
					e.printStackTrace();
				}
				
				while (ResSet.next())
				{
					out.writeUTF(ResSet.getString("name"));
					
					
					out.writeUTF(ResSet.getString("rating"));
					out.writeUTF(ResSet.getString("description"));
					out.writeUTF(ResSet.getString("genre"));
					out.writeUTF(ResSet.getString("date"));
				}
				break;
			case 5:
				name = in.readUTF();
				int filmID = 0;
				select = "select id from film where film.name = \"" + name + "\"";
				try
				{
					ResSet = statement.executeQuery(select);
				}catch(SQLException e)
				{
					e.printStackTrace();
				}
				while(ResSet.next())
				{
					filmID = ResSet.getInt("id");
				}
				select = "select count(*) from actor where actor.id in (Select actor_id from film_actor where film_id =" + filmID + ")";
				try
				{
					ResSet = statement.executeQuery(select);
				}catch(SQLException e)
				{
					e.printStackTrace();
				}
				count = 0;
				while (ResSet.next())
				{
					count = ResSet.getInt("count(*)");
				}
				out.writeInt(count);
				
				select = "select * from actor where actor.id in (Select actor_id from film_actor where film_id =" + filmID + ")";
				try
				{
					ResSet = statement.executeQuery(select);
				}catch(SQLException e)
				{
					e.printStackTrace();
				}
				while (ResSet.next())
				{
					out.writeUTF(ResSet.getString("name"));
				}
				break;
				
			case 6: 
				filmName = in.readUTF();
				select = "select count(*) from film_comment where film_comment.film_id = " 
						 + "(select id from film where film.name = \"" + filmName +"\")";
				try
				{
					ResSet = statement.executeQuery(select);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
				count = 0;
				while(ResSet.next())
				{
					count = ResSet.getInt("count(*)");
				}
				select = "select * from film_comment where film_comment.film_id = " 
						 + "(select id from film where film.name = \"" + filmName +"\")";
				try
				{
					ResSet = statement.executeQuery(select);
				}
				catch(SQLException e)
				{
				e.printStackTrace();
				}
				out.writeInt(count);
				while(ResSet.next())
				{
					System.out.println(ResSet.getString("user_id"));
					out.writeUTF(ResSet.getString("user_id"));
					System.out.println(ResSet.getString("comment"));
					out.writeUTF(ResSet.getString("comment"));
					
					System.out.println(ResSet.getString("rating"));
					
					out.writeUTF(ResSet.getString("rating"));
					
					System.out.println(ResSet.getString("likes"));
					out.writeUTF(ResSet.getString("likes"));
					System.out.println(ResSet.getString("dislikes"));
					out.writeUTF(ResSet.getString("dislikes"));
					System.out.println(ResSet.getString("film_comment_id"));
					out.writeUTF(ResSet.getString("film_comment_id"));
				}
				break;
			case 7:
				name = in.readUTF();
				select = "select id from user where user.login = \"" + name + "\"";
				try
				{
					ResSet = statement.executeQuery(select);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
				int ID = 0;
				while(ResSet.next())
				{
					ID = (ResSet.getInt("id"));
				}
				out.writeInt(ID);
				break;
			case 8:
				id = in.readInt();
				filmID = 0;
				name = in.readUTF();
				select = "select id from film where film.name = \"" + name + "\"";
				try
				{
					ResSet = statement.executeQuery(select);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
				while (ResSet.next())
				{
					filmID = ResSet.getInt("id");
				}
				String comment = in.readUTF();
				int rating = Integer.parseInt(in.readUTF());
				select = "insert into film_comment(comment, film_id, rating, likes, dislikes, user_id) values (\""
						+ comment + "\", " + filmID + ", " + rating + ", 0, 0, " + id + ")";
				System.out.println(select);
				try
				{
					statement.executeUpdate(select);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
				select = "select avg(rating) from film_comment where film_id = " + filmID;
				try
				{
					ResSet = statement.executeQuery(select);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
				while (ResSet.next())
				{
					rating = ResSet.getInt("avg(rating)");
				}
				select = "update film set rating = " + rating + " where id = " + filmID;
				System.out.println(select);
				try
				{
					statement.execute(select);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
				break;
			case 9:
				n = in.readInt();
				for (int i = 0; i < n; ++i)
				{
					String user = in.readUTF();
					select = "select login from user where user.id = \"" + user + "\"";
					try
					{
						ResSet = statement.executeQuery(select);
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
					while(ResSet.next())
					{
						out.writeUTF(ResSet.getString("login"));
					}
				}
				break;
			case 10:
				n = in.readInt();
				comment = in.readUTF();
				String autorID = in.readUTF();
				select = "select film_comment_id from film_comment where comment = \"" + comment + "\" and user_id = \"" + autorID + "\"";
				
				try
				{
					ResSet = statement.executeQuery(select);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
				int commentID = 0;
				while (ResSet.next())
				{
					commentID = ResSet.getInt("film_comment_id");
				}
				select = "select id from comment_like where user_id = " + n + " and comment_id = " + commentID;
				try
				{
					ResSet = statement.executeQuery(select);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
				id = 0;
				while (ResSet.next())
				{
					id = ResSet.getInt("id");
				}
				if (id == 0 && Integer.parseInt(autorID) != n)
				{
					out.writeUTF("NO");
				}
				else out.writeUTF("YES");
				break;
			case 11:
				n = in.readInt();
				id = in.readInt();
				int isLike = in.readInt();
				select = "insert into comment_like(liked, user_id, comment_id) values ( " + isLike + ", " + id + ", " + n + " )";
				try
				{
					statement.executeUpdate(select);
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
				if (isLike == 1)
				{
					select = "select likes from film_comment where film_comment_id =  " + n;
					System.out.println(select);
					try
					{
						ResSet = statement.executeQuery(select);
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
					int likes = 0;
					while (ResSet.next())
					{
						likes = ResSet.getInt("likes");
					}
					likes++;
					select = "update film_comment set likes = " + likes + " where film_comment_id = " + n;
					System.out.println(select);
					try
					{
						statement.execute(select);
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
				} else
				{
					select = "select dislikes from film_comment where film_comment_id =  " + n;
					System.out.println(select);
					try
					{
						ResSet = statement.executeQuery(select);
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
					int likes = 0;
					while (ResSet.next())
					{
						likes = ResSet.getInt("dislikes");
					}
					likes++;
					select = "update film_comment set dislikes = " + likes + " where film_comment_id = " + n;
					try
					{
						statement.execute(select);
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
				}
				break;
			case 13:
				waitForConnections(socket);
				break;
			}
		}
	}
}
