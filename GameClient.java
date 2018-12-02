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
					
						
			
			System.out.println("�� �� ���̰���?");
			
			
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
				
				
				if(response.equals("yes") == true) // the server accepted the login information. Load user profile.
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
			
			System.out.println("���� ȭ��");
			
			int choice; // the user's choice where to spend the day
			int betAmount; // amount of money bet to blackjack game
			int betResult; // final change of cash amount after blackjack game
			
			
			while(true)
			{
				
				System.out.println("������ ��𿡼� ������ �� �� �����ϼ���");
				System.out.println("1. ������");
				System.out.println("2. ȸ��");
				System.out.println("3. ��ξ�ü");
				
				choice = keyboard.nextInt();
				keyboard.nextLine();
				if (choice == 1) // play blackjack
				{
					clearScreen();
					
					BlackJack playBlackJack = new BlackJack();
					
					// get the amount of money to bet
					while(true)
					{
						System.out.println("������ �� �׼��� �Է��� �ּ���(���� : ����)");
						betAmount = keyboard.nextInt();
						keyboard.nextLine();
						
						if (betAmount <= getCashAmount(in, out))
						{
							break;
						}
						else
						{
							System.out.println("������ �ִ� ������ �� ���� ������ �� �����ϴ�.");
						}
					}
					
					
					// game ended. change the amount of cash by the result of the game
					betResult = playBlackJack.main(betAmount);
					System.out.println("���� ��� : " + betResult +"����");
					changeCash(betResult, out);
				}
				else if (choice == 2) // work - not finished
				{
					System.out.println("ȸ�� ���� ����");
				}
				else if (choice == 3) // bank - not finished
				{
					clearScreen();
					
					System.out.println("-��þ");
					System.out.println("�������� �Դ̢�?");
					
					System.out.println("��ξ�ü ���� ����");
					
				}
				else
				{
					System.out.println("�ùٸ� ���� ��� �ּ���.");
					continue;
				}
				
				
				// The day ended. Spend some money for living expenses
				System.out.println("�Ϸ簡 �������ϴ�.");
				if (getCashAmount(in, out) < 5) // no enough money to spend for daily expenditure - decrease satiety
				{
					System.out.println("���� ���� ���� ���� ���߽��ϴ�... ���� ���� ���ؾ� �մϴ�!");
					System.out.println("�������� 10 �����߽��ϴ�.");
					changeSatiety(-10, in, out);
				}
				else // you can pay the living expense. no penalty
				{
					System.out.println("�漼�� ��Ȱ��� 10������ �Һ��߽��ϴ�.");
					changeCash(-10, out);
					System.out.println("���� �� : " + getCashAmount(in, out));
				}
				
				if (getLoanAmount(in, out) > 0) // still have a debt - Zhang Chen is getting angry - decrease patience
				{
					System.out.println("��þ�� ���� ������ ���� ȭ�� ���� �� �����ϴ�. ���� ���� ���� ���� ���� �� �����ϴ�.");
					System.out.println("��þ�� �γ����� 5 �����߽��ϴ�.");
					changePatience(-5, in, out);
				}
				
				System.out.println("������ ���մϴ�...");
				
				// save the status of the character at the server
				while(true)
				{
					System.out.println("�����Ͻðڽ��ϱ�?\n1 : ��\n2 : �ƴϿ�");
					choice = keyboard.nextInt();
					keyboard.nextLine();
					if (choice == 1)
					{
						// save
						out.println("save/");
						break;
					}
					else if (choice == 2)
					{
						// do nothing
						break;
					}
					else
					{
						System.out.println("�ٽ� �Է��� �ּ���");
					}
				}
				
				
				// show informations of the character
				System.out.println("���� ��ҽ��ϴ�");
				System.out.println("���� ���� : " + getCashAmount(in, out));
				System.out.println("���� �� : " + getLoanAmount(in, out));
				System.out.println("������ : " + getSatietyAmount(in, out));
				System.out.println("��þ�� �γ��� : " + getPatienceAmount(in, out));
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
	
	
	
	
	
	
	
	
	
	private void changeCash(int amount, PrintWriter out) // request the server to change the amount of current cash
	{
		out.println("changeCash/" + amount);
	}
	
	
	
	
	private void changeLoan(int amount, PrintWriter out) // request the server to change the amount of current loan
	{
		out.println("changeLoan/" + amount);
	}
	
	
	
	
	
	private void changeSatiety(int amount, BufferedReader in, PrintWriter out) throws Exception // request the server to change the amount of current satiety
	{
		out.println("changeSatiety/" + amount);
		
		String response = in.readLine();
		if (response.equals("okay") == true) // you are not hungry enough to die
		{
			return;
		}
		else if (response.equals("gameOver") == true) // satiety is <= 0 . game over - hunger
		{
			// game over by hunger
			System.out.println("Game over! You died of hunger!");
			System.exit(0);
		}
		else
		{
			System.out.println("Something is wrong communicating with the server by changing satiety");
		}
		
	}
	
	
	
	
	private void changePatience(int amount, BufferedReader in, PrintWriter out) throws Exception // request the server to change the amount of patience of Zhang Chen (limit for repaying the loan)
	{
		out.println("changePatience/" + amount);
		
		String response = in.readLine();
		if (response.equals("okay") == true) // Zhang would give you some more time to repay the debt
		{
			return;
		}
		else if (response.equals("gameOver") == true) // Zhang cannot take it anymore. He is very angry because you didn't repay the debt for a long time. - game over
		{
			// game over by not repaying the loan to Zhang Chen
			// System.out.println("Game over! You were killed by angry Zhang Chen! You should have repaid your loan in time.");
			System.out.println("-��þ");
			System.out.println("�� �� ������ �ƴ�? �Ͼ���� ��þ�̾�!!!");
			System.out.println("�� ������? ��? �����淡 ���� �� ����");
			System.out.println("����, �̳� ���� ����");
			System.out.println("Game over! You were killed by angry Zhang Chen! You should have repaid your loan in time.");
			System.exit(0);
		}
		else
		{
			System.out.println("Something is wrong communicating with the server by changing patience");
		}
	}
	
	
	
	
	
	private int getCashAmount(BufferedReader in, PrintWriter out) throws Exception // get current cash amount from the server
	{
		out.println("getCashAmount/");
		
		String response = in.readLine();
		int answer = Integer.valueOf(response);
		
		return answer;
	}
	private int getLoanAmount(BufferedReader in, PrintWriter out) throws Exception // get current loan amount from the server
	{
		out.println("getLoanAmount/");
		
		String response = in.readLine();
		int answer = Integer.valueOf(response);
		
		return answer;
	}
	private int getSatietyAmount(BufferedReader in, PrintWriter out) throws Exception // get current satiety amount from the server
	{
		out.println("getSatietyAmount/");
		
		String response = in.readLine();
		int answer = Integer.valueOf(response);
		
		return answer;
	}
	private int getPatienceAmount(BufferedReader in, PrintWriter out) throws Exception // get current patience amount from the server
	{
		out.println("getPatienceAmount/");
		
		String response = in.readLine();
		int answer = Integer.valueOf(response);
		
		return answer;
	}
	
	
	
	
	
	
	
	
	

	public static void main(String[] args) {
		GameClient cl = new GameClient();
		cl.clientMain();
	
	}

}
