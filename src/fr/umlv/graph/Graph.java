package fr.umlv.graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Graph {
  private final ArrayList<ArrayList<Edge>> adjacenyList;

  public Graph(int vertexCount) {
    adjacenyList = new ArrayList<>(vertexCount);
    for (int v = 0; v < vertexCount; v++) {
      adjacenyList.add(new ArrayList<>());
    }
  }

  public int vertices() {
    return adjacenyList.size();
  }

  public void addEdge(Edge edge) {
    adjacenyList.get(edge.from).add(edge);
    adjacenyList.get(edge.to).add(edge);
  }

  public Iterable<Edge> adjacent(int vertex) {
    return adjacenyList.get(vertex);
  }

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
  
  public void initGraph(){
	 Iterable<Edge> iterable = edges();
	 for(Edge e : iterable){
			 System.out.println(e.from +" "+ e.to + " " + e.capacity);
			 
	 }
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
