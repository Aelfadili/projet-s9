package fr.inria.phoenix.diasuite.framework.context.coupling;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.context.getfitbitinfos.GetFitbitInfosValue;

/**
 * when provided GetSavedTime 
 * 		maybe publish;
 * 
 * <pre>
 * context Coupling as SleepPeriod[] indexed by period as Period {
 * 	when provided GetFitbitInfos 
 * 		get Sleep
 * 		maybe publish;
 * }
 * </pre>
 */
@SuppressWarnings("all")
public abstract class AbstractCoupling extends Service {
    
    public AbstractCoupling(ServiceConfiguration serviceConfiguration) {
        super("/Context/Coupling/", serviceConfiguration);
    }
    
    // Methods from the Service class
    @Override
    protected final void internalPostInitialize() {
        subscribeValue("getFitbitInfos", "/Context/GetFitbitInfos/"); // subscribe to GetFitbitInfos context
        postInitialize();
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("getFitbitInfos") && source.isCompatible("/Context/GetFitbitInfos/")) {
            GetFitbitInfosValue getFitbitInfosValue = new GetFitbitInfosValue((java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>) value);
            
            CouplingValuePublishable returnValue = onGetFitbitInfos(getFitbitInfosValue, new DiscoverForGetFitbitInfos());
            if(returnValue != null && returnValue.doPublish()) {
                setCoupling(returnValue.getValue(), returnValue.getPeriod());
            }
        }
    }
    
    @Override
    public final Object getValueCalled(java.util.Map<String, Object> properties, RemoteServiceInfo source, String valueName,
            Object... indexes) throws Exception {
        if (valueName.equals("coupling")) {
            return getLastValue((fr.inria.phoenix.diasuite.framework.datatype.period.Period) indexes[0]);
        }
        throw new InvocationException("Unsupported method call: " + valueName);
    }
    // End of methods from the Service class
    
    // Code relative to the return value of the context
    private java.util.Map<CouplingIndices, java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>> contextValues = new java.util.HashMap<CouplingIndices, java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>>();
    
    private void setCoupling(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> newContextValue, fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        CouplingIndices indices = new CouplingIndices(period);
        contextValues.put(indices, newContextValue);
        getProcessor().publishValue(getOutProperties(), "coupling", newContextValue, period);
    }
    
    /**
     * Get the last value of the context
     * 
     * @param period the index <code>period</code> for the last value
     * @return the latest value published by the context for the given indexes
     */
    protected final java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> getLastValue(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        CouplingIndices indices = new CouplingIndices(period);
        return contextValues.get(indices);
    }
    
    /**
     * A class that represents a value that might be published for the <code>Coupling</code> context. It is used by
     * event methods that might or might not publish values for this context.
     */
    protected final static class CouplingValuePublishable {
        
        // The value of the context
        private java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value;
        // The period index
        private fr.inria.phoenix.diasuite.framework.datatype.period.Period period;
        // Whether the value should be published or not
        private boolean doPublish;
        
        public CouplingValuePublishable(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value,
                fr.inria.phoenix.diasuite.framework.datatype.period.Period period,
                boolean doPublish) {
            this.value = value;
            this.period = period;
            this.doPublish = doPublish;
        }
        
        /**
         * @return the value of the context that might be published
         */
        public java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> getValue() {
            return value;
        }
        
        /**
         * Sets the value that might be published
         * 
         * @param value the value that will be published if {@link #doPublish()} returns true
         */
        public void setValue(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value) {
            this.value = value;
        }
        
        /**
         * @return the <code>period</code> index for the value of the context that might be published
         */
        public fr.inria.phoenix.diasuite.framework.datatype.period.Period getPeriod() {
            return period;
        }
        
        /**
         * Sets the <code>period</code> index for the value that might be published
         * 
         * @param period the <code>period</code> index
         */
        public void setPeriod(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
            this.period = period;
        }
        
        /**
         * @return true if the value should be published
         */
        public boolean doPublish() {
            return doPublish;
        }
        
        /**
         * Set the value to be publishable or not
         * 
         * @param doPublish if true, the value will be published
         */
        public void setDoPublish(boolean doPublish) {
            this.doPublish = doPublish;
        }
    }
    // End of code relative to the return value of the context
    
    // Interaction contract implementation
    /**
     * This method is called when the <code>GetFitbitInfos</code> context publishes a value.
     * 
     * <pre>
     * when provided GetFitbitInfos 
     * 		get Sleep
     * 		maybe publish;
     * </pre>
     * 
     * @param getFitbitInfosValue the value of the <code>GetFitbitInfos</code> context.
     * @param discover a discover object to get value from devices and contexts
     * @return a {@link CouplingValuePublishable} that says if the context should publish a value and which value it should publish
     */
    protected abstract CouplingValuePublishable onGetFitbitInfos(GetFitbitInfosValue getFitbitInfosValue, DiscoverForGetFitbitInfos discover);
    
    // End of interaction contract implementation
    
    // Discover object for GetFitbitInfos
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided GetFitbitInfos 
     * 		get Sleep
     * 		maybe publish;
     * </code>
     */
    protected final class DiscoverForGetFitbitInfos {
        private final fr.inria.diagen.core.service.filter.service.ServiceFilter sleepFilter = new fr.inria.diagen.core.service.filter.service.ServiceFilter("/Context/Sleep/");
        
        private RemoteServiceInfo getSleepRsi() {
            return getProcessor().discoverComponents(getOutProperties(), sleepFilter).get(0);
        }
        
        /**
         * Get the value of the <code>Sleep</code> context
         * 
         * @return the value of the <code>Sleep</code> context
         */
        public java.util.List<fr.inria.phoenix.diasuite.framework.datatype.period.Period> sleep() {
            return (java.util.List<fr.inria.phoenix.diasuite.framework.datatype.period.Period>) getProcessor().callGetValue(getOutProperties(), getSleepRsi(), "sleep");
        }
    }
    // End of discover object for GetFitbitInfos
}
