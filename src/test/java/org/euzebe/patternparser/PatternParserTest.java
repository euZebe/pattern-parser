package org.euzebe.patternparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

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
        Pattern pattern = Pattern.compile("\\$\\{TODAY(-|\\+)(\\d+)D\\}");

        String positiveValue = "${TODAY+20D}";
        Matcher positiveMatcher = pattern.matcher(positiveValue);
        positiveMatcher.find();
        int parsedPositiveInt = Integer.parseInt(positiveMatcher.group(1) + positiveMatcher.group(2));
        assertThat(parsedPositiveInt).isEqualTo(20);


        String negativeValue = "${TODAY-26D}";
        Matcher negativeMatcher = pattern.matcher(negativeValue);
        negativeMatcher.find();
        int parsedNegativeInt = Integer.parseInt(negativeMatcher.group(1) + negativeMatcher.group(2));
        assertThat(parsedNegativeInt).isEqualTo(-26);

    }

    private LocalDate toLocalDate(Date date) {
        return date //
                .toInstant() //
                .atZone(ZoneId.systemDefault()) //
                .toLocalDate();
    }
}
