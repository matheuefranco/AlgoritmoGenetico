
package agjava2024;

public class AGJava2024 {

    public static void main(String[] args) {
       AlgoritmoGenetico meuAg = new AlgoritmoGenetico(20);
       meuAg.carregaArquivo("dados.csv");
       meuAg.executar();
    }
    
}
