package agjava2024;

import java.io.*;
import java.util.*;

public class AlgoritmoGenetico {
    private int tamPopulacao;
    private int tamCromossomo=0;
    private double capacidade;
    private int probMutacao;
    private int qtdeCruzamentos;
    private int numeroGeracoes;
    private ArrayList<Produto> produtos = new ArrayList<>();
    private ArrayList<ArrayList> populacao = new ArrayList<>();
    private ArrayList<Integer> roletaVirtual = new ArrayList<>();

    public AlgoritmoGenetico(int tamanhoPopulacao, double capacidadeMochila,
                                int probabilidadeMutacao, int qtdeCruzamentos,
                                int numGeracoes){
        this.tamPopulacao = tamanhoPopulacao;
        this.capacidade = capacidadeMochila;
        this.probMutacao = probabilidadeMutacao;
        this.qtdeCruzamentos = qtdeCruzamentos;
        this.numeroGeracoes = numGeracoes;
    }
    
    public void executar(){
        this.criarPopulacao();
        // executar por gerações
        for(int i=0;i<this.numeroGeracoes;i++){
            System.out.println("Geracao: "+i);
           mostraPopulacao();
           operadoresGeneticos();
           novoPopulacao();
        }
        mostrarMochila(this.populacao.get(obterMelhor()));
    }
    
    public void mostraPopulacao(){
         for(int i=0;i<this.tamPopulacao;i++){
            System.out.println("Cromossomo "+i+":"+populacao.get(i));
            System.out.println("Avaliacao:"+fitness(populacao.get(i)));
        }
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
  //--------------------------   
     private void criarPopulacao(){
         for(int i=0;i<this.tamPopulacao;i++)
             this.populacao.add(criarCromossomo());
     }
  //----------------------------------
   private double fitness(ArrayList<Integer> cromossomo){
       double pesoTotal=0, valorTotal=0;
       for(int i=0;i<this.tamCromossomo;i++){
           if(cromossomo.get(i)==1){
               pesoTotal+=produtos.get(i).getPeso();
               valorTotal+=produtos.get(i).getValor();
           }// fim if teste se leva
       }
       if(pesoTotal<=this.capacidade)
           return valorTotal;
       else
           return 0;
   }
   private void gerarRoleta(){
       ArrayList<Double> fitnessIndividuos = new
           ArrayList<>();
       double totalFitness=0;
       for(int i=0;i<this.tamPopulacao;i++){
           fitnessIndividuos.add(i,fitness(this.populacao.get(i)));
           totalFitness+=fitnessIndividuos.get(i);
       }
       System.out.println("Soma total fitness:"+totalFitness);
       //System.out.println("Notas:"+fitnessIndividuos);
       for(int i=0;i<this.tamPopulacao;i++){
           double qtdPosicoes = (fitnessIndividuos.get(i)/totalFitness)*1000;
           for(int j=0;j<=qtdPosicoes;j++)
               roletaVirtual.add(i);
       }// fim for i
       //System.out.println("Roleta Virtual:"+roletaVirtual);
   }// fim gerarRoleta
   
   private int roleta(){
       Random r = new Random();
       int selecionado = r.nextInt(roletaVirtual.size());
       return roletaVirtual.get(selecionado);
   }// fim roleta
   
   private ArrayList cruzamento(){
        ArrayList<Integer> filho1 = new ArrayList(); 
        ArrayList<Integer> filho2 = new ArrayList();
        ArrayList<ArrayList>filhos = new ArrayList();
        ArrayList<Integer> pai1,pai2;
        int indice_pai1, indice_pai2;
        indice_pai1 = roleta(); // selecionados
        indice_pai2 = roleta();
        pai1 = populacao.get(indice_pai1);
        pai2 = populacao.get(indice_pai2);
        Random r = new Random();
        int pos = r.nextInt(this.tamCromossomo); // ponto de corte
        for(int i=0;i<=pos;i++){
            filho1.add(pai1.get(i));
            filho2.add(pai2.get(i));
        }
        for(int i=pos+1;i<this.tamCromossomo;i++){
           filho1.add(pai2.get(i));
           filho2.add(pai1.get(i));
        }
        filhos.add(filho1);
        filhos.add(filho2);
        return filhos;
   }
   
    private void mutacao(ArrayList<Integer> filho){
       Random r = new Random();
       int v = r.nextInt(100);
       if(v<this.probMutacao){
           int ponto = r.nextInt(this.tamCromossomo);
           if(filho.get(ponto)==1)
               filho.set(ponto,0);
           else
               filho.set(ponto,1);
           
           int ponto2 = r.nextInt(this.tamCromossomo);
           if(filho.get(ponto2)==1)
               filho.set(ponto2,0);
           else
               filho.set(ponto2,1);
           
         System.out.println("Ocorreu mutação!");
       }// fim if mutacao     
    }
     private void operadoresGeneticos(){
         ArrayList f1,f2;
         ArrayList<ArrayList> filhos;
         gerarRoleta();
         for(int i=0;i<this.qtdeCruzamentos;i++){
            filhos = cruzamento();
            f1 = filhos.get(0);
            f2 = filhos.get(1);
            mutacao(f1);
            mutacao(f2);
            populacao.add(f1);
            populacao.add(f2);         
         }    
     }
     
      protected int obterPior(){
       int indicePior=0;
         double pior,nota=0;
        pior = fitness((ArrayList)populacao.get(0));
        for(int i=1;i<this.tamPopulacao;i++){
           nota = fitness((ArrayList)populacao.get(i));
           if(nota < pior){
               pior = nota;
               indicePior = i;
            }// fim if
        }// fim for
        return indicePior;   
      }// fim funcao
      
     private void novoPopulacao(){
            for(int i=0;i<this.qtdeCruzamentos;i++){
                populacao.remove(obterPior());
                populacao.remove(obterPior());
            }
     }
     
     protected int obterMelhor(){
       int indiceMelhor=0;
         double melhor,nota=0;
        melhor = fitness((ArrayList)populacao.get(0));
        for(int i=1;i<this.tamPopulacao;i++){
           nota = fitness((ArrayList)populacao.get(i));
           if(nota > melhor){
               melhor = nota;
               indiceMelhor = i;
            }// fim if
        }// fim for
        return indiceMelhor;   
      }// fim funcao
     
       public void mostrarMochila(ArrayList<Integer> resultado){
        System.out.println("Avaliacao do Melhor:"+this.fitness(resultado));
           // percorrer a solução e mostrar os produtos
           System.out.println("Produtos levados na mochilha:");
           for(int i=0;i<resultado.size();i++){
                int leva = resultado.get(i);
                if(leva==1){
                 Produto p;
                 p= produtos.get(i);
                 System.out.println(p.getDescricao() + 
                         " Valor:"+p.getValor()+ " Peso:"+p.getPeso());
              }// fim if
           }// fim for
        
    }}// fim classe

     
     
     

   
