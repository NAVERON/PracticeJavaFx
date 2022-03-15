package org.practicefx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
	
	
	public static LocalDateTime parseStringToLocalDateTime(String originDateString) {
		LocalDateTime localDateTime = LocalDateTime.parse(originDateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		
		return localDateTime;
	}
	
	public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
		return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

}
