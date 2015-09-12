package newAlgorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kz.AttractorComparator;
import kz.AttractorElementComparator;

public class NewAlg {
	private int numberOfNodes;
	private List<List<Integer>> listOfNeighbours;
	private List<List<Boolean>> truthTable;
	private List<List<String>> attractors=new ArrayList<List<String>>();
	private List<String> stableStates=new ArrayList<String>();
	public NewAlg(String filename) {
		
		if(parseData(filename)){
			attractors=findAttractor();
			attractors.sort(new AttractorComparator());
			for(int i=0;i<attractors.size();i++){
				attractors.get(i).sort(new AttractorElementComparator());
			}
		}else{
			System.out.println("Error reading the file");
		}
		
		
	}
	public List<List<String>> getAttractors(){
		return attractors;
	}
	private boolean parseData(String filename){
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
					return false;
				}
				
				return true;
	}
	
	private List<List<String>> findAttractor(){
		List<List<String>> result=new ArrayList<List<String>>();
		
		
		stableStates=findStableStates();
		
		attractors=new ArrayList<List<String>>();
		List<String> fp=new ArrayList<String>();
		
		for(int i=0;i<stableStates.size();i++){
			fp.add(stableStates.get(i));
		}
		int ind=1;
		while (fp.size()>0){
			List<String> visited=new ArrayList<String>();
			List<String> res=new ArrayList<String>();
			visited.add(fp.get(0));
			fp.remove(0);
			
			res=list_dfs(visited.get(0),visited,fp,listOfNeighbours,truthTable);
			if (res!=null){
			if(res.size()>0){
				
				attractors.add(res);
			}
			}
			
		}
		
		return attractors;
	}
	private List<String> list_dfs(String current,List<String> visited,List<String> fp,List<List<Integer>> list,List<List<Boolean>> truth){
		List<String> attractor=new ArrayList<String>();
		attractor.add(current);
		int[] var=new int[numberOfNodes];
		
		//get byte representation of the string
		for(int i=0;i<numberOfNodes;i++){
			var[i]=Character.getNumericValue(current.charAt(i));
		}
		
		
		for(int i=0;i<numberOfNodes;i++){
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
				for(int j=0;j<numberOfNodes;j++){
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
					for(int j=0;j<stableStates.size();j++){
						if((stableStates.get(j).equalsIgnoreCase(r))&&(!r.equalsIgnoreCase(current))){
							stableStates.remove(visited.get(0));
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
	private List<String> findStableStates(){
		List<String> result=new ArrayList<String>();
		List<Integer> nodesIn=new ArrayList<Integer>();
		List<List<String>> graph=new ArrayList<List<String>>();
		int node;
		for(int i=0;i<numberOfNodes;i++){
		do{
		Random r=new Random();
		node=r.nextInt(numberOfNodes);
		}while(nodesIn.contains(node));
		
		graph=addNode(graph,node,nodesIn);
		
		List<String> temp=new ArrayList<String>();
		//for(int j=0;j<result.size();j++){
			temp.addAll(updateEntry(result, nodesIn.indexOf(node)).get(0));
			temp.addAll(updateEntry(result, nodesIn.indexOf(node)).get(1));
		//}
		result=temp;
		for(int j=0;j<graph.size();j++){
			if(graph.get(j).size()==1 && !result.contains(graph.get(j).get(0)))
				result.add(graph.get(j).get(0));
		}
		
		for(int j=result.size()-1;j>=0;j--){
			List<String> in=new ArrayList<String>();
			in.add(result.get(j));
			in.addAll(checkStates(graph, result.get(j), in));
			for(int k=0;k<result.size();k++){
				if(in.contains(result.get(k))&&k!=j)
					result.remove(j);
			}
		}
		
		}
		return result;
		
	}
	
	private List<String> checkStates(List<List<String>> graph,String current,List<String> in)
	{
		for(int i=0;i<graph.size();i++){
			if(graph.get(i).get(0).equals(current)){
				for(int j=1;j<graph.get(i).size();j++){
					if(!in.contains(graph.get(i).get(j))){
						in.add(graph.get(i).get(j));
						
						//in.addAll(
								checkStates(graph, graph.get(i).get(j), in);
								//);
					}
				}
			}
		}
		return in;
	}
	
	
	private List<List<String>> addNode(List<List<String>> graph,int node,List<Integer> nodesIn){
		List<List<String>> result=new ArrayList<List<String>>();
		nodesIn.add(node);
		nodesIn.sort(new SimpleIntComparator());
		int position=nodesIn.indexOf(node);
		if(graph.size()>0){
		for(int i=0;i<graph.size();i++){
			result.addAll(updateEntry(graph.get(i), position));
		}
		}else{
			List<String> t=new ArrayList<String>();
			t.add("0");
			result.add(t);
			t=new ArrayList<String>();
			t.add("1");
			result.add(t);
		}
		updateConnections(result,nodesIn,position,node);
		return result;
	}
	
	private List<List<String>> updateEntry(List<String> line,int position){
		List<List<String>> res=new ArrayList<List<String>>();
		List<String> temp=new ArrayList<String>();
		
		for(int i=0;i<line.size();i++){
			String t=line.get(i).substring(0, position);
			t+="0";
			t+=line.get(i).substring(position);
			temp.add(t);
		}
		res.add(temp);
		temp=new ArrayList<String>();
		for(int i=0;i<line.size();i++){
			String t=line.get(i).substring(0, position);
			t+="1";
			t+=line.get(i).substring(position);
			temp.add(t);
		}
		res.add(temp);
		
		return res;
	}
	
	private void updateConnections(List<List<String>> res,List<Integer> nodesIn,int position,int node){
		List<Integer> neighbours=listOfNeighbours.get(node);
		List<Integer> notIn=new ArrayList<Integer>();
		notIn=whetherAllIn(nodesIn, neighbours);
		neighbours.sort(new SimpleIntComparator());
		if(notIn.isEmpty()){
			for(int i=0;i<res.size();i++){
				
				char[] var=res.get(i).get(0).toCharArray();
				int sum=0;
				char newVal='0';
				for(int j=0;j<neighbours.size();j++){
					sum+=(var[nodesIn.indexOf(neighbours.get(j)-1)]-48)*Math.pow(2, neighbours.size()-1-j);
				}
				if(truthTable.get(node).get(sum))
					newVal='1';
				if(var[position]!=newVal){
					var[position]=newVal;
					String t=new String(var);
					if(!res.get(i).contains(t)){
						res.get(i).add(t);
					}
				}
			}
		}else{
			notIn.sort(new SimpleIntComparator());
			for(int i=0;i<res.size();i++){
				char[] var=res.get(i).get(0).toCharArray();
				boolean changing=true;
				int[] nVal=new int[neighbours.size()];
				
				for(int j=0;j<Math.pow(2, notIn.size());j++){
					int[] temp=new int[notIn.size()];
					int val=j;
					for(int k=0;k<notIn.size();k++){
						temp[notIn.size()-k-1]=val%2;
						val/=2;
					}
					
				for(int k=0;k<neighbours.size();k++){
					if(notIn.contains(neighbours.get(k))){
						nVal[k]=temp[notIn.indexOf(neighbours.get(k))];
					}else{
						if(res.get(i).get(0).charAt(nodesIn.indexOf(neighbours.get(k)-1))=='1'){
							nVal[k]=1;
						}else{
							nVal[k]=0;
						}
					}
				}
				int sum=0;
				char newVal='0';
				for(int k=0;k<neighbours.size();k++){
					sum+=nVal[k]*Math.pow(2, neighbours.size()-1-k);
				}
				if(truthTable.get(node).get(sum))
					newVal='1';
				if(var[position]==newVal){
					changing=false;
					break;
					}
				}
				if(changing){
					char newVal='0';
					if(newVal==var[position])
						newVal='1';
					var[position]=newVal;
					String t=new String(var);
					if(!res.get(i).contains(t)){
						res.get(i).add(t);
					}
				}
				}
			}
			
		}
	
	
	private List<Integer> whetherAllIn(List<Integer> nodesIn,List<Integer> neighbours){
		List<Integer> res=new ArrayList<Integer>();
		for(int i=0;i<neighbours.size();i++){
			if(!nodesIn.contains(neighbours.get(i)-1)){
				res.add(neighbours.get(i));
			}
		}
		return res;
	}
	
}
