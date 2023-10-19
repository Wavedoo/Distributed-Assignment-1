import java.io.*;
import java.net.*;
import java.lang.Thread;

public class GameClient extends Thread {
   public static void main(String argv[]) throws Exception {
     Socket server;
     DataInputStream chatInput;
     DataOutputStream chatOutput;

/*      if (argv.length != 1) {
      System.err.println(
          "Usage: Java GameClient.java <username>");
          System.exit(1);
      } */

    // String username = argv[0];

    server = new Socket("localhost", 3500);
    chatInput = new DataInputStream(server.getInputStream());
    chatOutput = new DataOutputStream(server.getOutputStream());
     
    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    System.out.println(chatInput.readLine());

    String userInput;

    while((userInput = stdIn.readLine()) != null){
        
        chatOutput.writeInt(Integer.parseInt(userInput));
        chatOutput.flush();
        String message = chatInput.readLine();
        System.out.println(message);
    }

    server.close();
    System.exit(0);
   }
}
