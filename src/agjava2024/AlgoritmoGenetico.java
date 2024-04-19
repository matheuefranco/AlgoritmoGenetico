package agjava2024;

import java.io.*;
import java.util.*;

public class AlgoritmoGenetico {
    private int tamPopulacao;
    private int tamCromossomo=0;
    private ArrayList<Produto> produtos = new ArrayList<>();
    private ArrayList<ArrayList> populacao = new ArrayList<>();
    
    public AlgoritmoGenetico(int tamanhoPopulacao){
        this.tamPopulacao = tamanhoPopulacao;
    }
    
    public void executar(){
        criarPopulacao();
        System.out.println("População:\n"+this.populacao);
    }
    
    public void carregaArquivo(String fileName){
       String csvFile = fileName;
        String line = "";
        String[] produto = null;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                produto = line.split(",");
                Produto novoProduto = new Produto();
                novoProduto.setDescricao(produto[0]);
                novoProduto.setPeso(Double.parseDouble(produto[1]));
                novoProduto.setValor(Double.parseDouble(produto[2]));
                produtos.add(novoProduto);
                System.out.println(novoProduto);
                this.tamCromossomo++;
            }// fim percurso no arquivo
            
            System.out.println("Tamanho do cromossomo:"+this.tamCromossomo);
           // this.tamCromossomo = desc_items.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     //---------------------
     private ArrayList criarCromossomo(){
         ArrayList<Integer> novoCromossomo = new ArrayList<>();
         for(int i=0;i<this.tamCromossomo;i++){
             if(Math.random()<0.6)
                 novoCromossomo.add(0);
             else
                 novoCromossomo.add(1);
         }// fim for
         return novoCromossomo;
     }
     
     private void criarPopulacao(){
         for(int i=0;i<this.tamPopulacao;i++)
             this.populacao.add(criarCromossomo());
     }

    
}
