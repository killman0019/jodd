// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.log.impl;

import jodd.log.Logger;
import jodd.log.LoggerProvider;

/**
 * Provider for {@link jodd.log.impl.SimpleLogger} adapter.
 */
public class SimpleLoggerProvider implements LoggerProvider {

	private final Logger.Level globalLevel;
	private final long startTime;

	public SimpleLoggerProvider(Logger.Level globalLevel) {
		this.globalLevel = globalLevel;
		this.startTime = System.currentTimeMillis();
	}

	/**
	 * Returns global level.
	 */
	public Logger.Level getLevel() {
		return globalLevel;
	}

	/**
	 * Returns elapsed time in milliseconds.
	 */
	public long getElapsedTime() {
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Logger createLogger(String name) {
		return new SimpleLogger(this, name);
	}

	/**
	 * Returns called class.
	 */
	protected String getCallerClass() {
		Exception exception = new Exception();

		StackTraceElement[] stackTrace = exception.getStackTrace();

		for (StackTraceElement stackTraceElement : stackTrace) {
			String className = stackTraceElement.getClassName();
			if (className.equals(SimpleLoggerProvider.class.getName())) {
				continue;
			}
			if (className.equals(SimpleLogger.class.getName())) {
				continue;
			}
			return shortenClassName(className)
				+ '.' + stackTraceElement.getMethodName()
				+ ':' + stackTraceElement.getLineNumber();
		}
		return "N/A";
	}

	/**
	 * Returns shorten class name.
	 */
	protected String shortenClassName(String className) {
		int lastDotIndex = className.lastIndexOf('.');
		if (lastDotIndex == -1) {
			return className;
		}

		StringBuilder shortClassName = new StringBuilder(className.length());

		int start = 0;
		while(true) {
			shortClassName.append(className.charAt(start));

			int next = className.indexOf('.', start);
			if (next == lastDotIndex) {
				break;
			}
			start = next + 1;
			shortClassName.append('.');
		}
		shortClassName.append(className.substring(lastDotIndex));

		return shortClassName.toString();
	}

}