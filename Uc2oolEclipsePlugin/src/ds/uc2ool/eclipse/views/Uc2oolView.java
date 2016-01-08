package ds.uc2ool.eclipse.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;


/**
 * This is the Eclipse view, not to be confused with the V in MVC.
 * It instantiates a controller which will create all the elements of the
 * display and connect up a model instance.
 */
public class Uc2oolView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "ds.uc2ool.eclipse.views.Uc2oolView";
	private Uc2oolController m_uc2oolController = null;
	
	/**
	 * The constructor.
	 */
	public Uc2oolView() {
	}

    /**
     * Create the Uc2ool controller
     * 
     * @see ViewPart#createPartControl
     */
    @Override
	public void createPartControl(Composite parent) {

	    m_uc2oolController = new Uc2oolController(parent, 0);
	}

    /**
     * Called when we must grab focus.
     * 
     * @see org.eclipse.ui.part.ViewPart#setFocus
     */
    @Override
    public void setFocus() {
        m_uc2oolController.setFocus();
    }

    /**
     * Called when the View is to be disposed
     */ 
    @Override
    public void dispose() {
        m_uc2oolController.dispose();
        m_uc2oolController = null;
        SWTResourceManager.dispose();
        super.dispose();
    }
}
