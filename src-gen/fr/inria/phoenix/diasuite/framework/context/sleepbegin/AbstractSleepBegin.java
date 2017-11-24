package fr.inria.phoenix.diasuite.framework.context.sleepbegin;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.device.routinescheduler.CurrentTimeFromRoutineScheduler;

/**
 * <pre>
 * context SleepBegin as String {
 * 	when provided currentTime from RoutineScheduler
 * 		get inactivityLevel from InactivitySensor,
 * 		lastInteraction from InactivitySensor
 * 	maybe publish;
 * }
 * </pre>
 */
@SuppressWarnings("all")
public abstract class AbstractSleepBegin extends Service {
    
    public AbstractSleepBegin(ServiceConfiguration serviceConfiguration) {
        super("/Context/SleepBegin/", serviceConfiguration);
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
            
            SleepBeginValuePublishable returnValue = onCurrentTimeFromRoutineScheduler(currentTimeFromRoutineScheduler, new DiscoverForCurrentTimeFromRoutineScheduler());
            if(returnValue != null && returnValue.doPublish()) {
                setSleepBegin(returnValue.getValue());
            }
        }
    }
    
    @Override
    public final Object getValueCalled(java.util.Map<String, Object> properties, RemoteServiceInfo source, String valueName,
            Object... indexes) throws Exception {
        if (valueName.equals("sleepBegin")) {
            return getLastValue();
        }
        throw new InvocationException("Unsupported method call: " + valueName);
    }
    // End of methods from the Service class
    
    // Code relative to the return value of the context
    private java.lang.String contextValue;
    
    private void setSleepBegin(java.lang.String newContextValue) {
        contextValue = newContextValue;
        getProcessor().publishValue(getOutProperties(), "sleepBegin", newContextValue);
    }
    
    /**
     * Get the last value of the context
     * 
     * @return the latest value published by the context
     */
    protected final java.lang.String getLastValue() {
        return contextValue;
    }
    
    /**
     * A class that represents a value that might be published for the <code>SleepBegin</code> context. It is used by
     * event methods that might or might not publish values for this context.
     */
    protected final static class SleepBeginValuePublishable {
        
        // The value of the context
        private java.lang.String value;
        // Whether the value should be published or not
        private boolean doPublish;
        
        public SleepBeginValuePublishable(java.lang.String value, boolean doPublish) {
            this.value = value;
            this.doPublish = doPublish;
        }
        
        /**
         * @return the value of the context that might be published
         */
        public java.lang.String getValue() {
            return value;
        }
        
        /**
         * Sets the value that might be published
         * 
         * @param value the value that will be published if {@link #doPublish()} returns true
         */
        public void setValue(java.lang.String value) {
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
     * 		get inactivityLevel from InactivitySensor,
     * 		lastInteraction from InactivitySensor
     * 	maybe publish;
     * </pre>
     * 
     * @param currentTimeFromRoutineScheduler the value of the <code>currentTime</code> source and the <code>RoutineScheduler</code> device that published the value.
     * @param discover a discover object to get value from devices and contexts
     * @return a {@link SleepBeginValuePublishable} that says if the context should publish a value and which value it should publish
     */
    protected abstract SleepBeginValuePublishable onCurrentTimeFromRoutineScheduler(CurrentTimeFromRoutineScheduler currentTimeFromRoutineScheduler, DiscoverForCurrentTimeFromRoutineScheduler discover);
    
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
     * 		get inactivityLevel from InactivitySensor,
     * 		lastInteraction from InactivitySensor
     * 	maybe publish;
     * </code>
     */
    protected final class DiscoverForCurrentTimeFromRoutineScheduler {
        private final InactivitySensorDiscovererForCurrentTimeFromRoutineScheduler inactivitySensorDiscoverer = new InactivitySensorDiscovererForCurrentTimeFromRoutineScheduler(AbstractSleepBegin.this);
        
        /**
         * @return a {@link InactivitySensorDiscovererForCurrentTimeFromRoutineScheduler} object to discover <code>InactivitySensor</code> devices
         */
        public InactivitySensorDiscovererForCurrentTimeFromRoutineScheduler inactivitySensors() {
            return inactivitySensorDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>InactivitySensor</code> devices to get their sources for the
     * <code>when provided currentTime from RoutineScheduler</code> interaction contract.
     * <p>
     * ------------------------------------------------------------
     * Presence Detector					||
     * ------------------------------------------------------------
     * enumeration Room {
     * ENTRANCE, BEDROOM, KITCHEN, BATHROOM, LIVING, TOILET
     * }
     * 
     * device IndoorLocationDetector extends SoftwareSensor {
     * // AR (14/02/17)
     * source currentRoom as Room;
     * action SetLocation;
     * }
     * 
     * action SetLocation {
     * setLocation(location as Room);
     * resetLocation();
     * }
     * ------------------------------------------------------------
     * InactivitySensor					||
     * ------------------------------------------------------------
     * AR (01/08/17)
     * 
     * <pre>
     * device InactivitySensor extends SoftwareSensor {
     * 	source inactivityLevel as Float;
     * 	source lastInteraction as Interaction;
     * 	action UpdateInactivityLevel;
     * }
     * </pre>
     */
    protected final static class InactivitySensorDiscovererForCurrentTimeFromRoutineScheduler {
        private Service serviceParent;
        
        private InactivitySensorDiscovererForCurrentTimeFromRoutineScheduler(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private InactivitySensorCompositeForCurrentTimeFromRoutineScheduler instantiateComposite() {
            return new InactivitySensorCompositeForCurrentTimeFromRoutineScheduler(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>InactivitySensor</code> devices
         * 
         * @return a {@link InactivitySensorCompositeForCurrentTimeFromRoutineScheduler} object composed of all discoverable <code>InactivitySensor</code>
         */
        public InactivitySensorCompositeForCurrentTimeFromRoutineScheduler all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>InactivitySensor</code> devices
         * 
         * @return a {@link InactivitySensorProxyForCurrentTimeFromRoutineScheduler} object pointing to a random discoverable <code>InactivitySensor</code> device
         */
        public InactivitySensorProxyForCurrentTimeFromRoutineScheduler anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>InactivitySensor</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link InactivitySensorCompositeForCurrentTimeFromRoutineScheduler} object composed of all matching <code>InactivitySensor</code> devices
         */
        public InactivitySensorCompositeForCurrentTimeFromRoutineScheduler whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>InactivitySensor</code> devices to get their sources for the
     * <code>when provided currentTime from RoutineScheduler</code> interaction contract.
     * <p>
     * ------------------------------------------------------------
     * Presence Detector					||
     * ------------------------------------------------------------
     * enumeration Room {
     * ENTRANCE, BEDROOM, KITCHEN, BATHROOM, LIVING, TOILET
     * }
     * 
     * device IndoorLocationDetector extends SoftwareSensor {
     * // AR (14/02/17)
     * source currentRoom as Room;
     * action SetLocation;
     * }
     * 
     * action SetLocation {
     * setLocation(location as Room);
     * resetLocation();
     * }
     * ------------------------------------------------------------
     * InactivitySensor					||
     * ------------------------------------------------------------
     * AR (01/08/17)
     * 
     * <pre>
     * device InactivitySensor extends SoftwareSensor {
     * 	source inactivityLevel as Float;
     * 	source lastInteraction as Interaction;
     * 	action UpdateInactivityLevel;
     * }
     * </pre>
     */
    protected final static class InactivitySensorCompositeForCurrentTimeFromRoutineScheduler extends fr.inria.diagen.core.service.composite.Composite<InactivitySensorProxyForCurrentTimeFromRoutineScheduler> {
        private InactivitySensorCompositeForCurrentTimeFromRoutineScheduler(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/SoftwareSensor/InactivitySensor/");
        }
        
        @Override
        protected InactivitySensorProxyForCurrentTimeFromRoutineScheduler instantiateProxy(RemoteServiceInfo rsi) {
            return new InactivitySensorProxyForCurrentTimeFromRoutineScheduler(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link InactivitySensorCompositeForCurrentTimeFromRoutineScheduler}, filtered using the attribute <code>id</code>.
         */
        public InactivitySensorCompositeForCurrentTimeFromRoutineScheduler andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
    }
    
    /**
     * A proxy to one <code>InactivitySensor</code> device to get its sources for the
     * <code>when provided currentTime from RoutineScheduler</code> interaction contract.
     * <p>
     * ------------------------------------------------------------
     * Presence Detector					||
     * ------------------------------------------------------------
     * enumeration Room {
     * ENTRANCE, BEDROOM, KITCHEN, BATHROOM, LIVING, TOILET
     * }
     * 
     * device IndoorLocationDetector extends SoftwareSensor {
     * // AR (14/02/17)
     * source currentRoom as Room;
     * action SetLocation;
     * }
     * 
     * action SetLocation {
     * setLocation(location as Room);
     * resetLocation();
     * }
     * ------------------------------------------------------------
     * InactivitySensor					||
     * ------------------------------------------------------------
     * AR (01/08/17)
     * 
     * <pre>
     * device InactivitySensor extends SoftwareSensor {
     * 	source inactivityLevel as Float;
     * 	source lastInteraction as Interaction;
     * 	action UpdateInactivityLevel;
     * }
     * </pre>
     */
    protected final static class InactivitySensorProxyForCurrentTimeFromRoutineScheduler extends Proxy {
        private InactivitySensorProxyForCurrentTimeFromRoutineScheduler(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Returns the value of the <code>inactivityLevel</code> source of this <code>InactivitySensor</code> device
         * 
         * @return the value of the <code>inactivityLevel</code> source
         */
        public java.lang.Float getInactivityLevel() throws InvocationException {
            return (java.lang.Float) callGetValue("inactivityLevel");
        }
        
        /**
         * Returns the value of the <code>lastInteraction</code> source of this <code>InactivitySensor</code> device
         * 
         * @return the value of the <code>lastInteraction</code> source
         */
        public fr.inria.phoenix.diasuite.framework.datatype.interaction.Interaction getLastInteraction() throws InvocationException {
            return (fr.inria.phoenix.diasuite.framework.datatype.interaction.Interaction) callGetValue("lastInteraction");
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
