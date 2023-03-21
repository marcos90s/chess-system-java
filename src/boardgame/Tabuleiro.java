package boardgame;

public class Tabuleiro {
	private int linhas;
	private int colunas;
	private Peça[][] pecas;

	public Tabuleiro(int linhas, int colunas) {
		if (linhas < 1 || colunas < 1) {
			throw new TabuleiroException(
					"Erro criando tabuleiro: é necessário que haja pelo menos uma linha e uma coluna");
		}
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peça[linhas][colunas];
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public Peça peca(int linha, int coluna) {
		if (!posicaoExiste(linha, coluna)) {
			throw new TabuleiroException("Posição inexistente no tabuleiro");
		}
		return pecas[linha][coluna];
	}

	public Peça peca(Posição posição) {
		if (!posicaoExiste(posição)) {
			throw new TabuleiroException("Posição inexistente no tabuleiro");
		}
		return pecas[posição.getLinha()][posição.getColuna()];
	}

	public void moverPeca(Peça peça, Posição posição) {
		if (temUmaPeca(posição)) {
			throw new TabuleiroException("Já tem uma peça nessa posição "+posição);
		}
		pecas[posição.getLinha()][posição.getColuna()] = peça;
		peça.posicao = posição;
	}
	public Peça removerPeca(Posição posição) {
		if(!posicaoExiste(posição)) {
			throw new TabuleiroException("Posição não está no tabuleiro");
		}
		if(peca(posição)== null) {
			return null;
		}
		Peça aux = peca(posição);
		aux.posicao = null;
		pecas[posição.getLinha()][posição.getColuna()] = null;
		return aux;
	}
	
	private boolean posicaoExiste(int linha, int coluna) {
		return linha >= 0 && linha < linhas && coluna >=0 && coluna< colunas;
	}

	public boolean posicaoExiste(Posição posição) {
		return posicaoExiste(posição.getLinha(), posição.getColuna());
	}

	public boolean temUmaPeca(Posição posição) {
		if (!posicaoExiste(posição)) {
			throw new TabuleiroException("Posição inexistente no tabuleiro");
		}
		return peca(posição) != null;
	}
	
	
}
