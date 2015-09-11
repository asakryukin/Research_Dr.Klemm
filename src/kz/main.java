package kz;
import java.util.ArrayList;
import java.util.List;

import cellularAutomata.CreateCellularAutomataDataset;
import newAlgorithm.NewAlg;
import datasetcreator.CreateTestingSet;


public class main {
	private int n;
	private static List<Integer> feedback_vertex_set;
	private static List<String> fixed_points;
	private static List<List<String>> attractors,attractorsSink,attractorNew;
	private static int trials=0,successfulTrials=0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int numberOfNodes=9;
		{
				trials++;
				//CreateTestingSet CT=new CreateTestingSet("dataset.txt", numberOfNodes, 2);
				//CT.generateFile();
			while(true){
				try{
				CreateCellularAutomataDataset CCAD=new CreateCellularAutomataDataset(numberOfNodes, 254, "cell.txt");
				CCAD.generateFile();
				List<String> nei=new ArrayList<String>();
				
				
				
				Graph graph=new Graph("cell.txt");
				
				List<String> test=new ArrayList<String>();
				
				//test=sT.findNeighbours("110");
				//Finding the FVS
				feedback_vertex_set=graph.feedbackFertexSet();
				
				System.out.println("Feedback Fertex Set:");
				for(int i=0;i<feedback_vertex_set.size();i++){
					System.out.print(""+(feedback_vertex_set.get(i)+1)+"\t");
				}
				System.out.println();
				
				fixed_points=graph.fixedPoints();
				
				System.out.println("Fixed Points:");
				for(int i=0;i<fixed_points.size();i++){
					System.out.print(""+fixed_points.get(i)+"\t");
				}
				System.out.println();
				attractors=graph.attractors();
				attractors.sort(new AttractorComparator());
				for(int i=0;i<attractors.size();i++){
					System.out.println("Attractor #"+(i+1));
					attractors.get(i).sort(new AttractorElementComparator());
					for(int j=0;j<attractors.get(i).size();j++){
						System.out.print(""+attractors.get(i).get(j)+"\t");
					}
					System.out.println();
				}
				System.out.println();
				
				//Finding fixed points
				
				
				//Finding attractors
				System.out.println();
				numberOfNodes++;
				}catch(StackOverflowError e){
					System.out.println();
					System.out.println(""+numberOfNodes);
					break;
				}
				
			}
			numberOfNodes=9;
			while(true){
				try{
					CreateCellularAutomataDataset CCAD=new CreateCellularAutomataDataset(numberOfNodes, 254, "cell.txt");
					CCAD.generateFile();
					SinkTest sT=new SinkTest("cell.txt");
					attractorsSink=sT.getAttractors();
				}catch(StackOverflowError e){
					System.out.println();
					System.out.println(""+numberOfNodes);
					break;
				}catch(OutOfMemoryError e){
					System.out.println();
					System.out.println(""+numberOfNodes);
					break;
				}
				numberOfNodes++;
			}
			numberOfNodes=9;
			while(true){
				try{
					CreateCellularAutomataDataset CCAD=new CreateCellularAutomataDataset(numberOfNodes, 254, "cell.txt");
					CCAD.generateFile();
					NewAlg nA=new NewAlg("cell.txt");
					
					attractorNew=nA.getAttractors();
					for(int i=0; i<attractorNew.size();i++){
						System.out.println("Attractor#"+(i+1)+":");
						for(int j=0;j<attractorNew.get(i).size();j++){
							System.out.print(attractorNew.get(i).get(j)+"\t");
							
						}
						System.out.println();
						System.out.println("Number of states:"+attractorNew.get(i).size());
					}
					/*
					if(attractors.equals(attractorNew)&&attractors.equals(attractorsSink)){
						successfulTrials++;
						System.out.println("EQUAL!");
					}*/
			
					numberOfNodes++;
				}catch(StackOverflowError e){
					System.out.println();
					System.out.println(""+numberOfNodes);
					break;
				}
				
			}
		}
		//System.out.println("Number of tests:"+trials);
		//System.out.println("Successful:"+successfulTrials);
		
	}

	
	
	
	
}
