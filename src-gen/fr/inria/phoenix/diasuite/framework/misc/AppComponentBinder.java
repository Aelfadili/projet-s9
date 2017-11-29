package fr.inria.phoenix.diasuite.framework.misc;

import fr.inria.diagen.core.deploy.AbstractDeploy;

import fr.inria.phoenix.diasuite.framework.context.alertmove.AbstractAlertMove;
import fr.inria.phoenix.diasuite.framework.context.congratulation.AbstractCongratulation;
import fr.inria.phoenix.diasuite.framework.context.propositiongoout.AbstractPropositionGoOut;

import fr.inria.phoenix.diasuite.framework.controller.coach_activitycontroller.AbstractCoach_activityController;

/**
 * This class should be implemented to bind the implementation of the various components
 */
public abstract class AppComponentBinder extends AbstractDeploy {

    // Context instances
    private AbstractAlertMove alertMoveInstance = null;
    private AbstractCongratulation congratulationInstance = null;
    private AbstractPropositionGoOut propositionGoOutInstance = null;

    // Controller instances
    private AbstractCoach_activityController coach_activityControllerInstance = null;
    
    @Override
    public void deployAll() {
        // Initialization of contexts
        if (alertMoveInstance == null)
            alertMoveInstance = getInstance(getAlertMoveClass());
        if (congratulationInstance == null)
            congratulationInstance = getInstance(getCongratulationClass());
        if (propositionGoOutInstance == null)
            propositionGoOutInstance = getInstance(getPropositionGoOutClass());
        // Intialization of controllers
        if (coach_activityControllerInstance == null)
            coach_activityControllerInstance = getInstance(getCoach_activityControllerClass());
        // Deploying contexts
        deploy(alertMoveInstance);
        deploy(congratulationInstance);
        deploy(propositionGoOutInstance);
        // Deploying controllers
        deploy(coach_activityControllerInstance);
    }
    
    @Override
    public void undeployAll() {
        // Undeploying contexts
        undeploy(alertMoveInstance);
        undeploy(congratulationInstance);
        undeploy(propositionGoOutInstance);
        // Undeploying controllers
        undeploy(coach_activityControllerInstance);
    }
    
    // Abstract binding methods for contexts
    /**
     * Overrides this method to provide the implementation class of the <code>AlertMove</code> context
    
    <pre>
    context AlertMove as CriticalNotification{
     * 	when provided tickHour from Clock
     * 	get steps from Fitbit,
     * 		dailyActivity from ActivityNotifier
     * 	maybe publish;
     * }
    </pre>
    @return a class object of a derivation of {@link AbstractAlertMove} that implements the <code>AlertMove</code> context
     */
    public abstract Class<? extends AbstractAlertMove> getAlertMoveClass();
    
    /**
     * Overrides this method to provide the implementation class of the <code>Congratulation</code> context
    <p>
    Sortie context : Si nombre de pas suffisant alors féliciter
    
    <pre>
    context Congratulation as CriticalNotification {
     * 	when provided tickHour from Clock
     * 	get steps from Fitbit,
     * 		sleepPeriods from Fitbit
     * 	maybe publish;
     * 	}
    </pre>
    @return a class object of a derivation of {@link AbstractCongratulation} that implements the <code>Congratulation</code> context
     */
    public abstract Class<? extends AbstractCongratulation> getCongratulationClass();
    
    /**
     * Overrides this method to provide the implementation class of the <code>PropositionGoOut</code> context
    <p>
    Boolean ou String ?
    Sortie context : Si nombre de pas insuffisant
    
    <pre>
    context PropositionGoOut as CriticalNotification {
     * 	when provided tickHour from Clock
     * 	get steps from Fitbit,
     * 		events from Agenda
     * 	maybe publish;
     * 	}
    </pre>
    @return a class object of a derivation of {@link AbstractPropositionGoOut} that implements the <code>PropositionGoOut</code> context
     */
    public abstract Class<? extends AbstractPropositionGoOut> getPropositionGoOutClass();
    
    // End of abstract binding methods for contexts
    
    // Abstract binding methods for controllers
    /**
     * Overrides this method to provide the implementation class of the <code>Coach_activityController</code> controller
    <p>
    Coach_activity controller
    
    <pre>
    controller Coach_activityController {
     * 	when provided PropositionGoOut
     * 	do SendCriticalNotification on Notifier;
     * 	when provided Congratulation
     * 	do SendCriticalNotification on Notifier;
     * 	when provided AlertMove
     * 	do SendCriticalNotification on Notifier;
     * }
    </pre>
    @return a class object of a derivation of {@link AbstractCoach_activityController} that implements the <code>Coach_activityController</code> controller
     */
    public abstract Class<? extends AbstractCoach_activityController> getCoach_activityControllerClass();
    
    // End of abstract binding methods for controllers
}
