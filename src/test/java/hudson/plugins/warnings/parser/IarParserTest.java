package hudson.plugins.warnings.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * Tests the class {@link IarParser}.
 *
 * @author Ulli Hafner
 */
public class IarParserTest extends ParserTester {
    private static final String TYPE = new IarParser().getGroup();

    /**
     * Parses a file with two IAR warnings.
     *
     * @throws IOException
     *      if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Collection<FileAnnotation> warnings = new IarParser().parse(openFile());

        Iterator<FileAnnotation> iterator = warnings.iterator();

        assertEquals("Wrong number of warnings detected.", 9, warnings.size());
        checkWarning(iterator.next(), 3, "an inline function cannot be root as well",
                "/tmp/x/icc-error-memory.c", TYPE, "Be031", Priority.HIGH);
    }

    /**
     * Parses a file with one IAR warning in the new 6.3 format.
     *
     * @throws IOException
     *      if the file could not be read
     */
    @Test
    public void testWarningsParserEwarm() throws IOException {
        Collection<FileAnnotation> warnings = new IarParser().parse(openFile("iar-ewarm-6.3.txt"));

        Iterator<FileAnnotation> iterator = warnings.iterator();

        assertEquals("Wrong number of warnings detected.", 1, warnings.size());
        checkWarning(iterator.next(), 43, "variable \"pgMsgEnv\" was declared but never referenced",
                "C:/dev/bsc/daqtask.c", TYPE, "Pe177", Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "iar-nowrap.log";
    }
}

