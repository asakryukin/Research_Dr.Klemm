package kz;

import java.util.Comparator;
import java.util.List;


public class AttractorComparator implements Comparator<List<String>>{
	@Override
	public int compare(List<String> o1, List<String> o2) {
		// TODO Auto-generated method stub
		return o1.size() < o2.size() ? -1 : o1.size() == o2.size() ? SinkTest.convertBinaryToNumber(o1.get(0))<SinkTest.convertBinaryToNumber(o2.get(0)) ? -1 : 1:1;
	}
}
