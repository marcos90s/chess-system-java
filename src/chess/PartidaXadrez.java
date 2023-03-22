package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Peça;
import boardgame.Posição;
import boardgame.Tabuleiro;
import chess.pecas.Peão;
import chess.pecas.Rei;
import chess.pecas.Torre;

public class PartidaXadrez {
	private Tabuleiro tabuleiro;
	private int turno;
	private Color jogadorAtual;
	private boolean check;
	private boolean checkMate;

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
		
		if(testeCheck(jogadorAtual)) {
			desfazerMove(source, target, pecaCapiturada);
			throw new ChessException("Você não pode se colocar em check");
		}
		check = (testeCheck(oponente(jogadorAtual))) ? true : false;
		if(testeCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}
		else {
		proximoTurno();
		}
		return (PeçaDeXadrez)pecaCapiturada;
	}

	private Peça makeMove(Posição source, Posição target) {
		PeçaDeXadrez p = (PeçaDeXadrez)tabuleiro.removerPeca(source);
		p.incrementarContadorMovimento();
		Peça capturedPiece = tabuleiro.removerPeca(target);
		tabuleiro.moverPeca(p, target);

		if (capturedPiece != null) {
			pecasNoTabuleiro.remove(capturedPiece);
			pecasCapturadas.add(capturedPiece);
		}
		return capturedPiece;
	}

	private void desfazerMove(Posição source, Posição target, Peça pecaCapturada) {
		PeçaDeXadrez p = (PeçaDeXadrez)tabuleiro.removerPeca(target);
		p.decrementarContadorMovimento();
		tabuleiro.moverPeca(p, source);
		if (pecaCapturada != null) {
			tabuleiro.moverPeca(pecaCapturada, target);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
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
		List<Peça> list = pecasNoTabuleiro.stream().filter(x -> ((PeçaDeXadrez)x).getColor() == color).collect(Collectors.toList());
		for (Peça p : list) {
			if (p instanceof Rei) {
				return (PeçaDeXadrez) p;
			}
		}
		throw new IllegalStateException("Não tem o rei " + color + " no tabuleiro");
	}

	private boolean testeCheck(Color color) {
		Posição reiPosicao = rei(color).getChessPosition().toPosition();
		List<Peça> pecasDoOponente = pecasNoTabuleiro.stream().filter(x -> ((PeçaDeXadrez) x).getColor() == oponente(color)).collect(Collectors.toList());
		for (Peça p : pecasDoOponente) {
			boolean[][] mat = p.movimentosPossiveis();
			if (mat[reiPosicao.getLinha()][reiPosicao.getColuna()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testeCheckMate(Color color) {
		if(!testeCheck(color)) {
			return false;
		}
		List<Peça> list = pecasNoTabuleiro.stream().filter(x -> ((PeçaDeXadrez) x).getColor() == color).collect(Collectors.toList());
		for(Peça p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for(int i = 0; i < tabuleiro.getLinhas(); i++) {
				for(int j = 0; j < tabuleiro.getColunas(); j++) {
					if(mat[i][j]) {
						Posição source = ((PeçaDeXadrez)p).getChessPosition().toPosition();
						Posição target = new Posição(i, j);
						Peça capturedPiece = makeMove(source, target);
						boolean testeCheck = testeCheck(color);
						desfazerMove(source, target, capturedPiece);
						if(!testeCheck) {
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
        placeNewPiece('e', 1, new Rei(tabuleiro, Color.WHITE));
        placeNewPiece('h', 1, new Torre(tabuleiro, Color.WHITE));
        placeNewPiece('a', 2, new Peão(tabuleiro, Color.WHITE));
        placeNewPiece('b', 2, new Peão(tabuleiro, Color.WHITE));
        placeNewPiece('c', 2, new Peão(tabuleiro, Color.WHITE));
        placeNewPiece('d', 2, new Peão(tabuleiro, Color.WHITE));
        placeNewPiece('e', 2, new Peão(tabuleiro, Color.WHITE));
        placeNewPiece('f', 2, new Peão(tabuleiro, Color.WHITE));
        placeNewPiece('g', 2, new Peão(tabuleiro, Color.WHITE));
        placeNewPiece('h', 2, new Peão(tabuleiro, Color.WHITE));
        
        placeNewPiece('a', 8, new Torre(tabuleiro, Color.BLACK));
        placeNewPiece('e', 8, new Rei(tabuleiro, Color.BLACK));
        placeNewPiece('h', 8, new Torre(tabuleiro, Color.BLACK));
        placeNewPiece('a', 7, new Peão(tabuleiro, Color.BLACK));
        placeNewPiece('b', 7, new Peão(tabuleiro, Color.BLACK));
        placeNewPiece('c', 7, new Peão(tabuleiro, Color.BLACK));
        placeNewPiece('d', 7, new Peão(tabuleiro, Color.BLACK));
        placeNewPiece('e', 7, new Peão(tabuleiro, Color.BLACK));
        placeNewPiece('f', 7, new Peão(tabuleiro, Color.BLACK));
        placeNewPiece('g', 7, new Peão(tabuleiro, Color.BLACK));
        placeNewPiece('h', 7, new Peão(tabuleiro, Color.BLACK));
	}
}
