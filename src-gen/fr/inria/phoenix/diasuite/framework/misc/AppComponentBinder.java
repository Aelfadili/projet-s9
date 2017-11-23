package fr.inria.phoenix.diasuite.framework.misc;

import fr.inria.diagen.core.deploy.AbstractDeploy;

import fr.inria.phoenix.diasuite.framework.context.coupling.AbstractCoupling;
import fr.inria.phoenix.diasuite.framework.context.getfitbitinfos.AbstractGetFitbitInfos;
import fr.inria.phoenix.diasuite.framework.context.getsavedtime.AbstractGetSavedTime;
import fr.inria.phoenix.diasuite.framework.context.sleepbegin.AbstractSleepBegin;
import fr.inria.phoenix.diasuite.framework.context.sleepend.AbstractSleepEnd;

import fr.inria.phoenix.diasuite.framework.controller.savesleeptime.AbstractSaveSleepTime;
import fr.inria.phoenix.diasuite.framework.controller.sleepanalyser.AbstractSleepAnalyser;

/**
 * This class should be implemented to bind the implementation of the various components
 */
public abstract class AppComponentBinder extends AbstractDeploy {

    // Context instances
    private AbstractCoupling couplingInstance = null;
    private AbstractGetFitbitInfos getFitbitInfosInstance = null;
    private AbstractGetSavedTime getSavedTimeInstance = null;
    private AbstractSleepBegin sleepBeginInstance = null;
    private AbstractSleepEnd sleepEndInstance = null;

    // Controller instances
    private AbstractSaveSleepTime saveSleepTimeInstance = null;
    private AbstractSleepAnalyser sleepAnalyserInstance = null;
    
    @Override
    public void deployAll() {
        // Initialization of contexts
        if (couplingInstance == null)
            couplingInstance = getInstance(getCouplingClass());
        if (getFitbitInfosInstance == null)
            getFitbitInfosInstance = getInstance(getGetFitbitInfosClass());
        if (getSavedTimeInstance == null)
            getSavedTimeInstance = getInstance(getGetSavedTimeClass());
        if (sleepBeginInstance == null)
            sleepBeginInstance = getInstance(getSleepBeginClass());
        if (sleepEndInstance == null)
            sleepEndInstance = getInstance(getSleepEndClass());
        // Intialization of controllers
        if (saveSleepTimeInstance == null)
            saveSleepTimeInstance = getInstance(getSaveSleepTimeClass());
        if (sleepAnalyserInstance == null)
            sleepAnalyserInstance = getInstance(getSleepAnalyserClass());
        // Deploying contexts
        deploy(couplingInstance);
        deploy(getFitbitInfosInstance);
        deploy(getSavedTimeInstance);
        deploy(sleepBeginInstance);
        deploy(sleepEndInstance);
        // Deploying controllers
        deploy(saveSleepTimeInstance);
        deploy(sleepAnalyserInstance);
    }
    
    @Override
    public void undeployAll() {
        // Undeploying contexts
        undeploy(couplingInstance);
        undeploy(getFitbitInfosInstance);
        undeploy(getSavedTimeInstance);
        undeploy(sleepBeginInstance);
        undeploy(sleepEndInstance);
        // Undeploying controllers
        undeploy(saveSleepTimeInstance);
        undeploy(sleepAnalyserInstance);
    }
    
    // Abstract binding methods for contexts
    /**
     * Overrides this method to provide the implementation class of the <code>Coupling</code> context
     * 
     * <pre>
     * context Coupling as SleepPeriod[] indexed by period as Period {
     * 	when provided GetFitbitInfos 
     * 		maybe publish;
     * 	when provided GetSavedTime 
     * 		maybe publish;
     * }
     * </pre>
     * @return a class object of a derivation of {@link AbstractCoupling} that implements the <code>Coupling</code> context
     */
    public abstract Class<? extends AbstractCoupling> getCouplingClass();
    
    /**
     * Overrides this method to provide the implementation class of the <code>GetFitbitInfos</code> context
     * 
     * <pre>
     * context GetFitbitInfos as SleepPeriod[] indexed by period as Period  {
     * 	when provided sleepPeriods from Fitbit
     * 		always publish;
     * }
     * </pre>
     * @return a class object of a derivation of {@link AbstractGetFitbitInfos} that implements the <code>GetFitbitInfos</code> context
     */
    public abstract Class<? extends AbstractGetFitbitInfos> getGetFitbitInfosClass();
    
    /**
     * Overrides this method to provide the implementation class of the <code>GetSavedTime</code> context
     * 
     * <pre>
     * context GetSavedTime as String[] {
     * 	when provided data from Storage
     * 		maybe publish;
     * }
     * </pre>
     * @return a class object of a derivation of {@link AbstractGetSavedTime} that implements the <code>GetSavedTime</code> context
     */
    public abstract Class<? extends AbstractGetSavedTime> getGetSavedTimeClass();
    
    /**
     * Overrides this method to provide the implementation class of the <code>SleepBegin</code> context
     * 
     * <pre>
     * context SleepBegin as String {
     * 	when provided tickHour from Clock
     * 		get inactivityLevel from InactivitySensor,
     * 		lastInteraction from InactivitySensor
     * 	maybe publish;
     * }
     * </pre>
     * @return a class object of a derivation of {@link AbstractSleepBegin} that implements the <code>SleepBegin</code> context
     */
    public abstract Class<? extends AbstractSleepBegin> getSleepBeginClass();
    
    /**
     * Overrides this method to provide the implementation class of the <code>SleepEnd</code> context
     * 
     * <pre>
     * context SleepEnd as String {
     * 	when provided inactivityLevel from InactivitySensor
     * 		get tickHour from Clock,
     * 		lastInteraction from InactivitySensor
     * 	maybe publish;
     * }
     * </pre>
     * @return a class object of a derivation of {@link AbstractSleepEnd} that implements the <code>SleepEnd</code> context
     */
    public abstract Class<? extends AbstractSleepEnd> getSleepEndClass();
    
    // End of abstract binding methods for contexts
    
    // Abstract binding methods for controllers
    /**
     * Overrides this method to provide the implementation class of the <code>SaveSleepTime</code> controller
     * 
     * <pre>
     * controller SaveSleepTime {
     * 	when provided SleepBegin
     * 		do PutStringData on Storage;
     * 	when provided SleepEnd
     * 		do PutStringData on Storage;	
     * }
     * </pre>
     * @return a class object of a derivation of {@link AbstractSaveSleepTime} that implements the <code>SaveSleepTime</code> controller
     */
    public abstract Class<? extends AbstractSaveSleepTime> getSaveSleepTimeClass();
    
    /**
     * Overrides this method to provide the implementation class of the <code>SleepAnalyser</code> controller
     * 
     * <pre>
     * controller SleepAnalyser {
     * 	when provided Coupling
     * 	do	SendMessage on Messenger;
     * }
     * </pre>
     * @return a class object of a derivation of {@link AbstractSleepAnalyser} that implements the <code>SleepAnalyser</code> controller
     */
    public abstract Class<? extends AbstractSleepAnalyser> getSleepAnalyserClass();
    
    // End of abstract binding methods for controllers
}
