package it.uniroma2.monitor.client;

import java.util.ArrayList;
import java.util.Collection;

public class MetricResult {

	private String name;
	private Collection<MetricValue> values;

	public MetricResult(String name) {
		this.name = name;
		values = new ArrayList<MetricValue>();
	}
	
	public void add(MetricValue metricValue){
		values.add(metricValue);
	}
	
	public double avg(){
		double sum = 0D;
		double count = 0D;
		for(MetricValue v : values){
			count++;
			sum+=v.getValue();
		}
		return sum/count;
	}
	
	
}
