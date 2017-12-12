/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedbackarcsetmejorado;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author juan
 */
public class FeedbackArcSetMejorado {

    /**
     * @param args the command line arguments
     */
    static int[] visitados;
    static int[] enfila;
    static int Vertices;
    static int[][] Adj;
    static boolean sw;
    static boolean sw2;
    static Set<List<Integer>> fbarcset = new HashSet<>();
    static List<List<Integer>> FBS = new ArrayList<List<Integer>>();
    static String dir = "Grafo/grafo7.txt";
    static int cont = 0;
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        Scanner sc = new Scanner(System.in);
        System.out.println("");
        BufferedReader textReader;
        //int K = 2;
        try{
            textReader = new BufferedReader(new FileReader(dir));
            Vertices = 0;
            while(textReader.readLine() != null){
                Vertices++;
            }
            textReader.close();
            Adj = new int[Vertices][Vertices];
            textReader = new BufferedReader(new FileReader(dir));
            for(int i = 0; i < Vertices; i++){
                StringTokenizer st = new StringTokenizer(textReader.readLine());
                for(int j = 0; j < Vertices; j++){
                    Adj[i][j] =  Integer.parseInt(st.nextToken());
                }
            }
            System.out.println("El grafo ingresado tiene la siguiente matriz de adyacencia: ");
            Graficar();
            System.out.println("Digite el valor de K ");
            int K = sc.nextInt();
            long ini = System.currentTimeMillis();
            //visitados = new int[Vertices];
            //enfila = new int[Vertices];
            for(int z = 0; z < Vertices; z++){
                Reset();
                EsCiclico(z);
                
            }
            
            if(sw){
                System.out.println("El grafo es ciclico \n");
            }else{
                System.out.println("El grafo no es ciclico \n");
            }
            Reset();
            for(int z = 0; z < Vertices; z++){
                CuentaCiclos(z, new ArrayList<Integer>());
            }
            //System.out.println("cont: "+cont);
            sw = false;
            Reset();
            System.out.println("Total de aristas en ciclos detectadas: "+FBS.size());
            
            CortaCiclos(1,K, new int[FBS.size()]);
            
            if(sw2){
                System.out.println("RESULTADO: Existe un Feedback Arc Set con cardinalidad menor o igual a "+ K);
            }else{
                System.out.println("RESULTADO: No existe un Feedback Arc Set con cardinalidad menor o igual a "+ K);
            }
            System.out.println("Tiempo total: "+(System.currentTimeMillis()-ini));
        }catch(Exception e){
            
            System.out.println(e);
        }
    }
    
    public static void EsCiclico(int v){
        
        visitados[v] = 1;
        enfila[v] = 1;
        for(int i = 0; i < Vertices; i++){
            cont++;
            if(Adj[v][i] == 1){
                if(visitados[i] == 0){
                    EsCiclico(i);
                }else if(enfila[i] == 1){
                    sw = true;
                    return;
                }
            }
        }
        enfila[v] = 0;
    }
    
    public static void Reset(){
        visitados = new int[Vertices];
        enfila = new int[Vertices];
    }
    
    public static void CuentaCiclos(int v, ArrayList<Integer> cic){
        cont++;
        visitados[v] = 1;
        enfila[v] = 1;
        cic.add(v);
        for(int i = 0; i < Vertices; i++){
            if(Adj[v][i] == 1){
                if(visitados[i] == 0){
                    CuentaCiclos(i, cic);
                }else if(enfila[i] == 1){
                    ArrayList<Integer> in = new ArrayList<Integer>(cic);
                    in.add(i);
                    Aristas(in);
                }
            }
        }
        enfila[v] = 0;
    }
    
    
    public static void Aristas(ArrayList<Integer> in){
        int ini = in.indexOf(in.get(in.size()-1));
        for(int i = ini; i < in.size()-1; i++){
            List<Integer> S = Arrays.asList(in.get(i),in.get(i+1));
            boolean is = false;
            for(int j = 0; j < FBS.size(); j++){
                if(FBS.get(j).equals(S)){
                    is = true;
                }
            }
            if(!is){
                FBS.add(S);
            }
            
        }
    }
    
    public static void CortaCiclos(int nivel, int K, int[] pass){
        
        if(nivel <= K && K <= FBS.size()){
            for(int i = 0; i < FBS.size(); i++){
                if(pass[i] == 0 && sw2 == false){
                    Adj[FBS.get(i).get(0)][FBS.get(i).get(1)] = 0;
                    sw = false;
                    //long t1 = System.currentTimeMillis();
                    for(int z = 0; z < Vertices; z++){
                        Reset();
                        EsCiclico(z);
                    }
                    //tDFS += System.currentTimeMillis()-t1;
                    if(!sw){
                        sw2 = true;
                        System.out.println("Feedback Arc Set con cardinalidad "+nivel);
                        Graficar();
                    }
                    int[] pass2 = Arrays.copyOf(pass, pass.length);
                    pass2[i] = 1;
                    CortaCiclos(nivel+1, K, pass2);
                    Adj[FBS.get(i).get(0)][FBS.get(i).get(1)] = 1;
                }
            }
        }
        
    }
    
    public static void Graficar(){
        for(int i = 0; i < Vertices; i++){
            for(int j = 0; j < Vertices; j++){
                System.out.print(Adj[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
}
