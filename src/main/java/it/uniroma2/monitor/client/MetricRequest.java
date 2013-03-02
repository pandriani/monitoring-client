package it.uniroma2.monitor.client;

import java.io.StringReader;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import au.com.bytecode.opencsv.CSVReader;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MetricRequest {

	private static final String GRAPHITE_HOST = "ec2-23-22-114-15.compute-1.amazonaws.com";
	private String metricName;
	private DateTime from;
	private DateTime until;

	public MetricRequest(String metricName, DateTime from, DateTime until) {
		super();
		this.metricName = metricName;
		this.from = from;
		this.until = until;
	}

	private String build() {
		StringBuffer url = new StringBuffer();
		url.append("http://").append(GRAPHITE_HOST).append("/render/?target=")
				.append(metricName).append("&from=").append(DateUtils.fromDateTimeToRequest(from))
				.append("&until=").append(DateUtils.fromDateTimeToRequest(until))
				.append("&format=csv");
		return url.toString();
	}

	public MetricResult makeRequest() {
		MetricResult metricResult = null;
		try {
			Client client = Client.create();
			String url = build();
			System.out.println(url);
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource.accept("application/csv")
					.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			String output = response.getEntity(String.class);
			CSVReader reader = new CSVReader(new StringReader(output), ',');
			metricResult = new MetricResult(metricName);
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				Double d;
				if ("".equals(nextLine[2]))
					continue;
				else
					d = Double.parseDouble(nextLine[2]);
				MetricValue metricValue = new MetricValue(
						DateUtils.fromCSVToDateTime(nextLine[1]), d);
				metricResult.add(metricValue);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return metricResult;
	}

	//http://ec2-23-22-114-15.compute-1.amazonaws.com/render/?target=Client-ip-10-196-118-172.it.uniroma2.HttpClient.duration.meanRate&from=18:10_20130223&until=18:30_20130223"&format=csv

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DateTime from = new DateTime(2013, 2, 23, 18, 10, DateTimeZone.UTC);
		DateTime until = new DateTime(2013, 2, 23, 18, 30, DateTimeZone.UTC);
		//System.out.println(DateUtils.fromDateTimeToRequest(from));
		MetricRequest req = new MetricRequest("*.AWS_EC2.it.uniroma2.imagetranscoder.monitor.LoadAverageMonitorThread.loadAverage.value", from , until);
		System.out.println(req.makeRequest().avg());
	}
}
