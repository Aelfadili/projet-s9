package fr.inria.phoenix.diasuite.framework.context.getfitbitinfos;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.device.fitbit.SleepPeriodsFromFitbit;

/**
 * <pre>
 * context GetFitbitInfos as SleepPeriod[] indexed by period as Period  {
 * 	when provided sleepPeriods from Fitbit
 * 		always publish;
 * }
 * </pre>
 */
@SuppressWarnings("all")
public abstract class AbstractGetFitbitInfos extends Service {
    
    public AbstractGetFitbitInfos(ServiceConfiguration serviceConfiguration) {
        super("/Context/GetFitbitInfos/", serviceConfiguration);
    }
    
    // Methods from the Service class
    @Override
    protected final void internalPostInitialize() {
        postInitialize();
    }
    
    @Override
    protected void postInitialize() {
        // Default implementation of post initialize: subscribe to all required devices
        discoverFitbitForSubscribe.all().subscribeSleepPeriods(); // subscribe to sleepPeriods from all Fitbit devices
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("sleepPeriods") && source.isCompatible("/Device/Device/Fitbit/")) {
            SleepPeriodsFromFitbit sleepPeriodsFromFitbit = new SleepPeriodsFromFitbit(this, source, (java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>) value,
                    (fr.inria.phoenix.diasuite.framework.datatype.period.Period) indexes[0]);
            
            GetFitbitInfosValue returnValue = onSleepPeriodsFromFitbit(sleepPeriodsFromFitbit);
            if(returnValue != null) {
                setGetFitbitInfos(returnValue.value(), returnValue.indices().period());
            } else {
                setGetFitbitInfos(null, null);
            }
        }
    }
    
    @Override
    public final Object getValueCalled(java.util.Map<String, Object> properties, RemoteServiceInfo source, String valueName,
            Object... indexes) throws Exception {
        if (valueName.equals("getFitbitInfos")) {
            return getLastValue((fr.inria.phoenix.diasuite.framework.datatype.period.Period) indexes[0]);
        }
        throw new InvocationException("Unsupported method call: " + valueName);
    }
    // End of methods from the Service class
    
    // Code relative to the return value of the context
    private java.util.Map<GetFitbitInfosIndices, java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>> contextValues = new java.util.HashMap<GetFitbitInfosIndices, java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>>();
    
    private void setGetFitbitInfos(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> newContextValue, fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        GetFitbitInfosIndices indices = new GetFitbitInfosIndices(period);
        contextValues.put(indices, newContextValue);
        getProcessor().publishValue(getOutProperties(), "getFitbitInfos", newContextValue, period);
    }
    
    /**
     * Get the last value of the context
     * 
     * @param period the index <code>period</code> for the last value
     * @return the latest value published by the context for the given indexes
     */
    protected final java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> getLastValue(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        GetFitbitInfosIndices indices = new GetFitbitInfosIndices(period);
        return contextValues.get(indices);
    }
    // End of code relative to the return value of the context
    
    // Interaction contract implementation
    /**
     * This method is called when a <code>Fitbit</code> device on which we have subscribed publish on its <code>sleepPeriods</code> source.
     * 
     * <pre>
     * when provided sleepPeriods from Fitbit
     * 		always publish;
     * </pre>
     * 
     * @param sleepPeriodsFromFitbit the value of the <code>sleepPeriods</code> source and the <code>Fitbit</code> device that published the value.
     * @return the value to publish
     */
    protected abstract GetFitbitInfosValue onSleepPeriodsFromFitbit(SleepPeriodsFromFitbit sleepPeriodsFromFitbit);
    
    // End of interaction contract implementation
    
    // Discover part for Fitbit devices
    /**
     * Use this object to discover Fitbit devices.
     * <p>
     * ------------------------------------------------------------
     * Fitbit							||
     * ------------------------------------------------------------
     * 
     * <pre>
     * device Fitbit extends Device {
     *         source calories as Integer indexed by period as Period;
     *         source distanceInMeters as Integer indexed by period as Period;
     *         source pulses as Pulse indexed by period as Period;
     *         source steps as Integer indexed by period as Period;
     *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
     *         source alarm as Alarm indexed by name as String;
     *         action ScheduleAlarm;
     *         action Vibrate;
     * }
     * </pre>
     * 
     * @see FitbitDiscoverer
     */
    protected final FitbitDiscoverer discoverFitbitForSubscribe = new FitbitDiscoverer(this);
    
    /**
     * Discover object that will exposes the <code>Fitbit</code> devices that can be discovered
     * <p>
     * ------------------------------------------------------------
     * Fitbit							||
     * ------------------------------------------------------------
     * 
     * <pre>
     * device Fitbit extends Device {
     *         source calories as Integer indexed by period as Period;
     *         source distanceInMeters as Integer indexed by period as Period;
     *         source pulses as Pulse indexed by period as Period;
     *         source steps as Integer indexed by period as Period;
     *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
     *         source alarm as Alarm indexed by name as String;
     *         action ScheduleAlarm;
     *         action Vibrate;
     * }
     * </pre>
     */
    protected final static class FitbitDiscoverer {
        private Service serviceParent;
        
        private FitbitDiscoverer(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private FitbitComposite instantiateComposite() {
            return new FitbitComposite(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Fitbit</code> devices
         * 
         * @return a {@link FitbitComposite} object composed of all discoverable <code>Fitbit</code>
         */
        public FitbitComposite all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Fitbit</code> devices
         * 
         * @return a {@link FitbitProxy} object pointing to a random discoverable <code>Fitbit</code> device
         */
        public FitbitProxy anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Fitbit</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link FitbitComposite} object composed of all matching <code>Fitbit</code> devices
         */
        public FitbitComposite whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Fitbit</code> devices
     * <p>
     * ------------------------------------------------------------
     * Fitbit							||
     * ------------------------------------------------------------
     * 
     * <pre>
     * device Fitbit extends Device {
     *         source calories as Integer indexed by period as Period;
     *         source distanceInMeters as Integer indexed by period as Period;
     *         source pulses as Pulse indexed by period as Period;
     *         source steps as Integer indexed by period as Period;
     *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
     *         source alarm as Alarm indexed by name as String;
     *         action ScheduleAlarm;
     *         action Vibrate;
     * }
     * </pre>
     */
    protected final static class FitbitComposite extends fr.inria.diagen.core.service.composite.Composite<FitbitProxy> {
        private FitbitComposite(Service serviceParent) {
            super(serviceParent, "/Device/Device/Fitbit/");
        }
        
        @Override
        protected FitbitProxy instantiateProxy(RemoteServiceInfo rsi) {
            return new FitbitProxy(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link FitbitComposite}, filtered using the attribute <code>id</code>.
         */
        public FitbitComposite andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Subscribes to the <code>sleepPeriods</code> source. After a call to this method, the context will be notified when a
         * <code>Fitbit</code> device of this composite publishes a value on its <code>sleepPeriods</code> source.
         */
        public void subscribeSleepPeriods() {
            subscribeValue("sleepPeriods");
        }
        
        /**
         * Unsubscribes from the <code>sleepPeriods</code> source. After a call to this method, the context will no more be notified
         * when a <code>Fitbit</code> device of this composite publishes a value on its <code>sleepPeriods</code> source.
         */
        public void unsubscribeSleepPeriods() {
            unsubscribeValue("sleepPeriods");
        }
    }
    
    /**
     * A proxy to one <code>Fitbit</code> device
     * <p>
     * ------------------------------------------------------------
     * Fitbit							||
     * ------------------------------------------------------------
     * 
     * <pre>
     * device Fitbit extends Device {
     *         source calories as Integer indexed by period as Period;
     *         source distanceInMeters as Integer indexed by period as Period;
     *         source pulses as Pulse indexed by period as Period;
     *         source steps as Integer indexed by period as Period;
     *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
     *         source alarm as Alarm indexed by name as String;
     *         action ScheduleAlarm;
     *         action Vibrate;
     * }
     * </pre>
     */
    protected final static class FitbitProxy extends Proxy {
        private FitbitProxy(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Subscribes to the <code>sleepPeriods</code> source. After a call to this method, the context will be notified when the
         * <code>Fitbit</code> device of this proxy publishes a value on its <code>sleepPeriods</code> source.
         */
        public void subscribeSleepPeriods() {
            subscribeValue("sleepPeriods");
        }
        
        /**
         * Unsubscribes from the <code>sleepPeriods</code> source. After a call to this method, the context will no more be notified
         * when the <code>Fitbit</code> device of this proxy publishes a value on its <code>sleepPeriods</code> source.
         */
        public void unsubscribeSleepPeriods() {
            unsubscribeValue("sleepPeriods");
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover part for Fitbit devices
}
