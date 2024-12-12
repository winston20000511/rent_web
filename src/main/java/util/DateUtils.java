package util;


import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	// 格式化日期
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}

	// 格式化時間
	public static String formatTime(Time time) {
		return timeFormat.format(time);
	}

	// 解析日期字符串
	public static Date parseDate(String dateStr) throws ParseException {
        java.util.Date utilDate = dateFormat.parse(dateStr);
        return new Date(utilDate.getTime());
    }

	// 解析時間字符串
	public static Time parseTime(String timeStr) throws ParseException {
		return new Time(timeFormat.parse(timeStr).getTime());
	}
}
