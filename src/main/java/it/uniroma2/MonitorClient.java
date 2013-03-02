package it.uniroma2;

import it.uniroma2.monitor.client.MetricResult;
import it.uniroma2.monitor.client.MetricValue;

import java.io.StringReader;

import org.joda.time.format.DateTimeFormat;

import au.com.bytecode.opencsv.CSVReader;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MonitorClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Client client = Client.create();
			WebResource webResource = client
					.resource("http://ec2-23-22-114-15.compute-1.amazonaws.com/render/?target="
							+ "Client-ip-10-196-118-172.it.uniroma2.HttpClient.duration.meanRate"
							+ "&from=18:10_20130223" 
							+ "&until=18:30_20130223"
							+ "&format=csv");
			ClientResponse response = webResource.accept("application/csv")
					.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			String output = response.getEntity(String.class);
			CSVReader reader = new CSVReader(new StringReader(output), ',');
			MetricResult metricResult = new MetricResult("Client-ip-10-196-118-172.it.uniroma2.HttpClient.duration.meanRate");
			String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
		    	Double d; 
		    	if("".equals(nextLine[2]))
		    		d = 0D;
		    	else
		    		d = Double.parseDouble(nextLine[2]);
		    	MetricValue metricValue = new MetricValue(DateTimeFormat.forPattern("yyyy-MM-dd H:m:s")
						.withZoneUTC().parseDateTime(nextLine[1]), d);
		    	metricResult.add(metricValue);
		    }
		    reader.close();
		    System.out.println(metricResult.avg());
		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}
