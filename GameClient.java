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
			
			System.out.println("메인 화면");
			
			int choice; // the user's choice where to spend the day
			int betAmount; // amount of money bet to blackjack game
			int betResult; // final change of cash amount after blackjack game
			
			
			while(true)
			{
				
				System.out.println("오늘은 어디에서 볼일을 볼 지 선택하세요");
				System.out.println("1. 도박장");
				System.out.println("2. 회사");
				System.out.println("3. 대부업체");
				
				choice = keyboard.nextInt();
				keyboard.nextLine();
				if (choice == 1) // play blackjack
				{
					clearScreen();
					
					BlackJack playBlackJack = new BlackJack();
					
					// get the amount of money to bet
					while(true)
					{
						System.out.println("베팅할 돈 액수를 입력해 주세요(단위 : 만원)");
						betAmount = keyboard.nextInt();
						keyboard.nextLine();
						
						if (betAmount <= getCashAmount(in, out))
						{
							break;
						}
						else
						{
							System.out.println("가지고 있는 돈보다 더 많이 베팅할 수 없습니다.");
						}
					}
					
					
					// game ended. change the amount of cash by the result of the game
					betResult = playBlackJack.main(betAmount);
					System.out.println("도박 결과 : " + betResult +"만원");
					changeCash(betResult, out);
				}
				else if (choice == 2) // work - not finished
				{
					System.out.println("회사 구현 예정");
				}
				else if (choice == 3) // bank - not finished
				{
					clearScreen();
					
					System.out.println("-장첸");
					System.out.println("돈빌러러 왔늬↗?");
					
					System.out.println("대부업체 구현 예정");
					
				}
				else
				{
					System.out.println("올바른 값을 골라 주세요.");
					continue;
				}
				
				
				// The day ended. Spend some money for living expenses
				System.out.println("하루가 지났습니다.");
				if (getCashAmount(in, out) < 5) // no enough money to spend for daily expenditure - decrease satiety
				{
					System.out.println("돈이 없어 밥을 먹지 못했습니다... 빨리 돈을 구해야 합니다!");
					System.out.println("포만감이 10 감소했습니다.");
					changeSatiety(-10, in, out);
				}
				else // you can pay the living expense. no penalty
				{
					System.out.println("방세와 생활비로 10만원을 소비했습니다.");
					changeCash(-10, out);
					System.out.println("남은 돈 : " + getCashAmount(in, out));
				}
				
				if (getLoanAmount(in, out) > 0) // still have a debt - Zhang Chen is getting angry - decrease patience
				{
					System.out.println("장첸이 대출 때문에 점점 화가 나는 것 같습니다. 빨리 돈을 갚는 것이 좋을 것 같습니다.");
					System.out.println("장첸의 인내심이 5 감소했습니다.");
					changePatience(-5, in, out);
				}
				
				System.out.println("수면을 취합니다...");
				
				// save the status of the character at the server
				while(true)
				{
					System.out.println("저장하시겠습니까?\n1 : 예\n2 : 아니오");
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
						System.out.println("다시 입력해 주세요");
					}
				}
				
				
				// show informations of the character
				System.out.println("날이 밝았습니다");
				System.out.println("보유 현금 : " + getCashAmount(in, out));
				System.out.println("남은 빚 : " + getLoanAmount(in, out));
				System.out.println("포만감 : " + getSatietyAmount(in, out));
				System.out.println("장첸의 인내심 : " + getPatienceAmount(in, out));
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
			System.out.println("-장첸");
			System.out.println("니 내 누군지 아늬? 하얼빈의 장첸이야!!!");
			System.out.println("니 누군데? 어? 누구길래 돈을 안 갚어");
			System.out.println("얘들아, 이놈 끌고 가라");
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
