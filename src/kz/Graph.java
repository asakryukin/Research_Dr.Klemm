package kz;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Graph {
	private int n;
	private List<List<Integer>> list;
	private List<List<Boolean>> truth;
	private List<Integer> feedback_vertex_set;
	private List<String> fixed_points;
	private List<List<String>> attractors;
	public Graph(String p) {
		// TODO Auto-generated constructor stub
		Path path=Paths.get(p); 
		BufferedReader reader;
		try {
			reader = Files.newBufferedReader(path);
			// n - number of nodes
			n=Integer.parseInt(reader.readLine());
			list = new ArrayList<List<Integer>>(n);
			truth=new ArrayList<List<Boolean>>(n);
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
			findFeedbackFertexSet();
			findFixedPoints();
			findAttractors();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public int numberOfNodes(){
		return n;
	}
	
	public List<Integer> feedbackFertexSet(){
		return feedback_vertex_set;
	}
	
	public List<String> fixedPoints(){
		return fixed_points;
	}
	
	public List<List<String>> attractors(){
		return attractors;
	}
	
	private List<Integer> findFeedbackFertexSet() {
		// TODO Auto-generated method stub
		int[][] adj=new int[n][n];
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				adj[i][j]=0;
			}
		}
		int[] in=new int[n];
		
		feedback_vertex_set=new ArrayList<Integer>();
		int k=0;
		List<Integer> used=new ArrayList<Integer>();
		for(int i=0;i<n;i++){
			
			boolean u=false;
			int ii=0;
			
			while(!u){
				u=true;
				ii=((int) Math.round((n-1)*Math.random()));
				for(int j=0;j<used.size();j++){
					if(ii==used.get(j)){
						u=false;
					}
				}
			}
			used.add(ii);
			add_node(adj,ii+1,in);
			if(if_there_is_a_cycle(adj)){
				feedback_vertex_set.add(ii);
				delete_node(adj, ii+1, in);
			}else{
				in[k]=ii+1;
				k++;
			}
		}
		
		feedback_vertex_set.sort(new FVSComparator());
		
		return feedback_vertex_set;
	}
	private List<String> findFixedPoints() {
		// TODO Auto-generated method stub
		
		fixed_points=new ArrayList<String>();
		
		int numberOfStates;
		numberOfStates=(int) Math.pow(2, feedback_vertex_set.size());
		int[] b=new int[feedback_vertex_set.size()];
		
		for(int i=0;i<feedback_vertex_set.size();i++){
			int sum=0;
			for(int j=0;j<truth.get(feedback_vertex_set.get(i)).size();j++){
				if(truth.get(feedback_vertex_set.get(i)).get(j)){
					sum+=1;
				}
			}
			
			float r=((float) sum)/(truth.get(feedback_vertex_set.get(i)).size());
			
			if (r!=0.5){
				b[i]=Math.round(r);
			}else{
				b[i]=(int) Math.round(Math.random());
			}
			
		}
		
		
		for(int i=0;i<numberOfStates;i++){
			int[] state;
			state=produceState(i);
			
			if(isFixed(state,b)){
				String s="";
				for(int j=0;j<state.length;j++){
					s+=state[j];
				}
				fixed_points.add(s);
				continue;
			}
			
			
			
		}
		
		return fixed_points;
	}

	private int[] produceState(int number){
		int[] result=new int[n];
		for(int i=0;i<n;i++){
			result[i]=-1;
		}
		for(int j=0;j<feedback_vertex_set.size();j++){
			result[feedback_vertex_set.get(feedback_vertex_set.size()-j-1)]=(int) (number-(2*Math.floor(number/2)));
					number=(int) Math.floor(number/2);
		}
		
		while(!isStateCompleted(result)){
			fillDependent(result);
		}
		
		return result;
	}
	
	private boolean isStateCompleted(int[] v){
		
		for(int i=0;i<v.length;i++){
			if(v[i]==-1)
				return false;
		}
		
		return true;
		
	}
	
	private void fillDependent(int[] v){
		
		for(int i=0;i<v.length;i++){
			if(v[i]==-1 && isAllDefined(i,v)){
				int ind=0;
				for(int j=0;j<list.get(i).size();j++){
					ind=ind+v[list.get(i).get(j)-1]*((int) Math.pow(2,list.get(i).size()-1-j));
				}
				if(truth.get(i).get(ind)){
					v[i]=1;
				}else{
					v[i]=0;
				}
			}
		}
		
	}
	
	private boolean isAllDefined(int i,int[] v){
		
		for(int j=0;j<list.get(i).size();j++){
			if(v[list.get(i).get(j)-1]==-1)
				return false;
		}
		return true;
		
	}
	
	private boolean isFixed(int[] v,int[] b){
		
		for(int i=0;i<feedback_vertex_set.size();i++){
			if (b[i]==v[feedback_vertex_set.get(i)]){
				continue;
			}else{
			int ind=0;
			for(int j=0;j<list.get(feedback_vertex_set.get(i)).size();j++){
				ind=ind+v[list.get(feedback_vertex_set.get(i)).get(j)-1]*((int) Math.pow(2,list.get(feedback_vertex_set.get(i)).size()-1-j));
			}
			if(truth.get(feedback_vertex_set.get(i)).get(ind) && v[feedback_vertex_set.get(i)]==0){
				return false;
			}else if(!truth.get(feedback_vertex_set.get(i)).get(ind) && v[feedback_vertex_set.get(i)]==1){
				return false;
			}
			}
		}
		
		return true;
		
	}
	
	private List<List<String>> findAttractors() {
		// TODO Auto-generated method stub
		attractors=new ArrayList<List<String>>();
		List<String> fp=new ArrayList<String>();
		
		for(int i=0;i<fixed_points.size();i++){
			fp.add(fixed_points.get(i));
		}
		int ind=1;
		while (fp.size()>0){
			List<String> visited=new ArrayList<String>();
			List<String> res=new ArrayList<String>();
			visited.add(fp.get(0));
			fp.remove(0);
			
			res=list_dfs(visited.get(0),visited,fp,list,truth);
			if (res!=null){
			if(res.size()>0){
				
				attractors.add(res);
			}
			}
			
		}
		return attractors;
	}
	private void add_node(int[][] adj,int node, int[] in){
		
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
	private void delete_node(int[][] adj,int node, int[] in){
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
	
	private void transpose(int [][] x){
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
	
	
	private boolean if_there_is_a_cycle(int[][] adj){
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
	
	private boolean do_dfs(int[][] adj,int[] labels,int node,int color){
		
		
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
	
	private boolean has_nodes(int[][] adj,int node){
		
		for(int i=0;i<adj.length;i++){
			if(adj[node][i]==1)
				return true;
		}
		return false;
	}
	
	private List<String> list_dfs(String current,List<String> visited,List<String> fp,List<List<Integer>> list,List<List<Boolean>> truth){
		List<String> attractor=new ArrayList<String>();
		attractor.add(current);
		int[] var=new int[n];
		
		//get byte representation of the string
		for(int i=0;i<n;i++){
			var[i]=Character.getNumericValue(current.charAt(i));
		}
		
		
		for(int i=0;i<n;i++){
			//going through neighbours of every node
			List<Integer> nei=list.get(i);
			int sum=0;
			//sum is a current state like 100 = 4
			for(int j=0;j<nei.size();j++){
				sum=sum+var[nei.get(j)-1]*((int) Math.pow(2, nei.size()-j-1));
			}
			
			//next state
			int num=0;
			if(truth.get(i).get(sum)){
				num=1;
			}
			
			//if next state is different from the present do
			if(num!=var[i]){
				String r="";
				//constructing next state
				for(int j=0;j<n;j++){
					if(j!=i){
						r=r+var[j];
					}else{
						r=r+num;
					}
				}
				
				//checking whether it was visited before
				boolean is_ok=true;
				for(int j=0;j<visited.size();j++){
					if(visited.get(j).equalsIgnoreCase(r)){
						is_ok=false;
						break;
					}
				}
				//if not continue dfs
				if(is_ok){
					//checking whether this state is in the fixed points
					for(int j=0;j<fixed_points.size();j++){
						if((fixed_points.get(j).equalsIgnoreCase(r))&&(!r.equalsIgnoreCase(current))){
							fixed_points.remove(visited.get(0));
							return null;
						}
					}
					visited.add(r);
					List<String> nn=list_dfs(r, visited, fp, list, truth);
					if(nn!=null){
					attractor.addAll(nn);}
					else{
						return null;
					}
				}
				
				
				
			}
			
		}
		
	return attractor;
	}
}
