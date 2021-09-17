/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author Cliftonb
 */
public class Client implements BroConstants {

	private char playerToken;
	private Socket socket;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private boolean gameOver = false;
	private boolean isWaitingForAction = true;
	private boolean isWaitingForOpponent = false;
	private int playerResult = -1;

	private int status = -1;
	private int opponentNextMove = -1;
	//private String playerName;
	//private String opponentName;

	public Client() {
		//this.playerName = playerName;
		//this.playerName = null;

	}

	public void connectToServer() throws IOException {
		//socket = new Socket("127.0.0.1", 8000);
		socket = new Socket("102.130.115.40", 8000);
		fromServer = new DataInputStream(socket.getInputStream());
		toServer = new DataOutputStream(socket.getOutputStream());

		//recieve the player token
		playerToken = fromServer.readChar();
		if (playerToken == PLAYER_TWO) {
			isWaitingForOpponent = true;
		}
		//write name to server
		//toServer.writeUTF(playerName);

//		if (playerToken == PLAYER_TWO) {
//			//read opponent name from server
//			opponentName = fromServer.readUTF();
//		}
	}

//	public void readOpponentName() throws IOException {
//		opponentName = fromServer.readUTF();
//	}
	public void receiveStatusFromServer() throws IOException {
		status = fromServer.readInt();
		log("status recieved from server: " + status);
		isWaitingForOpponent = false;

		switch (status) {
			case CONTINUE -> {
				opponentNextMove = fromServer.readInt();
				log("move recieved from server: " + opponentNextMove);
				isWaitingForAction = true;
			}

			case PLAYER_ONE_WON -> {
				gameOver = true;
				if (playerToken == PLAYER_ONE) {
					playerResult = WIN;
				} else {
					playerResult = LOSE;
					opponentNextMove = fromServer.readInt();
					log("move recieved from server: " + opponentNextMove);
				}
			}
			case PLAYER_TWO_WON -> {
				gameOver = true;
				if (playerToken == PLAYER_ONE) {
					playerResult = LOSE;
					opponentNextMove = fromServer.readInt();

					log("move recieved from server: " + opponentNextMove);
				} else {
					playerResult = WIN;
				}
			}
			case DRAW -> {
				gameOver = true;
				playerResult = DRAW;
				if (playerToken == PLAYER_TWO) {
					opponentNextMove = fromServer.readInt();
					log("move recieved from server: " + opponentNextMove);
				}
			}
		}
	}

	public void waitForPlayerAction() throws InterruptedException {
		while (isWaitingForAction) {
			Thread.sleep(100);
		}
	}

	public int getGameResult() {
		return playerResult;
	}

	public int getOpponentNextMove() {
		return opponentNextMove;
	}

//	public String getOpponentName() {
//		return opponentName;
//	}
	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isWaitingForAction() {
		return isWaitingForAction;
	}

	public boolean isWaitingForOpponent() {
		return isWaitingForOpponent;
	}

	public void sendMoveToServer(int panelIndex) throws IOException {
		toServer.writeInt(panelIndex);
		toServer.flush();
		log("move " + panelIndex + " sent to server.");
		isWaitingForOpponent = true;
		isWaitingForAction = false;
	}

	public char getToken() {
		return playerToken;
	}

	public void getFirstContact() throws IOException {
		fromServer.readInt();
	}

	public void log(String s) {
		System.out.println(new Date() + ": " + s);
	}

}
