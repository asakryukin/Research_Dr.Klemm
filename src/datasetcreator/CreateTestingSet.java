package datasetcreator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateTestingSet {
	
	private String filename;
	private int numberOfNodes;
	private int numberOfInputs;
	
	public CreateTestingSet(String f,int nN, int nI) {
		// TODO Auto-generated constructor stub
		filename=f;
		numberOfNodes=nN;
		numberOfInputs=nI;
	}
	
	public void generateFile(){
		
		try {
			PrintWriter writer = new PrintWriter("dataset.txt", "UTF-8");
			writer.print(""+numberOfNodes);
			for (int i=0;i<numberOfNodes;i++){
				writer.println();
				writer.print(""+(i+1)+" ");
				List<Integer> in=new ArrayList<Integer>();
				for(int j=0;j<numberOfInputs;j++){
					
					int randomNum;
					do{
					 Random rand = new Random();
					 randomNum = rand.nextInt((numberOfNodes - 1) + 1) + 1;
					}while(in.contains(randomNum));
					 in.add(randomNum);
				}
				in.sort(new SimpleNodeComparator());
				
				for(int j=0;j<in.size();j++){
					writer.print(""+in.get(j)+" ");
				}
				
			}
			
			for(int i=0;i<numberOfNodes;i++){
				writer.println();
				for(int j=0;j<Math.pow(2, numberOfInputs);j++){
					Random rand = new Random();
					 int randomNum = rand.nextInt(2);
					 writer.print(""+randomNum+" ");
				}
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
