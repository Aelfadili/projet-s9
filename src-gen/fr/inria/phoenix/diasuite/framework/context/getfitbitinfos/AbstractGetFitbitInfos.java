package fr.inria.phoenix.diasuite.framework.context.getfitbitinfos;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.device.routinescheduler.CurrentTimeFromRoutineScheduler;
import fr.inria.phoenix.diasuite.framework.device.clock.TickHourFromClock;

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
 * context GetFitbitInfos as SleepPeriod[] {
 * 	when provided currentTime from RoutineScheduler
 * 		get	lastSynchronization from Fitbit, sleepPeriods from Fitbit
 * 		maybe publish;
 * 	when provided tickHour from Clock
 * 		get lastSynchronization from Fitbit, sleepPeriods from Fitbit
 * 		maybe publish;
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
        discoverClockForSubscribe.all().subscribeTickHour(); // subscribe to tickHour from all Clock devices
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("tickHour") && source.isCompatible("/Device/Device/Service/Clock/")) {
            TickHourFromClock tickHourFromClock = new TickHourFromClock(this, source, (java.lang.Integer) value);
            
            GetFitbitInfosValuePublishable returnValue = onTickHourFromClock(tickHourFromClock, new DiscoverForTickHourFromClock());
            if(returnValue != null && returnValue.doPublish()) {
                setGetFitbitInfos(returnValue.getValue());
            }
        }
        if (eventName.equals("currentTime") && source.isCompatible("/Device/Device/Service/RoutineScheduler/")) {
            CurrentTimeFromRoutineScheduler currentTimeFromRoutineScheduler = new CurrentTimeFromRoutineScheduler(this, source, (fr.inria.phoenix.diasuite.framework.datatype.daytime.DayTime) value);
            
            GetFitbitInfosValuePublishable returnValue = onCurrentTimeFromRoutineScheduler(currentTimeFromRoutineScheduler, new DiscoverForCurrentTimeFromRoutineScheduler());
            if(returnValue != null && returnValue.doPublish()) {
                setGetFitbitInfos(returnValue.getValue());
            }
        }
    }
    
    @Override
    public final Object getValueCalled(java.util.Map<String, Object> properties, RemoteServiceInfo source, String valueName,
            Object... indexes) throws Exception {
        if (valueName.equals("getFitbitInfos")) {
            return getLastValue();
        }
        throw new InvocationException("Unsupported method call: " + valueName);
    }
    // End of methods from the Service class
    
    // Code relative to the return value of the context
    private java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> contextValue;
    
    private void setGetFitbitInfos(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> newContextValue) {
        contextValue = newContextValue;
        getProcessor().publishValue(getOutProperties(), "getFitbitInfos", newContextValue);
    }
    
    /**
     * Get the last value of the context
     * 
     * @return the latest value published by the context
     */
    protected final java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> getLastValue() {
        return contextValue;
    }
    
    /**
     * A class that represents a value that might be published for the <code>GetFitbitInfos</code> context. It is used by
     * event methods that might or might not publish values for this context.
     */
    protected final static class GetFitbitInfosValuePublishable {
        
        // The value of the context
        private java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value;
        // Whether the value should be published or not
        private boolean doPublish;
        
        public GetFitbitInfosValuePublishable(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value, boolean doPublish) {
            this.value = value;
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
     * This method is called when a <code>RoutineScheduler</code> device on which we have subscribed publish on its <code>currentTime</code> source.
     * 
     * <pre>
     * when provided currentTime from RoutineScheduler
     * 		get	lastSynchronization from Fitbit, sleepPeriods from Fitbit
     * 		maybe publish;
     * </pre>
     * 
     * @param currentTimeFromRoutineScheduler the value of the <code>currentTime</code> source and the <code>RoutineScheduler</code> device that published the value.
     * @param discover a discover object to get value from devices and contexts
     * @return a {@link GetFitbitInfosValuePublishable} that says if the context should publish a value and which value it should publish
     */
    protected abstract GetFitbitInfosValuePublishable onCurrentTimeFromRoutineScheduler(CurrentTimeFromRoutineScheduler currentTimeFromRoutineScheduler, DiscoverForCurrentTimeFromRoutineScheduler discover);
    
    /**
     * This method is called when a <code>Clock</code> device on which we have subscribed publish on its <code>tickHour</code> source.
     * 
     * <pre>
     * when provided tickHour from Clock
     * 		get lastSynchronization from Fitbit, sleepPeriods from Fitbit
     * 		maybe publish;
     * </pre>
     * 
     * @param tickHourFromClock the value of the <code>tickHour</code> source and the <code>Clock</code> device that published the value.
     * @param discover a discover object to get value from devices and contexts
     * @return a {@link GetFitbitInfosValuePublishable} that says if the context should publish a value and which value it should publish
     */
    protected abstract GetFitbitInfosValuePublishable onTickHourFromClock(TickHourFromClock tickHourFromClock, DiscoverForTickHourFromClock discover);
    
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
    
    // Discover part for Clock devices
    /**
     * Use this object to discover Clock devices.
     * 
     * <pre>
     * device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
     * </pre>
     * 
     * @see ClockDiscoverer
     */
    protected final ClockDiscoverer discoverClockForSubscribe = new ClockDiscoverer(this);
    
    /**
     * Discover object that will exposes the <code>Clock</code> devices that can be discovered
     * 
     * <pre>
     * device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
     * </pre>
     */
    protected final static class ClockDiscoverer {
        private Service serviceParent;
        
        private ClockDiscoverer(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private ClockComposite instantiateComposite() {
            return new ClockComposite(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Clock</code> devices
         * 
         * @return a {@link ClockComposite} object composed of all discoverable <code>Clock</code>
         */
        public ClockComposite all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Clock</code> devices
         * 
         * @return a {@link ClockProxy} object pointing to a random discoverable <code>Clock</code> device
         */
        public ClockProxy anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Clock</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link ClockComposite} object composed of all matching <code>Clock</code> devices
         */
        public ClockComposite whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Clock</code> devices
     * 
     * <pre>
     * device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
     * </pre>
     */
    protected final static class ClockComposite extends fr.inria.diagen.core.service.composite.Composite<ClockProxy> {
        private ClockComposite(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/Clock/");
        }
        
        @Override
        protected ClockProxy instantiateProxy(RemoteServiceInfo rsi) {
            return new ClockProxy(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link ClockComposite}, filtered using the attribute <code>id</code>.
         */
        public ClockComposite andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Subscribes to the <code>tickHour</code> source. After a call to this method, the context will be notified when a
         * <code>Clock</code> device of this composite publishes a value on its <code>tickHour</code> source.
         */
        public void subscribeTickHour() {
            subscribeValue("tickHour");
        }
        
        /**
         * Unsubscribes from the <code>tickHour</code> source. After a call to this method, the context will no more be notified
         * when a <code>Clock</code> device of this composite publishes a value on its <code>tickHour</code> source.
         */
        public void unsubscribeTickHour() {
            unsubscribeValue("tickHour");
        }
    }
    
    /**
     * A proxy to one <code>Clock</code> device
     * 
     * <pre>
     * device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
     * </pre>
     */
    protected final static class ClockProxy extends Proxy {
        private ClockProxy(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Subscribes to the <code>tickHour</code> source. After a call to this method, the context will be notified when the
         * <code>Clock</code> device of this proxy publishes a value on its <code>tickHour</code> source.
         */
        public void subscribeTickHour() {
            subscribeValue("tickHour");
        }
        
        /**
         * Unsubscribes from the <code>tickHour</code> source. After a call to this method, the context will no more be notified
         * when the <code>Clock</code> device of this proxy publishes a value on its <code>tickHour</code> source.
         */
        public void unsubscribeTickHour() {
            unsubscribeValue("tickHour");
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover part for Clock devices
    
    // Discover object for currentTime from RoutineScheduler
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided currentTime from RoutineScheduler
     * 		get	lastSynchronization from Fitbit, sleepPeriods from Fitbit
     * 		maybe publish;
     * </code>
     */
    protected final class DiscoverForCurrentTimeFromRoutineScheduler {
        private final FitbitDiscovererForCurrentTimeFromRoutineScheduler fitbitDiscoverer = new FitbitDiscovererForCurrentTimeFromRoutineScheduler(AbstractGetFitbitInfos.this);
        
        /**
         * @return a {@link FitbitDiscovererForCurrentTimeFromRoutineScheduler} object to discover <code>Fitbit</code> devices
         */
        public FitbitDiscovererForCurrentTimeFromRoutineScheduler fitbits() {
            return fitbitDiscoverer;
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
     * 	source lastSynchronization as Date;
     * 	source calories as Integer indexed by period as Period;
     * 	source distanceInMeters as Integer indexed by period as Period;
     * 	source pulses as Pulse indexed by period as Period;
     * 	source steps as Integer indexed by period as Period;
     * 	source heartActivity as HeartActivity indexed by period as Period, heartZone as HeartRate;
     * 	source sleepPeriods as SleepPeriod [] indexed by period as Period;
     * 	source physiologicalActivities as PhysiologicalActivity [] indexed by period as Period;
     * 	source alarm as Alarm indexed by name as String;
     * 	action Vibrate;
     * 	action ScheduleAlarm;
     * 	action RegisterPhysiologicalActivity;
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
     * 	source lastSynchronization as Date;
     * 	source calories as Integer indexed by period as Period;
     * 	source distanceInMeters as Integer indexed by period as Period;
     * 	source pulses as Pulse indexed by period as Period;
     * 	source steps as Integer indexed by period as Period;
     * 	source heartActivity as HeartActivity indexed by period as Period, heartZone as HeartRate;
     * 	source sleepPeriods as SleepPeriod [] indexed by period as Period;
     * 	source physiologicalActivities as PhysiologicalActivity [] indexed by period as Period;
     * 	source alarm as Alarm indexed by name as String;
     * 	action Vibrate;
     * 	action ScheduleAlarm;
     * 	action RegisterPhysiologicalActivity;
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
     * 	source lastSynchronization as Date;
     * 	source calories as Integer indexed by period as Period;
     * 	source distanceInMeters as Integer indexed by period as Period;
     * 	source pulses as Pulse indexed by period as Period;
     * 	source steps as Integer indexed by period as Period;
     * 	source heartActivity as HeartActivity indexed by period as Period, heartZone as HeartRate;
     * 	source sleepPeriods as SleepPeriod [] indexed by period as Period;
     * 	source physiologicalActivities as PhysiologicalActivity [] indexed by period as Period;
     * 	source alarm as Alarm indexed by name as String;
     * 	action Vibrate;
     * 	action ScheduleAlarm;
     * 	action RegisterPhysiologicalActivity;
     * }
     * </pre>
     */
    protected final static class FitbitProxyForCurrentTimeFromRoutineScheduler extends Proxy {
        private FitbitProxyForCurrentTimeFromRoutineScheduler(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Returns the value of the <code>lastSynchronization</code> source of this <code>Fitbit</code> device
         * 
         * @return the value of the <code>lastSynchronization</code> source
         */
        public fr.inria.phoenix.diasuite.framework.datatype.date.Date getLastSynchronization() throws InvocationException {
            return (fr.inria.phoenix.diasuite.framework.datatype.date.Date) callGetValue("lastSynchronization");
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
    // End of discover object for currentTime from RoutineScheduler
    
    // Discover object for tickHour from Clock
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided tickHour from Clock
     * 		get lastSynchronization from Fitbit, sleepPeriods from Fitbit
     * 		maybe publish;
     * </code>
     */
    protected final class DiscoverForTickHourFromClock {
        private final FitbitDiscovererForTickHourFromClock fitbitDiscoverer = new FitbitDiscovererForTickHourFromClock(AbstractGetFitbitInfos.this);
        
        /**
         * @return a {@link FitbitDiscovererForTickHourFromClock} object to discover <code>Fitbit</code> devices
         */
        public FitbitDiscovererForTickHourFromClock fitbits() {
            return fitbitDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Fitbit</code> devices to get their sources for the
     * <code>when provided tickHour from Clock</code> interaction contract.
     * <p>
     * ------------------------------------------------------------
     * Fitbit							||
     * ------------------------------------------------------------
     * 
     * <pre>
     * device Fitbit extends Device {
     * 	source lastSynchronization as Date;
     * 	source calories as Integer indexed by period as Period;
     * 	source distanceInMeters as Integer indexed by period as Period;
     * 	source pulses as Pulse indexed by period as Period;
     * 	source steps as Integer indexed by period as Period;
     * 	source heartActivity as HeartActivity indexed by period as Period, heartZone as HeartRate;
     * 	source sleepPeriods as SleepPeriod [] indexed by period as Period;
     * 	source physiologicalActivities as PhysiologicalActivity [] indexed by period as Period;
     * 	source alarm as Alarm indexed by name as String;
     * 	action Vibrate;
     * 	action ScheduleAlarm;
     * 	action RegisterPhysiologicalActivity;
     * }
     * </pre>
     */
    protected final static class FitbitDiscovererForTickHourFromClock {
        private Service serviceParent;
        
        private FitbitDiscovererForTickHourFromClock(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private FitbitCompositeForTickHourFromClock instantiateComposite() {
            return new FitbitCompositeForTickHourFromClock(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Fitbit</code> devices
         * 
         * @return a {@link FitbitCompositeForTickHourFromClock} object composed of all discoverable <code>Fitbit</code>
         */
        public FitbitCompositeForTickHourFromClock all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Fitbit</code> devices
         * 
         * @return a {@link FitbitProxyForTickHourFromClock} object pointing to a random discoverable <code>Fitbit</code> device
         */
        public FitbitProxyForTickHourFromClock anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Fitbit</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link FitbitCompositeForTickHourFromClock} object composed of all matching <code>Fitbit</code> devices
         */
        public FitbitCompositeForTickHourFromClock whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Fitbit</code> devices to get their sources for the
     * <code>when provided tickHour from Clock</code> interaction contract.
     * <p>
     * ------------------------------------------------------------
     * Fitbit							||
     * ------------------------------------------------------------
     * 
     * <pre>
     * device Fitbit extends Device {
     * 	source lastSynchronization as Date;
     * 	source calories as Integer indexed by period as Period;
     * 	source distanceInMeters as Integer indexed by period as Period;
     * 	source pulses as Pulse indexed by period as Period;
     * 	source steps as Integer indexed by period as Period;
     * 	source heartActivity as HeartActivity indexed by period as Period, heartZone as HeartRate;
     * 	source sleepPeriods as SleepPeriod [] indexed by period as Period;
     * 	source physiologicalActivities as PhysiologicalActivity [] indexed by period as Period;
     * 	source alarm as Alarm indexed by name as String;
     * 	action Vibrate;
     * 	action ScheduleAlarm;
     * 	action RegisterPhysiologicalActivity;
     * }
     * </pre>
     */
    protected final static class FitbitCompositeForTickHourFromClock extends fr.inria.diagen.core.service.composite.Composite<FitbitProxyForTickHourFromClock> {
        private FitbitCompositeForTickHourFromClock(Service serviceParent) {
            super(serviceParent, "/Device/Device/Fitbit/");
        }
        
        @Override
        protected FitbitProxyForTickHourFromClock instantiateProxy(RemoteServiceInfo rsi) {
            return new FitbitProxyForTickHourFromClock(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link FitbitCompositeForTickHourFromClock}, filtered using the attribute <code>id</code>.
         */
        public FitbitCompositeForTickHourFromClock andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
    }
    
    /**
     * A proxy to one <code>Fitbit</code> device to get its sources for the
     * <code>when provided tickHour from Clock</code> interaction contract.
     * <p>
     * ------------------------------------------------------------
     * Fitbit							||
     * ------------------------------------------------------------
     * 
     * <pre>
     * device Fitbit extends Device {
     * 	source lastSynchronization as Date;
     * 	source calories as Integer indexed by period as Period;
     * 	source distanceInMeters as Integer indexed by period as Period;
     * 	source pulses as Pulse indexed by period as Period;
     * 	source steps as Integer indexed by period as Period;
     * 	source heartActivity as HeartActivity indexed by period as Period, heartZone as HeartRate;
     * 	source sleepPeriods as SleepPeriod [] indexed by period as Period;
     * 	source physiologicalActivities as PhysiologicalActivity [] indexed by period as Period;
     * 	source alarm as Alarm indexed by name as String;
     * 	action Vibrate;
     * 	action ScheduleAlarm;
     * 	action RegisterPhysiologicalActivity;
     * }
     * </pre>
     */
    protected final static class FitbitProxyForTickHourFromClock extends Proxy {
        private FitbitProxyForTickHourFromClock(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Returns the value of the <code>lastSynchronization</code> source of this <code>Fitbit</code> device
         * 
         * @return the value of the <code>lastSynchronization</code> source
         */
        public fr.inria.phoenix.diasuite.framework.datatype.date.Date getLastSynchronization() throws InvocationException {
            return (fr.inria.phoenix.diasuite.framework.datatype.date.Date) callGetValue("lastSynchronization");
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
    // End of discover object for tickHour from Clock
}
