package chess;

import boardgame.Tabuleiro;
import chess.pecas.Rei;
import chess.pecas.Torre;

public class PartidaXadrez {
	private Tabuleiro tabuleiro;

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		setupInicial();
	}

	public PeçaDeXadrez[][] getPecas() {
		PeçaDeXadrez[][] mat = new PeçaDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PeçaDeXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}

	private void placeNewPiece(char column, int row, PeçaDeXadrez peca) {
		tabuleiro.moverPeca(peca, new ChessPosition(column, row).toPosition());
	}

	private void setupInicial() {
		placeNewPiece('c', 1, new Torre(tabuleiro, Color.WHITE));
        placeNewPiece('c', 2, new Torre(tabuleiro, Color.WHITE));
        placeNewPiece('d', 2, new Torre(tabuleiro, Color.WHITE));
        placeNewPiece('e', 2, new Torre(tabuleiro, Color.WHITE));
        placeNewPiece('e', 1, new Torre(tabuleiro, Color.WHITE));
        placeNewPiece('d', 1, new Rei(tabuleiro, Color.WHITE));

        placeNewPiece('c', 7, new Torre(tabuleiro, Color.BLACK));
        placeNewPiece('c', 8, new Torre(tabuleiro, Color.BLACK));
        placeNewPiece('d', 7, new Torre(tabuleiro, Color.BLACK));
        placeNewPiece('e', 7, new Torre(tabuleiro, Color.BLACK));
        placeNewPiece('e', 8, new Torre(tabuleiro, Color.BLACK));
        placeNewPiece('d', 8, new Rei(tabuleiro, Color.BLACK));
	}
}
