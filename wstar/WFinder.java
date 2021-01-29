package wstar;

import java.util.ArrayList;
import java.util.List;

public class WFinder {
	
	private List<No> aberta;;
	
	private List<No> resultado;
	
	private List<No> fechada;
	
	private int[][] mapa;
	
	private int[][] unidades;
	
	private int nxs;
	
	private int nys;
	
	private Heuriscita heuriscita;
	
	private boolean diagonal;
	
	private boolean hibrido;
	
	public WFinder(int nxs,int nys,boolean diagonal,boolean hibrido) {
		this.nxs = nxs;
		this.nys = nys;
		this.mapa = new int[nxs][nys];
		this.unidades = new int[nxs][nys];
		this.diagonal = diagonal;
		this.hibrido = hibrido;
		this.heuriscita = new Heuriscita() {
			
			@Override
			public long calcularHeuriscita(int x1, int y1, int x2, int y2) {			
				double dis = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
				return Math.round(dis);
			}
			
		};
	}
	
	public WFinder(int nxs,int nys,Heuriscita heuristica,boolean diagonal,boolean hibrido){		
		this.nxs = nxs;
		this.nys = nys;
		this.mapa = new int[nxs][nys];	
		this.unidades = new int[nxs][nys];	
		this.diagonal = diagonal;
		this.heuriscita = heuristica;
	}
	
	public List<No> findPath(Unidade unidade,int xi,int yi,int xf,int yf){
		aberta = new ArrayList<No>();
		
		resultado = new ArrayList<No>();
		
		fechada = new ArrayList<No>();
		
		No noAtual = No.createNo(heuriscita,unidade,mapa,unidades,xi,yi,xf,yf);
		noAtual.setCusto(0);
		noAtual.calculaF();
		
		aberta.add(noAtual);		
		
		No vizinhoAberto = null;
		while(true){
			No menorF = null;
			for(No n : aberta){
				if(menorF == null){
					menorF = n;
				}else if(n.getF() < menorF.getF()){
					menorF = n;
				}
			}
			if(menorF == null){
				break;
			}
			
			noAtual = menorF;
			aberta.remove(noAtual);
			fechada.add(noAtual);
			
			
			if(noAtual.getX() == xf && noAtual.getY() == yf){
				resultado.add(noAtual);
				No mNoPai = null;
				while((mNoPai = noAtual.getNoPai()) != null){
					noAtual = mNoPai;
					resultado.add(noAtual);
				}
			
				java.util.Collections.reverse(resultado);
//				if(resultado.size() > 0){
//					resultado.remove(0);
//				}
				return resultado;
			}		
			
			List<No> vizinhos = noAtual.calculaVizinhos(diagonal,hibrido,nxs,nys,xf,yf,aberta,fechada);
			
			if(vizinhos != null && vizinhos.size() > 0){
				
				for(No vizinho : vizinhos){
					int mx = 0;
					int my = 0;
					if(!vizinho.isFechado(mx = vizinho.getX(),my = vizinho.getY(),fechada)){
						if(!vizinho.isAberto(mx,my,aberta)){
							vizinho.calculaF();
							vizinho.setNoPai(noAtual);
							aberta.add(vizinho);							
						}else if(vizinho.getCusto() < (vizinhoAberto = vizinho.getAbertoChecado()).getCusto()){
							vizinho.setNoPai(noAtual);
							vizinhoAberto.setCusto(vizinho.getCusto());
							vizinhoAberto.setNoPai(noAtual);							
							vizinho.calculaF();
							vizinhoAberto.calculaF();
						}
					}
				}				
			}
			
		}	
		return resultado;
	}
	
	
	public static void main(String[] args){
		
		int tw = 5;
		int th = 5;
		
		int[][] mapa = new int[tw][th];
		
		int[][] unidades = new int[tw][th];
		
		//int[][] mapadraw = new int[tw][th];
		
		unidades[2][0] = -1;
		//unidades[2][1] = -1;
		unidades[2][2] = -1;
		unidades[2][3] = -1;
		unidades[4][2] = -1;//Com bloqueio no node destino
		//unidades[2][4] = -1;//sem acesso
		
		
//		mapadraw[2][0] = 5;
		//mapadraw[2][1] = 5;
//		mapadraw[2][2] = 5;
//		mapadraw[2][3] = 5;
		//mapadraw[2][4] = 5;//sem acesso
		
		List<No> aberta = new ArrayList<No>();
		
		List<No> resultado = new ArrayList<No>();
		
		List<No> fechada = new ArrayList<No>();
		
		int xi = 0;
		int yi = 2;
		int xf = 4;
		int yf = 2;
		
		unidades[xi][yi] = 1; // obstáculo no ponto inicial
		
//		mapadraw[xi][yi] = 1;
//		mapadraw[xf][yf] = 6;
		
		boolean diagonal = true;
		
		boolean hibrido = true; // Se diagonal ativa, a diagonal só é processada quando não tem nenhum nó adjacente como obstáculo
		
		No noAtual = No.createNo(mapa,unidades,xi,yi,xf,yf);
		
		noAtual.setCusto(0);
		noAtual.calculaF();
		
		aberta.add(noAtual);		
		
		No vizinhoAberto = null;
		while(true){
			No menorF = null;
			for(No n : aberta){
				if(menorF == null){
					menorF = n;
				}else if(n.getF() < menorF.getF()){
					menorF = n;
				}
			}
			if(menorF == null){
				return;
			}
			
			noAtual = menorF;
			aberta.remove(noAtual);
			fechada.add(noAtual);
			
//			mapadraw[noAtual.getX()][noAtual.getY()] = 2;
			
			if(noAtual.getX() == xf && noAtual.getY() == yf){
				resultado.add(noAtual);
				No mNoPai = null;
//				mapadraw[noAtual.getX()][noAtual.getY()] = 7;
				while((mNoPai = noAtual.getNoPai()) != null){
//					mapadraw[mNoPai.getX()][mNoPai.getY()] = 7;
					noAtual = mNoPai;
					resultado.add(noAtual);
				}
				
				for(int my = 0 ; my < th ; my++){				
					for(int mx = 0 ; mx < tw ; mx++){
//						System.out.print(mapadraw[mx][my]+",");					
					}
					System.out.println("");
				}
				System.out.println("###################");				
				
				java.util.Collections.reverse(resultado);
				if(resultado.size() > 0){
					resultado.remove(0);
				}
				return;
			}		
			
			List<No> vizinhos = noAtual.calculaVizinhos(diagonal,hibrido,tw,th,xf,yf,aberta,fechada);
			
			if(vizinhos != null && vizinhos.size() > 0){
				
				for(No vizinho : vizinhos){
					int mx = 0;
					int my = 0;
					if(!vizinho.isFechado(mx = vizinho.getX(),my = vizinho.getY(),fechada)){
						if(!vizinho.isAberto(mx,my,aberta)){
							vizinho.calculaF();
							vizinho.setNoPai(noAtual);
							aberta.add(vizinho);							
//							mapadraw[vizinho.getX()][vizinho.getY()] = 1;
						}else if(vizinho.getCusto() < (vizinhoAberto = vizinho.getAbertoChecado()).getCusto()){
							vizinho.setNoPai(noAtual);
							vizinhoAberto.setCusto(vizinho.getCusto());
							vizinhoAberto.setNoPai(noAtual);
							
							vizinho.calculaF();
							vizinhoAberto.calculaF();
						}
					}
				}
				
			}
			
			
			for(int my = 0 ; my < th ; my++){				
				for(int mx = 0 ; mx < tw ; mx++){
//					System.out.print(mapadraw[mx][my]+",");					
				}
				System.out.println("");
			}
			System.out.println("###################");
			
		}
		
		
		
	}
	
	

}
