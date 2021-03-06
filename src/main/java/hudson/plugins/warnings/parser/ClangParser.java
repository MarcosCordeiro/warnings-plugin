package hudson.plugins.warnings.parser;

import java.util.regex.Matcher;

import hudson.Extension;

import hudson.plugins.analysis.util.model.Priority;

/**
 * A parser for the Clang compiler warnings.
 *
 * @author Neil Davis
 */
@Extension
public class ClangParser extends RegexpLineParser {
    private static final long serialVersionUID = -3015592762345283182L;
    private static final String CLANG_WARNING_PATTERN = "^\\s*(?:\\d+%)?([^%]*?):(\\d+):(?:\\d+:)?(?:(?:\\{\\d+:\\d+-\\d+:\\d+\\})+:)?\\s*(warning|.*error):\\s*(.*?)(?:\\[(.*)\\])?$";

    /**
     * Creates a new instance of {@link ClangParser}.
     */
    public ClangParser() {
        super(Messages._Warnings_AppleLLVMClang_ParserName(),
                Messages._Warnings_AppleLLVMClang_LinkName(),
                Messages._Warnings_AppleLLVMClang_TrendName(),
                CLANG_WARNING_PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String filename = matcher.group(1);
        int lineNumber = getLineNumber(matcher.group(2));
        String type = matcher.group(3);
        String message = matcher.group(4);
        String category = matcher.group(5);

        Priority priority;
        if (type.contains("error")) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }
        if (category == null) {
            return createWarning(filename, lineNumber, message, priority);
        }

        return createWarning(filename, lineNumber, category, message, priority);
    }

    @Override
    protected String getId() {
        return "Apple LLVM Compiler (Clang)"; // old ID in serialization
    }
}
