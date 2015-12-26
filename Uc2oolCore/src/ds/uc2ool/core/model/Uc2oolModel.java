package ds.uc2ool.core.model;

import java.util.logging.Logger;

import ds.uc2ool.core.exceptions.UncheckedModelException;

/**
 * Uc2oolModel is the model part of the MVC for this application. It handles
 * all Unicode conversion tasks and codepoint validation. Any failures will
 * be reported as UncheckedModelExceptions.
 * 
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class Uc2oolModel {
    public enum InputType {
        CHARACTER,
        UTF8,
        DECCODEPOINT,
        HEXCODEPOINT
    }
    private final int MAX_CODEPOINT = 1114112; // As defined in Unicode 8.
    
	private String m_input;
	private InputType m_type;
	private int m_codepoint;
	
	private Logger m_logger;  // debug logger
	
	public Uc2oolModel(Logger l) {
	    m_logger = l;
	}
	
	/* Push the input string from the user into the calculator
	 * All other methods will drive off this data to produce the
	 * required conversions.
	 * 
	 * @param i the input String
	 */
	public void setInput(String i, InputType type) {
		m_input = i;
		m_type = type;
		
		// Create a codepoint for this input
		switch (type) {
		    case CHARACTER:
		        break;
		    case UTF8:
		        break;
		    case DECCODEPOINT:
		        validateDecimalCodePoint(i);
		        m_codepoint = Integer.valueOf(i);
		        break;
		    case HEXCODEPOINT:
		        validateHexCodePoint(i);
		        m_codepoint = parseHexInput(i);
		        break;
		}
	}
	
	// Validate the input codepoint value for a decimal input
	private void validateDecimalCodePoint(String i) {
	    try {
            int cp = Integer.valueOf(i);
            if (cp < 0 || cp > MAX_CODEPOINT) {
                throw new UncheckedModelException("INV_DEC_CP", i);
            }
        } catch (NumberFormatException e) {
            throw new UncheckedModelException("INV_DEC_CP", i);
        }
	}

	/*
	 * Parse the hex input string
	 */
	private int parseHexInput(String i) {
        String s = 
                i.trim().replaceFirst("0x", "").replaceFirst("U\\+", "");
        return Integer.valueOf(s, 16);
	}
	
    // Validate the input codepoint value for a hex input
	private void validateHexCodePoint(String i) {
        try {
            int cp = parseHexInput(i);
            if (cp < 0 || cp > MAX_CODEPOINT) {
                throw new UncheckedModelException("INV_HEX_CP", i);
            }
        } catch (NumberFormatException e) {
            throw new UncheckedModelException("INV_HEX_CP", i);
        }
	}
	
	/* Get the Unicode string description for the codepoint
	 * 
	 */
	public String getUnicodeCharacterName() {
	    return Character.getName(m_codepoint);
	}
	
	/*
	 * Return a String containing the UTF-16 encoding for the codepoint.
	 */
	public String getUTF16Encoding() {
	    int numChars = Character.charCount(m_codepoint);
	    char utf16[] = Character.toChars(m_codepoint);
	    StringBuffer sb = new StringBuffer();
	    for (int i=0; i < numChars; i++) {
            char a = (char) (utf16[i] >>> 8);
            char b = (char) (utf16[i] & 0x00ff);
            if (a <= 15) sb.append("0");
            sb.append(Integer.toHexString(a));
            if (b <= 15) sb.append("0");
            sb.append(Integer.toHexString(b));
            sb.append(" ");
	    }
	    return sb.toString().toUpperCase().trim();
	}
	
    /*
     * Return a String containing the UTF-8 encoding for the codepoint.
     */
	public String getUTF8Encoding() {
	    // First byte prefix bits
	    final int ONE_BYTE_UTF8_PFX     = 0b00000000;
	    final int TWO_BYTE_UTF8_PFX     = 0b11000000;
	    final int THREE_BYTE_UTF8_PFX   = 0b11100000;
	    final int FOUR_BYTE_UTF8_PFX    = 0b11110000;
	    final int FIVE_BYTE_UTF8_PFX    = 0b11111000;
	    final int SIX_BYTE_UTF8_PFX     = 0b11111100;
	    // Prefix for bytes 2 through 6
	    final int SUBSEQ_BYTE_UTF8_PFX  = 0b10000000;
	    final int SUBSEQ_BYTE_UTF8_MASK = 0b00111111;
	    
	    // Determine the correct number of bytes to carry the encoded form
	    int numBytes = 1;
	    int firstBytePrefix = ONE_BYTE_UTF8_PFX;
	    if (m_codepoint >= 0 && m_codepoint <= 0x7f) {
	        numBytes = 1;
	        firstBytePrefix = ONE_BYTE_UTF8_PFX;
	    } else if (m_codepoint >= 0x80 && m_codepoint <= 0x7ff) {
	        numBytes = 2;
	        firstBytePrefix = TWO_BYTE_UTF8_PFX;
	    } else if (m_codepoint >= 0x800 && m_codepoint <= 0xffff) {
            numBytes = 3;
            firstBytePrefix = THREE_BYTE_UTF8_PFX;
        } else if (m_codepoint >= 0x10000 && m_codepoint <= 0x1fffff) {
            numBytes = 4;
            firstBytePrefix = FOUR_BYTE_UTF8_PFX;
        } else if (m_codepoint >= 0x200000 && m_codepoint <= 0x3ffffff) {
            numBytes = 5;
            firstBytePrefix = FIVE_BYTE_UTF8_PFX;
        } else if (m_codepoint >= 0x4000000 && m_codepoint <= 0x7fffffff) {
            numBytes = 6;
            firstBytePrefix = SIX_BYTE_UTF8_PFX;
        } else {
            throw new IllegalArgumentException(
                    "Invalid code point : " + m_codepoint);
        }
	   
        char[] utf8 = new char[6];
        StringBuffer sb = new StringBuffer();
        int tmpCp = m_codepoint;
        for (int i=numBytes-1; i >= 0; i--) {
            switch(i){
            case 0:
                // Create the byte using lowest 6 bits of value
                utf8[i] = (char)(firstBytePrefix | tmpCp);
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                // Create the byte using lowest 6 bits of value
                utf8[i] = (char)(SUBSEQ_BYTE_UTF8_PFX | (tmpCp & SUBSEQ_BYTE_UTF8_MASK));
                // Shift the value right thus removing the lowest 6 bits
                tmpCp = tmpCp >>> 6;
                break;
            }
        }
        for (int i=0; i < numBytes; i++) {
            char a = (char) (utf8[i] & 0x00ff);
            if (a <= 15) sb.append("0");
            sb.append(Integer.toHexString(a));
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
	}
	
	/**
	 * Get the codepoint value as a decimal in String form.
	 * @return
	 */
	public String getDecimalCodePoint() {
	    return String.valueOf(m_codepoint);
	}
	
	public String getUnicodeCharacter() {
	    if (Character.isSupplementaryCodePoint(m_codepoint)) {
	       char[] c = new char[2];
	       Character.toChars(m_codepoint, c, 0);
	       return new StringBuilder().append(c).toString();
	    }
	    return new StringBuilder().appendCodePoint(m_codepoint).toString();
	}
	
    @Override
    public String toString() {
        return "Uc2ool [m_input=" + m_input + ", m_type=" + m_type + "]";
    }
}
