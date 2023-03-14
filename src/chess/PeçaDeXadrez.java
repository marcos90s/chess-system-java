package chess;

import boardgame.Peça;
import boardgame.Tabuleiro;

public class PeçaDeXadrez extends Peça {
	
	private Color color;

	public PeçaDeXadrez(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}	
