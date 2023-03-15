package chess.pecas;

import boardgame.Tabuleiro;
import chess.Color;
import chess.PeçaDeXadrez;

public class Rei extends PeçaDeXadrez {

	public Rei(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro, color);
	}
	@Override
	public String toString() {
		return "R";
	}
}
