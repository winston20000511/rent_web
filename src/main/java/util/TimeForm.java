package util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeForm {
	// convert sql.datetime to ZonedDateTime
	public static ZonedDateTime convertTimestampToZoneDateTime(Timestamp sqlTimestamp) {

		if (sqlTimestamp == null) return null; // Handle null case

		Instant instant = sqlTimestamp.toInstant();
		return instant.atZone(ZoneId.of("Asia/Taipei"));
	}

	// convert ZonedDateTime to sql.datetime
	public static Timestamp convertZoneDateTimeToTimestamp(ZonedDateTime zonedDateTime) {
		if (zonedDateTime == null) return null; // Handle null case
		return Timestamp.from(zonedDateTime.toInstant());
	}
	
	// convert String to ZonedDateTime
	public static ZonedDateTime convertStringToZonedDateTime(String dateTime) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        return ZonedDateTime.parse(dateTime, formatter.withZone(ZoneId.of("Asia/Taipei")));
		}catch(DateTimeParseException exception){
			System.out.println("invalid date format");
		}
		return null;
	}
	
	public static String convertZonedDateTimeToString(ZonedDateTime zonedDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
		return zonedDateTime.format(formatter);
	}
}
