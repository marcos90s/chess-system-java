package aplication;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessPosition;
import chess.PartidaXadrez;
import chess.PeçaDeXadrez;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PartidaXadrez partidaXadrez = new PartidaXadrez();
		List<PeçaDeXadrez> captured = new ArrayList<>();
		
		while (!partidaXadrez.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printMatch(partidaXadrez, captured);
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
				
				if(capturedPiece != null) {
					captured.add(capturedPiece);
				}
				
				if(partidaXadrez.getPromocao() != null) {
					System.out.print("Digite uma peça para a promoção (B/C/T/Q): ");
					String type = sc.nextLine().toUpperCase();
					while(!type.equals("B") && !type.equals("C") && !type.equals("T") && !type.equals("Q")) {
						System.out.print("Valor invalido! Digite uma peça para a promoção (B/C/T/Q): ");	
						type = sc.nextLine().toUpperCase();
					}
					partidaXadrez.substituirPeçaPromovida(type);
				}
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
		UI.clearScreen();
		UI.printMatch(partidaXadrez, captured);

	}

}
