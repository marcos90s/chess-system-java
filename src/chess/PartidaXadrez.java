package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Peça;
import boardgame.Posição;
import boardgame.Tabuleiro;
import chess.pecas.Bispo;
import chess.pecas.Cavalo;
import chess.pecas.Peão;
import chess.pecas.Rainha;
import chess.pecas.Rei;
import chess.pecas.Torre;

public class PartidaXadrez {
	private Tabuleiro tabuleiro;
	private int turno;
	private Color jogadorAtual;
	private boolean check;
	private boolean checkMate;
	private PeçaDeXadrez enPassantVulnerable;

	private List<Peça> pecasNoTabuleiro = new ArrayList<>();
	private List<Peça> pecasCapturadas = new ArrayList<>();
 
	
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

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public PeçaDeXadrez getEnPassantVulnerable() {
		return enPassantVulnerable;
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

		if (testeCheck(jogadorAtual)) {
			desfazerMove(source, target, pecaCapiturada);
			throw new ChessException("Você não pode se colocar em check");
		}

		PeçaDeXadrez peçaMovida = (PeçaDeXadrez) tabuleiro.peca(target);

		check = (testeCheck(oponente(jogadorAtual))) ? true : false;
		if (testeCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		} else {
			proximoTurno();
		}
		// Movimento especial en passant
		if (peçaMovida instanceof Peão
				&& (target.getLinha() == source.getLinha() - 2 || target.getLinha() == source.getLinha() + 2)) {
			enPassantVulnerable = peçaMovida;
		} else {
			enPassantVulnerable = null;
		}

		return (PeçaDeXadrez)pecaCapiturada;
	}

	private Peça makeMove(Posição source, Posição target) {
		PeçaDeXadrez p = (PeçaDeXadrez) tabuleiro.removerPeca(source);
		p.incrementarContadorMovimento();
		Peça capturedPiece = tabuleiro.removerPeca(target);
		tabuleiro.moverPeca(p, target);

		if (capturedPiece != null) {
			pecasNoTabuleiro.remove(capturedPiece);
			pecasCapturadas.add(capturedPiece);
		}
		// #Movimento Especial castling rook do lado do rei
		if (p instanceof Rei && target.getColuna() == source.getColuna() + 2) {
			Posição sourceT = new Posição(source.getLinha(), source.getColuna() + 3);
			Posição targetT = new Posição(source.getLinha(), source.getColuna() + 1);
			PeçaDeXadrez torre = (PeçaDeXadrez) tabuleiro.removerPeca(sourceT);
			tabuleiro.moverPeca(torre, targetT);
			torre.incrementarContadorMovimento();
		}
		// #Movimento Especial castling rook do lado do Rainha
		if (p instanceof Rei && target.getColuna() == source.getColuna() - 2) {
			Posição sourceT = new Posição(source.getLinha(), source.getColuna() - 4);
			Posição targetT = new Posição(source.getLinha(), source.getColuna() - 1);
			PeçaDeXadrez torre = (PeçaDeXadrez) tabuleiro.removerPeca(sourceT);
			tabuleiro.moverPeca(torre, targetT);
			torre.incrementarContadorMovimento();
		}

		// Movimento Especial en passant
		if (p instanceof Peão) {
			if (source.getColuna() != target.getColuna() && capturedPiece == null) {
				Posição posicaoPeao;
				if (p.getColor() == Color.WHITE) {
					posicaoPeao = new Posição(target.getLinha() + 1, target.getColuna());
				} else {
					posicaoPeao = new Posição(target.getLinha() - 1, target.getColuna());
				}
				capturedPiece = tabuleiro.removerPeca(posicaoPeao);
				pecasCapturadas.add(capturedPiece);
				pecasNoTabuleiro.remove(capturedPiece);
			}
		}

		return capturedPiece;
	}

	private void desfazerMove(Posição source, Posição target, Peça capturedPiece) {
		PeçaDeXadrez p = (PeçaDeXadrez) tabuleiro.removerPeca(target);
		p.decrementarContadorMovimento();
		tabuleiro.moverPeca(p, source);
		if (capturedPiece != null) {
			tabuleiro.moverPeca(capturedPiece, target);
			pecasCapturadas.remove(capturedPiece);
			pecasNoTabuleiro.add(capturedPiece);
		}
		// #Movimento Especial castling rook do lado do rei
		if (p instanceof Rei && target.getColuna() == source.getColuna() + 2) {
			Posição sourceT = new Posição(source.getLinha(), source.getColuna() + 3);
			Posição targetT = new Posição(source.getLinha(), source.getColuna() + 1);
			PeçaDeXadrez torre = (PeçaDeXadrez) tabuleiro.removerPeca(targetT);
			tabuleiro.moverPeca(torre, sourceT);
			torre.decrementarContadorMovimento();
		}
		// #Movimento Especial castling rook do lado do Rainha
		if (p instanceof Rei && target.getColuna() == source.getColuna() - 2) {
			Posição sourceT = new Posição(source.getLinha(), source.getColuna() - 4);
			Posição targetT = new Posição(source.getLinha(), source.getColuna() - 1);
			PeçaDeXadrez torre = (PeçaDeXadrez) tabuleiro.removerPeca(targetT);
			tabuleiro.moverPeca(torre, sourceT);
			torre.decrementarContadorMovimento();
		}
		// Movimento Especial en passant
		if (p instanceof Peão) {
			if (source.getColuna() != target.getColuna() && capturedPiece == enPassantVulnerable) {
				PeçaDeXadrez peão = (PeçaDeXadrez)tabuleiro.removerPeca(target);
				Posição posicaoPeao;
				if (p.getColor() == Color.WHITE) {
					posicaoPeao = new Posição(3, target.getColuna());
				} else {
					posicaoPeao = new Posição(4, target.getColuna());
				}
				tabuleiro.moverPeca(peão, posicaoPeao);
			}
		}
	}

	private void validarSourcePosition(Posição posição) {
		if (!tabuleiro.temUmaPeca(posição)) {
			throw new ChessException("Não tem peça na posição de origem");
		}
		if (jogadorAtual != ((PeçaDeXadrez) tabuleiro.peca(posição)).getColor()) {
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

	private Color oponente(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;

	}

	private PeçaDeXadrez rei(Color color) {
		List<Peça> list = pecasNoTabuleiro.stream().filter(x -> ((PeçaDeXadrez) x).getColor() == color)
				.collect(Collectors.toList());
		for (Peça p : list) {
			if (p instanceof Rei) {
				return (PeçaDeXadrez) p;
			}
		}
		throw new IllegalStateException("Não tem o rei " + color + " no tabuleiro");
	}

	private boolean testeCheck(Color color) {
		Posição reiPosicao = rei(color).getChessPosition().toPosition();
		List<Peça> pecasDoOponente = pecasNoTabuleiro.stream()
				.filter(x -> ((PeçaDeXadrez) x).getColor() == oponente(color)).collect(Collectors.toList());
		for (Peça p : pecasDoOponente) {
			boolean[][] mat = p.movimentosPossiveis();
			if (mat[reiPosicao.getLinha()][reiPosicao.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testeCheckMate(Color color) {
		if (!testeCheck(color)) {
			return false;
		}
		List<Peça> list = pecasNoTabuleiro.stream().filter(x -> ((PeçaDeXadrez) x).getColor() == color)
				.collect(Collectors.toList());
		for (Peça p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posição source = ((PeçaDeXadrez) p).getChessPosition().toPosition();
						Posição target = new Posição(i, j);
						Peça capturedPiece = makeMove(source, target);
						boolean testeCheck = testeCheck(color);
						desfazerMove(source, target, capturedPiece);
						if (!testeCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void placeNewPiece(char column, int row, PeçaDeXadrez peca) {
		tabuleiro.moverPeca(peca, new ChessPosition(column, row).toPosition());
		pecasNoTabuleiro.add(peca);
	}

	private void setupInicial() {
		placeNewPiece('a', 1, new Torre(tabuleiro, Color.WHITE));
		placeNewPiece('b', 1, new Cavalo(tabuleiro, Color.WHITE));
		placeNewPiece('c', 1, new Bispo(tabuleiro, Color.WHITE));
		placeNewPiece('d', 1, new Rainha(tabuleiro, Color.WHITE));
		placeNewPiece('e', 1, new Rei(tabuleiro, Color.WHITE, this));
		placeNewPiece('f', 1, new Bispo(tabuleiro, Color.WHITE));
		placeNewPiece('g', 1, new Cavalo(tabuleiro, Color.WHITE));
		placeNewPiece('h', 1, new Torre(tabuleiro, Color.WHITE));
		placeNewPiece('a', 2, new Peão(tabuleiro, Color.WHITE, this));
		placeNewPiece('b', 2, new Peão(tabuleiro, Color.WHITE, this));
		placeNewPiece('c', 2, new Peão(tabuleiro, Color.WHITE, this));
		placeNewPiece('d', 2, new Peão(tabuleiro, Color.WHITE, this));
		placeNewPiece('e', 2, new Peão(tabuleiro, Color.WHITE, this));
		placeNewPiece('f', 2, new Peão(tabuleiro, Color.WHITE, this));
		placeNewPiece('g', 2, new Peão(tabuleiro, Color.WHITE, this));
		placeNewPiece('h', 2, new Peão(tabuleiro, Color.WHITE, this));

		placeNewPiece('a', 8, new Torre(tabuleiro, Color.BLACK));
		placeNewPiece('b', 8, new Cavalo(tabuleiro, Color.BLACK));
		placeNewPiece('c', 8, new Bispo(tabuleiro, Color.BLACK));
		placeNewPiece('d', 8, new Rainha(tabuleiro, Color.BLACK));
		placeNewPiece('e', 8, new Rei(tabuleiro, Color.BLACK, this));
		placeNewPiece('f', 8, new Bispo(tabuleiro, Color.BLACK));
		placeNewPiece('g', 8, new Cavalo(tabuleiro, Color.BLACK));
		placeNewPiece('h', 8, new Torre(tabuleiro, Color.BLACK));
		placeNewPiece('a', 7, new Peão(tabuleiro, Color.BLACK, this));
		placeNewPiece('b', 7, new Peão(tabuleiro, Color.BLACK, this));
		placeNewPiece('c', 7, new Peão(tabuleiro, Color.BLACK, this));
		placeNewPiece('d', 7, new Peão(tabuleiro, Color.BLACK, this));
		placeNewPiece('e', 7, new Peão(tabuleiro, Color.BLACK, this));
		placeNewPiece('f', 7, new Peão(tabuleiro, Color.BLACK, this));
		placeNewPiece('g', 7, new Peão(tabuleiro, Color.BLACK, this));
		placeNewPiece('h', 7, new Peão(tabuleiro, Color.BLACK, this));
	}
}
