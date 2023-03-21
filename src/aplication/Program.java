package aplication;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessPosition;
import chess.PartidaXadrez;
import chess.PeçaDeXadrez;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PartidaXadrez partidaXadrez = new PartidaXadrez();
		
		while (true) {
			try {
				UI.clearScreen();
				UI.printMatch(partidaXadrez);
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.lerPosicao(sc);
				
				boolean[][]movimentosPossiveis = partidaXadrez.movimentosPossiveis(source);
				UI.clearScreen();
				UI.printTabuleiro(partidaXadrez.getPecas(), movimentosPossiveis);				
				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.lerPosicao(sc);
				
				PeçaDeXadrez capturedPiece = partidaXadrez.performChessMove(source, target);
			}
			catch(ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}

	}

}
