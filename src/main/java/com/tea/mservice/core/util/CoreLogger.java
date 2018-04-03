package com.tea.mservice.core.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public class CoreLogger implements org.slf4j.Logger {
	private static Logger loggerDebug = Logger.getLogger("debugfile");
	private static Logger loggerError = Logger.getLogger("errorfile");
	private static Logger loggerWarn = Logger.getLogger("warnfile");
	private static Logger loggerInfo = Logger.getLogger("infofile");
	private static final String TRACE_UNIMPLEMENTED = "trace方法未实现";
	private  final String classMsg;
	private String classMessage = null;

	public String getclassMessage() {
		return classMessage;
	}

	public CoreLogger(Object message) {
		this.classMessage = message.toString();
		classMsg = "[" + classMessage + "] - " ;
	}

	public synchronized static CoreLogger getCoreLogger(Object message) {

		return new CoreLogger(message);
	}

	@Override
	public String getName() {
		return classMessage;
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return false;
	}

	public void trace(String msg) {
		this.error(TRACE_UNIMPLEMENTED);
	}

	@Override
	public void trace(String format, Object arg) {
		this.error(TRACE_UNIMPLEMENTED);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		this.error(TRACE_UNIMPLEMENTED);
	}

	@Override
	public void trace(String format, Object[] argArray) {
		this.error(TRACE_UNIMPLEMENTED);
	}

	@Override
	public void trace(String msg, Throwable t) {
		this.error(TRACE_UNIMPLEMENTED);
	}

	@Override
	public void trace(Marker marker, String msg) {
		this.error(TRACE_UNIMPLEMENTED);
	}

	@Override
	public void trace(Marker marker, String format, Object arg) {
		this.error(TRACE_UNIMPLEMENTED);
	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		this.error(TRACE_UNIMPLEMENTED);
	}

	@Override
	public void trace(Marker marker, String format, Object[] argArray) {
		this.error(TRACE_UNIMPLEMENTED);

	}

	@Override
	public void trace(Marker marker, String msg, Throwable t) {
		this.error(TRACE_UNIMPLEMENTED);
	}

	/**
	 * 
	 * 
	 */
	@Override
	public boolean isDebugEnabled() {
		return loggerDebug.isEnabledFor(Level.DEBUG);
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return loggerDebug.isEnabledFor(Level.DEBUG);
	}

	@Override
	public void debug(String msg) {
		loggerDebug.debug(classMsg + msg);
	}

	public void debug(String format, Object arg) {
		if (loggerDebug.isEnabledFor(Level.DEBUG)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			loggerDebug.debug(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		if (loggerDebug.isEnabledFor(Level.DEBUG)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			loggerDebug.debug(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void debug(String format, Object[] argArray) {
		if (loggerDebug.isEnabledFor(Level.DEBUG)) {
			FormattingTuple ft = MessageFormatter.format(format, argArray);
			loggerDebug.debug(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void debug(String msg, Throwable t) {
		loggerDebug.debug(classMsg + msg + "," + t);
	}

	@Override
	public void debug(Marker marker, String msg) {
		loggerDebug.debug(classMsg + msg);

	}

	@Override
	public void debug(Marker marker, String format, Object arg) {
		if (loggerDebug.isEnabledFor(Level.DEBUG)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			loggerDebug.debug(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}

	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		if (loggerDebug.isEnabledFor(Level.DEBUG)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			loggerDebug.debug(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void debug(Marker marker, String format, Object[] argArray) {
		if (loggerDebug.isEnabledFor(Level.DEBUG)) {
			FormattingTuple ft = MessageFormatter.format(format, argArray);
			loggerDebug.debug(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void debug(Marker marker, String msg, Throwable t) {
		loggerDebug.debug(classMsg + msg + "," + t);
	}

	/**
	 * 
	 * 
	 */

	@Override
	public boolean isInfoEnabled() {

		return loggerInfo.isInfoEnabled();
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return loggerInfo.isInfoEnabled();
	}

	public void info(String format, Object arg) {
		if (loggerInfo.isEnabledFor(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			loggerInfo.info(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void info(String msg) {
		loggerInfo.info(classMsg + msg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		if (loggerInfo.isEnabledFor(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			loggerInfo.info(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void info(String format, Object[] argArray) {
		if (loggerInfo.isEnabledFor(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.format(format, argArray);
			loggerInfo.info(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void info(String msg, Throwable t) {
		loggerInfo.info(classMsg + msg + "," + t);
	}

	@Override
	public void info(Marker marker, String msg) {
		loggerInfo.info(classMsg + msg);

	}

	@Override
	public void info(Marker marker, String format, Object arg) {
		if (loggerInfo.isEnabledFor(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			loggerInfo.info(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		if (loggerInfo.isEnabledFor(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			loggerInfo.info(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void info(Marker marker, String format, Object[] argArray) {
		if (loggerInfo.isEnabledFor(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.format(format, argArray);
			loggerInfo.info(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void info(Marker marker, String msg, Throwable t) {
		loggerInfo.info(classMsg + msg + "," + t);
	}

	/**
	 * 
	 * 
	 */

	@Override
	public boolean isWarnEnabled() {
		return loggerWarn.isEnabledFor(Level.WARN);
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return loggerWarn.isEnabledFor(Level.WARN);
	}

	public void warn(String format, Object arg) {
		if (loggerWarn.isEnabledFor(Level.WARN)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			loggerWarn.warn(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void warn(String msg) {
		loggerWarn.warn(classMsg + msg);
	}

	@Override
	public void warn(String format, Object[] argArray) {
		if (loggerWarn.isEnabledFor(Level.WARN)) {
			FormattingTuple ft = MessageFormatter.format(format, argArray);
			loggerWarn.warn(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		if (loggerWarn.isEnabledFor(Level.WARN)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			loggerWarn.warn(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void warn(String msg, Throwable t) {
		loggerWarn.warn(classMsg + msg + "," + t);
	}

	@Override
	public void warn(Marker marker, String msg) {
		loggerWarn.warn(classMsg + msg);
	}

	@Override
	public void warn(Marker marker, String format, Object arg) {
		if (loggerWarn.isEnabledFor(Level.WARN)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			loggerWarn.warn(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		if (loggerWarn.isEnabledFor(Level.WARN)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			loggerWarn.warn(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void warn(Marker marker, String format, Object[] argArray) {
		if (loggerWarn.isEnabledFor(Level.WARN)) {
			FormattingTuple ft = MessageFormatter.format(format, argArray);
			loggerWarn.warn(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	@Override
	public void warn(Marker marker, String msg, Throwable t) {
		loggerWarn.warn(classMsg + msg + "," + t);
	}

	/**
	 * 
	 * 
	 */

	@Override
	public boolean isErrorEnabled() {
		return loggerError.isEnabledFor(Level.ERROR);
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return loggerError.isEnabledFor(Level.ERROR);
	}

	@Override
	public void error(String msg) {
		loggerError.error(classMsg + msg);

	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		if (loggerError.isEnabledFor(Level.ERROR)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			loggerError.error(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}

	}

	@Override
	public void error(String format, Object[] argArray) {
		if (loggerError.isEnabledFor(Level.ERROR)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
			loggerError.error(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}

	}

	public void error(String format, Object arg) {
		if (loggerError.isEnabledFor(Level.ERROR)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			loggerError.error(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}

	}

	public void error(Marker marker, String msg, Throwable t) {

		loggerError.error(classMsg + msg + "," + t);

	}

	public void error(Marker marker, String format, Object arg1, Object arg2) {
		if (loggerError.isEnabledFor(Level.ERROR)) {
			FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
			loggerError.error(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}

	}

	public void error(Marker marker, String format, Object arg) {

		if (loggerError.isEnabledFor(Level.ERROR)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			loggerError.error(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

	public void error(Marker marker, String msg) {
		loggerError.error(classMsg + msg);
	}

	public void error(String msg, Throwable t) {
		loggerError.error(classMsg + msg + "," + t);
	}

	@Override
	public void error(Marker marker, String format, Object[] argArray) {
		if (loggerError.isEnabledFor(Level.ERROR)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
			loggerError.error(classMsg + ft.getMessage() + "," + ft.getThrowable());
		}
	}

}
