package chess.pecas;

import boardgame.Tabuleiro;
import chess.Color;
import chess.PeçaDeXadrez;

public class Torre extends PeçaDeXadrez {

	public Torre(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro, color);
	}
	@Override
	public String toString() {
		return "T";
	}
}
