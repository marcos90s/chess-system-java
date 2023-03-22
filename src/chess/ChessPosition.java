package chess;

import boardgame.Posição;

public class ChessPosition {
	private char coluna;
	private int linha;

	public ChessPosition(char coluna, int linha) {
		if (coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8) {
			throw new ChessException("Erro iniciando ChessPosition. Valores válidos: a1 à h8.");
		}
		this.coluna = coluna;
		this.linha = linha;
	}

	public char getColuna() {
		return coluna;
	}

	public int getLinha() {
		return linha;
	}

	protected Posição toPosition() {
		return new Posição(8 - linha, coluna - 'a');
	}

	protected static ChessPosition fromPosition(Posição posição) {
		return new ChessPosition((char) ('a' + posição.getColuna()), 8 - posição.getLinha());
	}

	@Override
	public String toString() {
		return "" + coluna + linha;
	}
}
