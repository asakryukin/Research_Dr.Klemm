package kz;
import java.util.ArrayList;
import java.util.List;

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
		
		for(int numberOfNodes=4;numberOfNodes<10;numberOfNodes++){
			for(int numberOfTests=0;numberOfTests<10;numberOfTests++){
				trials++;
				CreateTestingSet CT=new CreateTestingSet("dataset.txt", numberOfNodes, 3);
				CT.generateFile();
				List<String> nei=new ArrayList<String>();
				
				SinkTest sT=new SinkTest("dataset.txt");
				Graph graph=new Graph("dataset.txt");
				NewAlg nA=new NewAlg("dataset.txt");
				
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
				
				attractorsSink=sT.getAttractors();
				attractorNew=nA.getAttractors();
				if(attractors.equals(attractorNew)&&attractors.equals(attractorsSink)){
					successfulTrials++;
					System.out.println("EQUAL!");
				}
		
			}
		}
		System.out.println("Number of tests:"+trials);
		System.out.println("Successful:"+successfulTrials);
	}

	
	
	
	
}
