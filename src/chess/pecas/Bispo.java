package chess.pecas;

import boardgame.Posição;
import boardgame.Tabuleiro;
import chess.Color;
import chess.PeçaDeXadrez;

public class Bispo extends PeçaDeXadrez {

	public Bispo(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro, color);
	}

	@Override
	public String toString() {
		return "B";
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		
		Posição p = new Posição(0, 0);
		// nw
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() -1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() -1,p.getColuna() -1 );
		}
		if (getTabuleiro().posicaoExiste(p) && temPecaDoOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		// ne
		p.setValues(posicao.getLinha() -1, posicao.getColuna() +1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() -1, p.getColuna() +1);;
		}
		if (getTabuleiro().posicaoExiste(p) && temPecaDoOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// se
		p.setValues(posicao.getLinha() +1, posicao.getColuna() + 1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() +1,p.getColuna() +1);
		}
		if (getTabuleiro().posicaoExiste(p) && temPecaDoOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// sw
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() -1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() +1,p.getColuna() -1);
		}
		if (getTabuleiro().posicaoExiste(p) && temPecaDoOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		return mat;
	}
}
