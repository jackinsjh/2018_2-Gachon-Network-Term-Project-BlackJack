import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;


public class GameServer {
	
	private static HashMap<String, String> ID_PW_List = new HashMap<String, String>(); // list of IDs and PWs
	private static final int initial_cash = 500; // amount of given cash at the beginning of the game. 1 indicates 10 thousands
	private static final int initial_loan = 500; // amount of assigned amount of loan at the beginning of the game. 1 indicates 10 thousands
	private static final int initial_satiety = 100; // amount of satiety parameter at the beginning of the game
	private static final int initial_patience = 100; // amount of Zhang Chen's patience parameter at the beginning of the game.

	
	// the server uses port 9001 only
	
	public static void main(String[] args) {
		
		System.out.println("Server is ON");
		
		
		try
		{
			String buffer; // input buffer
			String IDBuffer; // ID input buffer
			String PWBuffer; // PW input buffer
			BufferedReader inputStream = null; // used to read ID_PW_List.dat file
			
			inputStream = new BufferedReader(new FileReader("ID_PW_List.dat"));
			
			
			// read all data from "ID_PW_List.dat" and store all data to ID_PW_List HashMap
			while(true)
			{
		
				buffer = inputStream.readLine();
				if (buffer == null) // end of the list file
				{
					break;
				}
				IDBuffer = new String(buffer);
				
				buffer = inputStream.readLine();
				if (buffer == null) // the number of lines of file is not even(ID, PW) - data corrupted
				{
					System.out.println("Error on ID_PW_List.dat");
					break;
					
				}
				PWBuffer = new String(buffer);
				
				ID_PW_List.put(IDBuffer, PWBuffer); // put a ID and a password to hashmap by pairs
				
				
			}
			
			inputStream.close();
			
			// the listener waits for the clients and assign a new thread of the server for each client
			ServerSocket listener = new ServerSocket(9001);
			while(true)
			{
				new clientThread(listener.accept()).start();
			}
		}
		catch (Exception a) // exception on listener
		{
			System.out.println("Failed to make a new thread for the client");
		}
		
		
		
		
	}

	
	// main thread of server for a client. exchange many kinds of data with the client
	private static class clientThread extends Thread
	{
		private Socket socket; // currently using socket
		private BufferedReader in; // data stream sent from the client
		private PrintWriter out; // data stream to send to the client
		
		// initiate socket information
		public clientThread(Socket socket)
		{
			this.socket = socket;
		}
		
		
		// overriding Thread class' run() function.
		// main body of the server's processing of communication with the client
		public void run()
		{
			try
			{
				PrintWriter outputStream = null;
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				String inFromClient; // the String which is sent from the client
				int borderIndex1; // border of the client's message (index of '/' letter in the message)
				int intBuffer;
				String stringBuffer;
				
				Scanner fileReader;
				PrintWriter fileWriter;
				
				
				
				
				
				String ID; // ID buffer
				String password; // password buffer
				
				String curUserID = ""; // this user's ID
				int curUserCash = 1; // this user's cash amount
				int curUserLoan = 1; // this user's loan amount
				int curUserSatiety = 1; // this user's satiety
				int curUserPatience = 1; // this user's patience
				
				
				while(true)
				{
					inFromClient = in.readLine(); // read the message from the client
					if (inFromClient == null) // there was no message from the client
					{
						continue;
					}
					
					
					// every server-client communication protocols are here!!!!
					else if (inFromClient.startsWith("login/")) // client requests login
					{
						// structure of the message from client
						// login/(ID)/(password)
						
						borderIndex1 = inFromClient.lastIndexOf("/"); // get location of the border '/'
						ID = inFromClient.substring(6,borderIndex1); // extract ID from the message
						password = inFromClient.substring(borderIndex1 + 1); // extract password from the message
						
						// for debug
						System.out.println("A user signed in");
						System.out.println("ID : " + ID);
						System.out.println("PW : " + password + "\n");
						
						

						// if the login info is correct, send yes message to the client and let the client log in
						// if the login info is wrong, send error message to the client
						while (true)
						{
							if (ID_PW_List.containsKey(ID) == true) // the entered ID exists
							{
								if (ID_PW_List.get(ID).equals(password)) // the password matches to the ID
								{
									// successfully logged in
									out.println("yes");
									
									
									// load parameters from the user profile
									fileReader = new Scanner(new File(ID + ".dat"));
									
									curUserID = new String(ID);
									curUserCash = fileReader.nextInt();
									curUserLoan = fileReader.nextInt();
									curUserSatiety = fileReader.nextInt();
									curUserPatience = fileReader.nextInt();
									
									
									fileReader.close();
									
									
									break;
								}
								else // wrong password entered
								{
									out.println("wrongPW");
								}
								
								
							}
							else // wrong ID entered
							{
								out.println("wrongID");
							}
						}
						
					}
					
					else if (inFromClient.startsWith("signUp/")) // the client requested to register a new account
					{
						// structure of the message from client
						// signUp/(ID)/(password)
						
						// creating account
						
						
						borderIndex1 = inFromClient.lastIndexOf("/"); // get index of the border of message '/'
						ID = inFromClient.substring(7, borderIndex1); // extract ID from the message
						password = inFromClient.substring(borderIndex1 + 1); // extract password from the message
						
						// when the ID already exists - cannot use this ID for a new account
						if(ID_PW_List.containsKey(ID) == true)
						{
							out.println("no");
						}
						else // the new ID is available
						{
							out.println("yes");
							
							// register the new account
							
							// add the ID in ID_PW_List HashMap
							ID_PW_List.put(ID, password);
							
							// add the ID in "ID_PW_List.dat"
							outputStream = new PrintWriter(new FileWriter("ID_PW_List.dat", true));
							outputStream.println(ID);
							outputStream.println(password);
							outputStream.close();
							
							// make a new profile file for the new ID and initiate parameters
							outputStream = new PrintWriter(new FileWriter(ID.concat(".dat")));
							outputStream.println(initial_cash);
							outputStream.println(initial_loan);
							outputStream.println(initial_satiety);
							outputStream.println(initial_patience);
							outputStream.close();
						}
					}
					
					
					
					
					else if(inFromClient.startsWith("changeCash/")) // change the amount of current cash - client requested this
					{
						stringBuffer = inFromClient.substring(11);
						intBuffer = Integer.valueOf(stringBuffer);
						
						curUserCash += intBuffer;
						if (curUserCash < 0) // cash cannot be below 0
						{
							curUserCash = 0;
						}
					}
					else if(inFromClient.startsWith("changeLoan/")) // change the amount of current loan - client requested this
					{
						stringBuffer = inFromClient.substring(11);
						intBuffer = Integer.valueOf(stringBuffer);
						
						curUserLoan += intBuffer;
						
					}
					else if(inFromClient.startsWith("changeSatiety/")) // change the amount of current satiety - client requested this
					{
						stringBuffer = inFromClient.substring(14);
						intBuffer = Integer.valueOf(stringBuffer);
						
						curUserSatiety += intBuffer;
						if (curUserSatiety < 0) // game over of hunger - inform this to the client
						{
							
							out.println("gameOver");
						}
						else // not game over
						{
							out.println("okay");
						}
					}
					else if(inFromClient.startsWith("changePatience/")) // change the amount of patience of Zhang Chen (limit for repaying the loan) - client requested this
					{
						stringBuffer = inFromClient.substring(15);
						intBuffer = Integer.valueOf(stringBuffer);
						
						curUserPatience += intBuffer;
						if (curUserPatience < 0) // game over of Zhang's impatience - inform this to the client
						{
							// game over by Zhang Chen
							out.println("gameOver");
						}
						else // not game over
						{
							out.println("okay");
						}
					}
					else if(inFromClient.startsWith("getCashAmount/")) // client requested the amount of current cash - inform that information to the client
					{
						out.println(curUserCash);
					}
					else if(inFromClient.startsWith("getLoanAmount/")) // client requested the amount of current loan - inform that information to the client
					{
						out.println(curUserLoan);
					}
					else if(inFromClient.startsWith("getSatietyAmount/")) // client requested the amount of current satiety - inform that information to the client
					{
						out.println(curUserSatiety);
					}
					else if(inFromClient.startsWith("getPatienceAmount/")) // client requested the amount of current patience - inform that information to the client
					{
						out.println(curUserPatience);
					}
					else if(inFromClient.startsWith("save/")) // save all current user parameters to the user's file((User ID).dat) 
					{
						fileWriter = new PrintWriter (curUserID + ".dat");
						
						fileWriter.println(curUserCash);
						fileWriter.println(curUserLoan);
						fileWriter.println(curUserSatiety);
						fileWriter.println(curUserPatience);
						
						fileWriter.close();
					}
					
					
					else // unknown message received
					{
						System.out.println("Undefined message from a client");
					}
					
					
					
					
				}
				
				
			}
			catch (Exception a) // exception of the server thread
			{
				System.out.println("One of the server threads was terminated");
			}
			finally
			{
				// if you need anything to be done after a client closes the connection, write here
			}
			
			
			
		}
	}
}
