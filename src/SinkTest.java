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
		System.out.println(convertNumberToBinary(6));
		boolean[] visited=new boolean[8];
		for(int j=0;j<8;j++){
			visited[j]=false;
		}
		int[][] adj;
		int size=(int) Math.pow(2, numberOfNodes);
		while(!ifMarked(visited)){
			int startNode=0;
			for(int i=0;i<visited.length;i++){
				if(visited[i]==false){
					startNode=i;
					break;
				}
			}
			
			
				adj=new int[size][size];
				for(int i=0;i<size;i++){
					for(int j=0;j<size;j++)
						adj[i][j]=0;
					}
			
			startPoints.add(startNode);
			dfsTrees.add(constructDFS(convertNumberToBinary(startNode), visited,adj));
			
		}
		/*
		dfsTrees=new ArrayList<int[][]>();
		startPoints=new ArrayList<Integer>();
		
		int[][] ax=
			{
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0}
			};
		int[][] bx=
			{
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0}
			};
		int[][] cx=
			{
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0},
				{0,0,1,0}
			};
		
		dfsTrees.add(ax);
		dfsTrees.add(bx);
		dfsTrees.add(cx);
		
		startPoints.add(0);
		startPoints.add(1);
		startPoints.add(2);
		*/
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
		}
		
		
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
		//adj is adjacency matrix 1st dimension to node 2nd dimension from node
		
		
		List<String> neighbours=findNeighbours(node);
		
		for(int i=0;i<neighbours.size();i++){
			if(visited[convertBinaryToNumber(neighbours.get(i))]==false){
				System.out.println(neighbours.get(i));
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
					if(adj[k][j]==1){
						end=false;
						
					}
					if(adj[j][k]==1){
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
		List<List<String>> temp=detectSinkLeaves();
		
		if(!temp.isEmpty()){
			result.addAll(temp);
		}
		
		List<List<String>> r=detectSinkComponents();
		if(r!=null){
			if(r.size()>0){
				result.addAll(r);
			}
		}
		
		if(result.isEmpty()){
			List<String> t=new ArrayList<String>();
			for(int i=0; i<Math.pow(2, numberOfNodes);i++){
				t.add(convertNumberToBinary(i));
			}
			result.add(t);
		}
		
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
		List<List<String>> temp=new ArrayList<List<String>>();
		List<String> endPoints=new ArrayList<String>();
				
		for(int i=0;i<dfsTrees.size();i++){
			endPoints=findEndPoint(dfsTrees.get(i));
			temp=returnBranches(endPoints, dfsTrees.get(i));
			
			int j=0;
			int n=temp.size();
			while(j<n){
				boolean deeper=false;
				List<String> nei=findNeighbours(temp.get(j).get(temp.get(j).size()-1));
				for(int k=0;k<nei.size();k++){
					if(!temp.get(j).contains(nei.get(k))){
						deeper=true;
						break;
					}
				}
				if(!deeper){
					result.add(temp.get(j));
					temp.remove(j);
					j--;
				}
				j++;
				n=temp.size();
			}
			
			if(temp.size()>0){
				j=0;
				n=temp.size();
				while(j<n)
				{
					
					List<String> nn=findNeighbours(temp.get(j).get(temp.get(j).size()-1));
					int sum=0;
					for(int k=0;k<temp.size();k++){
						if(temp.get(k).contains(temp.get(j).get(temp.get(j).size()-1))){
							sum++;
						}
					}
					
					if(sum==nn.size()){
						List<String> temp_res=new ArrayList<String>();
						temp_res.addAll(temp.get(j));
						for(int k=j+1;k<temp.size();k++){
							if(temp.get(k).contains(temp.get(j).get(temp.get(j).size()-1))){
							temp.get(k).remove(temp.get(k).indexOf(temp.get(j).get(temp.get(j).size()-1)));
							temp_res.addAll(temp.get(k));
							}
						}
						result.add(temp_res);
					}
						int ind=0;
						int count=temp.size();
						while(ind<count){
							
							if(temp.get(ind).contains(temp.get(j).get(temp.get(j).size()-1))){
								temp.remove(ind);
							}
							
							ind++;
							count=temp.size();
						}
					
						
						
					
					
					j++;
					n=temp.size();
				}
			}
			
		}
		
		
		return result;
		
		
	}
	
	List<String> findEndPoint(int[][] adj){
		List<String> result=new ArrayList<String>();
		for(int i=0;i<adj.length;i++){
			int sumc=0;
			int sumr=0;
			for(int j=0;j<adj.length;j++){
				sumc+=adj[j][i];
				sumr+=adj[i][j];
			}
			if(sumc==0 && sumr!=0){
				result.add(convertNumberToBinary(i));
			}
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
