package fr.inria.phoenix.scenario.coach_activity.impl;
        
import fr.inria.phoenix.diasuite.framework.context.alertmove.AbstractAlertMove;
import fr.inria.phoenix.diasuite.framework.context.congratulation.AbstractCongratulation;
import fr.inria.phoenix.diasuite.framework.context.propositiongoout.AbstractPropositionGoOut;
import fr.inria.phoenix.diasuite.framework.controller.coach_activitycontroller.AbstractCoach_activityController;
import fr.inria.phoenix.diasuite.framework.misc.AppComponentBinder;

/* (non-Javadoc)
 * The binder to provides the various components of the application
 * @see fr.inria.phoenix.diasuite.framework.misc.AppComponentBinder
 */
public class ComponentBinder extends AppComponentBinder {

	@Override
	public Class<? extends AbstractAlertMove> getAlertMoveClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends AbstractCongratulation> getCongratulationClass() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Class<? extends AbstractPropositionGoOut> getPropositionGoOutClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends AbstractCoach_activityController> getCoach_activityControllerClass() {
		// TODO Auto-generated method stub
		return null;
	}
}
