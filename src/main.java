import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;


public class main {
	private int n;
	private static List<Integer> feedback_vertex_set;
	private static List<String> fixed_points;
	private static List<List<String>> attractors;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SinkTest sT=new SinkTest("cycle.txt");
		Graph graph=new Graph("cycle.txt");
		String s="abc";
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
		
		
		
		
		
	}

	
	
	
	
}
