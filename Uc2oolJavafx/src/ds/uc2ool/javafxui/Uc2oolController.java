package ds.uc2ool.javafxui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import ds.debug.DebugLogger;
import ds.uc2ool.core.exceptions.Uc2oolFatalException;
import ds.uc2ool.core.exceptions.Uc2oolRuntimeException;
import ds.uc2ool.core.model.Uc2oolModel;
import ds.uc2ool.core.model.Uc2oolModel.InputType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Uc2oolController is the primary controller for the Uc2ool application. It
 * works with the Uc2ool.fxml JavaFX UI configuration. In case it was not
 * already obvious this application is a Model View Controller (MVC)
 * application. This is the controller.
 * 
 * Uc2ool is a Unicode tool which allows you to examine different UTF encodings
 * for Unicode characters, seeing glyphs for particular codepoints in any font,
 * and allows you to determine the codepoint for a keyboard entered Unicode
 * character. Other function will be added as time and requirements dictate.
 * 
 * It is all written in JavaFX and Java 8 and is written for Unicode version 8.
 * 
 * Exception handling is done using unchecked exceptions to minimise declaration
 * of throws and error catching code. Instead all error translation occurs as
 * close to the site of the initial exception. An unchecked exception with
 * appropriate error message is thrown immediately. There is a general exception
 * handling method in this module which will handle logging of error information
 * and display of error messages to the user, through the
 * <code>ErrorController</code> and <code>ErrorDialog.fxml</code>-defined 
 * dialog box.
 * 
 * All public methods that might be called from the UI must surround their
 * code in a try/catch which calls the exception handler method,
 * <code>Uc2oolController</code>.
 * 
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class Uc2oolController {
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle m_resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL m_location;

	@FXML //  fx:id="m_process"
	private Button m_process; // Value injected by FXMLLoader
	
	@FXML //  fx:id="m_inputCharacter"
	private TextField m_inputCharacter; // Value injected by FXMLLoader
	
	// RadioButton set for input types
	@FXML // fx:id="m_inputType"
	private ToggleGroup m_inputType;
	@FXML // fx:id="m_characterRB"
    private RadioButton m_characterRB;
	@FXML // fx:id="m_hexCodePoint"
    private RadioButton m_hexCodePointRB;
    @FXML // fx:id="m_decimalCodePoint"	
    private RadioButton m_decimalCodePointRB;
    @FXML // fx:id="m_UTF8Encoding"     
    private RadioButton m_UTF8EncodingRB;
    
    @FXML // fx:id="m_unicodeName"
    private Label m_unicodeName;
    
    @FXML // fx:id="m_unicodeName"
    private Label m_utf16Encoding;
    
    @FXML // fx:id="m_unicodeName"
    private Label m_utf8Encoding;
    
    @FXML // fx:id="m_unicodeName"
    private Label m_decimalCodePoint;
    
    @FXML // fx:id="m_glyph"
    private TextArea m_glyph;
    
    @FXML // fx:id="m_font"
    private ComboBox<String> m_font;
    private final String DEFAULT_FONT_NAME = "Cardo";
    private String m_fontValue = DEFAULT_FONT_NAME;
    
    @FXML // fx:id="m_fontSize"
    private ComboBox<String> m_fontSize;
    private final String DEFAULT_FONT_SIZE = "80";
    private double m_fontSizeValue = Double.valueOf(DEFAULT_FONT_SIZE);
    
    @FXML // fx:id="m_statusBar"
    private Label m_statusBar;
    /**
     * Instances of the Status class provide support for storing one or more
     * status messages and their corresponding arguments. They are stored in
     * the order they are added. These messages are assumed to use messages
     * Ids which are known to the client. The client is expected to handle
     * looked of the message in an appropriate bundle and to apply the
     * arguments to it. Status objects merely provide storage. 
     * 
     * There is no multi-threading support.
     * 
     * @author	Daniel Semler
     * @version	%I%, %G%
     * @since	1.0
     */
    private class Status {
        List<String> m_msgIds;
        List<List<Object>> m_args;
        
        Status() {
            m_msgIds = new ArrayList<String>();
            m_args = new ArrayList<List<Object>>();
        };
        
        /**
         * Add a status message and arguments to this Status
         * 
         * @param msgId the message Id for this status message
         * @param args a list of arguments required by the message
         */
        void add(String msgId, Object... args) {
            m_msgIds.add(msgId);
            m_args.add(Arrays.asList(args));
        }
        
        /**
         * Clear down the status object
         */
        void clear() {
            m_msgIds = new ArrayList<String>();
            m_args = new ArrayList<List<Object>>();
        }
        
        /**
         * Get the message Id
         * 
         * @param index the index of the message Id to return
         */
        String getId(int index) {
            return m_msgIds.get(index);
        }
        
        /**
         * Get the message arguments as an Object[].
         * 
         * @param index the specific message argument List object
         */
        Object[] getArgs(int index) {
            return m_args.get(index).toArray();
        }
        
        /**
         * Determine whether there are any status messages to display.
         * 
         * @return true if there are no status messages, false otherwise
         */
        boolean isEmpty() {
            return m_msgIds.isEmpty();
        }
    }
    private Status m_status = new Status();
    
    // The calculator for doing all the conversions
	private Uc2oolModel m_model;
	
	// Debug logger
	private Logger m_logger;
	private final static String LOGGER_NAME = "ds.uc2ool";
	private final static String CLASS_NAME =
	        Uc2oolController.class.getName();
	private final static String DEBUG_FILE_NAME = "%t/uc2ool%g.log";
	
	// Message bundle
    private final static String RESOURCE_BUNDLE_NAME =
            "ds.uc2ool.javafxui.resources.Messages";

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        try {
            m_logger = new DebugLogger(LOGGER_NAME, DEBUG_FILE_NAME);
            
            m_logger.entering(CLASS_NAME, "initialize");
            
            verifyLoaderInitialzation();
            
            initializeUIElements();
            
            connectToCalculator();
            
            // set the exception handler for the JavaFX application thread
            Thread.currentThread().setUncaughtExceptionHandler(
                    (Thread thr, Throwable t) -> {
                        handleException(t);
            });
            
            m_logger.exiting(CLASS_NAME, "initialize");
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Initialize any UI elements that need to be set up.
     */
    private void initializeUIElements() {
        /* Initialize the radio buttons for indicating the input string
         * interpretation.
         */ 
        if (m_inputType != null) {
            StringBuilder sb = new StringBuilder("m_inputType buttons:\n");
            m_characterRB.setUserData(InputType.CHARACTER);
            m_hexCodePointRB.setUserData(InputType.HEXCODEPOINT);
            m_decimalCodePointRB.setUserData(InputType.DECCODEPOINT);
            m_UTF8EncodingRB.setUserData(InputType.UTF8);
            
            m_inputType.getToggles().forEach(
                    (rb) -> {sb.append("  ").append(rb.toString()).
                             append(" ").append(rb.isSelected()).
                             append("\n");});
            m_logger.log(Level.FINEST, sb.toString());
        }
        
        /* Initialize the combobox for the font used for displaying the
         * glyph. Set up a listener for changes in the property value.
         */
        if (m_font != null) {
            // Get and sort all font names on the system and add
            // them to the m_font ComboBox.
            List<String> fonts = Font.getFontNames();
            Collections.sort(fonts);
            m_font.getItems().addAll(fonts);
            m_font.valueProperty().addListener(
                new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> o, String ov, String nv) {
                            m_fontValue = nv;
                            m_glyph.setFont(new Font(m_fontValue, m_fontSizeValue));
                        }
            });

            m_font.setValue(DEFAULT_FONT_NAME);
            
        }
        
        /* Set up the font size combo box with a range of values to choose
         * from, and set the initial value. Add a listener to observe
         * changes in selection.
         */
        if (m_fontSize != null) {
            m_fontSize.getItems().addAll("5", "7", "9", "12", "14", "16",
                                         "18", "20", "24", "28", "32", "40",
                                         "45", "52", "60", "72", "80", "100",
                                         "130");
            m_fontSize.valueProperty().addListener(
                new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> o, String ov, String nv) {
                            m_fontSizeValue = Double.valueOf(nv);
                            m_glyph.setFont(new Font(m_fontValue, m_fontSizeValue));
                        }
            });
            m_fontSize.setValue(DEFAULT_FONT_SIZE);
        }
        
        /*
         * Clear the status bar.
         */
        if (m_statusBar != null) {
            m_statusBar.setText("");
        }
    }

    private String getLocalizedMessage(String msgId, Object... args) {
        ResourceBundle mb = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
        String msg = mb.getString(msgId);
        return new StringBuilder(
                String.format(msg, args)).toString();
    }
    
    /**
     * I am not sure I need this kind of validation but if it fails
     * it will throw Uc2oolFatalException which will log and then System.exit().
     */
    private void verifyLoaderInitialzation() {
        if (m_process == null) {
            throw new Uc2oolFatalException("INIT_FAILED",
                                           "fx:id=\"m_process\"",
                                           "uc2ool.fxml");
        }
        if (m_inputCharacter == null) {
            throw new Uc2oolFatalException("INIT_FAILED", 
                                           "fx:id=\"m_inputCharacter\"",
                                           "uc2ool.fxml");
        }
    }

    /* Called when the Process button is fired.
     * When the application is running this is the primary processing
     * API which is called.
     *
     * @param event the action event.
     */
    @FXML
    void processFired(ActionEvent event) {
    	try {
            if (m_inputCharacter != null) {
            	m_logger.log(Level.FINEST, m_inputCharacter.getText());
            	
            	// Clear status bar
            	m_status.clear();
            	
            	// Get the input type and prime Uc2oolModel
            	InputType type = 
            	    (InputType)
            	        ((RadioButton)m_inputType.getSelectedToggle()).
            	        getUserData();
            	m_model.setInput(m_inputCharacter.getText(), type);
            	m_logger.log(Level.FINEST, m_model.toString());
            	
            	// Now populate output display fields with data from the 
            	// Uc2oolModel
            	m_unicodeName.setText(m_model.getUnicodeCharacterName());
            	m_utf16Encoding.setText(m_model.getUTF16Encoding());
            	m_utf8Encoding.setText(m_model.getUTF8Encoding());
            	m_decimalCodePoint.setText(m_model.getDecimalCodePoint());
            	m_glyph.setFont(new Font(m_fontValue, m_fontSizeValue));
            	m_glyph.clear();
            	m_glyph.appendText(getUnicodeCharacter());
            	
            	// Paint status last in case any operations have posted a
            	// status update.
            	if (!m_status.isEmpty()) {
                	m_statusBar.setText(
                	    getLocalizedMessage(m_status.getId(0),
                	                        m_status.getArgs(0)));
            	} else {
            	    m_statusBar.setText("");
            	}
            }
        } catch (Exception cre) {
            handleException(cre);
        }
    }

    private String getUnicodeCharacter() {
        java.awt.Font awtFont = 
                new java.awt.Font(m_fontValue,
                                  java.awt.Font.PLAIN,
                                  new Double(m_fontSizeValue).intValue());
        if (!awtFont.canDisplay(m_model.getCodepoint())) {
            m_status.add("NO_GLYPH", new Object[] {});
            return new String("");
        }
        return m_model.getUnicodeCharacter();
    }
    
    /* Handle exception popping up an error dialog to the user and logging
     * the error to the diagnostic log if required.
     * 
     * @param e the Exception to handle
     */
    private void handleException(Throwable t) {
        
        // Return codes that may be processed at the command line.
        final int RC_OK            =  0;
        final int RC_UNHANDLED_EXC = -1;
        final int RC_FATAL_EXC     = -2;
        
        if (t instanceof Uc2oolFatalException) {
            if (m_logger != null) {
                m_logger.log(Level.SEVERE, t.getLocalizedMessage(), t);
            }
            showErrorDialog(t);
            System.exit(RC_FATAL_EXC);
        } else if (t instanceof Uc2oolRuntimeException) {
            
            showErrorDialog(t);
        } else {
            // These are considered fatal to the application.
            // FIXME application state may need to be considered here.
            // For now a simple brutal exit will hold.
            t.printStackTrace();
            System.exit(RC_UNHANDLED_EXC);
        }
    }
    
    private void showErrorDialog(Throwable e) {
        try {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("ErrorDialog.fxml"));
            Parent root = loader.load();
            ErrorController ec = loader.getController();
            ec.setErrorText(e.getLocalizedMessage());
            dialog.setScene(new Scene(root));
            dialog.setTitle("Error");
            dialog.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    /* Connect to the calculator model so that we can process Unicode character
     * operations.
     */
    private void connectToCalculator() {
        if (m_model == null) {
            m_model = new Uc2oolModel(m_logger);
        }
    }

}
