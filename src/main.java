import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class main {
	private static int n;
	private static List<List<Integer>> list;
	private static List<List<Boolean>> truth;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Path path=Paths.get("graph.txt");
		try {
			
			BufferedReader reader =Files.newBufferedReader(path);
			
			n=Integer.parseInt(reader.readLine());
			
			list = new ArrayList<List<Integer>>(n);
			truth=new ArrayList<List<Boolean>>(n);
			int[] functions = new int[n];
			System.out.print(n);
			int[][] adj=new int[n][n];
			int[] values=new int[n];
			
			for(int i=0;i<n;i++){
				for (int j=0;j<n;j++){
					
					adj[i][j]=0;
				}
			}
			System.out.println();
			
			
			String line;
			int i=0;
			while (i<n){
				List<Integer> l=new ArrayList<Integer>();
				if((line=reader.readLine())!=null){
				int s=line.indexOf(" ");;
				while((s+1)<line.length()){
					int s1=line.substring(s+1).indexOf(" ");
					s1=s1+s+1;
					l.add(Integer.parseInt(line.substring(s+1, s1)));
					s=s1;
					
				}
				
			}
			i++;
			list.add(l);
			}
			
			i=0;
			while (i<n){
				List<Boolean> l=new ArrayList<Boolean>();
			if((line=reader.readLine())!=null){
				int s=-1;
				while((s+1)<line.length()){
					int s1=line.substring(s+1).indexOf(" ");
					s1=s1+s+1;
					l.add((Integer.parseInt(line.substring(s+1, s1))==1));
					s=s1;
					
				}
			}
			i++;
			truth.add(l);
			}
			System.out.println();
			for( i=0;i<n;i++){
				System.out.println();
				for(int j=0;j<truth.get(i).size();j++){
					if(truth.get(i).get(j))
					System.out.print("1"+" ");
					else
						System.out.print("0"+" ");
				}
			}
			
			construct(values);
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	static int fnc_not(int value){
		int res;
		
		res=1-value;
		
		return res;
		
	}
	
	static int fnc_and(int[] values){
		
		for(int i=0;i<values.length;i++){
			
			if (values[i]==0){
				return 0;
			}
		}
		
		
		
		return 1;
		
	}
	
	static int fnc_or(int[] values){
		for(int i=0;i<values.length;i++){
			
			if (values[i]==1){
				return 1;
			}
		}
		
		
		
		return 0;
		
	}
	
	static int fnc(int[] values,int number){
		int res=-1;
		
		switch(number){
		case 0:{res=fnc_not(values[0]); break;}
		case 1:{res=fnc_and(values); break;}
		case 2:{res=fnc_or(values); break;}
		}
		return res;
	}
	
	static void construct(int[] values){
		int d=(int) Math.pow(2, n);
		String[] table=new String[d];
		
		for(int i=0;i<d;i++){
			table[i]="";
		}
		
		int l=values.length;
		
		for(int i=0;i<d;i++){
			int[] curr=new int[l];
			for (int j=0;j<l;j++){
				int sum=0;
				
				for(int k=0;k<j;k++){
					sum=sum+curr[k]*((int) Math.pow(2, l-k-1));
				}
				
				curr[j]=(int) Math.floor((i-sum)/((int) Math.pow(2, l-j-1)));
				table[i]=table[i]+curr[j];
			}
			
			table[i]=table[i]+":";
			
			for(int j=0;j<n;j++){
				
				int neighbours_number=list.get(j).size();
				List<Integer> neighbours=list.get(j);
				int[] neighbour_values=new int[neighbours_number];
				
				int n_val=0;
				
				for(int k=0;k<neighbours_number;k++){
					int x=0;
					n_val=n_val+curr[neighbours.get(k)-1]*((int) Math.pow(2, k));
				}
				
				int out=0;
				if (truth.get(j).get(n_val))
				out=1;
				
				if (out!=curr[j]){
					for(int k=0;k<n;k++){
						if(k!=j){
							table[i]=table[i]+curr[k];
						}else{
							table[i]=table[i]+out;
						}
					}
					table[i]=table[i]+" ";
				}
				
			}
		}
		
			System.out.println();
			
		
		
		for(int i=0;i<d;i++){
			System.out.println(table[i]);
		}
		
	}

	static void print_graph(int[][] adj,int n){
		for (int j=0;j<n;j++){ 
			for (int i=0;i<n;i++){
				System.out.print(""+adj[j][i]+" ");
			}
			System.out.println();
			}
			System.out.println();
	}
}
