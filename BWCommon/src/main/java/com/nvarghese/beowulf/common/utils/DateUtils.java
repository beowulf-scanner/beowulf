package com.nvarghese.beowulf.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {

	public static String DEFAULT_TIME_ZONE = "GMT";

	/**
	 * This field has been kept public so that a client code knows what is the
	 * default date format used in this class
	 */
	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public final static String DEFAULT_DATE_FORMAT_WITH_ZONE = "yyyy-MM-dd HH:mm:ssZ";

	public final static String JODA_DATE_FORMAT_WITH_ZONE = "yyyy-MM-dd'T'HH:mm:ssZ";

	static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	/**
	 * 
	 * @param date
	 * @param srcFormat
	 * @param destFormat
	 * @return
	 */
	public static String getTimeFormat(String date, String srcFormat, String destFormat) {

		if (date == null || date.equals(""))
			return "";
		if (srcFormat == null || srcFormat.equals(""))
			return "";
		if (destFormat == null || destFormat.equals(""))
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(srcFormat);
		SimpleDateFormat to_sdf = new SimpleDateFormat(destFormat);
		try {
			return to_sdf.format(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return date.replace("/", "-");
		}
	}

	/**
	 * 
	 * @param dateObj
	 * @param format
	 * @return
	 */
	public static String convertToFormat(Date dateObj, String format) {

		if (dateObj == null)
			return "";
		if (format == null || format.equals(""))
			return "";
		String convDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		convDate = sdf.format(dateObj);
		return convDate.toString();
	}

	/**
	 * 
	 * @param dateObj
	 * @param format
	 * @return
	 */
	public static String convertToFormat(Date dateObj, int dateFormatType, int timeFormatStyle) {

		if (dateObj == null)
			return "";
		return DateFormat.getDateTimeInstance(dateFormatType, timeFormatStyle).format(dateObj);
	}

	/**
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date convertToDate(String dateStr, String format) {

		if (dateStr == null || dateStr.equals(""))
			return new Date();
		if (format == null || format.equals(""))
			return new Date();
		Date toConv = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			toConv = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return toConv;
	}

	/**
	 * 
	 * @return
	 */
	public static String getCurrentTimeInGMT() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

		return sdf.format(cal.getTime());
	}

	/**
	 * 
	 * @return
	 */
	public static Date getCurrentTimeInGMTDate() {

		Date date = null;

		DateTime jodaDateTime = new DateTime();
		DateTime dtGMT = jodaDateTime.withZone(DateTimeZone.forID("Etc/GMT"));
		DateTimeFormatter fmt = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT);
		date = convertToDate(dtGMT.toString(fmt), DEFAULT_DATE_FORMAT);

		return date;
	}

	/**
	 * 
	 * @return
	 */
	public static Calendar dateToCalendar() {

		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		return cal;
	}

	/**
	 * 
	 * @param cal
	 * @param hoursToAdd
	 * @return
	 */
	public static Calendar addHours(Calendar cal, int hoursToAdd) {

		if (null != cal) {
			cal.add(Calendar.HOUR, hoursToAdd);
			cal.clear(Calendar.ZONE_OFFSET);
			cal.clear(Calendar.MILLISECOND);
		}
		return cal;
	}

	/**
	 * 
	 * @param lastNotified
	 * @return
	 */
	public static Calendar stringToCalender(String lastNotified) {

		long lDate = Long.parseLong(lastNotified);
		Date date = new Date(lDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.clear(Calendar.ZONE_OFFSET);
		cal.clear(Calendar.MILLISECOND);
		return cal;
	}

	/**
	 * Returns current date and time as <code>java.util.Calendar</code> object.
	 * However, this calendar will be have time zone offset suppressed.
	 * 
	 * @return calendar
	 */
	public static Calendar getCurrentCalendar() {

		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.ZONE_OFFSET);
		cal.clear(Calendar.MILLISECOND);
		return cal;
	}

	/**
	 * @return added hours
	 */
	public static Calendar addHoursToCurrentTime() {

		Calendar addedHour = Calendar.getInstance();
		addedHour.add(Calendar.HOUR, 5);
		return addedHour;
	}

	/**
	 * 
	 * @param src
	 * @param target
	 * @param format
	 * @return
	 */
	public static int compareDates(String src, String target, String format) {

		Calendar srcCal = Calendar.getInstance(TimeZone.getDefault());
		Calendar tarCal = Calendar.getInstance(TimeZone.getDefault());
		SimpleDateFormat sdf1 = new SimpleDateFormat(format);
		SimpleDateFormat sdf2 = new SimpleDateFormat(format);
		try {
			Date srcDate = sdf1.parse(src);
			Date tarDate = sdf2.parse(target);
			srcCal.setTime(srcDate);
			tarCal.setTime(tarDate);
			return srcCal.compareTo(tarCal);
		} catch (ParseException e) {
			return -1;
		}
	}

	/**
	 * Converts Date in Long format to Calendar Object.
	 * 
	 * @param lDatetimeinSecs
	 * @return
	 */
	public static Calendar longToCalendar(long lDatetimeinSecs) {

		Calendar calender = Calendar.getInstance(TimeZone.getDefault());
		calender.setTimeInMillis(lDatetimeinSecs * 1000);
		calender.clear(Calendar.ZONE_OFFSET);
		calender.clear(Calendar.MILLISECOND);
		return (Calendar) calender;
	}

	public static String convertDateToString(Date date, String srcFormat) {

		DateFormat formatter = new SimpleDateFormat(srcFormat);
		return formatter.format(date);
	}

	/**
	 * Converts a date string with zone information to the default host's time
	 * zone 2012-03-21 19:37:54+0230
	 * 
	 * @param dateString
	 *            should be of the form "yyyy-MM-dd HH:mm:ssZ"
	 * @return
	 */
	public static Date convertDateStringWithZoneToDefaultDate(String dateString) {

		java.text.SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT_WITH_ZONE);
		Date date = null;
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			logger.error("Date parsing failed for the string {} with format {}", dateString, DEFAULT_DATE_FORMAT_WITH_ZONE);
		}

		return date;

	}

	/**
	 * Converts a date string with zone information to the default host's time
	 * zone 2012-03-21 19:37:54+0230. Ignore the timzone information in the date
	 * object. Its a bug in Date/Time utils to display local zone only
	 * 
	 * @param dateString
	 *            should be of the form "yyyy-MM-dd HH:mm:ssZ"
	 * @return
	 */
	public static Date convertDateStringWithZoneToGMT(String dateString) {

		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT_WITH_ZONE);

		Date date = null;
		try {
			DateTime jodaDateTime = new DateTime(format.parse(dateString));
			DateTime dtGMT = jodaDateTime.withZone(DateTimeZone.forID("Etc/GMT"));
			DateTimeFormatter fmt = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT);
			date = convertToDate(dtGMT.toString(fmt), DEFAULT_DATE_FORMAT);
		} catch (ParseException e) {
			logger.error("Date parsing failed for the string {} with format {}", dateString, DEFAULT_DATE_FORMAT_WITH_ZONE);
		}

		return date;

	}

	public static void main(String[] args) throws ParseException {

		Date date = new Date();
		System.out.println(convertDateToString(date, "yyyy-MM-dd HH:mm:ss z"));
		// System.out.println("Current time in GMT: " + convertDateToGMT(new
		// Date()));

		java.text.SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
		java.util.Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		format.setCalendar(cal);
		// format.setTimeZone(TimeZone.getTimeZone("GMT"));
		System.out.println(cal.getTime());
		System.out.println(cal.getTime());
		System.out.println(cal.getTime());
		Date dt = format.parse("2012-03-21 16:37:54+0200");
		System.out.println(dt.toString());

		// DateTimeZone timezoneFrom =
		// DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT"));
		// DateTimeZone timezoneTo = DateTimeZone.UTC;
		// Date utcDate = new
		// Date(timezoneFrom.convertLocalToUTC(date.getTime(), false));
		// Date localDate = new
		// Date(timezoneTo.convertUTCToLocal(utcDate.getTime()));
		//
		// System.out.println(convertDateToString(utcDate,
		// "yyyy-MM-dd HH:mm:ss"));
		// System.out.println(convertDateToString(localDate,
		// "yyyy-MM-dd HH:mm:ss"));
		//
		// DateTime dt = new DateTime(convertDateToString(date2,
		// "yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

		System.out.println(convertDateStringWithZoneToDefaultDate("2012-03-21 16:37:54+0230"));

		System.out.println(TimeZone.getTimeZone("GMT+0"));

		System.out.println("toGMT: " + convertDateStringWithZoneToGMT("2012-03-23 14:52:00-0500"));

		System.out.println(getCurrentTimeInGMT());
		System.out.println(getCurrentTimeInGMTDate().getTime());
		System.out.println(new Date().getTime());

	}
}
