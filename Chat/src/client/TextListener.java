package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TextListener implements Runnable {
    ServerSocket echoServer = null;
    Socket clientSocket = null;
    String clientUsername;

    public TextListener(ServerSocket serverSocket, String clientUsername){
        this.echoServer = serverSocket;
        this.clientUsername = clientUsername;
    }

    @Override
    public void run() {
        while (true){
            String line;
            BufferedReader is;
            try {
                clientSocket = echoServer.accept();
                is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while(true) {
                    line = is.readLine();
                    if(line == null) {
                        break;
                    }
                    System.out.println();
                    System.out.println(line);
                    System.out.print(this.clientUsername + " > ");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
