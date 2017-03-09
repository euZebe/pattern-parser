package org.euzebe.patternparser;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternParser {

	private static final String PARTIAL_TODAY_PATTERN = "${TODAY";
	static final String TODAY_PATTERN = PARTIAL_TODAY_PATTERN + "}";

	Optional<Date> replaceValues(Object initialValue) {
		if (initialValue == null) { // failfast: prevent NullPointerException
			return Optional.empty();
		}

		if (TODAY_PATTERN.equals(initialValue)) {
			return Optional.of(new Date());
		}

       return parseValue(initialValue.toString());
	}

    private Optional<Date> parseValue(String initialValue) {
        Pattern pattern = Pattern.compile("\\$\\{TODAY(-|\\+)(\\d+)D\\}");
        Matcher matcher = pattern.matcher(initialValue);
        if (matcher.find()) {
            Long signedOffset = Long.parseLong(matcher.group(1) + matcher.group(2)); // group1 = sign, group2 = number
            LocalDate resultDate = LocalDate.now().plusDays(signedOffset);
            Date date = Date.from(resultDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            return Optional.of(date);
        }
        return Optional.empty();
    }
}
