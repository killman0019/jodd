// Copyright (c) 2003-2010, Jodd Team (jodd.org). All Rights Reserved.

package jodd.props;

/**
 * Holds original props value and generated one.
 */
class PropsValue {

	PropsValue(String value) {
		this.value = value;
	}

	/**
	 * Original value.
	 */
	public String value;

	/**
	 * Value with all macros resolved. May be <code>null</code> when
	 * value doesn't contain anything to resolve.
	 */
	public String resolved;

	/**
	 * Returns either resolved or real value.
	 */
	public String getValue() {
		return resolved != null ? resolved : value;
	}

	@Override
	public String toString() {
		return "PropsValue{" + value + (resolved == null ? "" : "}{" + resolved) + '}';
	}
}
