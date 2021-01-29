package wstar;

import java.util.ArrayList;
import java.util.List;


public class WStar {
	
	private int[][][] mapa;
	
	private int[][] unidades;
	
	private long[][] fs;
	
	private int[] noAtual;
	
	private int[] noMenorF;

	private int nxs;
	
	private int nys;	
	
	private Heuriscita heuristica;
	
	private boolean diagonal;
	
	private boolean hibrido;
	
	private int custoLado = 10;
	
	private int custoDiagonal = 10;
	
	private int xInicio;
	
	private int yInicio;
	
	private int xFim;
	
	private int yFim;
	
	private List<int[]> abertosPendentes;
	private List<int[]> abertosPendentesR;
	
	private boolean achouDestino;
	
	public static void main(String[] args){
//		WStar wStar = new WStar(5,5,true,true);//TESTE 2
		
		WStar wStar = new WStar(30,30,true,true);//TESTE 1
		
//		wStar.preencherTerreno(1,2,1,1,3);//TESTE 2
//		wStar.preencherTerreno(1,2,1,3,1);//TESTE 2
		
		wStar.preencherTerreno(1,4,4,26,1);//TESTE 1
		wStar.preencherTerreno(1,1,5,5,17);//TESTE 1
		wStar.preencherTerreno(1,0,25,29,1);//TESTE 1
		wStar.preencherTerreno(1,25,10,1,19);//TESTE 1
		wStar.preencherTerreno(1,13,5,1,4);//TESTE 1
		wStar.preencherTerreno(1,1,15,23,1);//TESTE 1
		wStar.preencherTerreno(1,14,10,11,1);//TESTE 1
		wStar.preencherTerreno(1,14,10,1,4);//TESTE 1
		wStar.preencherTerreno(1,7,11,7,1);//TESTE 1
		
		Unidade u = new Unidade() {
			
			@Override
			public boolean bloqueado(int[][][] terrenos, int[][] unidades, int x, int y,int xf,int yf) {
				if(x == xf && y == yf){
					return false;
				}
				if(unidades[x][y] != 0){
					return true;
				}
				if(terrenos[x][y][4] == 1){
					return true;
				}
				return false;
			}
		};
		
//		wStar.findPath(u,0,2,4,2);//TESTE 2
		wStar.findPath(u,29,0,0,27);//TESTE 1
		
	}
	
	public WStar(int nxs,int nys,boolean diagonal,boolean hibrido){
		this.abertosPendentes = new ArrayList<int[]>();
		this.abertosPendentesR = new ArrayList<int[]>();
		this.hibrido = hibrido;
		this.diagonal = diagonal;
		this.nxs = nxs;
		this.nys = nys;
		this.mapa = new int[nxs][nys][5];//0 = nao_calculado{0},aberto{1},fechado{2}; 1 = G; 2 = xpai ; 3 = ypai, 4 = terreno
		this.unidades = new int[nxs][nys];
		this.fs = new long[nxs][nys];
		this.noAtual = new int[6];//0 = x; 1 = y ; 2 = aberto,fechado ; 3 = G ; 4 = xpai ; 5 = ypai
		this.noMenorF = new int[6];//0 = x; 1 = y ; 2 = aberto,fechado ; 3 = G ; 4 = xpai ; 5 = ypai
		this.heuristica = new Heuriscita() {
			
			@Override
			public long calcularHeuriscita(int x1, int y1, int x2, int y2) {
				double dis = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
				return Math.round(dis);
			}
		};	
	}
	
	public void findPath(Unidade unidade,int xi,int yi,int xf,int yf){
		if(isCoord(xi, yi) && isCoord(xf,yf)){
			achouDestino = false;
			this.xInicio = xi;
			this.yInicio = yi;
			this.xFim = xf;
			this.yFim = yf;
			
			this.mapa[xi][yi][0] = 2;
			this.mapa[xi][yi][1] = 0;
			this.mapa[xi][yi][2] = -1;
			this.mapa[xi][yi][3] = -1;
			this.noAtual[0] = xi;
			this.noAtual[1] = yi;
			this.noAtual[2] = 2;
			this.noAtual[3] = 0;
			this.noAtual[4] = -1;
			this.noAtual[5] = -1;
			this.noMenorF[0] = xi;
			this.noMenorF[1] = yi;
			this.noMenorF[2] = 2;
			this.noMenorF[3] = 0;
			this.noMenorF[4] = -1;
			this.noMenorF[5] = -1;
			this.calculaF(xi,yi,xf,yf);
			
			desenhaMapa();
			
			while(true){			
				
				recalculaVizinhosPeloNoAtual(unidade);
				
				if(achouDestino){
					carregarCaminho();
					return;
				}
				
				calculaMenorF();
				
				int mx = 0;
				int my = 0;
				
				noAtual[0] = (mx = noMenorF[0]);//X
				noAtual[1] = (my = noMenorF[1]);//Y	
				this.mapa[mx][my][0] = 2;//FECHADO
				noAtual[2] = 2;
				noMenorF[2] = 2;
				noAtual[2] = 2;//ABERTO,FECHADO;
				noAtual[3] = this.mapa[mx][(my)][1];//CUSTO
				noAtual[4] = this.mapa[mx][(my)][2];//X PAI
				noAtual[5] = this.mapa[mx][(my)][3];//Y PAI
				
				removeListaAberta();
				
				desenhaMapa();
				
				if(mx == xFim && my == yFim){
					//ACHOU O NÓ DESTINO
					carregarCaminho();
					return;
				}
				if(abertosPendentes.size() <= 0 && abertoParaRemover == null){
					//Não achou o caminho
					return;
				}
				
//				if(this.mapa[mx][(my)][0] == 2){	
//					return;
//					NÃO ACHOU DESTINO
					//Recomeça por outro caminho caso este caminho não dê certo
//					boolean achou = false;
//					int[] pendenteEncontrado = null;
//					if(abertosPendentes.size() > 0){
//						for(int[] p : abertosPendentes){
//							if(this.mapa[p[0]][p[1]][0] == 1){
//								achou = true;
//								pendenteEncontrado = p;
//							}else{
//								abertosPendentesR.add(p);
//							}
//						}
//						for(int[] p : abertosPendentesR){
//							abertosPendentes.remove(p);
//						}
//						abertosPendentesR.clear();
//					}
//					if(achou){
//						//0 = nao_calculado{0},aberto{1},fechado{2}; 1 = G; 2 = xpai ; 3 = ypai, 4 = terreno
//						//0 = x; 1 = y ; 2 = aberto,fechado ; 3 = G ; 4 = xpai ; 5 = ypai
//						int xx = pendenteEncontrado[0];
//						int yy = pendenteEncontrado[1];
//						this.noAtual[0] = xx;
//						this.noAtual[1] = yy;
//						this.noAtual[2] = 1;
//						this.noAtual[3] = this.mapa[xx][yy][1];
//						this.noAtual[4] = this.mapa[xx][yy][2];
//						this.noAtual[5] = this.mapa[xx][yy][3];
//						this.noMenorF[0] = xx;
//						this.noMenorF[1] = yy;
//						this.noMenorF[2] = 1;
//						this.noMenorF[3] = this.mapa[xx][yy][1];
//						this.noMenorF[4] = this.mapa[xx][yy][2];
//						this.noMenorF[5] = this.mapa[xx][yy][3];
//						
//						this.mapa[mx][(my)][0] = 2;//FECHADO
//						
//						noMenorF[2] = 2;
//						noAtual[2] = 2;//ABERTO,FECHADO;
//						
//					}else{
//						//Não encontrou
//						return;
//					}
//				}else{
//					this.mapa[mx][(my)][0] = 2;//FECHADO
//					
//					noMenorF[2] = 2;
//					noAtual[2] = 2;//ABERTO,FECHADO;
//					noAtual[3] = this.mapa[mx][(my)][1];//CUSTO
//					noAtual[4] = this.mapa[mx][(my)][2];//X PAI
//					noAtual[5] = this.mapa[mx][(my)][3];//Y PAI
//				}						
								
				
			}
			
		}		
	}
	
	private void removeListaAberta(){
		abertosPendentes.remove(abertoParaRemover);
	}
	
	private int[] abertoParaRemover;
	private void calculaMenorF(){
		abertoParaRemover = null;
		boolean first = true;
		for(int[] p  : abertosPendentes){
			if(first){
				first = false;
				noMenorF[0] = p[0];
				noMenorF[1] = p[1];
				abertoParaRemover = p;
			}else{
				long f1 = fs[noMenorF[0]][noMenorF[1]];
				long f2 = fs[p[0]][p[1]];
				if(f2 < f1){
					noMenorF[0] = p[0];
					noMenorF[1] = p[1];
					abertoParaRemover = p;
				}
			}
		}
	}
	
	private void recalculaVizinhosPeloNoAtual(Unidade u){
		int x = this.noAtual[0];
		int y = this.noAtual[1];
		
		if(x == 0 && y == 4){
			System.out.println("AQUI");
		}
		if(x == 1 && y == 5){
			System.out.println("AQUI");
		}
		
		int xe = x - 1;
		int xd = x + 1;
		int yc = y - 1;
		int yb = y + 1;
		
		boolean esquerda = (isCoord(xe,y) && !u.bloqueado(mapa, unidades, xe, y,-1,-1));
		boolean direita = (isCoord(xd,y) && !u.bloqueado(mapa, unidades, xd, y,-1,-1));
		boolean cima = (isCoord(x,yc) && !u.bloqueado(mapa, unidades, x, yc,-1,-1));
		boolean baixo = (isCoord(x,yb) && !u.bloqueado(mapa, unidades, x, yb,-1,-1));		
		
		boolean eb = !esquerda;
		boolean db = !direita;
		boolean cb = !cima;
		boolean bb = !baixo;
		
		int v = -1;
		
		if(esquerda){
			if((v = this.mapa[xe][y][0]) == 0){//Nó não calculado
				calculaPrimeiroNo(xe,y,x,y,this.custoLado);				
			}else if(v == 1){//Nó calculado, na lista aberta
				calculaSegundoNo(xe,y,x,y,this.custoLado);				
			}
			if(xe == xFim && y == yFim){
				this.mapa[xe][y][2] = x;
				this.mapa[xe][y][3] = y;
				achouDestino = true;
				noAtual[0] = xe;
				noAtual[0] = y;
				noAtual[4] = x;
				noAtual[5] = y;
				return;
			}
		}
		if(direita){
			if((v = this.mapa[xd][y][0]) == 0){//Nó não calculado
				calculaPrimeiroNo(xd,y,x,y,this.custoLado);				
			}else if(v == 1){//Nó calculado, na lista aberta
				calculaSegundoNo(xd,y,x,y,this.custoLado);
			}
			if(xd == xFim && y == yFim){
				this.mapa[xd][y][2] = x;
				this.mapa[xd][y][3] = y;
				achouDestino = true;
				noAtual[0] = xd;
				noAtual[0] = y;
				noAtual[4] = x;
				noAtual[5] = y;
				return;
			}
		}
		if(cima){
			if((v = this.mapa[x][yc][0]) == 0){//Nó não calculado
				calculaPrimeiroNo(x,yc,x,y,this.custoLado);				
			}else if(v == 1){//Nó calculado, na lista aberta
				calculaSegundoNo(x,yc,x,y,this.custoLado);
			}
			if(x == xFim && yc == yFim){
				this.mapa[x][yc][2] = x;
				this.mapa[x][yc][3] = y;
				achouDestino = true;
				noAtual[0] = x;
				noAtual[0] = yc;
				noAtual[4] = x;
				noAtual[5] = y;
				return;
			}
		}
		if(baixo){
			if((v = this.mapa[x][yb][0]) == 0){//Nó não calculado
				calculaPrimeiroNo(x,yb,x,y,this.custoLado);				
			}else if(v == 1){//Nó calculado, na lista aberta
				calculaSegundoNo(x,yb,x,y,this.custoLado);
			}
			if(x == xFim && yb == yFim){
				this.mapa[x][yb][2] = x;
				this.mapa[x][yb][3] = y;
				achouDestino = true;
				noAtual[0] = x;
				noAtual[0] = yb;
				noAtual[4] = x;
				noAtual[5] = y;
				return;
			}
		}
		
		
		//DIAGONAIS
		if(diagonal){
			if(esquerda && cima && !u.bloqueado(mapa, unidades, xe, yc,-1,-1)){
				if((hibrido && !eb && !cb) || !hibrido){
					if((v = this.mapa[xe][yc][0]) == 0){//Nó não calculado
						calculaPrimeiroNo(xe,yc,x,y,this.custoDiagonal);				
					}else if(v == 1){//Nó calculado, na lista aberta
						calculaSegundoNo(xe,yc,x,y,this.custoDiagonal);
					}
					if(xe == xFim && yc == yFim){
						this.mapa[xe][yc][2] = x;
						this.mapa[xe][yc][3] = y;
						achouDestino = true;
						noAtual[0] = xe;
						noAtual[0] = yc;
						noAtual[4] = x;
						noAtual[5] = y;
						return;
					}
				}
			}
			if(direita && cima && !u.bloqueado(mapa, unidades, xd, yc,-1,-1)){
				if((hibrido && !db && !cb) || !hibrido){
					if((v = this.mapa[xd][yc][0]) == 0){//Nó não calculado
						calculaPrimeiroNo(xd,yc,x,y,this.custoDiagonal);				
					}else if(v == 1){//Nó calculado, na lista aberta
						calculaSegundoNo(xd,yc,x,y,this.custoDiagonal);
					}
					if(xd == xFim && yc == yFim){
						this.mapa[xd][yc][2] = x;
						this.mapa[xd][yc][3] = y;
						achouDestino = true;
						noAtual[0] = xd;
						noAtual[0] = yc;
						noAtual[4] = x;
						noAtual[5] = y;
						return;
					}
				}		
			}
			if(esquerda && baixo && !u.bloqueado(mapa, unidades, xe, yb,-1,-1)){
				if((hibrido && !eb && !bb) || !hibrido){
					if((v = this.mapa[xe][yb][0]) == 0){//Nó não calculado
						calculaPrimeiroNo(xe,yb,x,y,this.custoDiagonal);				
					}else if(v == 1){//Nó calculado, na lista aberta
						calculaSegundoNo(xe,yb,x,y,this.custoDiagonal);
					}
					if(xe == xFim && yb == yFim){
						this.mapa[xe][yb][2] = x;
						this.mapa[xe][yb][3] = y;
						achouDestino = true;
						noAtual[0] = xe;
						noAtual[0] = yb;
						noAtual[4] = x;
						noAtual[5] = y;
						return;
					}
				}
			}
			if(direita && baixo && !u.bloqueado(mapa, unidades, xd, yb,-1,-1)){
				if((hibrido && !db && !bb) || !hibrido){
					if((v = this.mapa[xd][yb][0]) == 0){//Nó não calculado
						calculaPrimeiroNo(xd,yb,x,y,this.custoDiagonal);				
					}else if(v == 1){//Nó calculado, na lista aberta
						calculaSegundoNo(xd,yb,x,y,this.custoDiagonal);
					}
					if(xd == xFim && yb == yFim){
						this.mapa[xd][yb][2] = x;
						this.mapa[xd][yb][3] = y;
						achouDestino = true;
						noAtual[0] = xd;
						noAtual[0] = yb;
						noAtual[4] = x;
						noAtual[5] = y;
						return;
					}
				}
			}
		}
		
	}
	
	private void calculaPrimeiroNo(int x,int y,int xPai,int yPai,int custo){
		this.mapa[x][y][1] = this.mapa[xPai][yPai][1] + custo;//Adiciona o custo com relação ao No atual
		calculaF(x,y,xFim,yFim);//Calcula o F deste nó
		this.mapa[x][y][2] = xPai;//X do nó pai
		this.mapa[x][y][3] = yPai;//Y do nó pai
		this.mapa[x][y][0] = 1;//Seta o nó como aberto
		this.abertosPendentes.add(new int[]{x,y});
		
		
		//Recalcula o menor F para ser obtido na próxima iteração do algorítmo
//		long f1 = this.fs[x][y];
//		long f2 = this.fs[this.noMenorF[0]][this.noMenorF[1]];
//		if(f1 < f2){
//			this.noMenorF[0] = x;
//			this.noMenorF[1] = y;
//		}else if(this.noMenorF[2] == 2){
//			this.noMenorF[0] = x;
//			this.noMenorF[1] = y;
//			this.noMenorF[2] = 1;
//		}
	}
	
	private void calculaSegundoNo(int x,int y,int xPai,int yPai,int custo){
		int custoDoNo = this.mapa[x][y][1];
		int novoCusto = this.mapa[xPai][yPai][1] + custo;
		if(novoCusto < custoDoNo){
			this.mapa[x][y][2] = xPai;//X do nó pai
			this.mapa[x][y][3] = yPai;//Y do nó pai
			this.mapa[x][y][1] = novoCusto;
			calculaF(x,y,xFim,yFim);//Calcula o F deste nó
			
			//Recalcula o menor F para ser obtido na próxima iteração do algorítmo
//			long f1 = this.fs[x][y];
//			long f2 = this.fs[this.noMenorF[0]][this.noMenorF[1]];
//			if(f1 < f2){
//				this.noMenorF[0] = x;
//				this.noMenorF[1] = y;
//			}else if(this.noMenorF[2] == 2){
//				this.noMenorF[0] = x;
//				this.noMenorF[1] = y;
//				this.noMenorF[2] = 1;
//			}			
		}else{
			//Recalcula o menor F para ser obtido na próxima iteração do algorítmo
//			long f1 = this.fs[x][y];
//			long f2 = this.fs[this.noMenorF[0]][this.noMenorF[1]];
//			if(f1 < f2){
//				this.noMenorF[0] = x;
//				this.noMenorF[1] = y;
//			}else if(this.noMenorF[2] == 2){
//				this.noMenorF[0] = x;
//				this.noMenorF[1] = y;
//				this.noMenorF[2] = 1;
//			}
		}
	}
	
	private void calculaF(int x,int y,int xf,int yf){
		this.fs[x][y] = this.mapa[x][y][1] + this.heuristica.calcularHeuriscita(x, y, xf, yf);
	}
	
	private boolean isCoord(int x,int y){
		return (x < nxs && x >= 0 && y < nys && y >= 0);
	}
	
	public void preencherTerreno(int tipoTerreno,int x,int y,int largura,int altura){
		int xT = x + largura;
		int yT = y + altura;
		for (int xp=x ; xp < xT ; xp++) {
			for (int yp=y ; yp < yT ; yp++) {
				mapa[xp][yp][4] = tipoTerreno;
			}
		}
	}
	
	public void limparTerrenos(){
		for(int x = 0 ; x < nxs ; x++){
			for(int y = 0 ; y < nys ; y++){
				mapa[x][y][4] = 0;
			}			
		}
	}
	
	public void preencherUnidade(int tipoUnidade,int x,int y,int largura,int altura){
		int xT = x + largura;
		int yT = y + altura;
		for (int xp=x ; xp < xT ; xp++) {
			for (int yp=y ; yp < yT ; yp++) {
				unidades[xp][yp] = tipoUnidade;
			}
		}
	}
	
	public void limparUnidades(){
		for(int x = 0 ; x < nxs ; x++){
			for(int y = 0 ; y < nys ; y++){
				unidades[x][y] = 0;
			}			
		}
	}
	
	private void desenhaMapa(){
		int mx = noAtual[0];
		int my = noAtual[1];
		System.out.println("##########");
		for(int y = 0 ; y < nys ; y++){
			for(int x = 0 ; x < nxs ; x++){
				if(x == mx && y == my){
					System.out.print("8,");
				}
				else if(mapa[x][y][4] > 0){
					System.out.print("9,");
				}else{
					System.out.print(mapa[x][y][0]+",");
				}				
			}
			System.out.println("");
		}
		System.out.println("##########");
	}
	
	List<int[]> caminhos;
	private int[][] mapaFim;
	private void carregarCaminho(){
		mapaFim = new int[nxs][nys];
		caminhos = new ArrayList<>();
		int xPai = 0;
		int yPai = 0;
		caminhos.add(new int[]{noAtual[0],noAtual[1]});
		mapaFim[noAtual[0]][noAtual[1]] = 1;
		while(((xPai = noAtual[4]) != -1) && ((yPai = noAtual[5]) != -1)){
			mapaFim[xPai][yPai] = 1;
			caminhos.add(new int[]{xPai,yPai});
			noAtual[4] = this.mapa[xPai][yPai][2];
			noAtual[5] = this.mapa[xPai][yPai][3];
		}
		System.out.println("Calculado");
		desenhaMapaLista();
	}
	
	private void desenhaMapaLista(){
		System.out.println("##########");
		for(int y = 0 ; y < nys ; y++){
			abertosPendentes.clear();
			for(int x = 0 ; x < nxs ; x++){
//				if(mapa[x][y][2] != -1){
//					System.out.print("1,");
//				}else if(mapa[x][y][4] > 0){
//					System.out.print("9,");
//				}else{
//					System.out.print("0,");
//				}
				if(mapa[x][y][4] > 0){
					System.out.print("9,");
				}else if(mapaFim[x][y] == 1){
					System.out.print("1,");
				}else{
					System.out.print("0,");
				}
			}
			System.out.println("");
		}
		System.out.println("##########");
	}

}
