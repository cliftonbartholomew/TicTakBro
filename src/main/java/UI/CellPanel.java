/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.BroConstants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 * @author Cliftonb
 */
public class CellPanel extends JPanel implements BroConstants {

	private char token = ' ';
	private int cellIndex;

	public CellPanel(int cellIndex) {
		setBorder(new LineBorder(Color.black, 1));
		this.cellIndex = cellIndex;
	}

	public char getToken() {
		return token;
	}

	public void setToken(char token) {
		this.token = token;
		repaint();
	}

	public int getCellIndex() {
		return cellIndex;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));

		int xBuffer = this.getWidth() / 100 * 10;
		int yBuffer = this.getHeight() / 100 * 10;
		switch (this.token) {
			case PLAYER_ONE -> {

				g2.drawLine(xBuffer, yBuffer, this.getWidth() - xBuffer, this.getHeight() - yBuffer);
				g2.drawLine(this.getWidth() - xBuffer, yBuffer, xBuffer, this.getHeight() - yBuffer);
			}
			case PLAYER_TWO -> {
				g2.drawOval(xBuffer, yBuffer, this.getWidth() - xBuffer * 2, this.getHeight() - yBuffer * 2);
			}
			default -> {
			}
		}
	}

}
