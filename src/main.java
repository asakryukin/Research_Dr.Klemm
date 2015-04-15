import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Path path=Paths.get("graph.txt");
		try {
			BufferedReader reader =Files.newBufferedReader(path);
			int n;
			n=Integer.parseInt(reader.readLine());
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
			while ((line=reader.readLine())!=null){
				int s1=line.indexOf(" ");
				int s2=line.substring(s1+1).indexOf(" ");
				int s3=line.substring(s2+1).indexOf(" ");
				s2=s2+s1+1;
				s3=s3+s2+1;
				
				int i1,i2,i3;
				
				i1=Integer.parseInt(line.substring(0, s1));
				i2=Integer.parseInt(line.substring(s1+1, s2));
				i3=Integer.parseInt(line.substring(s2+1, s3));
				
				adj[i1-1][i2-1]=1;
				values[i1-1]=i3;
				
			}
			
			
			
			print_graph(adj,n);
			
			int[][][] res=new int[8][3][3];
			
			res=construct(values);
			
			int x=8;
			x=x+2;
			
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	static int[] fnc_1(int[] values){
		int[] res=new int[values.length];
		
		for(int i=0;i<values.length;i++){
			res[i]=values[i];
		}
		
		res[0]=1-values[1];
		
		return res;
		
	}
	
	static int[] fnc_2(int[] values){
		int[] res=new int[values.length];
		
		for(int i=0;i<values.length;i++){
			res[i]=values[i];
		}
		
		res[1]=values[0]*values[2];
		
		return res;
		
	}
	
	static int[] fnc_3(int[] values){
		int[] res=new int[values.length];
		
		for(int i=0;i<values.length;i++){
			res[i]=values[i];
		}
		
		res[2]=1-values[1];
		
		return res;
		
	}
	
	static int[] fnc(int[] values, int node){
		int[] res=new int[values.length];
		
		switch(node){
		case 0:{res=fnc_1(values); break;}
		case 1:{res=fnc_2(values); break;}
		case 2:{res=fnc_3(values); break;}
		}
		return res;
	}
	
	static int[][][] construct(int[] values){
		int d=(int) Math.pow(2, values.length);
		int l=values.length;
		int[][][] res=new int [d][l][l];
	
		for(int i=0;i<d;i++){
			for(int j=0;j<l;j++){
				for(int k=0;k<l;k++){
					res[i][j][k]=-1;
				}
			}
		}
		
		for(int i=0;i<d;i++){
			int[] curr=new int[l];
			for (int j=0;j<l;j++){
				int sum=0;
				
				for(int k=0;k<j;k++){
					sum=sum+curr[k]*((int) Math.pow(2, l-k-1));
				}
				
				curr[j]=(int) Math.floor((i-sum)/((int) Math.pow(2, l-j-1)));
			}
				
			
			
			for(int j=0;j<l;j++){
				int[] r=new int[l];
				r=fnc(curr,j);
				
				res[i][j]=r;
				
			}
			
			String cr="";
			
			for(int j=0;j<l;j++){
				cr=cr+curr[j];
			}
			
			System.out.print(cr+": ");
			
			for(int j=0;j<l;j++){
				String tmp="";
				for(int k=0;k<l;k++){
					tmp=tmp+res[i][j][k];
				}
				if(!cr.equals(tmp)){
					System.out.print(tmp+" ");
				}
			}
			System.out.println();
			
		}
		
		return res;
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
