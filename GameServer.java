/**
 * @author Qusay H. Mahmoud
 */

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Random;

public class GameServer extends Thread {

   ServerSocket hi;
   Socket client;
   DataInputStream br;
   DataOutputStream dos;

   public static boolean busyServer = false;
   static int clients = 0;
   static int maxClients = 2;
    private ServerSocket gameServer;

   public static void main(String argv[]) throws Exception {
     new GameServer();
   }
 
   public GameServer() throws Exception {
     gameServer = new ServerSocket(3500);
     System.out.println("Server listening on port 3500.");
     this.start();
   }
 
   @SuppressWarnings("unused")
   public void run() {
     while (true) {
       try {
         System.out.println("Waiting for connections.");
         Socket client = gameServer.accept();
         if(clients < maxClients){
          System.out.println("Accepted a connection from: " + client.getRemoteSocketAddress());
          clients += 1;
         }else{
          busyServer = true;
          System.out.println("Denied connection from: " + client.getRemoteSocketAddress());
          // Connect c = new Connect(client, true);
         }
         Connect c = new Connect(client);
       } catch (Exception e) {
       }
     }
   }
}

class Connect extends Thread {
	private Socket client = null;
	private DataInputStream ois = null;
	private DataOutputStream oos = null;

	public Connect(Socket clientSocket) throws Exception {
		client = clientSocket;
		try {
			ois = new DataInputStream(client.getInputStream());
			oos = new DataOutputStream(client.getOutputStream());
            new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
		} catch (Exception e1) {
      System.out.println("Caught exception " + e1.getMessage());
			try {
				client.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return;
		}
		this.start();
	}

	public void run() {
	    try {
        // oos.writeBytes("Welcome to text based game center!\n");            
        oos.writeBytes("0 = Quit, 1 = Rock Paper Scissors, 2 = Blind Tic Tac Toe\n");
        oos.flush();
        System.out.println("Waiting for response");
        int choice = ois.readInt();
        System.out.println("Choice: " + choice);
        while(choice != 0){
            if(choice == 1){
                System.out.println("choice is 1");
                RockPaperScissors();
            }else if(choice == 2){
              System.out.println("Choice is 2");
              BlindTicTacToe();
            }
            oos.writeBytes("0 = Quit, 1 = Rock Paper Scissors, 2 = Blind Tic Tac Toe\n");
            oos.flush();
            choice = ois.readInt();
          }
			// close streams and connections
			ois.close();
			oos.close();
			client.close();
		} catch (Exception e) {
		}
	}

    public void RockPaperScissors(){
        try{
            oos.writeBytes("1 = Rock, 2 = Scissors, 3 = Paper\n");
            oos.flush();
            int userMove = ois.readInt();

            Random rand = new Random();
            int num = rand.nextInt(3);
            String move;
            if(num == 0){
                move = "rock";
            }else if(num == 1){
                move = "paper";
            }else{
                move = "scissors";
            }
            String result = "Computer move: " + move + ". You";
            if(num == userMove){
                result += "Tied.";
            }else if (userMove - 1 == num || (userMove == 0 && num == 2)){
                result += "Win!";
            }else{
                result += "Lose!";
            }
            result += " Enter anything to continue.\n";
            oos.writeBytes(result);
            oos.flush();

            int excess = ois.readInt();
        }catch (Exception e) {
		}
    }

    public void BlindTicTacToe(){
      int[] board = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
      try{
        /* oos.writeBytes("Don't worry about how to play or the rules, just pick a position between 1-9");
        oos.flush(); */

        int result = BlindTicTacToeResults(board);
        System.out.println(result);
        String message = "";
        while(result == 0){
          System.out.println("Here");
          oos.writeBytes("Don't worry about how to play or the rules, just pick a position between 1-9");
          oos.flush();
          System.out.println("here3");
          int userMove = ois.readInt() - 1;
          if(userMove >=0 && userMove <=8){
            if(board[userMove] == -1){
              board[userMove] = 0;
            }
          }
          result = BlindTicTacToeResults(board);
          if(result == 1 || result == -1){
            break;
          }
          Random rand = new Random();
          int num = rand.nextInt(9);
          message = "Computer chose: " + num + ". ";
          System.out.println(message);
          result = BlindTicTacToeResults(board);
          if(result == 0){
            oos.writeBytes(message);
            oos.flush();
          }
        }
        
        if(result == 1){
          oos.writeBytes(message + " You win! Enter any number to continue.\n");
          oos.flush();
        }else if(result == 2)
        {
          oos.writeBytes(message + " You lose. Enter any number to continue.\n");
          oos.flush();
        }else if(result == -1){
          oos.writeBytes(message + " Draw. Enter any number to continue.\n");
          oos.flush();
        }
      }catch(Exception e){

      }
    }

    private int BlindTicTacToeResults(int[] board){
      for(int i = 0; i <9; i+=3){
        if(board[i] == board[i+1] && board[i+1] == board[i+2]){
          if(board[i] == 0){
            return 1; //Player wins
          }else if(board[i] == 1){
            return 2; //Computer wins
          }
        }
      }
      for(int i = 0; i <3;i++){
        if(board[i] == board[i+3] && board[i+3] == board[i+6]){
          if(board[i] == 0){
            return 1; //Player wins
          }else if(board[i] == 1){
            return 2; //Computer wins
          }
        }
      }

      
      if(board[0] == board[4] && board[4] == board[8]){
        if(board[4] == 0){
            return 1; //Player wins
          }else if(board[4] == 1){
            return 2; //Computer wins
          }
      }

      if (board[2] == board[4] && board[4] == board[6]){
        if(board[4] == 0){
            return 1; //Player wins
          }else if(board[4] == 1){
            return 2; //Computer wins
          }
      }

      for(int i = 0; i <9; i++){
        if(board[i] == -1){
          break;
        }
        if(i == 8){
          return -1; //It's a draw
        }
      }
      return 0; //REsume play
    }
}