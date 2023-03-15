package chess;

import boardgame.Posição;

public class ChessPosition {
	private char column;
	private int row;

	public ChessPosition(char column, int row) {
		if (column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException("Erro iniciando ChessPosition. Valores válidos: a1 à h8.");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	protected Posição toPosition() {
		return new Posição(8 - row, column - 'a');
	}

	protected static ChessPosition fromPosition(Posição posição) {
		return new ChessPosition((char) ('a' - posição.getColuna()), 8 - posição.getColuna());
	}

	@Override
	public String toString() {
		return "" + column + row;
	}
}
