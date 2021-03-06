/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package org.agnitas.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class AgnTagUtils {
	private static final Pattern AGN_TAG_PATTERN = Pattern.compile("\\[agn[^]]+]");
	private static final Pattern ESCAPED_AGN_TAG_PATTERN = Pattern.compile("\\[agn.*?=&quot;.*?&quot;/?]");
	private static final String DEFAULT_DYN_NAME = "BLANK_DYN_NAME";
	
	public static String escapeAgnTags(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}

		StringBuffer sb = new StringBuffer();
		Matcher matcher = AGN_TAG_PATTERN.matcher(text);

		while (matcher.find()) {
			matcher.appendReplacement(sb, StringEscapeUtils.escapeHtml4(matcher.group()));
		}
		matcher.appendTail(sb);

		return sb.toString();
	}

	public static List<String> getParametersForTag(final String tagName) {
		return ListUtils.union(getMandatoryParametersForTag(tagName), getOptionalParametersForTag(tagName));
	}

	public static List<String> getMandatoryParametersForTag(String tagName) {
		if (tagName != null) {
			switch (tagName) {
				case "agnDB":
					return Collections.singletonList("column");

				case "agnTITLE":
				case "agnTITLEFULL":
				case "agnTITLEFIRST":
					return Collections.singletonList("type");

				case "agnIMGLINK":
				case "agnFORM":
				case "agnDYN":
					return Collections.singletonList("name");

				case "agnVOUCHER":
					return Arrays.asList("name");

				default:
					return Collections.emptyList();
			}
		} else {
			return Collections.emptyList();
		}
	}

	public static List<String> getOptionalParametersForTag(final String tagName) {
		final String safeTagName = StringUtils.defaultIfEmpty(tagName, "");
		switch (safeTagName) {
			case "agnVOUCHER":
				return Collections.singletonList("default");

			default:
				return Collections.emptyList();
		}
	}

	public static String unescapeAgnTags(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}

		StringBuffer sb = new StringBuffer();
		Matcher matcher = ESCAPED_AGN_TAG_PATTERN.matcher(text);

		while (matcher.find()) {
			matcher.appendReplacement(sb, StringEscapeUtils.unescapeHtml4(matcher.group()));
		}
		matcher.appendTail(sb);

		return sb.toString();
	}

	public static String toSafeDynName(String name) {
		return toSafeDynName(name, "", DEFAULT_DYN_NAME);
	}
	
	public static String toSafeDynName(String name, String squareBracketsPlaceholder) {
		return toSafeDynName(name, squareBracketsPlaceholder, DEFAULT_DYN_NAME);
	}
	
	public static String toSafeDynName(String name, String squareBracketsPlaceholder, String defaultName) {
		if (StringUtils.isBlank(name)) {
			return defaultName;
		}
		
		String placeHolder = squareBracketsPlaceholder
				.replace("[", "")
				.replace("]", "");

		name = name.replace("\"", "&quot;")
				.replace("\'", "&apos;")
				.replace("[", placeHolder)
				.replace("]", placeHolder);
		
		return StringUtils.isBlank(name) ? defaultName : name;
	}
}
