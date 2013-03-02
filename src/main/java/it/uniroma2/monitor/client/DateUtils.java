package it.uniroma2.monitor.client;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

	public static DateTime fromCSVToDateTime(String date){
		return DateTimeFormat.forPattern("yyyy-MM-dd H:m:s")
				.withZoneUTC().parseDateTime(date);
	}
	
	public static String fromDateTimeToRequest(DateTime dateTime){
		DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm_yyyyMMdd").withZoneUTC();
		return fmt.print(dateTime);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dateString = "2013-02-23 18:30:10";
		DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd H:m:s")
				.withZoneUTC().parseDateTime(dateString);
		System.out.println(dateTime);
		//18:10_20130223
		DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm_yyyyMMdd").withZoneUTC();
		String str = fmt.print(new DateTime());
		System.out.println(str);


	}

}
