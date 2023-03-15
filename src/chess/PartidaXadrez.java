package chess;

import boardgame.Posição;
import boardgame.Tabuleiro;
import chess.pecas.Rei;
import chess.pecas.Torre;

public class PartidaXadrez {
	private Tabuleiro tabuleiro;
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		setupInicial();
	}
	public PeçaDeXadrez[][] getPecas(){
		PeçaDeXadrez[][] mat = new PeçaDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for(int j = 0; j < tabuleiro.getColunas();j++) {
				mat[i][j] = (PeçaDeXadrez)tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	private void setupInicial() {
		tabuleiro.moverPeca(new Torre(tabuleiro, Color.WHITE), new Posição(2, 1));
		tabuleiro.moverPeca(new Rei(tabuleiro, Color.BLACK),new Posição(0, 4));
		tabuleiro.moverPeca(new Rei(tabuleiro, Color.WHITE),new Posição(7, 4));
	}
}
