package wstar;

import java.util.List;

public class Ponto {
	
	private int x;
	
	private int y;
	
	private List<Ponto> vizinhos;
	
	private Ponto pontoPai;
	
	private Ponto pontoDestino;
	
	private boolean visitado;	


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Ponto getPontoPai() {
		return pontoPai;
	}

	public void setPontoPai(Ponto pontoPai) {
		this.pontoPai = pontoPai;
	}

	public Ponto getPontoDestino() {
		return pontoDestino;
	}

	public void setPontoDestino(Ponto pontoDestino) {
		this.pontoDestino = pontoDestino;
	}

	public List<Ponto> getVizinhos() {
		return vizinhos;
	}

	public void setVizinhos(List<Ponto> vizinhos) {
		this.vizinhos = vizinhos;
	}

	public boolean isVisitado() {
		return visitado;
	}

	public void setVisitado(boolean visitado) {
		this.visitado = visitado;
	}
	
}
