package ueh.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;

public class HtmlTagValidator {

    private Pattern pattern;
    private Matcher matcher;
    private static final String HTML_TAG_FORMAT_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";

    public HtmlTagValidator() {
        pattern = Pattern.compile(HTML_TAG_FORMAT_PATTERN);
    }
  
    public boolean validate(final String tag) {
        matcher = pattern.matcher(tag);
        return matcher.matches();
    }

    public static boolean validateWithStack(String rawHtml) {
        if (rawHtml == null || rawHtml.isEmpty()) {
            return false;
        }

        Stack<String> tagStack = new Stack<>();
        boolean insideTag = false;
        StringBuilder currentTag = new StringBuilder();

        try {
            for (int i = 0; i < rawHtml.length(); i++) {
                char currentChar = rawHtml.charAt(i);

                if (currentChar == '<') {
                    insideTag = true;
                    currentTag.setLength(0);
                }

                if (insideTag) {
                    currentTag.append(currentChar);
                }

                if (currentChar == '>') {
                    insideTag = false;
                    String tag = currentTag.toString().trim();

                    if (tag.startsWith("</")) {
                        if (tagStack.isEmpty()) {
                            return false;
                        }
                        String openingTag = tagStack.pop();
                        String closingTag = tag.substring(2, tag.length() - 1).trim();
                        if (!openingTag.equals(closingTag)) {
                            return false;
                        }
                    } else if (tag.startsWith("<") && !tag.endsWith("/>")) {
                        String openingTag = tag.substring(1, tag.length() - 1).trim();
                        tagStack.push(openingTag);
                    }
                }
            }

            return tagStack.isEmpty();

        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        HtmlTagValidator validator = new HtmlTagValidator();
        
        // System.out.println(validator.validateWithStack("<div>Valid HTML Tag</div>"));  // Output: true
        // System.out.println(validator.validateWithStack("<div>Invalid HTML Tag"));    // Output: false
    }
}
