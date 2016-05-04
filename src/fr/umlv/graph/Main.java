package fr.umlv.graph;

import java.io.IOException;
import java.nio.file.Paths;



public class Main {

	public static void main(String[] args) throws IOException {
		int image[][] = SeamCarving.readpgm(Paths.get("ex3.pgm"));
		int interest[][];
		int imageCoupe[][] ;
		int tabIndice[];
		Graph graph ;
		
		for(int i = 0; i < 50; i++){
			interest = SeamCarving.interest(image);
			graph = Graph.toGraph(interest);
			graph.initGraph(interest);
			graph.calculFlotMax();
			tabIndice = graph.coupeMinimal(interest.length, interest[0].length);
			imageCoupe = SeamCarving.cutImage(image, tabIndice);
			image = imageCoupe;
			System.out.println("Colonne "+ i);
		}
		
		SeamCarving.writepgm(image, "result.pgm");
		System.out.println("Fin!");
	}

}
