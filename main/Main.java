package main;

import java.util.List;

import wstar.No;
import wstar.Unidade;
import wstar.WStar;

class Main {

	public static void main(String[] args){
		WStar wStar = new WStar(30,30,true,true);
		
		wStar.preencherTerreno(1,4,4,26,1);
		wStar.preencherTerreno(1,1,5,5,17);
		wStar.preencherTerreno(1,0,25,29,1);
		wStar.preencherTerreno(1,25,10,1,19);
		wStar.preencherTerreno(1,13,5,1,4);
		wStar.preencherTerreno(1,1,15,23,1);
		wStar.preencherTerreno(1,14,10,11,1);
		wStar.preencherTerreno(1,14,10,1,4);
		wStar.preencherTerreno(1,7,11,7,1);
		
		Unidade u = new Unidade() {
			
			@Override
			public boolean bloqueado(int[][][] terrenos, int[][] unidades, int x, int y,int xf,int yf) {
				if(x == xf && y == yf){
					return false;
				}
				if(unidades[x][y] != 0){
					return true;
				}
				if(terrenos[x][y][4] != 0){
					return true;
				}
				return false;
			}
		};
		
		try {
			List<No> caminhos = wStar.irPara(u,29,0,0,27);
			
			int[][] terreno = wStar.getMapa();
			System.out.println(caminhos.size()+" caminhos encontrados");
			System.out.println("###MAPA#####");
			for(int y = 0 ; y < wStar.getNys() ; y++){
				for(int x = 0 ; x < wStar.getNxs() ; x++){
					
					boolean naoescrever = false;
					for(No p : caminhos){
						if(p.getX() == x && p.getY() == y){							
							naoescrever = true;
							System.out.print("P");
							break;
						}
					}
					if(terreno[x][y] != 0 && !naoescrever){
						System.out.print("X");
					}
					else if(!naoescrever){
						System.out.print("-");
					}
					
					
				}
				System.out.print("\r\n");
			}
			
			System.out.println("############");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
