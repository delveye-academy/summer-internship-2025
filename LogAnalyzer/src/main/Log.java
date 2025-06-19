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
	public Log(String[] logStringArray) throws DateTimeParseException  {		
		this(LocalDate.parse(logStringArray[0]), 
				LocalTime.parse(logStringArray[1]), 
				logStringArray[2], 
				logStringArray[3]);
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
		// TODO Auto-generated method stub
		return day.toString() + " " + time.toString() 
		+ " " + logType + " " + logInfo;
	}
}
