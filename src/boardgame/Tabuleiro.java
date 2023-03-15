package boardgame;

public class Tabuleiro {
	private int linhas;
	private int colunas;
	private Peça[][] pecas;
	public Tabuleiro(int linhas, int colunas) {
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peça[linhas][colunas];
	}
	public int getLinhas() {
		return linhas;
	}
	public void setLinhas(int linhas) {
		this.linhas = linhas;
	}
	public int getColunas() {
		return colunas;
	}
	public void setColunas(int colunas) {
		this.colunas = colunas;
	}
	public Peça peca(int linha, int coluna) {
		return pecas[linha][coluna];
	}
	public Peça peca(Posição posição) {
		return pecas[posição.getLinha()][posição.getColuna()];
	}
	public void moverPeca(Peça peça, Posição posição) {
		pecas[posição.getLinha()][posição.getColuna()] = peça;
		peça.posicao = posição;
	}
}
