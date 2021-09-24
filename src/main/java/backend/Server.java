/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author Cliftonb
 */
public class Server implements BroConstants {

	private int sessionNum = 0;

	public static void main(String[] args) {
		new Server();
	}

	//The general model for server and multiple clients:
	//Each of the clients are waiting for a status.
	//The server cycles through all the clients (if there are multiple clients in one session) and sends them a status update (followed by additional information if the status calls for it).
	//When they recieve a status, they know if they will receive additional input or not.
	//They then send a response to the server and go back to waiting.
	//Note: a session is normally one client, one server.
	public Server() {

		try {
			ServerSocket serverSocket = new ServerSocket(8000);
			log("Server started.");

			while (true) {

				//Listen for a connection from p1 (this is a blocking method, code stops until a connection is made)
				Socket playerOneSocket = serverSocket.accept();
				//Let player one know their player number
				DataOutputStream playerOneOutput = new DataOutputStream(playerOneSocket.getOutputStream());
				DataInputStream playerOneInput = new DataInputStream(playerOneSocket.getInputStream());
				playerOneOutput.writeChar(PLAYER_ONE);
				playerOneOutput.flush();
				//String playerOneName = playerOneInput.readUTF();
				log("Player 1 connected");
				//log("Player 1 name: " + playerOneName);
				log("IP Address: " + playerOneSocket.getInetAddress());

				//Listen for a connection from p1 (this is a blocking method, code stops until a connection is made)
				Socket playerTwoSocket = serverSocket.accept();
				//Let player two know their player number
				DataOutputStream playerTwoOutput = new DataOutputStream(playerTwoSocket.getOutputStream());
				DataInputStream playerTwoInput = new DataInputStream(playerTwoSocket.getInputStream());
				playerTwoOutput.writeChar(PLAYER_TWO);
				playerTwoOutput.flush();
				//String playerTwoName = playerTwoInput.readUTF();
				log("Player 2 connected");
				//log("Player 2 name: " + playerTwoName);
				log("IP Address: " + playerTwoSocket.getInetAddress());

				sessionNum++;

				Session task = new Session(playerOneSocket, playerTwoSocket);
				new Thread(task).start();

				log("Session " + sessionNum + " started.");
			}

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	private void log(String s) throws IOException {
		System.out.println(new Date() + ": " + s);

		PrintWriter textLog = new PrintWriter(new FileWriter("data//serverlog.txt", true));
		textLog.append(new Date() + ": " + s + "\n");
		textLog.close();
	}

}
