package fr.umlv.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class SeamCarving {
  
	public static int[][] readpgm(Path path) throws IOException {
    
		try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			reader.readLine();  // magic
      
			String line = reader.readLine();
			while (line.startsWith("#")) {
				line = reader.readLine();
			}
      
			Scanner scanner = new Scanner(line);
			int width = scanner.nextInt();
			int height = scanner.nextInt();
      
			line = reader.readLine();
			scanner = new Scanner(line);
			scanner.nextInt();  // maxVal
      
			int[][] im = new int[height][width];
			scanner = new Scanner(reader);
			int count = 0;
			while (count < height * width) {
				im[count / width][count % width] = scanner.nextInt();
				count++;
			}
			return im;
		}
	}
	/* Jean, créer un fichier à partir d'un tableau 2D de "pixel" */
	public static void writepgm(int[][] image, String filename){
		File f = new File(filename);
		try{
			FileWriter fw = new FileWriter (f);
			
			fw.write("P2\n");
			fw.write(String.valueOf(image[0].length)+" "+String.valueOf(image.length)+"\n");
			fw.write(String.valueOf(255)+"\n");
			
			for(int i[] : image){
				for(int j : i)
					fw.write(String.valueOf(j)+" ");
				fw.write("\n");
			}
			fw.close();
		}
		
		catch(IOException exception){
			System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}
	}
	
	/*Jean*/
	public static int[][] interest (int [][] image){
		int inter[][] = new int [image.length][image[0].length];
		for(int i = 0; i<inter.length; i++)
			for(int j = 0; j<inter[i].length; j++){
				if(j == 0)
					inter[i][j] = Math.abs(image[i][j] - image[i][j+1]);
				else if(j+1 == inter[i].length)
					inter[i][j] = Math.abs(image[i][j] - image[i][j-1]);
				else
					inter[i][j] = Math.abs(image[i][j] - (image[i][j+1] + image[i][j-1]) / 2);
			}	
		
		return inter;
	}
	
	public static int[][] cutImage(int[][] imageOriginal, int[] indiceCoupe){
		int[][] newImage = new int[imageOriginal.length][imageOriginal[0].length -1];
		
		for(int i = 0; i < imageOriginal.length; i++){
			int j2 = 0;
			for(int j = 0; j < imageOriginal[0].length; j++){
				
				if (j != indiceCoupe[i]){
					newImage[i][j2] = imageOriginal[i][j];
					j2++;
				}
			}
		}
		return newImage;
		
	}
	
}
