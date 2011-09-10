package no.insane.insane.handlers;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ConsoleHandler

  implements Filter {
	
	private String remove = "Can't keep up! Did the system time change, or is the server overloaded?";
	
	public boolean isLoggable(LogRecord logRecord) {
		return !logRecord.getMessage().equals(this.remove);
	}
}