package ds.uc2ool.core.model;

import java.util.logging.Logger;
import java.util.regex.Pattern;

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
    
    // First byte prefix bits
    private final int ONE_BYTE_UTF8_PFX     = 0b00000000;
    private final int TWO_BYTE_UTF8_PFX     = 0b11000000;
    private final int THREE_BYTE_UTF8_PFX   = 0b11100000;
    private final int FOUR_BYTE_UTF8_PFX    = 0b11110000;
    // Prefix for bytes 2 through 4
    private final int SUBSEQ_BYTE_UTF8_PFX  = 0b10000000;
    private final int SUBSEQ_BYTE_UTF8_MASK = 0b00111111;    

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
		        m_codepoint = convertUTF8ToCodepoint(i);
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
	
	private int convertUTF8ToCodepoint(String i) {
	    i = i.trim();
	    
	    // Check that we have 1, 2, 3 or 4 hex pairs
	    final String hp = "(?:0[xX]){0,1}";
	    final String h1 = "[0-9a-fA-F]";
	    final String hex1byte = hp + h1 + "{2}";
        final String hex2byte = hex1byte + "\\s+" + hex1byte;
        final String hex3byte = hex2byte + "\\s+" + hex1byte;
        final String hex4byte = hex3byte + "\\s+" + hex1byte;
        Pattern pHex1byte = Pattern.compile(hex1byte);
        Pattern pHex2byte = Pattern.compile(hex2byte);
        Pattern pHex3byte = Pattern.compile(hex3byte);
        Pattern pHex4byte = Pattern.compile(hex4byte);
        
        // Check for the correct first byte encoding for the number of hex pairs
        String inp = 
                i.replaceAll("\\s", "").replace("0x",  "").replace("0X",  "");
        int len = inp.length();
        String firstByte = inp.substring(0,2);
        int hInt = Integer.valueOf(firstByte, 16);
        
        // Run the checks and convert it to an integer codepoint if ok
	    if ((pHex1byte.matcher(i).matches() ||
	         pHex2byte.matcher(i).matches() ||
	         pHex3byte.matcher(i).matches() ||
	         pHex4byte.matcher(i).matches()) &&
	        ((((hInt & ONE_BYTE_UTF8_PFX) == ONE_BYTE_UTF8_PFX) && len == 2) ||
	         (((hInt & TWO_BYTE_UTF8_PFX) == TWO_BYTE_UTF8_PFX) && len == 4) ||
	         (((hInt & THREE_BYTE_UTF8_PFX) == THREE_BYTE_UTF8_PFX) && len == 6) ||
             (((hInt & FOUR_BYTE_UTF8_PFX) == FOUR_BYTE_UTF8_PFX) && len == 8)))
	    {
	        int cp = 0;
	        
	        // Handle the first hex byte
	        switch (len) {
	        case 2:
	            cp = hInt &  ~ONE_BYTE_UTF8_PFX;
	            break;
	        case 4:
                cp = hInt & ~TWO_BYTE_UTF8_PFX;
	            break;
	        case 6:
                cp = hInt & ~THREE_BYTE_UTF8_PFX;
	            break;
	        case 8:
                cp = hInt & ~FOUR_BYTE_UTF8_PFX;
	            break;
	        }

	        for (int j = 2; j < len; j = j + 2) {
	            String h = inp.substring(j, j + 2);
	            int hi = Integer.valueOf(h, 16);
	            cp = cp << 6;
	            cp |= (hi & SUBSEQ_BYTE_UTF8_MASK);
	        }
	        return cp;
	    }
	        
	    throw new UncheckedModelException("INV_UTF8_ENCODING", i);
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
