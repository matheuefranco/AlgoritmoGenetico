
package agjava2024;

public class AGJava2024 {

    public static void main(String[] args) {
        int populacao = 20;
        double limitePeso = 8;
        int probabilidadeMutacao = 5; // 5%
        int qtdeCruzamentos = 5;
        int numeroGeracoes = 10;
       AlgoritmoGenetico meuAg = 
               new AlgoritmoGenetico(populacao,limitePeso,probabilidadeMutacao,qtdeCruzamentos, numeroGeracoes);
       meuAg.carregaArquivo("dados.csv");
       meuAg.executar();
    }
    
}
