package fr.inria.phoenix.scenario.areyouhome.controller;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.phoenix.diasuite.framework.context.areyouhomecontext.AreYouHomeContextValue;
import fr.inria.phoenix.diasuite.framework.controller.areyouhomecontroller.AbstractAreYouHomeController;

public abstract class AreYouHomeController extends AbstractAreYouHomeController {

	public AreYouHomeController(ServiceConfiguration serviceConfiguration) {
		super(serviceConfiguration);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onAreYouHomeContext(AreYouHomeContextValue areYouHomeContext,
			DiscoverForAreYouHomeContext discover) {
		// TODO Auto-generated method stub

	}

}
