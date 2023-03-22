package chess.pecas;

import boardgame.Posição;
import boardgame.Tabuleiro;
import chess.Color;
import chess.PartidaXadrez;
import chess.PeçaDeXadrez;

public class Rei extends PeçaDeXadrez {

	private PartidaXadrez partidaXadrez;

	public Rei(Tabuleiro tabuleiro, Color color, PartidaXadrez partidaXadrez) {
		super(tabuleiro, color);
		this.partidaXadrez = partidaXadrez;
	}

	@Override
	public String toString() {
		return "R";
	}

	private boolean podeMover(Posição posição) {
		PeçaDeXadrez p = (PeçaDeXadrez) getTabuleiro().peca(posição);
		return p == null || p.getColor() != getColor();
	}

	private boolean testeRookCastrling(Posição posição) {
		PeçaDeXadrez p = (PeçaDeXadrez) getTabuleiro().peca(posição);
		return p != null && p instanceof Torre && p.getColor() == getColor() && p.getContadorMovimento() == 0;
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posição p = new Posição(0, 0);
		// acima
		p.setValues(posicao.getLinha() - 1, posicao.getColuna());
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// abaixo
		p.setValues(posicao.getLinha() + 1, posicao.getColuna());
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// esquerda
		p.setValues(posicao.getLinha(), posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// direita
		p.setValues(posicao.getLinha(), posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// nw
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// ne
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// sw
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		// se
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// #Movimento especial castling
		if (getContadorMovimento() == 0 && !partidaXadrez.getCheck()) {
			// #Movimento especial Rook do lado do rei
			Posição posT1 = new Posição(posicao.getLinha(), posicao.getColuna() + 3);
			if (testeRookCastrling(posT1)) {
				Posição p1 = new Posição(posicao.getLinha(), posicao.getColuna() + 1);
				Posição p2 = new Posição(posicao.getLinha(), posicao.getColuna() + 2);
				if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null) {
					mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
				}
			}
			// #Movimento especial Rook do lado do Rainha
			Posição posT2 = new Posição(posicao.getLinha(), posicao.getColuna() -4);
			if (testeRookCastrling(posT2)) {
				Posição p1 = new Posição(posicao.getLinha(), posicao.getColuna() - 1);
				Posição p2 = new Posição(posicao.getLinha(), posicao.getColuna() - 2);
				Posição p3 = new Posição(posicao.getLinha(), posicao.getColuna() - 3);
				if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null && getTabuleiro().peca(p3)== null) {
					mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
				}
			}
		}

		return mat;
	}
}
