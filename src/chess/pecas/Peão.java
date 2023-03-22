package chess.pecas;

import boardgame.Posição;
import boardgame.Tabuleiro;
import chess.Color;
import chess.PartidaXadrez;
import chess.PeçaDeXadrez;

public class Peão extends PeçaDeXadrez {

	private PartidaXadrez partidaXadrez;

	public Peão(Tabuleiro tabuleiro, Color color, PartidaXadrez partidaXadrez) {
		super(tabuleiro, color);
		this.partidaXadrez = partidaXadrez;
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posição p = new Posição(0, 0);

		if (getColor() == Color.WHITE) {
			p.setValues(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() - 2, posicao.getColuna());
			Posição p2 = new Posição(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p) && getTabuleiro().posicaoExiste(p)
					&& !getTabuleiro().temUmaPeca(p) && getContadorMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExiste(p) && temPecaDoOponente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExiste(p) && temPecaDoOponente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			// #Movimento especial en passant white
			if (posicao.getLinha() == 3) {
				Posição left = new Posição(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExiste(left) && temPecaDoOponente(left)
						&& getTabuleiro().peca(left) == partidaXadrez.getEnPassantVulnerable()) {
					mat[left.getLinha() - 1][left.getColuna()] = true;
				}
				Posição right = new Posição(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().posicaoExiste(right) && temPecaDoOponente(right)
						&& getTabuleiro().peca(right) == partidaXadrez.getEnPassantVulnerable()) {
					mat[right.getLinha() - 1][right.getColuna()] = true;
				}

			}

		} else {
			p.setValues(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() + 2, posicao.getColuna());
			Posição p2 = new Posição(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p) && getTabuleiro().posicaoExiste(p)
					&& !getTabuleiro().temUmaPeca(p) && getContadorMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExiste(p) && temPecaDoOponente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExiste(p) && temPecaDoOponente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			// #Movimento especial en passant black
			if (posicao.getLinha() == 4) {
				Posição left = new Posição(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExiste(left) && temPecaDoOponente(left)
						&& getTabuleiro().peca(left) == partidaXadrez.getEnPassantVulnerable()) {
					mat[left.getLinha() +1][left.getColuna()] = true;
				}
				Posição right = new Posição(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().posicaoExiste(right) && temPecaDoOponente(right)
						&& getTabuleiro().peca(right) == partidaXadrez.getEnPassantVulnerable()) {
					mat[right.getLinha() +1][right.getColuna()] = true;
				}

			}
		}
		return mat;
	}

	@Override
	public String toString() {
		return "P";
	}
}
