package kz;
import java.util.Comparator;


public class FVSComparator implements Comparator<Integer>{
	@Override
	public int compare(Integer o1, Integer o2) {
		// TODO Auto-generated method stub
		return o1 < o2 ? -1 : o1 == o2 ? 0 : 1;
	}
}
