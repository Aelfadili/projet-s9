package fr.inria.phoenix.diasuite.framework.context.getfitbitinfos;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.device.routinescheduler.CurrentTimeFromRoutineScheduler;

/**
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
        discoverRoutineSchedulerForSubscribe.all().subscribeCurrentTime(); // subscribe to currentTime from all RoutineScheduler devices
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("currentTime") && source.isCompatible("/Device/Device/Service/RoutineScheduler/")) {
            CurrentTimeFromRoutineScheduler currentTimeFromRoutineScheduler = new CurrentTimeFromRoutineScheduler(this, source, (fr.inria.phoenix.diasuite.framework.datatype.daytime.DayTime) value);
            
            GetFitbitInfosValue returnValue = onCurrentTimeFromRoutineScheduler(currentTimeFromRoutineScheduler, new DiscoverForCurrentTimeFromRoutineScheduler());
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
     * This method is called when a <code>RoutineScheduler</code> device on which we have subscribed publish on its <code>currentTime</code> source.
     * 
     * <pre>
     * when provided currentTime from RoutineScheduler
     * 		get	sleepPeriods from Fitbit,
     * 		tickHour from Clock
     * 		always publish;
     * </pre>
     * 
     * @param currentTimeFromRoutineScheduler the value of the <code>currentTime</code> source and the <code>RoutineScheduler</code> device that published the value.
     * @param discover a discover object to get value from devices and contexts
     * @return the value to publish
     */
    protected abstract GetFitbitInfosValue onCurrentTimeFromRoutineScheduler(CurrentTimeFromRoutineScheduler currentTimeFromRoutineScheduler, DiscoverForCurrentTimeFromRoutineScheduler discover);
    
    // End of interaction contract implementation
    
    // Discover part for RoutineScheduler devices
    /**
     * Use this object to discover RoutineScheduler devices.
     * 
     * <pre>
     * device RoutineScheduler extends Service {
     * 	source currentTime as DayTime;
     * 	source startMonitoring as DailyActivityName;
     * 	source stopMonitoring as DailyActivityName;
     * 	action UpdateDayTime;
     * 	action UpdateRoutines;
     * }
     * </pre>
     * 
     * @see RoutineSchedulerDiscoverer
     */
    protected final RoutineSchedulerDiscoverer discoverRoutineSchedulerForSubscribe = new RoutineSchedulerDiscoverer(this);
    
    /**
     * Discover object that will exposes the <code>RoutineScheduler</code> devices that can be discovered
     * 
     * <pre>
     * device RoutineScheduler extends Service {
     * 	source currentTime as DayTime;
     * 	source startMonitoring as DailyActivityName;
     * 	source stopMonitoring as DailyActivityName;
     * 	action UpdateDayTime;
     * 	action UpdateRoutines;
     * }
     * </pre>
     */
    protected final static class RoutineSchedulerDiscoverer {
        private Service serviceParent;
        
        private RoutineSchedulerDiscoverer(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private RoutineSchedulerComposite instantiateComposite() {
            return new RoutineSchedulerComposite(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>RoutineScheduler</code> devices
         * 
         * @return a {@link RoutineSchedulerComposite} object composed of all discoverable <code>RoutineScheduler</code>
         */
        public RoutineSchedulerComposite all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>RoutineScheduler</code> devices
         * 
         * @return a {@link RoutineSchedulerProxy} object pointing to a random discoverable <code>RoutineScheduler</code> device
         */
        public RoutineSchedulerProxy anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>RoutineScheduler</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link RoutineSchedulerComposite} object composed of all matching <code>RoutineScheduler</code> devices
         */
        public RoutineSchedulerComposite whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>RoutineScheduler</code> devices
     * 
     * <pre>
     * device RoutineScheduler extends Service {
     * 	source currentTime as DayTime;
     * 	source startMonitoring as DailyActivityName;
     * 	source stopMonitoring as DailyActivityName;
     * 	action UpdateDayTime;
     * 	action UpdateRoutines;
     * }
     * </pre>
     */
    protected final static class RoutineSchedulerComposite extends fr.inria.diagen.core.service.composite.Composite<RoutineSchedulerProxy> {
        private RoutineSchedulerComposite(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/RoutineScheduler/");
        }
        
        @Override
        protected RoutineSchedulerProxy instantiateProxy(RemoteServiceInfo rsi) {
            return new RoutineSchedulerProxy(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link RoutineSchedulerComposite}, filtered using the attribute <code>id</code>.
         */
        public RoutineSchedulerComposite andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Subscribes to the <code>currentTime</code> source. After a call to this method, the context will be notified when a
         * <code>RoutineScheduler</code> device of this composite publishes a value on its <code>currentTime</code> source.
         */
        public void subscribeCurrentTime() {
            subscribeValue("currentTime");
        }
        
        /**
         * Unsubscribes from the <code>currentTime</code> source. After a call to this method, the context will no more be notified
         * when a <code>RoutineScheduler</code> device of this composite publishes a value on its <code>currentTime</code> source.
         */
        public void unsubscribeCurrentTime() {
            unsubscribeValue("currentTime");
        }
    }
    
    /**
     * A proxy to one <code>RoutineScheduler</code> device
     * 
     * <pre>
     * device RoutineScheduler extends Service {
     * 	source currentTime as DayTime;
     * 	source startMonitoring as DailyActivityName;
     * 	source stopMonitoring as DailyActivityName;
     * 	action UpdateDayTime;
     * 	action UpdateRoutines;
     * }
     * </pre>
     */
    protected final static class RoutineSchedulerProxy extends Proxy {
        private RoutineSchedulerProxy(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Subscribes to the <code>currentTime</code> source. After a call to this method, the context will be notified when the
         * <code>RoutineScheduler</code> device of this proxy publishes a value on its <code>currentTime</code> source.
         */
        public void subscribeCurrentTime() {
            subscribeValue("currentTime");
        }
        
        /**
         * Unsubscribes from the <code>currentTime</code> source. After a call to this method, the context will no more be notified
         * when the <code>RoutineScheduler</code> device of this proxy publishes a value on its <code>currentTime</code> source.
         */
        public void unsubscribeCurrentTime() {
            unsubscribeValue("currentTime");
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover part for RoutineScheduler devices
    
    // Discover object for currentTime from RoutineScheduler
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided currentTime from RoutineScheduler
     * 		get	sleepPeriods from Fitbit,
     * 		tickHour from Clock
     * 		always publish;
     * </code>
     */
    protected final class DiscoverForCurrentTimeFromRoutineScheduler {
        private final FitbitDiscovererForCurrentTimeFromRoutineScheduler fitbitDiscoverer = new FitbitDiscovererForCurrentTimeFromRoutineScheduler(AbstractGetFitbitInfos.this);
        private final ClockDiscovererForCurrentTimeFromRoutineScheduler clockDiscoverer = new ClockDiscovererForCurrentTimeFromRoutineScheduler(AbstractGetFitbitInfos.this);
        
        /**
         * @return a {@link FitbitDiscovererForCurrentTimeFromRoutineScheduler} object to discover <code>Fitbit</code> devices
         */
        public FitbitDiscovererForCurrentTimeFromRoutineScheduler fitbits() {
            return fitbitDiscoverer;
        }
        
        /**
         * @return a {@link ClockDiscovererForCurrentTimeFromRoutineScheduler} object to discover <code>Clock</code> devices
         */
        public ClockDiscovererForCurrentTimeFromRoutineScheduler clocks() {
            return clockDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Fitbit</code> devices to get their sources for the
     * <code>when provided currentTime from RoutineScheduler</code> interaction contract.
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
    protected final static class FitbitDiscovererForCurrentTimeFromRoutineScheduler {
        private Service serviceParent;
        
        private FitbitDiscovererForCurrentTimeFromRoutineScheduler(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private FitbitCompositeForCurrentTimeFromRoutineScheduler instantiateComposite() {
            return new FitbitCompositeForCurrentTimeFromRoutineScheduler(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Fitbit</code> devices
         * 
         * @return a {@link FitbitCompositeForCurrentTimeFromRoutineScheduler} object composed of all discoverable <code>Fitbit</code>
         */
        public FitbitCompositeForCurrentTimeFromRoutineScheduler all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Fitbit</code> devices
         * 
         * @return a {@link FitbitProxyForCurrentTimeFromRoutineScheduler} object pointing to a random discoverable <code>Fitbit</code> device
         */
        public FitbitProxyForCurrentTimeFromRoutineScheduler anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Fitbit</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link FitbitCompositeForCurrentTimeFromRoutineScheduler} object composed of all matching <code>Fitbit</code> devices
         */
        public FitbitCompositeForCurrentTimeFromRoutineScheduler whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Fitbit</code> devices to get their sources for the
     * <code>when provided currentTime from RoutineScheduler</code> interaction contract.
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
    protected final static class FitbitCompositeForCurrentTimeFromRoutineScheduler extends fr.inria.diagen.core.service.composite.Composite<FitbitProxyForCurrentTimeFromRoutineScheduler> {
        private FitbitCompositeForCurrentTimeFromRoutineScheduler(Service serviceParent) {
            super(serviceParent, "/Device/Device/Fitbit/");
        }
        
        @Override
        protected FitbitProxyForCurrentTimeFromRoutineScheduler instantiateProxy(RemoteServiceInfo rsi) {
            return new FitbitProxyForCurrentTimeFromRoutineScheduler(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link FitbitCompositeForCurrentTimeFromRoutineScheduler}, filtered using the attribute <code>id</code>.
         */
        public FitbitCompositeForCurrentTimeFromRoutineScheduler andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
    }
    
    /**
     * A proxy to one <code>Fitbit</code> device to get its sources for the
     * <code>when provided currentTime from RoutineScheduler</code> interaction contract.
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
    protected final static class FitbitProxyForCurrentTimeFromRoutineScheduler extends Proxy {
        private FitbitProxyForCurrentTimeFromRoutineScheduler(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Returns the value of the <code>sleepPeriods</code> source of this <code>Fitbit</code> device
         * 
         * @param period the value of the <code>period</code> index.
         * @return the value of the <code>sleepPeriods</code> source
         */
        public java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> getSleepPeriods(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws InvocationException {
            return (java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>) callGetValue("sleepPeriods", period);
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    
    /**
     * Discover object that will exposes the <code>Clock</code> devices to get their sources for the
     * <code>when provided currentTime from RoutineScheduler</code> interaction contract.
     * 
     * <pre>
     * device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
     * </pre>
     */
    protected final static class ClockDiscovererForCurrentTimeFromRoutineScheduler {
        private Service serviceParent;
        
        private ClockDiscovererForCurrentTimeFromRoutineScheduler(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private ClockCompositeForCurrentTimeFromRoutineScheduler instantiateComposite() {
            return new ClockCompositeForCurrentTimeFromRoutineScheduler(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Clock</code> devices
         * 
         * @return a {@link ClockCompositeForCurrentTimeFromRoutineScheduler} object composed of all discoverable <code>Clock</code>
         */
        public ClockCompositeForCurrentTimeFromRoutineScheduler all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Clock</code> devices
         * 
         * @return a {@link ClockProxyForCurrentTimeFromRoutineScheduler} object pointing to a random discoverable <code>Clock</code> device
         */
        public ClockProxyForCurrentTimeFromRoutineScheduler anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Clock</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link ClockCompositeForCurrentTimeFromRoutineScheduler} object composed of all matching <code>Clock</code> devices
         */
        public ClockCompositeForCurrentTimeFromRoutineScheduler whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Clock</code> devices to get their sources for the
     * <code>when provided currentTime from RoutineScheduler</code> interaction contract.
     * 
     * <pre>
     * device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
     * </pre>
     */
    protected final static class ClockCompositeForCurrentTimeFromRoutineScheduler extends fr.inria.diagen.core.service.composite.Composite<ClockProxyForCurrentTimeFromRoutineScheduler> {
        private ClockCompositeForCurrentTimeFromRoutineScheduler(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/Clock/");
        }
        
        @Override
        protected ClockProxyForCurrentTimeFromRoutineScheduler instantiateProxy(RemoteServiceInfo rsi) {
            return new ClockProxyForCurrentTimeFromRoutineScheduler(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link ClockCompositeForCurrentTimeFromRoutineScheduler}, filtered using the attribute <code>id</code>.
         */
        public ClockCompositeForCurrentTimeFromRoutineScheduler andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
    }
    
    /**
     * A proxy to one <code>Clock</code> device to get its sources for the
     * <code>when provided currentTime from RoutineScheduler</code> interaction contract.
     * 
     * <pre>
     * device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
     * </pre>
     */
    protected final static class ClockProxyForCurrentTimeFromRoutineScheduler extends Proxy {
        private ClockProxyForCurrentTimeFromRoutineScheduler(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Returns the value of the <code>tickHour</code> source of this <code>Clock</code> device
         * 
         * @return the value of the <code>tickHour</code> source
         */
        public java.lang.Integer getTickHour() throws InvocationException {
            return (java.lang.Integer) callGetValue("tickHour");
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover object for currentTime from RoutineScheduler
}
