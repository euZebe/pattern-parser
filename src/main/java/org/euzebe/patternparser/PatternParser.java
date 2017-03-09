package org.euzebe.patternparser;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternParser {

	private static final String PARTIAL_TODAY_PATTERN = "${TODAY";
	static final String TODAY_PATTERN = PARTIAL_TODAY_PATTERN + "}";
	private static final Pattern pattern = Pattern.compile("\\$\\{TODAY(-|\\+)(\\d+)D\\}");

	Optional<LocalDate> replaceValues(Object initialValue) {
		if (initialValue == null) { // failfast: prevent NullPointerException
			return Optional.empty();
		}

		if (TODAY_PATTERN.equals(initialValue)) {
			return Optional.of(LocalDate.now());
		}

       return parseValue(initialValue.toString());
	}

    private Optional<LocalDate> parseValue(String initialValue) {
        Matcher matcher = pattern.matcher(initialValue);
        if (matcher.find()) {
            Long signedOffset = Long.parseLong(matcher.group(1) + matcher.group(2)); // group1 = sign, group2 = number
            LocalDate resultDate = LocalDate.now().plusDays(signedOffset);
            return Optional.of(resultDate);
        }
        return Optional.empty();
    }
}
