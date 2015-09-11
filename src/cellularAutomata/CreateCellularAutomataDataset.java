package cellularAutomata;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import datasetcreator.SimpleNodeComparator;

public class CreateCellularAutomataDataset {
	
	private int numberOfNodes;
	private int rule;
	private String filename;
	
	public CreateCellularAutomataDataset(int n,int r,String f) {
		// TODO Auto-generated constructor stub
		numberOfNodes=n;
		rule=r;
		filename=f;
	}
	
	public void generateFile(){

		try {
			PrintWriter writer = new PrintWriter(filename, "UTF-8");
			writer.print(""+numberOfNodes);
			for (int i=0;i<numberOfNodes;i++){
				writer.println();
				writer.print(""+(i+1)+" ");
				List<Integer> in=new ArrayList<Integer>();
				if(i==0){
					in.add(numberOfNodes);
				}else{
					in.add(i);
				}
				in.add((i+1));
				if(i==numberOfNodes-1){
					in.add(1);
				}else{
					in.add((i+2));
				}
				in.sort(new SimpleNodeComparator());
				
				for(int j=0;j<in.size();j++){
					writer.print(""+in.get(j)+" ");
				}
				
			}
			String s="";
			int t=rule;
			for(int i=0;i<8;i++){
				int tt=t%2;
				t=t/2;
				s=""+tt+" "+s;
				
			}
			for(int i=0;i<numberOfNodes;i++){
				writer.println();
				writer.print(""+s);
				
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
