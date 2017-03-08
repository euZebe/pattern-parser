package org.euzebe.patternparser;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;

public class PatternParser {

	private static final String PARTIAL_TODAY_PATTERN = "${TODAY";
	static final String TODAY_PATTERN = PARTIAL_TODAY_PATTERN + "}";

	Optional<Date> replaceValues(Object initialValue) {
		if (initialValue == null) { // failfast: prevent NullPointerException
			return Optional.empty();
		}

		String toReplace = initialValue.toString();

		if (TODAY_PATTERN.equals(initialValue)) {
			return Optional.of(new Date());
		}
		if (toReplace.startsWith(PARTIAL_TODAY_PATTERN)) {
			String operation = toReplace.substring(7, 8);
			String paramDays = toReplace.substring(8, toReplace.indexOf("D}"));
			Integer days = Integer.parseInt(paramDays);
			if (days > 0) {
				if (operation.equals("+")) {
					return Optional.of(DateUtils.addDays(new Date(), days));
				} else if (operation.equals("-")) {
					return Optional.of(DateUtils.addDays(new Date(), -days));
				}
			}
		}
		return Optional.empty();
	}
}
