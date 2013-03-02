package it.uniroma2.monitor.client;

import org.joda.time.DateTime;

public class MetricValue {

	private DateTime time;
	private double value;
	
	public MetricValue(DateTime time, double value) {
		super();
		this.time = time;
		this.value = value;
	}

	public DateTime getTime() {
		return time;
	}

	public void setTime(DateTime time) {
		this.time = time;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	} 
	
	
	
}
