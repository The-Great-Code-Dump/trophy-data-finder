package uk.tojourn.trophydatafinder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.util.AssertionErrors.assertEquals;


class TrophyDataFinderApplicationTests {

	final String regex = "(?<=_)([a-z]+)";
	final String string = "/images/icons/trophy_platinum.png";

	final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
	final Matcher matcher = pattern.matcher(string);
	@Test
	void regexWorks() {
		String expected = "platinum";
		String input = "/images/icons/trophy_platinum.png";
		String result ="";
		while (matcher.find()) {
			result = matcher.group(0);
		}
		assertEquals(String.format("Test for regex input: %s expected: %s", input, expected), expected, result);


	}

}
