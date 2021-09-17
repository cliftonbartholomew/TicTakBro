/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

/**
 *
 * @author Cliftonb
 */
public interface BroConstants {

	//PLAYER NUMBERS (first message received by client)
	public static final char PLAYER_ONE = 'X';
	public static final char PLAYER_TWO = 'O';

	//GAME STATE (message recieved after each move)
	public static final int CONTINUE = 0;
	public static final int PLAYER_ONE_WON = 1;
	public static final int PLAYER_TWO_WON = 2;
	public static final int DRAW = 3;

	//PLAYER RESULTS
	public static final int WIN = 1;
	public static final int LOSE = 2;

}
