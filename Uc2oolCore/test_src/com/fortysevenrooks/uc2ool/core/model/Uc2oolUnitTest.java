package com.fortysevenrooks.uc2ool.core.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fortysevenrooks.debug.DebugLogger;
import com.fortysevenrooks.uc2ool.core.exceptions.UncheckedModelException;
import com.fortysevenrooks.uc2ool.core.model.Uc2oolModel;
import com.fortysevenrooks.uc2ool.core.model.Uc2oolModel.InputType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.StringContains.containsString;

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
     * Test conversions of the following codepoints to UTF-8
     *
     * 0 - 0x7f
     * 0x80 - 0x7ff
     * 0x800 - 0xffff
     * 0x10000 - 0x1fffff - this range terminates at 0x10FFFF in Unicode 8.
     * 0x200000 - 0x7fffffff - there are currently no codepoints in this range
     * 
     * Specification
     *
     * For each range in each conversion algorithm test:
     *   edges of each conversion range and one within each range
     *   invalid input cases
     *   unassigned code point
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
            assertThat(uce.getLocalizedMessage(),
                       allOf(containsString("Invalid hexadecimal code point"),
                             containsString("0x1fffff")));
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
     * Test conversions of the following codepoints to UTF-16
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
    
    /*
     * Test UTF-8 encoding input.
     * Specification :
     *   valid 1 byte inputs with and w/o prefix "0x" and "0X"
     *   valid 1 bute inputs with leading and/or trailing blanks
     *   valid 2 byte inputs 
     *      with no prefix
     *      with mixture of "0[xX]" prefixed and non-prefixed
     *   valid 3 bytes inputs
     *   valid 4 bytes inputs
     *   
     *   invalid 1 byte sequences
     *   invalid 2 byte sequences
     *   invalid 3 byte sequences
     *   invalid 4 byte sequences
     *   invalid 5 byte sequences
     */
    
    @Test
    public void testUTF8pre1Byte() {
        testUTF8Input("0x41", 65);
    }
    
    @Test
    public void testUTF8preCapX1Byte() {
        testUTF8Input("0X41", 65);
    }

    @Test
    public void testUTF8pre1ByteLeadingBlank() {
        testUTF8Input("  0x41", 65);
    }

    @Test
    public void testUTF8pre1ByteTrailingBlank() {
        testUTF8Input("0x41  ", 65);
    }

    @Test
    public void testUTF8pre1ByteLeadingAndTrailingBlanks() {
        testUTF8Input(" \t0x41  ", 65);
    }

    @Test
    public void testUTF8NoPrere1ByteLow() {
        testUTF8Input("41", 65);
    }
    
    @Test
    public void testUTF8NoPrere1ByteMid() {
        testUTF8Input("65", 101);
    }
    
    @Test
    public void testUTF8NoPrere1ByteHigh() {
        testUTF8Input("7f", 127);
    }
    
    @Test
    public void testUTF8pre2Byte() {
        testUTF8Input("0xc4 0X80", 256);
    }
    
    @Test
    public void testUTF8preCapX2Byte() {
        testUTF8Input("0XC4 0x80", 256);
    }

    @Test
    public void testUTF8NoPre2ByteLow() {
        testUTF8Input("c2 80", 128);
    }
    
    @Test
    public void testUTF8NoPre2ByteMid() {
        testUTF8Input("D0 80", 1024);
    }
    
    @Test
    public void testUTF8NoPre2ByteHigh() {
        testUTF8Input("DF BF", 2047);
    }
    
    @Test
    public void testUTF8NoPre3ByteLow() {
        testUTF8Input("E0 A0 80", 2048);
    }
    
    @Test
    public void testUTF8NoPre3ByteMid() {
        testUTF8Input("E9 8A BF", 37567);
    }
    
    @Test
    public void testUTF8NoPre3ByteHigh() {
        testUTF8Input("EF BF BF", 65535);
    }

    @Test
    public void testUTF8NoPre4ByteLow() {
        testUTF8Input("F0 90 80 80", 65536);
    }
    
    @Test
    public void testUTF8NoPre4ByteMid() {
        testUTF8Input("F3 B4 89 80", 1000000);
    }
    
    @Test
    public void testUTF8NoPre4ByteHigh() {
        testUTF8Input("F4 8F BF BF", 1114111);
    }

    @Test
    public void testUTF8Invalid1Byte() {
        try {
            // Incorrect encoding of 0x82 BREAK PERMITTED HERE character.
            // The correct encoding C2 82.
            testUTF8Input("82", 130);
            fail("Expected exception not thrown");
        } catch (UncheckedModelException uce) {
            // Expected
            assertThat(uce.getLocalizedMessage(),
                       allOf(containsString("Invalid UTF-8 input sequence"),
                             containsString("82")));
        }
    }

    @Test
    public void testUTF8Invalid2ByteA() {
        try {
            // Incorrect encoding of 3071.
            // Correct encoding is E0 AF BF.
            testUTF8Input("EF BF", 3071);
        } catch (UncheckedModelException uce) {
            // Expected
            assertThat(uce.getLocalizedMessage(),
                       allOf(containsString("Invalid UTF-8 input sequence"),
                             containsString("EF BF")));
        }
    }

    @Test
    public void testUTF8Invalid2ByteB() {
        try {
            // Incorrect encoding.
            testUTF8Input("DF 70", 3071);
        } catch (UncheckedModelException uce) {
            // Expected
            assertThat(uce.getLocalizedMessage(),
                       allOf(containsString("Invalid UTF-8 input sequence"),
                             containsString("DF 70")));
        }
    }

    @Test
    public void testUTF8Invalid3ByteA() {
        try {
            // Incorrect encoding of 131071.
            // The correct encoding F0 9F BF BF
            testUTF8Input("FF BF BF", 131071);
            fail("Expected exception not thrown");
        } catch (UncheckedModelException uce) {
            // Expected
            assertThat(uce.getLocalizedMessage(),
                       allOf(containsString("Invalid UTF-8 input sequence"),
                             containsString("FF BF BF")));
        }
    }

    @Test
    public void testUTF8Invalid3ByteB() {
        try {
            // Incorrect encoding
            testUTF8Input("E0 90 BF", 131071);
            fail("Expected exception not thrown");
        } catch (UncheckedModelException uce) {
            // Expected
            assertThat(uce.getLocalizedMessage(),
                       allOf(containsString("Invalid UTF-8 input sequence"),
                             containsString("E0 90 BF")));
        }
    }

    @Test
    public void testUTF8Invalid3ByteC() {
        try {
            // Incorrect encoding of 131071.
            // The correct encoding F0 9F BF BF
            testUTF8Input("E0 BF 70", 131071);
            fail("Expected exception not thrown");
        } catch (UncheckedModelException uce) {
            // Expected
            assertThat(uce.getLocalizedMessage(),
                       allOf(containsString("Invalid UTF-8 input sequence"),
                             containsString("E0 BF 70")));
        }
    }

    @Test
    public void testUTF8Invalid4Byte() {
        try {
            // Incorrect encoding of 1110271.
            // The correct encoding F4 8F 83 BF.
            testUTF8Input("F4 8F C3 BF", 1110271);
            fail("Expected exception not thrown");
        } catch (UncheckedModelException uce) {
            // Expected
            assertThat(uce.getLocalizedMessage(),
                       allOf(containsString("Invalid UTF-8 input sequence"),
                             containsString("F4 8F C3 BF")));
        }
    }
    
    @Test
    public void testUTF8Invalid4Byte2() {
        try {
            // Beyond the range.
            testUTF8Input("F4 9F BF BF", 1179647);
            fail("Expected exception not thrown");
        } catch (UncheckedModelException uce) {
            // Expected
            assertThat(uce.getLocalizedMessage(),
                       allOf(containsString("Invalid UTF-8 input sequence"),
                             containsString("F4 9F BF BF")));
        }
    }

    @Test
    public void testUTF8Invalid5Byte() {
        try {
            // Beyond the range.
            testUTF8Input("F4 9F BF BF 90", 1179647);
            fail("Expected exception not thrown");
        } catch (UncheckedModelException uce) {
            // Expected
            assertThat(uce.getLocalizedMessage(),
                       allOf(containsString("Invalid UTF-8 input sequence"),
                             containsString("F4 9F BF BF 90")));
        }
    }

    /**
     * Core UTF-8 input encoding test routine.
     * 
     * @param utf8 the input UTF-8 encoding
     * @param resultCp
     */
    private void testUTF8Input(String utf8, int resultCp) {
        m_calc.setInput(utf8, InputType.UTF8);
        assertTrue("Invalid input encoding : " + utf8 +
                       ". result=" + m_calc.getCodepoint() + ".",
                   m_calc.getCodepoint() == resultCp);
    }
    
    /*
     *  Test additional exception cases
     */
    @Test
    public void testHCPUBelow0() {
        try {
            m_calc.setInput("-12", InputType.HEXCODEPOINT);
            fail("Excepted exception not thrown");
        } catch (UncheckedModelException uce) {
            // Expected
            assertThat(uce.getLocalizedMessage(),
                       allOf(containsString("Invalid hexadecimal code point"),
                             containsString("-12")));
        }
    }
    
    
}
