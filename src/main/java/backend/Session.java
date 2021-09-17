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
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author Cliftonb
 */
public class Session implements Runnable, BroConstants {

	private char[] gridStatus = new char[9];
	//private boolean gameOver;

	//private String nextUpdateMessage = "Player 1's turn\n";
	private Socket playerOneSocket;
	private DataInputStream fromPlayerOne;
	private DataOutputStream toPlayerOne;
	//private String playerOneName;

	private Socket playerTwoSocket;
	private DataInputStream fromPlayerTwo;
	private DataOutputStream toPlayerTwo;
	//private String playerTwoName;

	public Session(Socket playerOneSocket, Socket playerTwoSocket) {
		this.playerOneSocket = playerOneSocket;
		this.playerTwoSocket = playerTwoSocket;
		//this.playerOneName = playerOneName;
		//this.playerTwoName = playerTwoName;

		for (int i = 0; i < gridStatus.length; i++) {
			gridStatus[i] = ' ';
		}
		//gameOver = false;
	}

	private void printGrid() {
		for (int i = 0; i < gridStatus.length; i++) {
			if (i % 3 == 0) {
				System.out.println("");
			}
			System.out.print(gridStatus[i] + " ");
		}
	}

	private boolean isWon(char player) {
		return checkRowsForWinner(player) || checkColsForWinner(player) || checkDiagsForWinner(player);
	}

	private boolean checkRowsForWinner(char player) {
		int[] rowCount = {0, 0, 0};
		for (int i = 0; i < gridStatus.length; i++) {
			if (gridStatus[i] == player) {
				rowCount[i / 3]++;
			}
		}
		for (int i = 0; i < rowCount.length; i++) {
			if (rowCount[i] == 3) {
				return true;
			}
		}
		return false;

	}

	private boolean checkColsForWinner(char player) {
		int[] colCount = {0, 0, 0};
		for (int i = 0; i < gridStatus.length; i++) {
			if (gridStatus[i] == player) {
				colCount[i % 3]++;
			}
		}
		for (int i = 0; i < colCount.length; i++) {
			if (colCount[i] == 3) {
				return true;
			}
		}
		return false;
	}

	private boolean checkDiagsForWinner(char player) {
		if (gridStatus[0] == player && gridStatus[4] == player && gridStatus[8] == player) {
			return true;
		} else if (gridStatus[2] == player && gridStatus[4] == player && gridStatus[6] == player) {
			return true;
		}
		return false;
	}

	private boolean isFull() {
		for (char c : gridStatus) {
			if (c == ' ') {
				return false;
			}
		}
		return true;
	}

	@Override
	public void run() {
		try {
			//create the IOStreams for the players
			fromPlayerOne = new DataInputStream(playerOneSocket.getInputStream());
			toPlayerOne = new DataOutputStream(playerOneSocket.getOutputStream());
			fromPlayerTwo = new DataInputStream(playerTwoSocket.getInputStream());
			toPlayerTwo = new DataOutputStream(playerTwoSocket.getOutputStream());

			//toPlayerOne.writeUTF(playerTwoName);
			//toPlayerTwo.writeUTF(playerOneName);
			//tell player 1 to start
			toPlayerOne.writeInt(-1);

			//game loop
			while (true) {
				//get a move from player 1
				int panelIndexMove = fromPlayerOne.readInt();
				gridStatus[panelIndexMove] = 'X';
				log("Player 1 move: " + panelIndexMove);
				if (isWon(PLAYER_ONE)) {
					//send status to player one
					toPlayerOne.writeInt(PLAYER_ONE_WON);
					//send status to player two
					toPlayerTwo.writeInt(PLAYER_ONE_WON);
					//send move to player two
					toPlayerTwo.writeInt(panelIndexMove);
					break;
				} else if (isFull()) {
					//send status to player one
					toPlayerOne.writeInt(DRAW);
					//send status to player two
					toPlayerTwo.writeInt(DRAW);
					//send move to player two
					toPlayerTwo.writeInt(panelIndexMove);
					//stop the game
					break;
				} else {
					//send status to player two
					toPlayerTwo.writeInt(CONTINUE);
					//send move to player two
					toPlayerTwo.writeInt(panelIndexMove);
				}

				//get a move from player two
				panelIndexMove = fromPlayerTwo.readInt();
				gridStatus[panelIndexMove] = 'O';
				log("player 2 move " + panelIndexMove);
				if (isWon(PLAYER_TWO)) {
					//send status to player one
					toPlayerOne.writeInt(PLAYER_TWO_WON);
					//send status to player two
					toPlayerTwo.writeInt(PLAYER_TWO_WON);
					//send move to player one
					toPlayerOne.writeInt(panelIndexMove);
				} else {
					//send status to player one
					toPlayerOne.writeInt(CONTINUE);
					//send move to player one
					toPlayerOne.writeInt(panelIndexMove);
				}

			}

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void log(String s) throws IOException {
		System.out.println(new Date() + ": " + s);

		PrintWriter textLog = new PrintWriter(new FileWriter("data//serverlog.txt", true));
		textLog.append(new Date() + ": " + s + "\n");
		textLog.close();
	}

}
