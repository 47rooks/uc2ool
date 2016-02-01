package com.fortysevenrooks.uc2ool.eclipse.views;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fortysevenrooks.debug.DebugLogger;
import com.fortysevenrooks.uc2ool.core.exceptions.Uc2oolFatalException;
import com.fortysevenrooks.uc2ool.core.exceptions.Uc2oolRuntimeException;
import com.fortysevenrooks.uc2ool.core.info.Info;
import com.fortysevenrooks.uc2ool.core.model.Uc2oolModel;
import com.fortysevenrooks.uc2ool.core.model.Uc2oolModel.InputType;
import com.fortysevenrooks.uc2ool.eclipse.Activator;
import com.fortysevenrooks.uc2ool.eclipse.statusmanagement.Uc2oolStatus;

/**
 * Uc2oolController is the controller/view class for the Eclipse plugin
 * version of the Uc2ool.
 * 
 * FIXME - describe exception handling.
 * 
 * @author	Daniel Semler
 * @version	%I%, %G%
 * @since	1.0
 */
public class Uc2oolController extends Composite {
    // The calculator for doing all the conversions
    private Uc2oolModel m_model;
    
    // Debug logger
    private Logger m_logger;
    private final static String LOGGER_NAME = "com.fortysevenrooks.uc2ool";

    private static final String INFO_RESOURCE_BUNDLE_NAME =
            "com.fortysevenrooks.uc2ool.eclipse.info.InfoMessages";

    // GUI fields
    private Text m_inputCharacter;
    private InputType m_inputType = InputType.HEXCODEPOINT;
    private Text m_glyph;
    
    private Label m_unicodeName;
    private Label m_utf16Encoding;
    private Label m_utf8Encoding;
    private Label m_decimalCodePoint;
    private Combo fontCombo;
    private Combo sizeCombo;
    
    private final String DEFAULT_FONT_SIZE = "80";
    private final String DEFAULT_FONT_NAME = "Cardo";
    private String fontName = DEFAULT_FONT_NAME;
    private int fontSize = Integer.valueOf(DEFAULT_FONT_SIZE);

    private Info m_Info = new Info();
    
    private ViewPart m_viewPart;
    
    private static final String DEBUG_PROP =
            "com.fortysevenrooks.debug.logging.level";
    
    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public Uc2oolController(Composite parent, int style, ViewPart myVP) {
        super(parent, style);
        m_viewPart = myVP;
        
        // Check for debug flags
        checkDebug();
        
        m_logger = new DebugLogger(LOGGER_NAME, new PLFHandler());
        
        // Create the GUI layout and setup all the listeners
        createGUI();

        // Create a model object to drive the GUI
        connectToCalculator();
    }

    /**
     * Check for the debug flags in the and setup the Core library debug
     * property if required.
     */
    private void checkDebug() {
        if (Activator.getDefault().isDebugging()) {
            System.out.println("Setting debug");
            System.setProperty(DEBUG_PROP, "FINEST");
        }
    }

    /**
     * Create the GUI
     */
    private void createGUI() {
        // Major area layouts
        setLayout(new GridLayout(1, false));
        
        Group grpUcool = new Group(this, SWT.NONE);
        grpUcool.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        grpUcool.setLayout(new GridLayout(2, true));
        GridData gd_grpUcool = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd_grpUcool.heightHint = 239;
        gd_grpUcool.widthHint = 519;
        grpUcool.setLayoutData(gd_grpUcool);
        grpUcool.setText("Unicode 2ool");
        
        Group grpInput = new Group(grpUcool, SWT.NONE);
        GridData gd_grpInput = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
        gd_grpInput.widthHint = 220;
        gd_grpInput.heightHint = 195;
        grpInput.setLayoutData(gd_grpInput);
        grpInput.setText("Input");
        
        // Input field
        m_inputCharacter = new Text(grpInput, SWT.BORDER);
        m_inputCharacter.setBounds(10, 10, 210, 19);
        m_inputCharacter.addListener(SWT.DefaultSelection, new Listener() {
            @Override
            public void handleEvent(Event e) {
                processNow();
            }
            
        });
        
        // Radio button set for indicating input type
        Button btnCharacterEncoding = new Button(grpInput, SWT.RADIO);
        btnCharacterEncoding.setEnabled(false);
        btnCharacterEncoding.setBounds(10, 35, 119, 18);
        btnCharacterEncoding.setText("Character");
        btnCharacterEncoding.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                m_inputType = InputType.CHARACTER;
            }
        });
        
        Button btnUtf8Encoding = new Button(grpInput, SWT.RADIO);
        btnUtf8Encoding.setBounds(10, 59, 119, 18);
        btnUtf8Encoding.setText("UTF-8 encoding");
        btnUtf8Encoding.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                m_inputType = InputType.UTF8;
            }
        });
        
        Button btnDecimalCodePoint = new Button(grpInput, SWT.RADIO);
        btnDecimalCodePoint.setBounds(10, 83, 146, 18);
        btnDecimalCodePoint.setText("Decimal code point");
        btnDecimalCodePoint.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                m_inputType = InputType.DECCODEPOINT;
            }
        });

        Button btnHexCodePoint = new Button(grpInput, SWT.RADIO);
        btnHexCodePoint.setSelection(true);
        btnHexCodePoint.setBounds(10, 107, 119, 18);
        btnHexCodePoint.setText("Hex code point");
        btnHexCodePoint.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                m_inputType = InputType.HEXCODEPOINT;
            }
        });
       
        Button btnProcess = new Button(grpInput, SWT.NONE);
        btnProcess.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                processNow();
            }
        });
        btnProcess.setBounds(136, 103, 84, 28);
        btnProcess.setText("Process");
        
        Label label = new Label(grpInput, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setBounds(10, 137, 210, 9);
        
        // Font and size combo boxes
        Label lblFont = new Label(grpInput, SWT.NONE);
        lblFont.setBounds(10, 156, 59, 14);
        lblFont.setText("Font");
        
        Label lblSize = new Label(grpInput, SWT.NONE);
        lblSize.setBounds(10, 180, 59, 14);
        lblSize.setText("Size");
        
        fontCombo = new Combo(grpInput, SWT.NONE);
        fontCombo.setBounds(58, 152, 162, 22);
        sizeCombo = new Combo(grpInput, SWT.NONE);
        sizeCombo.setBounds(58, 176, 162, 22);
        
        // Glyph display
        Group grpGlyph = new Group(grpUcool, SWT.NONE);
        GridData gd_grpGlyph = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
        gd_grpGlyph.widthHint = 220;
        gd_grpGlyph.heightHint = 195;
        grpGlyph.setLayoutData(gd_grpGlyph);
        grpGlyph.setText("Glyph");
        
        m_glyph = new Text(grpGlyph, SWT.BORDER);
        m_glyph.setBounds(10, 10, 210, 185);
        
        // Unicode details pane
        Group grpUnicodedetails = new Group(this, SWT.NONE);
        grpUnicodedetails.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        grpUnicodedetails.setLayout(new GridLayout(2, false));
        GridData gd_grpUnicodedetails = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        gd_grpUnicodedetails.heightHint = 84;
        gd_grpUnicodedetails.widthHint = 431;
        grpUnicodedetails.setLayoutData(gd_grpUnicodedetails);
        grpUnicodedetails.setText("Unicode Details");
        
        Label lblCharacter = new Label(grpUnicodedetails, SWT.NONE);
        lblCharacter.setText("Character Name");
        m_unicodeName = new Label(grpUnicodedetails, SWT.NONE);
        m_unicodeName.setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
                
        Label lblUtfEncoding = new Label(grpUnicodedetails, SWT.NONE);
        lblUtfEncoding.setText("UTF-16 encoding");
        m_utf16Encoding = new Label(grpUnicodedetails, SWT.NONE);
        m_utf16Encoding.setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        
        Label lblUtfEncoding_1 = new Label(grpUnicodedetails, SWT.NONE);
        lblUtfEncoding_1.setText("UTF-8 encoding");
        m_utf8Encoding = new Label(grpUnicodedetails, SWT.NONE);
        m_utf8Encoding.setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        
        Label lblDecimalCodepoint = new Label(grpUnicodedetails, SWT.NONE);
        lblDecimalCodepoint.setText("Decimal codepoint");
        m_decimalCodePoint = new Label(grpUnicodedetails, SWT.NONE);
        m_decimalCodePoint.setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        
        /* Initialize the combobox for the font used for displaying the
         * glyph. Set up a listener for changes in the property value.
         */
        if (fontCombo != null) {
            // Get, remove duplicates and sort all font names on the system
            // and add them to the m_font ComboBox.
            FontData fonts[] = Display.getCurrent().getFontList(null, true);
            Set<String> fset = new HashSet<String>();
            for (int i=0; i < fonts.length; i++) {
                fset.add(fonts[i].getName());
            }
            int i=0;
            SortedSet<String> ss = new TreeSet<String>(fset);
            for (String fn : ss) {
                fontCombo.add(fn);
                if (fn.equals("Cardo")) {
                    fontCombo.select(i);
                }
                i++;
            }
            fontCombo.addSelectionListener(
                new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        fontName = fontCombo.getItem(fontCombo.getSelectionIndex());
                        m_glyph.setFont(new Font(Display.getCurrent(), 
                            new FontData(fontName,
                                         fontSize,
                                         SWT.NONE)));
                        }
                    });            
        }
        
        /* Set up the font size combo box with a range of values to choose
         * from, and set the initial value. Add a listener to observe
         * changes in selection.
         */
        if (sizeCombo != null) {
            sizeCombo.setItems(
                    new String[] { "5", "7", "9", "12", "14", "16",
                                   "18", "20", "24", "28", "32", "40",
                                   "45", "52", "60", "72", "80", "100",
                                   "130" });
            sizeCombo.select(16);
            sizeCombo.addSelectionListener(
                new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        fontSize = Integer.valueOf(sizeCombo.getItem(sizeCombo.getSelectionIndex()));
                        m_glyph.setFont(new Font(Display.getCurrent(), 
                                new FontData(fontName,
                                             fontSize,
                                             SWT.NONE)));
                    }
            });
        }
    }

    /* Called when the Process button is fired.
     * When the application is running this is the primary processing
     * API which is called.
     */
    private void processNow() {
        try {
            // Clear Info bar
            m_Info.clear();
            
            if (m_inputCharacter != null) {
                m_logger.log(Level.FINEST, m_inputCharacter.getText());
                
                // Get the input type and prime Uc2oolModel
                m_model.setInput(m_inputCharacter.getText(), m_inputType);
                m_logger.log(Level.FINEST, m_model.toString());
                
                // Now populate output display fields with data from the 
                // Uc2oolModel
                String uncName = m_model.getUnicodeCharacterName();
                m_unicodeName.setText(uncName != null ? uncName : "");
                m_utf16Encoding.setText(m_model.getUTF16Encoding());
                m_utf8Encoding.setText(m_model.getUTF8Encoding());
                m_decimalCodePoint.setText(m_model.getDecimalCodePoint());
                m_glyph.setFont(
                    new Font(Display.getCurrent(), 
                             new FontData(fontName,
                                          fontSize,
                                          SWT.NONE)));
                m_glyph.setText(getUnicodeCharacter());
                
                // Set Info line appropriately
                IActionBars bars = m_viewPart.getViewSite().getActionBars();
                IStatusLineManager manager = bars.getStatusLineManager();
                if (!m_Info.isEmpty()) {

                    manager.setMessage(
                            getInfoMessage(m_Info.getId(0),
                                           m_Info.getArgs(0)));
                } else {
                    manager.setMessage(null);
                }   
            }
        } catch (Uc2oolRuntimeException cre) {
            handleException(cre);
        }
    }
    
    private String getInfoMessage(String msgId, Object... args) {
        ResourceBundle mb = ResourceBundle.getBundle(INFO_RESOURCE_BUNDLE_NAME);
        String msg = mb.getString(msgId);
        return new StringBuilder(
                String.format(msg, args)).toString();
    }

    private String getUnicodeCharacter() {
        java.awt.Font awtFont = 
                new java.awt.Font(fontName,
                                  java.awt.Font.PLAIN,
                                  fontSize);
        if (!awtFont.canDisplay(m_model.getCodepoint())) {
            m_Info.add("NO_GLYPH", new Object[] {});
        }
        return m_model.getUnicodeCharacter();
    }
    
    /* Handle exception popping up an error dialog to the user and logging
     * the error to the diagnostic log if required.
     * 
     * @param e the Exception to handle
     */
    private void handleException(Uc2oolRuntimeException ure) {
                
        if (ure instanceof Uc2oolFatalException) {
            if (m_logger != null) {
                m_logger.log(Level.SEVERE, ure.getLocalizedMessage(), ure);
            }
            // Create Uc2oolStatus object object
            IStatus stat = Uc2oolStatus.getStatusWithException(ure);
            StatusManager.getManager().handle(stat,
                                              StatusManager.SHOW |
                                              StatusManager.LOG);
        } else if (ure instanceof Uc2oolRuntimeException) {
            
            // Create Uc2oolStatus object object
            IStatus stat = 
                    Uc2oolStatus.getStatus(ure);
            StatusManager.getManager().handle(stat, StatusManager.SHOW);
        }
    }

    /**
     * Connect to the calculator model so that we can process Unicode character
     * operations.
     */
    private void connectToCalculator() {
        if (m_model == null) {
            m_model = new Uc2oolModel(m_logger);
        }
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
    
    /**
     * Instances of this class log records to the Eclipse Platform Logging
     * facility.
     * 
     * @author	Daniel Semler
     * @version	%I%, %G%
     * @since	1.0
     */
    private class PLFHandler extends Handler {
        
        @Override
        public void publish(LogRecord record) {

            IStatus s = new org.eclipse.core.runtime.Status(
                                   org.eclipse.core.runtime.Status.INFO,
                                   Activator.PLUGIN_ID,
                                   org.eclipse.core.runtime.Status.OK,
                                   record.getMessage(),
                                   record.getThrown());
            Activator.getDefault().getLog().log(s);
        }

        @Override
        public void flush() {
            // No-op
        }

        @Override
        public void close() throws SecurityException {
            // No-op
        }
        
    }
}
