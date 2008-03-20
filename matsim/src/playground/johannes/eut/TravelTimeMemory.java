/**
 * 
 */
package playground.johannes.eut;

import java.util.LinkedList;
import java.util.List;

import org.matsim.network.Link;
import org.matsim.network.NetworkLayer;
import org.matsim.router.util.TravelTimeI;

/**
 * @author illenberger
 *
 */
public class TravelTimeMemory {
	
	private LinkedList<TimevariantTTStorage> storageList = new LinkedList<TimevariantTTStorage>();
	
	private int maxMemomry = 11;
	
	private double learningrate = 0.1;

	public TimevariantTTStorage makeTTStorage(TravelTimeI ttcalc, NetworkLayer network, int binsize, int starttime, int endtime) {
		TimevariantTTStorage storage = new TimevariantTTStorage(network, starttime, endtime, binsize);
		
		for(Link link : network.getLinks().values()) {
			for(int t = starttime; t < endtime; t += binsize) {
				storage.setLinkTravelTime(link, t, ttcalc.getLinkTravelTime(link, t));
			}
		}
		
		return storage;
	}
	
	protected LinkedList<TimevariantTTStorage> getStorageList() {
		return storageList;
	}
	
	public void setMaxMemorySlots(int slots) {
		maxMemomry = slots;
	}
	
	public int getMaxMemorySlots() {
		return maxMemomry;
	}
	
	public void setLearningRate(double rate) {
		learningrate = rate;
	}
	
	public double getLearningRate() {
		return learningrate;
	}
	
	public void appendNewStorage(TimevariantTTStorage storage) {
		storageList.add(storage);
		if(storageList.size() > maxMemomry) {
			TimevariantTTStorage history = storageList.remove();
			storageList.getFirst().accumulate(history, 1 - learningrate);
		}
	}
	
	public TimevariantTTStorage getTravelTimes(int index) {
		return storageList.get(index);
	}
	
	public List<TimevariantTTStorage> getTravelTimes() {
		return storageList;
	}
	
	public TravelTimeI getMeanTravelTimes() {
		return new MeanLinkCost(storageList);
		
	}
	
	private class MeanLinkCost implements TravelTimeI {

		private List<TimevariantTTStorage> linkcosts;
		
		public MeanLinkCost(List<TimevariantTTStorage> linkcosts) {
			this.linkcosts = linkcosts;
		}

		public double getLinkTravelTime(Link link, double time) {
			double sum = 0;
			for(TravelTimeI linkcost : linkcosts)
				sum += linkcost.getLinkTravelTime(link, time);
			
			return sum/(double)linkcosts.size();
		}
		
	}
	
//	private class EvalLinkCost implements EvaluatedLinkCostI {
//		
//		private MeanLinkCost meanlinkcost;
//		
//		public EvalLinkCost(MeanLinkCost linkcost) {
//			meanlinkcost = linkcost;
//		}
//
//		public ScalarRandomPropertiesI getLinkCost(BasicLinkI link, int time_s) {
//			return new Properties(link, time_s);
//		}
//
//		private class Properties implements ScalarRandomPropertiesI {
//			
//			private double expectation;
//			
//			private double variance;
//			
//			public Properties(BasicLinkI link, int time) {
//				expectation = meanlinkcost.getLinkTravelCost(link, time);
//				
//				double sum = 0;
//				for (RoutableLinkCostI cost : meanlinkcost.linkcosts)
//					sum += Math.pow(cost.getLinkTravelCost(link, time) - expectation, 2);
//
//				variance = Math.sqrt((1.0 / (meanlinkcost.linkcosts.size() - 1)) * sum);
//			}
//			
//			public double getExpectation() {
//				return expectation;
//			}
//
//			public double getVariance() {
//				return variance;
//			}
//		}
//
//	}
}
