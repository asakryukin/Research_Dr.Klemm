package kz;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class SinkTest {
	private int numberOfNodes;
	private List<List<Integer>> listOfNeighbours;
	private List<List<Boolean>> truthTable;
	private List<List<String>> dfsTree;
	private List<List<String>> sink;
	private List<int[][]> dfsTrees=new ArrayList<int[][]>();
	private List<Integer> startPoints=new ArrayList<Integer>();
	private List<List<String>> attractors=new ArrayList<List<String>>();
	private int size;
	public SinkTest(String filename) {
		// TODO Auto-generated constructor stub
		Path path=Paths.get(filename);
		BufferedReader reader;
		try {
			reader = Files.newBufferedReader(path);
			
			numberOfNodes=Integer.parseInt(reader.readLine());
			listOfNeighbours = new ArrayList<List<Integer>>(numberOfNodes);
			truthTable=new ArrayList<List<Boolean>>(numberOfNodes);
			String line;
			int i=0;
			while (i<numberOfNodes){
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
			listOfNeighbours.add(l);
			}
			i=0;
			while (i<numberOfNodes){
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
			truthTable.add(l);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean[] visited=new boolean[(int)Math.pow(2, numberOfNodes)];
		for(int j=0;j<Math.pow(2, numberOfNodes);j++){
			visited[j]=false;
		}
		int[][] adj;
		size=(int) Math.pow(2, numberOfNodes);
		while(!ifMarked(visited)){
			int startNode=0;
			for(int i=0;i<visited.length;i++){
				if(visited[i]==false){
					startNode=i;
					break;
				}
			}
			/*
			Random r=new Random();
			startNode=r.nextInt(size);
			while(visited[startNode]){
				startNode=r.nextInt(size);
			}
			*/
				adj=new int[size][size];
				for(int i=0;i<size;i++){
					for(int j=0;j<size;j++)
						adj[i][j]=0;
					}
			
			startPoints.add(startNode);
			dfsTrees.add(constructDFS(convertNumberToBinary(startNode), visited,adj));
			
		}
		List<String> x=new ArrayList<String>();
		//x=findNeighbours("0101110");
		treeCon();
		List<List<String>> result=new ArrayList<List<String>>();
		result=findSinkComponents();
		result.sort(new AttractorComparator());
		for(int i=0;i<result.size();i++){
			result.get(i).sort(new AttractorElementComparator());
		}
		if(result.size()>0){
			for(int i=0;i<result.size();i++){
				System.out.println("Attractor #"+(i+1));
				for(int j=0;j<result.get(i).size();j++){
					System.out.print(result.get(i).get(j)+"\t");
				}
				System.out.println();
			}
			attractors=result;
		}
		
		
	}
	
	List<List<String>> getAttractors(){
		return attractors;
	}
	
	static boolean ifMarked(boolean[] s){
		for(int i=0;i<s.length;i++){
			if(s[i]==false){
				return false;
			}
		}
		return true;
	}
	
	String convertNumberToBinary(int number){
		String result="";
		String append="";
		while(number>0){
			result+=number%2;
			number=number/2;
		}
		char temp;
		char[] t;
		t=result.toCharArray();
		for(int i=0;i<result.length()/2;i++){
			temp=t[i];
			t[i]=t[result.length()-i-1];
			t[result.length()-i-1]=temp;
			
		}
		result=new String(t);
		if(result.length()<numberOfNodes){
			for(int i=0;i<numberOfNodes-result.length();i++)
				append+='0';
		}
		result=append+result;
		return result;
	}
	
	static int convertBinaryToNumber(String s){
		int[] var=new int[s.length()];
		int result=0;
		
		//get byte representation of the string
		for(int i=0;i<s.length();i++){
			var[i]=Character.getNumericValue(s.charAt(i));
		}
		
		for(int j=0;j<s.length();j++){
			result=result+var[s.length()-j-1]*((int) Math.pow(2,j));
		}
		
		return result;
		
	}
	
	int[][] constructDFS(String node,boolean[] visited,int[][] adj){
		
		visited[convertBinaryToNumber(node)]=true;
		int size=(int) Math.pow(2, numberOfNodes);
		//adj is adjacency matrix 1st dimension from node 2nd dimension to node
		
		
		List<String> neighbours=findNeighbours(node);
		neighbours.sort(new AttractorElementComparator());
		for(int i=0;i<neighbours.size();i++){
			if(visited[convertBinaryToNumber(neighbours.get(i))]==false){
				//System.out.println(neighbours.get(i));
				adj[convertBinaryToNumber(node)][convertBinaryToNumber(neighbours.get(i))]=1;
				constructDFS(neighbours.get(i), visited,adj);
			}
		}
		
		return adj;
	}
	
	List<String> findNeighbours(String node){
		List<String> result=new ArrayList<String>();
		char[] state;
		state=node.toCharArray();
		
		for(int i=0;i<numberOfNodes;i++){
			String s=new String();
			for(int j=0;j<listOfNeighbours.get(i).size();j++){
				s+=state[listOfNeighbours.get(i).get(j)-1];
			}
			boolean temp=false;
			if(state[i]=='1'){
				temp=true;
			}
			char[] neighbour=new char[state.length];
			for(int k=0;k<state.length;k++){
				neighbour[k]=state[k];
			}
			if(truthTable.get(i).get(convertBinaryToNumber(s))!=temp){
				if(state[i]=='1'){
					neighbour[i]='0';
				}else{
					neighbour[i]='1';
				}
				s=new String();
				for(int k=0;k<numberOfNodes;k++){
					s+=neighbour[k];
				}
				result.add(s);
			}
		}
		
		
		return result;
	}
	
	void treeCon(){
		//Check neighbours is not equal starting node
		
		//Checking every adjacency matrix
		int i=0;
		int n=dfsTrees.size();
		boolean test;
		while(i<n){
			test=false;
			//checking every column
			for(int j=0;j<dfsTrees.get(i).length;j++){
				//every element
				
				boolean end=true;
				boolean has_anc=false;
				int[][] adj=dfsTrees.get(i);
				for(int k=0;k<dfsTrees.get(i).length;k++){
					if(adj[j][k]==1){
						end=false;
						
					}
					if(adj[k][j]==1){
						has_anc=true;
						
					}
				}
				//if it is not pointing to any node
				if((end&&has_anc) || (end&&(j==startPoints.get(i)))){
					//find its neighbours
					List<String> nei=new ArrayList<String>();
					nei=findNeighbours(convertNumberToBinary(j));
					//for every neighbour
					for(int k=0;k<nei.size();k++){
						//check 
						for(int l=0;l<startPoints.size();l++){
							//whether it has starting point of another tree as a neighbours
							if(startPoints.get(l)==convertBinaryToNumber(nei.get(k)) && startPoints.get(i)!=startPoints.get(l)){
								//if it has
								
								adj[j][startPoints.get(l)]=1;
								
								int[][] temp=dfsTrees.get(l);
								for(int indi=0;indi<dfsTrees.get(i).length;indi++){
									for(int indj=0;indj<dfsTrees.get(i).length;indj++){
										adj[indi][indj]=adj[indi][indj]+temp[indi][indj];
									}
								}
								dfsTrees.remove(l);
								startPoints.remove(l);
								i--;
								test=true;
								break;
							}
							
						}
						if(test){
							break;
						}
					}
					if(test){
						break;
					}
				}
				
			}
			i++;
			n=dfsTrees.size();
		}
		
		
	}
	List<List<String>> findSinkComponents(){
		List<List<String>> result=new ArrayList<List<String>>();
		result=detectSinkComponents();
		return result;
		
	}
	
	List<List<String>> detectSinkLeaves(){
		List<List<String>> result=new ArrayList<List<String>>();
		
		for(int i=0;i<Math.pow(2, numberOfNodes);i++){
			List<String> t=new ArrayList<String>();
			if(findNeighbours(convertNumberToBinary(i)).isEmpty()){
				t.add(convertNumberToBinary(i));
				result.add(t);
			}
		}
		
		return result;
		
	}
	
	List<List<String>> detectSinkComponents(){
		List<List<String>> result=new ArrayList<List<String>>();
		List<String> endPoints=new ArrayList<String>();
		
		for(int i=0;i<dfsTrees.size();i++){
			endPoints=findEndPoint(dfsTrees.get(i),startPoints.get(i));
			for(int j=endPoints.size()-1;j>=0;j--){
				List<String> branch=new ArrayList<String>();
				List<String> res=new ArrayList<String>();
				List<String> pointers=new ArrayList<String>();
				branch.add(endPoints.get(j));
				
				res=buildUpSinkComponent(branch, pointers, dfsTrees.get(i));
				if(res!=null){
					result.add(res);
				}
				else{
					List<String> temp=new ArrayList<String>();
					temp.add(convertNumberToBinary(startPoints.get(i)));
					int[][] adj=dfsTrees.get(i);
					for(int k=0;k<adj.length;k++){
						int sum=0;
						for(int ind=0;ind<adj.length;ind++){
							sum+=adj[ind][k];
						}
						if(sum>0){
							temp.add(convertNumberToBinary(k));
						}
					}
					boolean changed=true;
					//bad implementation
					while(changed){
						changed=false;
						for(int k=0;k<temp.size();k++){
							List<String> nei=findNeighbours(temp.get(k));
							for(int ind=0;ind<nei.size();ind++){
								if(!temp.contains(nei.get(ind))){
									temp.add(nei.get(ind));
									changed=true;
								}
							}
						}
					}
					
					result.add(temp);
				}
			}
			
		}
		
		int i=0;
		int n=result.size();
		
		while(i<n){
			
			for(int j=0;j<result.size();j++){
				if(result.get(i).containsAll(result.get(j))&&i!=j){
					result.remove(i);
					i--;
					break;
				}
			}
			
			i++;
			n=result.size();
		}
		return result;
		
		
	}
	
	List<String> buildUpSinkComponent(List<String> branch, List<String> pointers, int[][] adj){
		/*
		//Adding neighbours of the highest node as pointers
		List<String> nei=findNeighbours(branch.get(branch.size()-1));
		nei.sort(new AttractorElementComparator());
		for(int i=0;i<nei.size();i++){
			if(!pointers.contains(nei.get(i))){
				pointers.add(nei.get(i));
			}
		}
		
		
		//Checking for pointers which already in the branch and removing them
		for(int i=pointers.size()-1;i>=0;i--){
			if(branch.contains(pointers.get(i))){
				pointers.remove(i);
			}
		}
		//If no pointers left - search is finished
		if(pointers.isEmpty()){
			return branch;
		}else{
			//else adding ancestors to the branch and repeat 
			int node_ind=convertBinaryToNumber(branch.get(branch.size()-1));
			boolean hasAncestor=false;
			for(int i=0;i<adj.length;i++){
			if(adj[i][node_ind]!=0){
				if(branch.contains(convertNumberToBinary(i))){
					
				}else{
					branch.add(convertNumberToBinary(i));
					
					buildUpSinkComponent(branch, pointers, adj);
					if(pointers.isEmpty()){
						break;
					}
				}
			}
				
			}
		}
		
		//if pointers are empty return branch else the tree has cross edge to another tree
		if(pointers.isEmpty()){
			return branch;
		}else{
		return null;
		}*/
		String node=branch.get(0);
		boolean[] v=new boolean[size];
		int[][] a=new int[size][size];
		
		for(int i=0;i<numberOfNodes;i++){
			v[i]=false;
		}
		v[convertBinaryToNumber(node)]=true;
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				a[i][j]=0;
			}
		}
		
		int[][] res=constructDFS(node, v, a);
		
		List<String> result=new ArrayList<String>();
		
		for(int i=0;i<size;i++){
			if(v[i]){
				result.add(convertNumberToBinary(i));
			}
		}
		return result;
	}
	
	List<String> findEndPoint(int[][] adj,int starting){
		List<String> result=new ArrayList<String>();
		for(int i=0;i<adj.length;i++){
			int sumc=0;
			int sumr=0;
			for(int j=0;j<adj.length;j++){
				sumc+=adj[j][i];
				sumr+=adj[i][j];
			}
			if(sumc!=0 && sumr==0){
				result.add(convertNumberToBinary(i));
			}
		}
		if(result.isEmpty()){
			result.add(convertNumberToBinary(starting));
		}
		return result;
	}
	
	//return all branches for all end points
	List<List<String>> returnBranches(List<String> endPoints, int[][] adj){
		List<List<String>> result=new ArrayList<List<String>>();
		
		for(int i=0;i<endPoints.size();i++){
			List<String> br=returnBranch(endPoints.get(i), adj);
			//find whether there are cross edges except branching one
			List<String> res=new ArrayList<String>();
			for(int j=0;j<br.size();j++){
				res.add(br.get(br.size()-1-j));
			}
			br=res;
			br=contractBranch(br);
			if(br!=null){
				result.add(br);
			}
		}
		return result;
		
	}
	//check adj 
	List<String> returnBranch(String node,int[][] adj){
		List<String> result=new ArrayList<String>();
		result.add(node);
		
		int sum=0;
		int n=convertBinaryToNumber(node);
		int next=0;
		for(int i=0;i<adj.length;i++){
			sum+=adj[n][i];
					if(adj[n][i]==1){
						next=i;
					}
		}
		
		if(sum==1){
			result.addAll(returnBranch(convertNumberToBinary(next), adj));
		}
		
		
		return result;
		
	}
	
	List<String> contractBranch(List<String> branch){
		List<String> result=new ArrayList<String>();
		result=branch;
		String end=result.get(0);
		
		List<String> nei=findNeighbours(end);
		int[] pos=new int[nei.size()];
		if(nei.size()>0){
			for(int i=0;i<nei.size();i++){
				if (result.contains(nei.get(i))){
					pos[i]=result.indexOf(nei.get(i));
				}
			}
			int max=pos[0];
			for(int i=0;i<pos.length;i++){
				if(pos[i]>max){
					max=pos[i];
				}
			}
			
			int n=result.size();
			if(max+1!=n){
			for(int i=max+1;i<n;i++){
				result.remove(max+1);
			}
			}
			if(max==(n-1)){
				n=max-1;
			}
			
			for(int i=0;i<max+1;i++){
				List<String> neitemp=findNeighbours(result.get(i));
				for(int j=0;j<neitemp.size();j++){
					if(!result.contains(neitemp.get(j))){
						return null;
					}
				}
				
			}
			return result;
			
		}else{
			return null;
		}
		
		
	}
	
}
