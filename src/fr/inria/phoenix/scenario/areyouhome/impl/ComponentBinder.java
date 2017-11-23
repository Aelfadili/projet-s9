package fr.inria.phoenix.scenario.areyouhome.impl;
        
import fr.inria.phoenix.diasuite.framework.context.areyouhomecontext.AbstractAreYouHomeContext;
import fr.inria.phoenix.diasuite.framework.context.ishome.AbstractIsHome;
import fr.inria.phoenix.diasuite.framework.context.isidentified.AbstractIsIdentified;
import fr.inria.phoenix.diasuite.framework.controller.areyouhomecontroller.AbstractAreYouHomeController;
import fr.inria.phoenix.diasuite.framework.misc.AppComponentBinder;

/* (non-Javadoc)
 * The binder to provides the various components of the application
 * @see fr.inria.phoenix.diasuite.framework.misc.AppComponentBinder
 */
public class ComponentBinder extends AppComponentBinder {

	@Override
	public Class<? extends AbstractAreYouHomeContext> getAreYouHomeContextClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends AbstractIsHome> getIsHomeClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends AbstractIsIdentified> getIsIdentifiedClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends AbstractAreYouHomeController> getAreYouHomeControllerClass() {
		// TODO Auto-generated method stub
		return null;
	}
}
