/**
 * Copyright Daniel Semler 2015
 */
package ds.uc2ool.javafxui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * ErrorController provides a simple controller for the error dialog popup.
 * It expects to operate with a JavaFX error dialog as defined in
 * ErrorDialog.fxml.
 * 
 * @author  Daniel Semler
 * @version %I%, %G%
 * @since   1.0
 */
public class ErrorController {
    @FXML
    private Label m_errorMessage;
    
    /**
     * Set the error text to be displayed to the user. 
     */
    public void setErrorText(String text) {
        m_errorMessage.setText(text);
    }

    @FXML
    private void close() {
        m_errorMessage.getScene().getWindow().hide();
    }
}
