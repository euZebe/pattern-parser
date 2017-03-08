package org.euzebe.patternparser;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PatternParserTest {

	PatternParser util = new PatternParser();

	@Test
	public void should_return_an_empty_optional_when_input_is_null() {
		assertThat(util.replaceValues(null).isPresent()).isFalse();
	}

	@Test
	public void should_return_an_empty_optional_when_input_does_not_start_with_$TODAY() {
		String valueToParse = "bidule";
		assertThat(util.replaceValues(valueToParse).isPresent()).isFalse();
	}

	@Test
	public void should_return_today_when_input_$TODAY() {
		Date date = util.replaceValues(PatternParser.TODAY_PATTERN).get();
		assertThat(toLocalDate(date)).isEqualTo(LocalDate.now());
	}

	@Test
	public void should_return_a_date_in_the_future() {
		String valueToParse = "${TODAY+12D}";

		Optional<Date> replacement = util.replaceValues(valueToParse);
		assertThat(replacement.isPresent()).isTrue();

		LocalDate expectedDate = LocalDate.now() //
				.plusDays(12);

		assertThat(toLocalDate(replacement.get())).isEqualTo(expectedDate);
	}

	@Test
	public void should_return_a_date_in_the_past_when_value_is_like_$TODAY_MINUS_X() {
		String valueToParse = "${TODAY-20D}";

		Optional<Date> replacement = util.replaceValues(valueToParse);
		assertThat(replacement.isPresent()).isTrue();

		LocalDate expectedDate = LocalDate.now() //
				.minusDays(20);

		assertThat(toLocalDate(replacement.get())).isEqualTo(expectedDate);
	}

	@Test
	public void testStringSplitting() {
		String valueToParse = "${TODAY+20D}";

		Pattern pattern = Pattern.compile("\\$\\{TODAY(-|\\+)(\\d+)D\\}");
		Matcher matcher = pattern.matcher(valueToParse);
		while (matcher.find()) {
			System.out.println(matcher.group(1)); // +
			System.out.println(matcher.group(2)); // 20
		}
	}

	private LocalDate toLocalDate(Date date) {
		return date //
				.toInstant() //
				.atZone(ZoneId.systemDefault()) //
				.toLocalDate();
	}
}
