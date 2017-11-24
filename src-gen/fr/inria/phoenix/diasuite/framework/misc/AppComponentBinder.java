package fr.inria.phoenix.diasuite.framework.misc;

import fr.inria.diagen.core.deploy.AbstractDeploy;

import fr.inria.phoenix.diasuite.framework.context.coupling.AbstractCoupling;
import fr.inria.phoenix.diasuite.framework.context.getfitbitinfos.AbstractGetFitbitInfos;
import fr.inria.phoenix.diasuite.framework.context.sleep.AbstractSleep;

import fr.inria.phoenix.diasuite.framework.controller.sleepanalyser.AbstractSleepAnalyser;

/**
 * This class should be implemented to bind the implementation of the various components
 */
public abstract class AppComponentBinder extends AbstractDeploy {

    // Context instances
    private AbstractCoupling couplingInstance = null;
    private AbstractGetFitbitInfos getFitbitInfosInstance = null;
    private AbstractSleep sleepInstance = null;

    // Controller instances
    private AbstractSleepAnalyser sleepAnalyserInstance = null;
    
    @Override
    public void deployAll() {
        // Initialization of contexts
        if (couplingInstance == null)
            couplingInstance = getInstance(getCouplingClass());
        if (getFitbitInfosInstance == null)
            getFitbitInfosInstance = getInstance(getGetFitbitInfosClass());
        if (sleepInstance == null)
            sleepInstance = getInstance(getSleepClass());
        // Intialization of controllers
        if (sleepAnalyserInstance == null)
            sleepAnalyserInstance = getInstance(getSleepAnalyserClass());
        // Deploying contexts
        deploy(couplingInstance);
        deploy(getFitbitInfosInstance);
        deploy(sleepInstance);
        // Deploying controllers
        deploy(sleepAnalyserInstance);
    }
    
    @Override
    public void undeployAll() {
        // Undeploying contexts
        undeploy(couplingInstance);
        undeploy(getFitbitInfosInstance);
        undeploy(sleepInstance);
        // Undeploying controllers
        undeploy(sleepAnalyserInstance);
    }
    
    // Abstract binding methods for contexts
    /**
     * Overrides this method to provide the implementation class of the <code>Coupling</code> context
     * <p>
     * when provided GetSavedTime 
     * 		maybe publish;
     * 
     * <pre>
     * context Coupling as SleepPeriod[] indexed by period as Period {
     * 	when provided GetFitbitInfos 
     * 		maybe publish;
     * }
     * </pre>
     * @return a class object of a derivation of {@link AbstractCoupling} that implements the <code>Coupling</code> context
     */
    public abstract Class<? extends AbstractCoupling> getCouplingClass();
    
    /**
     * Overrides this method to provide the implementation class of the <code>GetFitbitInfos</code> context
     * <p>
     * context SleepEnd as String {
     * 	when provided inactivityLevel from InactivitySensor
     * 		get currentTime from RoutineScheduler,
     * 		lastInteraction from InactivitySensor
     * 	maybe publish;
     * }
     * controller SaveSleepTime {
     * 	when provided SleepBegin
     * 		do PutStringData on Storage;
     * 	when provided SleepEnd
     * 		do PutStringData on Storage;	
     * }
     * context GetSavedTime as String[] {
     * 	when provided data from Storage
     * 		maybe publish;
     * }
     * 
     * <pre>
     * context GetFitbitInfos as SleepPeriod[] indexed by period as Period  {
     * 	when provided currentTime from RoutineScheduler
     * 		get	sleepPeriods from Fitbit,
     * 		tickHour from Clock
     * 		always publish;
     * }
     * </pre>
     * @return a class object of a derivation of {@link AbstractGetFitbitInfos} that implements the <code>GetFitbitInfos</code> context
     */
    public abstract Class<? extends AbstractGetFitbitInfos> getGetFitbitInfosClass();
    
    /**
     * Overrides this method to provide the implementation class of the <code>Sleep</code> context
     * 
     * <pre>
     * context Sleep as String {
     * 	when provided inactivityLevel from InactivitySensor 
     * 		get currentTime from RoutineScheduler,
     * 		lastInteraction from InactivitySensor
     * 	maybe publish;
     * }
     * </pre>
     * @return a class object of a derivation of {@link AbstractSleep} that implements the <code>Sleep</code> context
     */
    public abstract Class<? extends AbstractSleep> getSleepClass();
    
    // End of abstract binding methods for contexts
    
    // Abstract binding methods for controllers
    /**
     * Overrides this method to provide the implementation class of the <code>SleepAnalyser</code> controller
     * 
     * <pre>
     * controller SleepAnalyser {
     * 	when provided Coupling
     * 	do SendNonCriticalNotification on Notifier,
     * 	 PutStringData on Storage;
     * }
     * </pre>
     * @return a class object of a derivation of {@link AbstractSleepAnalyser} that implements the <code>SleepAnalyser</code> controller
     */
    public abstract Class<? extends AbstractSleepAnalyser> getSleepAnalyserClass();
    
    // End of abstract binding methods for controllers
}
