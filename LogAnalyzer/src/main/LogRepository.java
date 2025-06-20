package main;

import java.util.Arrays;
import java.util.List;

public class LogRepository {
	public static List<String> getLogs() {
		List<String> logs = Arrays.asList("2025-06-16 14:00:00 INFO Application started",
				"2025-06-16 14:15:42 ERROR NullPointerException at line 52",
				"2025-06-16 13:55:00 ERROR Timeout while connecting", "MALFORMED LOG LINE",
				"2021 1 1 13:00:00 STATUS Testing", null, "null null null null");
		return logs;
	}
}
