package kz;
import java.util.ArrayList;
import java.util.List;


public interface GraphAttractor {
	abstract List<Integer> findFeedbackFertexSet();
	abstract List<String>  findFixedPoints();
	abstract List<String>  findAttractors();
}
