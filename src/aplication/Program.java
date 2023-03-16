package aplication;

import java.util.Scanner;

import chess.ChessPosition;
import chess.PartidaXadrez;
import chess.PeçaDeXadrez;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PartidaXadrez partidaXadrez = new PartidaXadrez();
		
		while (true) {
			UI.printTabuleiro(partidaXadrez.getPecas());
			System.out.println();
			System.out.print("Source: ");
			ChessPosition source = UI.lerPosicao(sc);
			
			System.out.println();
			System.out.print("Target: ");
			ChessPosition target = UI.lerPosicao(sc);
			
			PeçaDeXadrez capturedPiece = partidaXadrez.performChessMove(source, target);
		}

	}

}
