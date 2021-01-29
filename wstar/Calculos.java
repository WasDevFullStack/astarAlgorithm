package wstar;

import java.util.ArrayList;
import java.util.List;

class Calculos {
	
	public static class Direcao{
		public static final int ESQUERDA_CIMA = 1;
		public static final int CIMA = 2;
		public static final int CIMA_DIREITA = 3;
		public static final int DIREITA = 4;
		public static final int DIREITA_BAIXO = 5;
		public static final int BAIXO = 6;
		public static final int ESQUERDA_BAIXO = 7;
		public static final int ESQUERDA = 8;
		public static final int NONE = 9;
		
		
		public static int ObterDirecao(int x,int y,int xDestino,int yDestino){
			boolean esquerda = x > xDestino;
			boolean direita = x < xDestino;
			boolean cima = y > yDestino;
			boolean baixo = y < yDestino;
			
			if(esquerda && cima){
				return ESQUERDA_CIMA;
			}
			else if(esquerda && baixo){
				return ESQUERDA_BAIXO;
			}else if(direita && baixo){
				return DIREITA_BAIXO;
			}else if(direita && cima){
				return CIMA_DIREITA;
			}else if(cima){
				return CIMA;
			}else if(baixo){
				return BAIXO;
			}else if(esquerda){
				return ESQUERDA;
			}else if(direita){
				return DIREITA;
			}
			
			
			return NONE;
		}
		
	}
	
	
	/**
	 * Registra os vizinhos do ponto passado no parâmetro
	 */
	protected static List<Ponto> registrarPontosVizinhos(Ponto pontoPai,int nxs,int nys,int[][] visitados,boolean diagonal){
		List<Ponto> pontos = new ArrayList<Ponto>();
		
		int x = pontoPai.getX();
		int y = pontoPai.getY();
		
		if(isCoord(nxs, nys, x, y)){
			
			//Esquerda
			if(isCoord(nxs, nys, x - 1, y)){
				Ponto ponto = new Ponto();
				ponto.setPontoPai(pontoPai);
				ponto.setPontoDestino(pontoPai.getPontoDestino());
				ponto.setX(x - 1);
				ponto.setY(y);
				ponto.setVisitado(visitados[ponto.getX()][ponto.getY()] == 1 ? true : false);
				pontos.add(ponto);		
			}
			//Esquerda cima
			if(isCoord(nxs, nys, x - 1, y - 1) && diagonal){
				Ponto ponto = new Ponto();
				ponto.setPontoPai(pontoPai);
				ponto.setPontoDestino(pontoPai.getPontoDestino());
				ponto.setX(x - 1);
				ponto.setY(y - 1);
				ponto.setVisitado(visitados[ponto.getX()][ponto.getY()] == 1 ? true : false);
				pontos.add(ponto);
			}
			//Cima
			if(isCoord(nxs, nys, x, y - 1)){
				Ponto ponto = new Ponto();
				ponto.setPontoPai(pontoPai);
				ponto.setPontoDestino(pontoPai.getPontoDestino());
				ponto.setX(x);
				ponto.setY(y - 1);
				ponto.setVisitado(visitados[ponto.getX()][ponto.getY()] == 1 ? true : false);
				pontos.add(ponto);
			}
			//Direita cima
			if(isCoord(nxs, nys, x + 1, y - 1) && diagonal){
				Ponto ponto = new Ponto();
				ponto.setPontoPai(pontoPai);
				ponto.setPontoDestino(pontoPai.getPontoDestino());
				ponto.setX(x + 1);
				ponto.setY(y - 1);
				ponto.setVisitado(visitados[ponto.getX()][ponto.getY()] == 1 ? true : false);
				pontos.add(ponto);
			}
			//Direita
			if(isCoord(nxs, nys, x + 1, y)){
				Ponto ponto = new Ponto();
				ponto.setPontoPai(pontoPai);
				ponto.setPontoDestino(pontoPai.getPontoDestino());
				ponto.setX(x + 1);
				ponto.setY(y);
				ponto.setVisitado(visitados[ponto.getX()][ponto.getY()] == 1 ? true : false);
				pontos.add(ponto);
			}
			//Direita baixo
			if(isCoord(nxs, nys, x + 1, y + 1) && diagonal){
				Ponto ponto = new Ponto();
				ponto.setPontoPai(pontoPai);
				ponto.setPontoDestino(pontoPai.getPontoDestino());
				ponto.setX(x + 1);
				ponto.setY(y + 1);
				ponto.setVisitado(visitados[ponto.getX()][ponto.getY()] == 1 ? true : false);
				pontos.add(ponto);
			}
			//Baixo
			if(isCoord(nxs, nys, x, y + 1)){
				Ponto ponto = new Ponto();
				ponto.setPontoPai(pontoPai);
				ponto.setPontoDestino(pontoPai.getPontoDestino());
				ponto.setX(x);
				ponto.setY(y + 1);
				ponto.setVisitado(visitados[ponto.getX()][ponto.getY()] == 1 ? true : false);
				pontos.add(ponto);
			}
			//Esquerda Baixo
			if(isCoord(nxs, nys, x - 1, y + 1) && diagonal){
				Ponto ponto = new Ponto();
				ponto.setPontoPai(pontoPai);
				ponto.setPontoDestino(pontoPai.getPontoDestino());
				ponto.setX(x - 1);
				ponto.setY(y + 1);
				ponto.setVisitado(visitados[ponto.getX()][ponto.getY()] == 1 ? true : false);
				pontos.add(ponto);
			}			
			return pontos;
		}
		
		return pontos;
	}
	
	/**
	 * Pega o ponto que está mais próximo do ponto destino e que não já tenha sido visitado antes, de acordo com a heuristica passada
	 */
	protected static Ponto obterPontoMaisProximoNaoVisitado(List<Ponto> pontos,int xDestino,int yDestino, HeuristicaI heuristica){
		Ponto pontoMaisProximo = null;
		float distancia = 0;
		float distanciaP = 0;
		
		for(Ponto p : pontos){
			if(!p.isVisitado()){
				if(pontoMaisProximo == null){
					pontoMaisProximo = p;
					distancia = heuristica.calcularHeuristica(pontoMaisProximo.getX(),pontoMaisProximo.getY(),xDestino,yDestino);
				}else{
					distanciaP = heuristica.calcularHeuristica(p.getX(),p.getY(),xDestino,yDestino);
					if(distanciaP < distancia){
						distancia = distanciaP;
						pontoMaisProximo = p;
					}
				}
			}
		}		
		
		return pontoMaisProximo;
	}
	
	
	
	/**
	 * Obtem o vizinho que mais se adequa a chegada do ponto final
	 */
	protected static Ponto obterVizinhoComPassoMaior(Ponto ponto,int[][] passos){
		List<Ponto> vizinhos = ponto.getVizinhos();
		Ponto pontoPassoMaior = null;
		int passoMaior = 0;
		
		int passo = 0;
		if(vizinhos != null){
			for(Ponto p  : vizinhos){
				passo = passos[p.getX()][p.getY()];
				if(passo != 0 && pontoPassoMaior == null){
					pontoPassoMaior = p;					
					passoMaior = passo;
					
				}else if(passo != 0 && passo > passoMaior){
					pontoPassoMaior = p;
					passoMaior = passo;
				}
			}
		}
		return pontoPassoMaior;
	}
	
	/**
	 * Obtem o vizinho que mais se adequa a chegada do ponto final
	 */
	protected static Ponto obterVizinhoComMaisProximoDoDestino(List<Ponto> caminhos,Ponto ponto,int[][] passos){
		
		Ponto pontoMaisProximo = null;
		float menorDistancia = 0.0f;
		int passoMaior = 0;
		int passo = 0;
		
		for(Ponto p : caminhos){
			if(p != ponto){
				float dx = p.getX() - ponto.getX();
		        float dy = p.getY() - ponto.getY();  
		       
		         
		        float resultado = (float) (dx*dx)+(dy*dy);
		        if(resultado < 2f){
		        	//Verifica se é vizinho dele
		        	
		        	dx = ponto.getPontoDestino().getX() - p.getX();
			        dy = ponto.getPontoDestino().getY() - p.getY();  
			       
			         
			        resultado = (float) (dx*dx)+(dy*dy);
			        
			        passo = passos[p.getX()][p.getY()];
			        
			        
					if(pontoMaisProximo == null){
						pontoMaisProximo = p;
						menorDistancia = resultado;
						passoMaior = passo;
					}else if(resultado < menorDistancia && passo > passoMaior){
						pontoMaisProximo = p;
						menorDistancia = resultado;
						passoMaior = passo;
					}
		        	
		        }
			}
		}
		caminhos.remove(ponto);
		
		/*
		List<Ponto> vizinhos = ponto.getVizinhos();
		Ponto pontoMaisProximo = null;
		float menorDistancia = 0.0f;
		
		if(vizinhos != null){
			for(Ponto p  : vizinhos){
				
				float dx = ponto.getPontoDestino().getX() - p.getX();
		        float dy = ponto.getPontoDestino().getY() - p.getY();  
		       
		         
		        float resultado = (float) (dx*dx)+(dy*dy);
		        
		        
				if(pontoMaisProximo == null){
					pontoMaisProximo = p;
					menorDistancia = resultado;
				}else if(resultado < menorDistancia){
					pontoMaisProximo = p;
					menorDistancia = resultado;
				}
			}
		}
		*/
		return pontoMaisProximo;
	}
	
	
	protected static Ponto obterPonto(int[][] mapa,int x,int y){
		Ponto ponto = new Ponto();
		
		return ponto;
	}
	
	protected static boolean isCoord(int nxs,int nys, int x,int y){
		return x < nxs && x >= 0 && y < nys && y >= 0;
	}
	
	protected static boolean isCoord(int nxs,int nys, int[] xy){
		return xy[0] < nxs && xy[0] >= 0 && xy[1] < nys && xy[1] >= 0;
	}
}

