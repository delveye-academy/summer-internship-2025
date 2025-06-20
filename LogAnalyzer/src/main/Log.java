package main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class Log {

	private LocalDate day;
	private LocalTime time;
	private String logType;
	private String logInfo;

	public Log(LocalDate day, LocalTime time, String logType, String logInfo) {
		this.day = day;
		this.time = time;
		this.logType = logType;
		this.logInfo = logInfo;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	@Override
	public String toString() {
		return day.toString() + " " + time.toString() + " " + logType + " " + logInfo;
	}

	public static Log fromStringArray(String[] arr) throws LogParseException {
		if (arr.length < LogAnalyzerApp.NUM_OF_STRINGS_IN_LOG || arr[0] == null || arr[1] == null || arr[2] == null
				|| arr[3] == null) {
			throw new LogParseException("Incomplete or null log entry");
		}

		return new Log(LocalDate.parse(arr[0]), LocalTime.parse(arr[1]), arr[2], arr[3]);
	}
}
