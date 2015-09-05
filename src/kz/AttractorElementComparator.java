package kz;
import java.util.Comparator;
import java.util.List;


public class AttractorElementComparator implements Comparator<String>{
	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		return SinkTest.convertBinaryToNumber(o1) < SinkTest.convertBinaryToNumber(o2) ? -1 : SinkTest.convertBinaryToNumber(o1) == SinkTest.convertBinaryToNumber(o2) ? 0 : 1;
	}
}
