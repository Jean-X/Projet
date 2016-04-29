package fr.umlv.graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Graph {
  private final ArrayList<ArrayList<Edge>> adjacenyList;
  /*Crée un graph avec vertexCount nombre de sommet*/
  public Graph(int vertexCount) {
    adjacenyList = new ArrayList<>(vertexCount);
    for (int v = 0; v < vertexCount; v++) {
      adjacenyList.add(new ArrayList<>());
    }
  }
/*renvoie le nombre de sommets*/
  public int vertices() {
    return adjacenyList.size();
  }
/*Ajoute dans l'adjacenyList un edge à l'indice d'origine et de destination*/
  public void addEdge(Edge edge) {
    adjacenyList.get(edge.from).add(edge);
    adjacenyList.get(edge.to).add(edge);
  }
/*renvoie la liste des edges dont le sommet vertex est sois l'origine, sois la destination*/
  public Iterable<Edge> adjacent(int vertex) {
    return adjacenyList.get(vertex);
  }
  
/*Renvoie une liste de tout les edges sans doublons*/
  public Iterable<Edge> edges() {
    ArrayList<Edge> list = new ArrayList<>();
    for (int vertex = 0; vertex < vertices(); vertex++) {
      for (Edge edge : adjacent(vertex)) {
        if (edge.to != vertex) {
          list.add(edge);
        }
      }
    }
    return list;
  }
  
  
/*initialise le tableau d'intérêt*/
  public static Graph toGraph(int itr[][]){
	  Graph graph = new Graph((itr.length * itr[0].length) + 2);
	  for(int i = 0; i < itr.length; i++){
		  graph.addEdge(new Edge(itr.length * itr[0].length, itr[0].length*i, 1000, 0));
		  for(int j = 0; j < itr[0].length; j++){
			  if(j+1 < itr[0].length)
				  graph.addEdge(new Edge(itr[0].length*i + j, itr[0].length*i + j + 1, itr[i][j], 0));
			  if(j == itr[0].length - 1)
				  graph.addEdge(new Edge(itr[0].length*i + j, itr[0].length*itr.length + 1, itr[i][j], 0));
			  if(j-1 >= 0)
				  graph.addEdge(new Edge(itr[0].length*i + j, itr[0].length*i + j - 1, 1000, 0));
			  if(j-1 >= 0 && i-1 >= 0)
				  graph.addEdge(new Edge(itr[0].length*i + j, itr[0].length*(i-1) + j - 1, 1000, 0));
			  if(j-1 >= 0 && i+1 < itr.length)
				  graph.addEdge(new Edge(itr[0].length*i + j, itr[0].length*(i+1) + j - 1, 1000, 0));
		  }
	  }
	  return graph;
  }
  
  /* initialise le graphe au minimum*/
  public void initGraph(int itr[][]){
	  Iterable<Edge> iterable = edges();
	  int min;
	  for(int i = 0; i< itr.length; i++){
		  min = itr[i][0];
		  for(int j = 1; j<itr[0].length; j++){
			  if(min > itr[i][j])
				  min = itr[i][j];
		  }
		  for(Edge e : iterable){
			  if(e.from >= itr[0].length * i && e.from <= itr[0].length * i + itr.length && e.capacity < 1000)
				  e.setUsed(min);
		  }
	  }
  }
  
  /* A ameliorer, renvoie la différence entre la capaciter et l'utiliser pour un pont*/
  public int ameliorationResiduelle(ArrayList <Edge> edges, int from, int to){
	  for(Edge e : edges){
		  if(e.from == from && e.to == to)
			  return e.capacity - e.used;
	  }
	  return 0;
  }
  
  /* renvoie la valeur maximum dont on peut augmenter le chemin*/
  public int augmentationMax(int[] pred ){
	  int i = adjacenyList.size() - 1;
	  int tmp;
	  int min = ameliorationResiduelle(adjacenyList.get(pred[i]), pred[i], i);
	  while(i != adjacenyList.size() - 2){
		  if((tmp = ameliorationResiduelle(adjacenyList.get(pred[i]), pred[i], i)) < min)
			  min = tmp;
		i = pred[i];
	  }
	  return min;
  }
  
  /* augmente un edge de la valeur augmentation */
  public void upgradeEdge(int to, int from, int augmentation){
	  for(Edge e : adjacenyList.get(from)){
		  if(e.from == from && e.to == to)
			  e.used += augmentation;
	  }
  }
  
  /* augmente tout les edge d'un graphe par rapport au chemin de pred */
  public void upgradeGraph(int augmentation, int pred[]){
	  int i = adjacenyList.size() - 1;
	  while( i != adjacenyList.size() - 2){
		  upgradeEdge(i, pred[i], augmentation);
		  i = pred[i];
	  }
  }
  
  /* crée le flot max du graphe */
  public int calculFlotMax(){
	  int pred[];
	  pred = verifChemin(); 
	  while(pred[adjacenyList.size() - 1] != -1){
		  upgradeGraph(augmentationMax(pred), pred);
		  pred = verifChemin();
	  }
	  return 0;
  }
  
  /* vérifie s'il existe des chemins existants et renvoie le tableau des predececeurs */
  public int[] verifChemin(){
	  int head;
	  
	  int pred[] = new int[adjacenyList.size()] ;
	  
	  for(int i = 0; i < adjacenyList.size(); i++)
		  pred[i] = -1;

	  ArrayList<Integer> file = new ArrayList<>();
		  
	  file.add( adjacenyList.size() - 2 );
	  while(!file.isEmpty()){
		  
		  head = file.get(0);

		  for( Edge e : adjacent( head )){
			  if(e.from == head && (pred[e.to] == -1) && e.capacity>e.used){
				  file.add(e.to);
				  pred[e.to] = head;
				  if(e.to == adjacenyList.size() - 1)
					  return pred;
			  }
		  }
		  file.remove(0); /*On coupe la tete*/
	  }
	  return pred;
  }
  
  public boolean condition(int indiceCoupe[], int colonne, Edge e){
	  if(e.from/colonne == 0)
		  return true;
	  if(indiceCoupe[e.from/colonne-1] == e.from%colonne)
		  return true;
	  if(indiceCoupe[e.from/colonne-1] == e.from%colonne - 1)
		  return true;
	  if(indiceCoupe[e.from/colonne-1] == e.from%colonne + 1)
		  return true;
	  return false;
  }
  
  /* crée ma coupe minimal en renvoyant les indices à surpimer */
  public int[] coupeMinimal(int ligne, int colonne){
	  int indiceCoupe[] = new int[ligne];
	  for(Edge e: edges()){
		  if(e.used == e.capacity ){
			  indiceCoupe[e.from/colonne] = e.from%colonne;
		  }
	  }
	  return indiceCoupe;
  }
  
  public void writeFile(Path path) throws IOException {
    try(BufferedWriter writer = Files.newBufferedWriter(path);
        PrintWriter printer = new PrintWriter(writer)) {
      
      printer.println("digraph G{");
      for (Edge e : edges()) {
        printer.println(e.from + "->" + e.to + "[label=\"" + e.used + "/" + e.capacity + "\"];");
      }
      printer.println("}");
    }
  }
}
