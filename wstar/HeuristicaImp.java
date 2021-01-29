package wstar;

public class HeuristicaImp implements HeuristicaI{

	@Override
	public float calcularHeuristica(int xAtual, int yAtual, int xDestino,
			int yDestino) {
		 float dx = xDestino - xAtual;
         float dy = yDestino - yAtual;  
       
         
         float resultado = (float) (dx*dx)+(dy*dy);
         
         
         return resultado;
	}

}
