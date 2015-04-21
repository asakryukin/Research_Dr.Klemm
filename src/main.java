import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


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
				if((line=reader.readLine())!=null && line.length()>2){
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
			if((line=reader.readLine())!=null && line.length()>2){
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
		
		int[][] adj=new int[n][n];
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				adj[i][j]=0;
			}
		}
		int[] neighbours={2};
		int[] in=new int[n];
		/*
		add_node(adj,1,in);
		in[0]=1;
		
		add_node(adj,3,in);
		in[1]=3;
		int[] neighbours2={1,3};
		add_node(adj,2,in);
		in[2]=2;
		//delete_node(adj, 1, in);*/
		
		if(if_there_is_a_cycle(adj)){
			System.out.println("YES");
		}else{
			System.out.println("NO");
		}
		
		List<Integer> feedback_vertex_set=new ArrayList<Integer>();
		int k=0;
		for(int i=0;i<n;i++){
			add_node(adj,i+1,in);
			if(if_there_is_a_cycle(adj)){
				feedback_vertex_set.add(i);
				delete_node(adj, i+1, in);
			}else{
				in[k]=i+1;
				k++;
			}
		}
		
		for(int i=0;i<feedback_vertex_set.size();i++){
			System.out.println(""+(feedback_vertex_set.get(i)+1));
		}
		
	}

	
	static boolean if_there_is_a_cycle(int[][] adj){
		boolean con=true;
		int i=0;
		
		int[] labels=new int[adj.length];
		
		for(int j=0;j<adj.length;j++){
			if(has_nodes(adj,j)){
			labels[j]=0;}
			else{
				labels[j]=-1;
			}
		}
		
		int color=1;
		
		while(i<adj.length ){
			if(labels[i]==0){
				
				if(do_dfs(adj,labels,i,color)){
					return true;
				}else{
					color++;
				}
				
				
			}
			i++;
		}
			return false;
		
	}
	
	static boolean do_dfs(int[][] adj,int[] labels,int node,int color){
		
		
		int i=node;
		
		if(has_nodes(adj, i)){
			for(int j=0;j<adj.length;j++){
				if(adj[node][j]==1){
					if(node==j)
						return true;
					else {
						if(labels[j]==0){
						labels[j]=color;
						if(do_dfs(adj,labels,j,color)){
							return true;
						}
						color++;
						}else if (labels[j]==color){
							return true;
						}
					}
				}
			}
		}
		
		labels[node]=color;
		
		return false;
		
	}
	
	static boolean has_nodes(int[][] adj,int node){
		
		for(int i=0;i<adj.length;i++){
			if(adj[node][i]==1)
				return true;
		}
		return false;
	}
	
	static void add_node(int[][] adj,int node, int[] in){
		
		transpose(adj);
		
		for(int i=0;i<adj.length;i++){
			if(i!=node-1)
			{
			for(int k=0;k<in.length;k++){
				if(in[k]==(i+1)){
					for(int j=0;j<list.get(i).size();j++){
						if(node==list.get(i).get(j)){
							adj[i][node-1]=1;
						}
					}
					break;
				}
			}
			}
		}
		
		for(int i=0;i<list.get(node-1).size();i++){
			for(int j=0;j<in.length;j++){
				if(in[j]==list.get(node-1).get(i)){
					adj[node-1][in[j]-1]=1;
					break;
				}else if((node-1)==list.get(node-1).get(i)-1){
					adj[node-1][node-1]=1;
					break;
				}
			}
		}
		transpose(adj);
	}
	static void delete_node(int[][] adj,int node, int[] in){
		transpose(adj);
		
		for(int i=0;i<adj.length;i++){
			if(i!=node-1)
			{
			for(int k=0;k<in.length;k++){
				if(in[k]==(i+1)){
					for(int j=0;j<list.get(i).size();j++){
						if(node==list.get(i).get(j)){
							adj[i][node-1]=0;
						}
					}
					break;
				}
			}
			}
		}
		
		for(int i=0;i<adj.length;i++){
			
					adj[node-1][i]=0;
					

		}
		transpose(adj);
	}
	
	static void detect_cycles(int[][] adj,int[] in){
		int[] inside=new int[in.length]; 
		for(int i=0;i<in.length;i++){
			inside[i]=in[i];
		}
		
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
				if (list.get(j).size()>0){
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
	public static void transpose(int [][] x){
        int[][] t = new int[x[0].length][x.length];
        for (int i = 0; i < x.length; i++){
            for (int j = 0; j < x[0].length; j++)
                t[i][j] = x[i][j];
        }
        for (int i = 0; i < x.length; i++){
            for (int j = 0; j < x[0].length; j++)
                x[j][i] = t[i][j];
        }
       
    }
}
