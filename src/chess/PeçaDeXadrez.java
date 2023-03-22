package chess;

import boardgame.Peça;
import boardgame.Posição;
import boardgame.Tabuleiro;

public abstract class PeçaDeXadrez extends Peça {
	
	private Color color;
	private int contadorMovimento;

	public PeçaDeXadrez(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro);
		this.color = color;
	}

	public Color getColor() {
		return color;
	} 
	
	public int getContadorMovimento() {
		return contadorMovimento;
	}
	
	public void incrementarContadorMovimento() {
		contadorMovimento++;
	}
	
	public void decrementarContadorMovimento() {
		contadorMovimento--;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(posicao); 
	}
	
	protected boolean temPecaDoOponente(Posição posição) {
		PeçaDeXadrez p = (PeçaDeXadrez)getTabuleiro().peca(posição);
		return p != null && p.getColor() != color;
	}
}	
