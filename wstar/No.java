package wstar;

import java.util.ArrayList;
import java.util.List;

public class No {
	
	private int[][] mapa;
	
	private int[][] unidades;
	
	private int xt;
	
	private int yt;
	
	private int x;
	
	private int y;
	
	private No noPai;	
	
	private No abertoChecado;
	
	private No fechadoChecado;
	
	private List<No> vizinhos;
	
	private int custo;	
	
	private long f;
	
	private Heuriscita heuriscita;
	
	private Unidade unidade;
	
	public long getF(){
		return f;
	}
	
	public void calculaF(){
		f = this.custo + this.heuriscita.calcularHeuriscita(this.x,this.y,this.xt,this.yt);
	}
	
	public boolean isAberto(int x,int y,List<No> aberto){
		for(No no : aberto){
			if(no.x == x && no.y == y){
				this.abertoChecado = no;
				return true;
			}
		}
		return false;
	}
	
	public boolean isFechado(int x,int y,List<No> fechado){
		for(No no : fechado){
			if(no.x == x && no.y == y){
				this.fechadoChecado = no;
				return true;
			}
		}
		return false;
	}
	

	public List<No> calculaVizinhos(boolean diagonal,boolean hibrido,int tw,int th,int xt,int yt,List<No> aberto,List<No> fechado){
		if(vizinhos.size() > 0){
			return vizinhos;
		}
		
		boolean esquerda = false;
		boolean direita = false;
		boolean cima = false;
		boolean baixo = false;
		
		boolean esquerdaBlock = false;
		boolean direitaBlock = false;
		boolean cimaBlock = false;
		boolean baixoBlock = false;
		
		int mx = 0;
		int my = 0;
		
		int icima = 0;
		int ibaixo = 0;
		int iesquerda = 0;
		int idireita = 0;
		
		if((mx = x - 1) >= 0){			
			esquerda = true;
			if(!unidade.bloqueado(mapa, unidades,mx,y,xt,yt)){
				No n = createNo(heuriscita,unidade,mapa,unidades,mx,y,xt,yt);
				n.custo += this.custo;
				vizinhos.add(n);
			}else{
				esquerdaBlock = true;
			}
			
//			if(!unidade.bloqueado(mapa, unidades,mx,y) && isFechado(mx,y,fechado)){
//				if(isAberto(mx,y,aberto)){
//					
//				}else{
//					No n = createNo(mapa,unidades,mx,y,xt,yt);
//					n.noPai = this;
//					aberto.add(n);
//				}
//			}
		}
		iesquerda = mx;
		if((mx = x + 1) < tw){
			direita = true;
			
			if(!unidade.bloqueado(mapa, unidades,mx,y,xt,yt)){
				No n = createNo(heuriscita,unidade,mapa,unidades,mx,y,xt,yt);
				n.custo += this.custo;
				vizinhos.add(n);
			}else{
				direitaBlock = true;
			}
			
//			if(!unidade.bloqueado(mapa, unidades,mx,y) && isFechado(mx,y,fechado)){
//				if(isAberto(mx,y,aberto)){
//					
//				}else{
//					No n = createNo(mapa,unidades,mx,y,xt,yt);
//					n.noPai = this;
//					aberto.add(n);
//				}
//			}
		}
		idireita = mx;
		if((my = y - 1) >= 0 ){
			cima = true;	
			
			if(!unidade.bloqueado(mapa, unidades,x,my,xt,yt)){
				No n = createNo(heuriscita,unidade,mapa,unidades,x,my,xt,yt);
				n.custo += this.custo;
				vizinhos.add(n);
			}else{
				cimaBlock = true;
			}
			
//			if(!unidade.bloqueado(mapa, unidades,x,my) && isFechado(x,my,fechado)){
//				if(isAberto(x,my,aberto)){
//					
//				}else{
//					No n = createNo(mapa,unidades,x,my,xt,yt);
//					n.noPai = this;
//					aberto.add(n);
//				}
//			}
		}
		icima = my;
		if((my = y + 1) < th){
			baixo = true;
			
			if(!unidade.bloqueado(mapa, unidades,x,my,xt,yt)){
				No n = createNo(heuriscita,unidade,mapa,unidades,x,my,xt,yt);
				n.custo += this.custo;
				vizinhos.add(n);
			}else{
				baixoBlock = true;
			}
			
//			if(!unidade.bloqueado(mapa, unidades,x,my) && isFechado(x,my,fechado)){
//				if(isAberto(x,my,aberto)){
//					
//				}else{
//					No n = createNo(mapa,unidades,x,my,xt,yt);
//					n.noPai = this;
//					aberto.add(n);
//				}
//			}
		}		
		ibaixo = my;
		
		if(esquerda && cima && diagonal){
			
			if(!unidade.bloqueado(mapa, unidades,iesquerda,icima,xt,yt)){
				if(!hibrido){
					No n = createNo(heuriscita,unidade,mapa,unidades,iesquerda,icima,xt,yt);
					n.custo += this.custo;
					vizinhos.add(n);
				}else if(!esquerdaBlock && !cimaBlock){
					No n = createNo(heuriscita,unidade,mapa,unidades,iesquerda,icima,xt,yt);
					n.custo += this.custo;
					vizinhos.add(n);
				}
			}
			
//			if(!unidade.bloqueado(mapa, unidades,iesquerda,icima) && isFechado(iesquerda,icima,fechado)){
//				if(isAberto(iesquerda,icima,aberto)){
//					
//				}else{
//					No n = createNo(mapa,unidades,iesquerda,icima,xt,yt);
//					n.noPai = this;
//					aberto.add(n);
//				}
//			}
		}
		if(esquerda && baixo && diagonal){
			
			if(!unidade.bloqueado(mapa, unidades,iesquerda,ibaixo,xt,yt)){				
				if(!hibrido){
					No n = createNo(heuriscita,unidade,mapa,unidades,iesquerda,ibaixo,xt,yt);
					n.custo += this.custo;
					vizinhos.add(n);
				}
				else if(!esquerdaBlock && !baixoBlock){
					No n = createNo(heuriscita,unidade,mapa,unidades,iesquerda,ibaixo,xt,yt);
					n.custo += this.custo;
					vizinhos.add(n);
				}
				
			}
			
//			if(!unidade.bloqueado(mapa, unidades,iesquerda,ibaixo) && isFechado(iesquerda,ibaixo,fechado)){
//				if(isAberto(iesquerda,ibaixo,aberto)){
//					
//				}else{
//					No n = createNo(mapa,unidades,iesquerda,ibaixo,xt,yt);
//					n.noPai = this;
//					aberto.add(n);
//				}
//			}			
		}
		if(direita && cima && diagonal){		
			
			if(!unidade.bloqueado(mapa, unidades,idireita,icima,xt,yt)){
				if(!hibrido){
					No n = createNo(heuriscita,unidade,mapa,unidades,idireita,icima,xt,yt);
					n.custo += this.custo;
					vizinhos.add(n);
				}
				else if(!direitaBlock && !cimaBlock){
					No n = createNo(heuriscita,unidade,mapa,unidades,idireita,icima,xt,yt);
					n.custo += this.custo;
					vizinhos.add(n);
				}
			}
			
//			if(!unidade.bloqueado(mapa, unidades,idireita,icima) && isFechado(idireita,icima,fechado)){
//				if(isAberto(idireita,icima,aberto)){
//					
//				}else{
//					No n = createNo(mapa,unidades,idireita,icima,xt,yt);
//					n.noPai = this;
//					aberto.add(n);
//				}
//			}
		}
		if(direita && baixo && diagonal){	

			if(!unidade.bloqueado(mapa, unidades,idireita,ibaixo,xt,yt)){
				
				if(!hibrido){
					No n = createNo(heuriscita,unidade,mapa,unidades,idireita,ibaixo,xt,yt);
					n.custo += this.custo;
					vizinhos.add(n);
				}
				else if(!direitaBlock && !baixoBlock){
					No n = createNo(heuriscita,unidade,mapa,unidades,idireita,ibaixo,xt,yt);
					n.custo += this.custo;
					vizinhos.add(n);
				}				
				
			}
			
//			if(!unidade.bloqueado(mapa, unidades,idireita,ibaixo) && isFechado(idireita,ibaixo,fechado)){
//				if(isAberto(idireita,ibaixo,aberto)){
//					
//				}else{
//					No n = createNo(mapa,unidades,idireita,ibaixo,xt,yt);
//					n.noPai = this;
//					aberto.add(n);
//				}
//			}
		}
		
		return vizinhos;
	}
	
	public static No createNo(int[][] mmapa,int[][] munidades,int x, int y,int xt,int yt){
		No no = new No();
		
		no.vizinhos = new ArrayList<No>();
		no.unidades = munidades;
		no.mapa = mmapa;
		no.xt = xt;
		no.yt = yt;
		no.x = x;
		no.y = y;
		no.custo = 10;
		
		no.heuriscita = new Heuriscita() {			
			@Override
			public long calcularHeuriscita(int x1, int y1, int x2, int y2) {			
				
				//Heurística antiga, mais utilizada para movimentos diagonais
				double dis = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
				return Math.round(dis);
				
//				int difx = Math.max(x1,x2) - Math.min(x1,x2); 
//				int dify = Math.max(y1,y2) - Math.min(y1,y2); 
				
//				return (difx + dify) * 10;
			}
		};
		
		no.unidade = new Unidade() {
			
			@Override
			public boolean bloqueado(int[][] mapa,int[][] unidades,int x, int y,int xt,int yt) {
				if(x == xt && y == yt){
					return false;
				}
				if(unidades[x][y] == -1){
					return true;
				}
				return false;
			}
		};
		
		
		
		return no;
	}
	
	public static No createNo(Heuriscita heuristica,Unidade unidade,int[][] mmapa,int[][] munidades,int x, int y,int xt,int yt){
		No no = new No();
		
		no.vizinhos = new ArrayList<No>();
		no.unidades = munidades;		
		no.mapa = mmapa;
		no.xt = xt;
		no.yt = yt;
		no.x = x;
		no.y = y;
		no.custo = 10;		
		no.heuriscita = heuristica;
		no.unidade = unidade;
		
		
		
		
		
		return no;
	}

	public List<No> getVizinhos() {
		return vizinhos;
	}

	public void setVizinhos(List<No> vizinhos) {
		this.vizinhos = vizinhos;
	}

	public int[][] getMapa() {
		return mapa;
	}

	public void setMapa(int[][] mapa) {
		this.mapa = mapa;
	}

	public int[][] getUnidades() {
		return unidades;
	}

	public void setUnidades(int[][] unidades) {
		this.unidades = unidades;
	}

	public int getXt() {
		return xt;
	}

	public void setXt(int xt) {
		this.xt = xt;
	}

	public int getYt() {
		return yt;
	}

	public void setYt(int yt) {
		this.yt = yt;
	}

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

	public No getNoPai() {
		return noPai;
	}

	public void setNoPai(No noPai) {
		this.noPai = noPai;
	}

	public int getCusto() {
		return custo;
	}

	public void setCusto(int custo) {
		this.custo = custo;
	}

	public Heuriscita getHeuriscita() {
		return heuriscita;
	}

	public void setHeuriscita(Heuriscita heuriscita) {
		this.heuriscita = heuriscita;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public No getAbertoChecado() {
		return abertoChecado;
	}

	public void setAbertoChecado(No abertoChecado) {
		this.abertoChecado = abertoChecado;
	}

	public No getFechadoChecado() {
		return fechadoChecado;
	}

	public void setFechadoChecado(No fechadoChecado) {
		this.fechadoChecado = fechadoChecado;
	}
	
	
}
