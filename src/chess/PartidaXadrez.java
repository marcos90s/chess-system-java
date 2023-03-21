package chess;

import boardgame.Peça;
import boardgame.Posição;
import boardgame.Tabuleiro;
import chess.pecas.Rei;
import chess.pecas.Torre;

public class PartidaXadrez {
	private Tabuleiro tabuleiro;
	private int turno;
	private Color jogadorAtual;

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Color.WHITE;
		setupInicial();
	}

	public int getTurno() {
		return turno;
	}

	public Color getJogadorAtual() {
		return jogadorAtual;
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

	public boolean[][] movimentosPossiveis(ChessPosition sourcePosition) {
		Posição posição = sourcePosition.toPosition();
		validarSourcePosition(posição);
		return tabuleiro.peca(posição).movimentosPossiveis();
	}

	public PeçaDeXadrez performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Posição source = sourcePosition.toPosition();
		Posição target = targetPosition.toPosition();
		validarSourcePosition(source);
		validarTargetPosition(source, target);
		Peça pecaCapiturada = makeMove(source, target);
		proximoTurno();
		return (PeçaDeXadrez) pecaCapiturada;
	}

	private Peça makeMove(Posição source, Posição target) {
		Peça p = tabuleiro.removerPeca(source);
		Peça capturedPiece = tabuleiro.removerPeca(target);
		tabuleiro.moverPeca(p, target);
		return capturedPiece;
	}

	private void validarSourcePosition(Posição posição) {
		if (!tabuleiro.temUmaPeca(posição)) {
			throw new ChessException("Não tem peça na posição de origem");
		}
		if(jogadorAtual != ((PeçaDeXadrez)tabuleiro.peca(posição)).getColor()) {
			throw new ChessException("A peça escolhida não é sua");
		}
		if (!tabuleiro.peca(posição).temAlgumMovimentoPossivel()) {
			throw new ChessException("Não existe movimentos possiveis para essa peça");
		}
	}

	private void validarTargetPosition(Posição source, Posição target) {
		if (!tabuleiro.peca(source).movimentoPossivel(target)) {
			throw new ChessException("A peça de origem não pode se mover para a posição de destino");
		}
	}
	
	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Color.WHITE) ? Color.BLACK : Color.WHITE;
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
