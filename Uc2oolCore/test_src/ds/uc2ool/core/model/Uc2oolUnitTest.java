package ds.uc2ool.core.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ds.debug.DebugLogger;
import ds.uc2ool.core.exceptions.UncheckedModelException;
import ds.uc2ool.core.model.Uc2oolModel;
import ds.uc2ool.core.model.Uc2oolModel.InputType;

/**
 * These are the core {@link Uc2oolModel} tests testing basic conversion and
 * validation functions.
 * 
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class Uc2oolUnitTest {

    private Uc2oolModel m_calc;
    
    @Before
    public void setUp() throws Exception {
        m_calc = new Uc2oolModel(new DebugLogger("Uc2oolUnitTest",
                                                 "uc2oolTests%g.log"));
    }

    @After
    public void tearDown() throws Exception {
        m_calc = null;
    }

    /*
     *  Test Specification
     *
     * For each range in each conversion algorithm test:
     *   edges of each conversion range and one within each range
     *   invalid input cases
     *   unassigned code point
     */
    
    /*
     *  Test conversions of the following codepoints to UTF-8
     *
     * 0 - 0x7f
     * 0x80 - 0x7ff
     * 0x800 - 0xffff
     * 0x10000 - 0x1fffff - this range terminates at 0x10FFFF in Unicode 8.
     * 0x200000 - 0x3ffffff - there are currently no codepoints in this range
     * 0x4000000 - 0x7fffffff - there are currently no codepoints in this range
     */
    
    /*
     * Hexadecimal input tests
     */
    
    @Test
    public void testHCPUTF81ByteLow() {
        // NULL
        testCPUTF8Conversions("0", InputType.HEXCODEPOINT, "00");
    }

    @Test
    public void testHCPUTF81Byte() {
        // LATIN CAPITAL LETTER A
        testCPUTF8Conversions("41", InputType.HEXCODEPOINT, "41");
    }

    @Test
    public void testHCPUTF81ByteHigh() {
        // DELETE
        testCPUTF8Conversions("7f", InputType.HEXCODEPOINT, "7F");
    }
    
    @Test
    public void testHCPUTF82BytesLow() {
        // <UNNAMED - seems to be Euro character
        testCPUTF8Conversions("80", InputType.HEXCODEPOINT, "C2 80");
    }
    
    @Test
    public void testHCPUTF82Bytes() {
        // ARABIC NUMBER SIGN
        testCPUTF8Conversions("600", InputType.HEXCODEPOINT, "D8 80");
    }
    
    @Test
    public void testHCPUTF82BytesHigh() {
        // <UNNAMED>
        testCPUTF8Conversions("7ff", InputType.HEXCODEPOINT, "DF BF");
    }
    
    @Test
    public void testHCPUTF83BytesLow() {
        // SAMARITAN LETTER ALAF
        testCPUTF8Conversions("800", InputType.HEXCODEPOINT, "E0 A0 80");
    }
    
    @Test
    public void testHCPUTF83Bytes() {
        // HEBREW LETTER WIDE LAMED
        testCPUTF8Conversions("fb25", InputType.HEXCODEPOINT, "EF AC A5");
    }
    
    @Test
    public void testHCPUTF83BytesHigh() {
        // <UNNAMED>
        testCPUTF8Conversions("ffff", InputType.HEXCODEPOINT, "EF BF BF");
    }
    
    @Test
    public void testHCPUTF84BytesLow() {
        // LINEAR B SYLLABLE B008 A
        testCPUTF8Conversions("10000", InputType.HEXCODEPOINT, "F0 90 80 80");
    }
    
    @Test
    public void testHCPUTF84Bytes() {
        // UGARITIC LETTER HO
        testCPUTF8Conversions("10385",
                               InputType.HEXCODEPOINT, "F0 90 8E 85");
    }
    
    @Test
    public void testHCPUTF84BytesLimit() {
        // LAST CODE POINT VALUE
        testCPUTF8Conversions("0x01ffff", 
                               InputType.HEXCODEPOINT, "F0 9F BF BF");
    }

    @Test
    public void testHCPUTF84BytesHigh() {
        try {
            // NOT ASSIGNED
            testCPUTF8Conversions("0x1fffff",
                                   InputType.HEXCODEPOINT, "F7 BF BF BF");
        } catch (UncheckedModelException uce) {
            // Expected
            assertTrue("Got : " + uce.getLocalizedMessage(),
                       uce.getLocalizedMessage().equals(
                           "Invalid hexadecimal code point 0x1fffff."));
        }
    }
    
    /*
     * Decimal input tests
     */

    @Test
    public void testDCPUTF81ByteLow() {
        // NULL
        testCPUTF8Conversions("0", InputType.DECCODEPOINT, "00");
    }

    @Test
    public void testDCPUTF81Byte() {
        // LATIN CAPITAL LETTER A
        testCPUTF8Conversions("65", InputType.DECCODEPOINT, "41");
    }

    @Test
    public void testDCPUTF81ByteHigh() {
        // DELETE
        testCPUTF8Conversions("127", InputType.DECCODEPOINT, "7F");
    }
    
    @Test
    public void testDCPUTF82BytesLow() {
        // <UNNAMED - seems to be Euro character
        testCPUTF8Conversions("128", InputType.DECCODEPOINT, "C2 80");
    }
    
    @Test
    public void testDCPUTF82Bytes() {
        // ARABIC NUMBER SIGN
        testCPUTF8Conversions("1536", InputType.DECCODEPOINT, "D8 80");
    }
    
    @Test
    public void testDCPUTF82BytesHigh() {
        // <UNNAMED>
        testCPUTF8Conversions("2047", InputType.DECCODEPOINT, "DF BF");
    }
    
    @Test
    public void testDCPUTF83BytesLow() {
        // SAMARITAN LETTER ALAF
        testCPUTF8Conversions("2048", InputType.DECCODEPOINT, "E0 A0 80");
    }
    
    @Test
    public void testDCPUTF83Bytes() {
        // HEBREW LETTER WIDE LAMED
        testCPUTF8Conversions("64293", InputType.DECCODEPOINT, "EF AC A5");
    }
    
    @Test
    public void testDCPUTF83BytesHigh() {
        // <UNNAMED>
        testCPUTF8Conversions("65535", InputType.DECCODEPOINT, "EF BF BF");
    }
    
    @Test
    public void testDCPUTF84BytesLow() {
        // LINEAR B SYLLABLE B008 A
        testCPUTF8Conversions("65536", InputType.DECCODEPOINT, "F0 90 80 80");
    }
    
    @Test
    public void testDCPUTF84Bytes() {
        // UGARITIC LETTER HO
        testCPUTF8Conversions("66437",
                               InputType.DECCODEPOINT, "F0 90 8E 85");
    }
    
    @Test
    public void testDCPUTF84BytesLimit() {
        // LAST CODE POINT VALUE
        testCPUTF8Conversions("131071", 
                               InputType.DECCODEPOINT, "F0 9F BF BF");
    }

    /*
     * Core test method for all UTF-8 conversions and boundary conditions
     * 
     * @param i the input string to convert
     * @param type the InputType
     * @param result the expected result from <code>getUTF8Encoding()</code>
     * call
     */
    private void testCPUTF8Conversions(String i,
                                        InputType type, 
                                        String result) {
        m_calc.setInput(i, type);
        System.out.println("*" + m_calc.getUTF8Encoding() + "*");
        assertTrue(m_calc.getUTF8Encoding(),
                   m_calc.getUTF8Encoding().equals(result));
    }
    
    /*
     *  Test conversions of the following codepoints to UTF-16
     * 0 - 0xffff
     * 0x10000 - 0x10FFFF terminates here in Unicode 8
     */
    @Test
    public void testHCPUTF161IntLow() {
        // NULL
        testHCPUTF16Conversions("0", InputType.HEXCODEPOINT, "0000");
    }

    @Test
    public void testHCPUTF161Int() {
        // LATIN CAPITAL LETTER A
        testHCPUTF16Conversions("41", InputType.HEXCODEPOINT, "0041");
    }

    @Test
    public void testHCPUTF161IntHigh() {
        // <UNNAMED>
        testHCPUTF16Conversions("0xffff", InputType.HEXCODEPOINT, "FFFF");
    }
    
    @Test
    public void testHCPUTF162IntLow() {
        // LINEAR B SYLLABLE B008 A
        testHCPUTF16Conversions("10000", InputType.HEXCODEPOINT, "D800 DC00");
    }
    
    @Test
    public void testHCPUTF162Int() {
        // BRAHMI SIGN CANDRABINDU
        testHCPUTF16Conversions("11000", InputType.HEXCODEPOINT, "D804 DC00");
    }
    
    @Test
    public void testHCPUTF162IntHigh() {
        // <UNNAMED>
        testHCPUTF16Conversions("0x10ffff",
                                InputType.HEXCODEPOINT, "DBFF DFFF");
    }
    
    /*
     * Core test method for the UTF-16 conversions and boundary conditions
     * 
     * @param i the input string to convert
     * @param type the InputType
     * @param result the expected result from <code>getUTF16Encoding()</code>
     * call
     */
    private void testHCPUTF16Conversions(String i,
                                         InputType type,
                                         String result) {
        m_calc.setInput(i, type);
        System.out.println("*" + m_calc.getUTF16Encoding() + "*");
        assertTrue(m_calc.getUTF16Encoding(),
                   m_calc.getUTF16Encoding().equals(result));
    }
    
    // Test exception cases
    @Test
    public void testHCPUBelow0() {
        try {
            m_calc.setInput("-12", InputType.HEXCODEPOINT);
            fail("Excepted exception not thrown");
        } catch (UncheckedModelException uce) {
            // Expected
            assertTrue("Got : " + uce.getLocalizedMessage(),
                       uce.getLocalizedMessage().equals(
                           "Invalid hexadecimal code point -12."));
        }
    }
    
    
}
