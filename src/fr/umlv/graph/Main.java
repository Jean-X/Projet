package fr.umlv.graph;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) throws IOException {
		int image[][] = SeamCarving.readpgm(Paths.get("test.pgm"));
		int interest[][] = SeamCarving.interest(image);
		Graph graph = Graph.toGraph(interest);
		graph.writeFile(FileSystems.getDefault().getPath(".", "access.log"));
		SeamCarving.writepgm(image, "result.pgm");
	}

}
