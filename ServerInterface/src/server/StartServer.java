package server;

import java.io.IOException;
import java.sql.SQLException;

public class StartServer {
	public static void main(String [] args) throws IOException, SQLException, InterruptedException
	{
		new Server();
	}
}
