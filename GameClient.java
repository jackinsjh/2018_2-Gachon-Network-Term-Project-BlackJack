import java.util.Scanner;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

/*
 * client part of the program
 */
public class GameClient {
	
	BufferedReader in; // stream of data from the server
	PrintWriter out; // stream of data to send to the server
	
	
	// main body of the client. exchange many kinds of data with the server, and executes the game
	private void clientMain()
	{
		
		Scanner keyboard = new Scanner(System.in);
		
		// establish connection
		String serverIP; // IP of the server
		
		// get IP from the server
		System.out.println("Enter the IP address of the server");
		System.out.println("ex) 127.0.0.1");
		serverIP = keyboard.nextLine();
		
		try
		{
			Socket socket = new Socket(serverIP, 9001); // make socket to connect with the server
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		
		
		
		
			
			String curUserID; // After logged in, the user uses this ID
			String ID; // ID buffer
			String password; // password buffer
			String response; // buffer of the server's response
			String buffer; // input buffer
					
						
			
			System.out.println("내 돈 아이갚니?");
			
			
			// check if the user wants to register a new account
			System.out.println("Are you new to this game? Do you want to sign up? (y/n)");
			while(true)
			{
				
				buffer = keyboard.nextLine();
				if (buffer.equalsIgnoreCase("y") == true) // the user wants to register a new ID
				{
					while (true)
					{
						// register a new ID - get ID and password
						System.out.println("ID and password are case-sensitive");
						System.out.println("No space or / letter is allowed in ID or password");
						System.out.print("Enter ID : ");
						ID = keyboard.nextLine();
						System.out.print("Enter password : ");
						password = keyboard.nextLine();
						out.println("signUp/" + ID + "/" + password); // send signUp message to the server
						
						// wait for server's response and deal with it
						response = in.readLine();
						
						if (response.equals("yes")) // the server accepted and registered new ID
						{
							System.out.println("Account registered");
							break;
						}
						else if (response.equals("no")) // ID already exists
						{
							System.out.println("ID already exists. Try with another ID");
							continue;
						}
						else // error
						{
							System.out.println("Wrong answer from server while registering an account");
						}
					}
					
					break;
				}
				else if (buffer.equalsIgnoreCase("n") == true) // the user doesn't want to register a new ID
				{
					break;
				}
				else // the user entered wrong input
				{
					System.out.println("try again");
				}
			}
			
			
			
			// log-in session
			System.out.println("Please log in");
			System.out.println("ID and password are case-sensitive");
			while (true)
			{
				
				System.out.print("Enter ID : ");
				ID = keyboard.nextLine();
				System.out.print("Enter password : ");
				password = keyboard.nextLine();
			
				// send login info to server and wait for response
				out.println("login/" + ID + "/" + password);
				response = in.readLine();
				
				
				if(response.equals("yes") == true) // the server accepted the login information
				{
					System.out.println("Sucessfully logged in");
					curUserID = new String(ID);
					break;
				}
				else
				{
					if (response.equals("wrongID") == true) // the user entered wrong ID
					{
						System.out.println("Wrong ID");
					}
					else if (response.equals("wrongPW") == true) // the user entered wrong password
					{
						System.out.println("Wrong password");
					}
				}
			}
			
			
			
			// executes the game
			clearScreen(); // erases the program screen
			
			System.out.println("메인 화면");
			
			int choice; // the user's choice where to spend the day
			
			while(true)
			{
				
				System.out.println("오늘은 어디에서 볼일을 볼 지 선택하세요");
				System.out.println("1. 도박장");
				System.out.println("2. 회사");
				System.out.println("3. 은행");
				
				choice = keyboard.nextInt();
				if (choice == 1)
				{
					System.out.println("도박장 구현 예정");
				}
				else if (choice == 2)
				{
					System.out.println("회사 구현 예정");
				}
				else if (choice == 3)
				{
					System.out.println("은행 구현 예정");
				}
				else
				{
					System.out.println("올바른 값을 골라 주세요.");
					continue;
				}
				
				
				// The day ended. Spend some money for living expenses
				System.out.println("하루가 지났습니다. 수면을 취합니다. 그리고 생활비로 돈 일부가 차감됩니다.");
				System.out.println("날이 밝았습니다");
			}
			
			
			
			
			
		
		}
		catch(Exception a) // exception occured.
		{
			System.out.println("Error");
		}
		
		
		keyboard.close();
	}
	
	
	
	
	private void clearScreen() // erases the program screen by \n
	{
		int i;
		for (i = 0; i <= 80; i++)
		{
			System.out.println();
		}
	}
	
	
	
	

	public static void main(String[] args) {
		GameClient cl = new GameClient();
		cl.clientMain();
	
	}

}
