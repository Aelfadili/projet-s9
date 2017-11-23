package fr.inria.phoenix.diasuite.framework.context.sleepbegin;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.device.clock.TickHourFromClock;

/**
 * <pre>
 * context SleepBegin as String {
 * 	when provided tickHour from Clock
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
        discoverClockForSubscribe.all().subscribeTickHour(); // subscribe to tickHour from all Clock devices
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("tickHour") && source.isCompatible("/Device/Device/Service/Clock/")) {
            TickHourFromClock tickHourFromClock = new TickHourFromClock(this, source, (java.lang.Integer) value);
            
            SleepBeginValuePublishable returnValue = onTickHourFromClock(tickHourFromClock, new DiscoverForTickHourFromClock());
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
     * This method is called when a <code>Clock</code> device on which we have subscribed publish on its <code>tickHour</code> source.
     * 
     * <pre>
     * when provided tickHour from Clock
     * 		get inactivityLevel from InactivitySensor,
     * 		lastInteraction from InactivitySensor
     * 	maybe publish;
     * </pre>
     * 
     * @param tickHourFromClock the value of the <code>tickHour</code> source and the <code>Clock</code> device that published the value.
     * @param discover a discover object to get value from devices and contexts
     * @return a {@link SleepBeginValuePublishable} that says if the context should publish a value and which value it should publish
     */
    protected abstract SleepBeginValuePublishable onTickHourFromClock(TickHourFromClock tickHourFromClock, DiscoverForTickHourFromClock discover);
    
    // End of interaction contract implementation
    
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
    
    // Discover object for tickHour from Clock
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided tickHour from Clock
     * 		get inactivityLevel from InactivitySensor,
     * 		lastInteraction from InactivitySensor
     * 	maybe publish;
     * </code>
     */
    protected final class DiscoverForTickHourFromClock {
        private final InactivitySensorDiscovererForTickHourFromClock inactivitySensorDiscoverer = new InactivitySensorDiscovererForTickHourFromClock(AbstractSleepBegin.this);
        
        /**
         * @return a {@link InactivitySensorDiscovererForTickHourFromClock} object to discover <code>InactivitySensor</code> devices
         */
        public InactivitySensorDiscovererForTickHourFromClock inactivitySensors() {
            return inactivitySensorDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>InactivitySensor</code> devices to get their sources for the
     * <code>when provided tickHour from Clock</code> interaction contract.
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
    protected final static class InactivitySensorDiscovererForTickHourFromClock {
        private Service serviceParent;
        
        private InactivitySensorDiscovererForTickHourFromClock(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private InactivitySensorCompositeForTickHourFromClock instantiateComposite() {
            return new InactivitySensorCompositeForTickHourFromClock(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>InactivitySensor</code> devices
         * 
         * @return a {@link InactivitySensorCompositeForTickHourFromClock} object composed of all discoverable <code>InactivitySensor</code>
         */
        public InactivitySensorCompositeForTickHourFromClock all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>InactivitySensor</code> devices
         * 
         * @return a {@link InactivitySensorProxyForTickHourFromClock} object pointing to a random discoverable <code>InactivitySensor</code> device
         */
        public InactivitySensorProxyForTickHourFromClock anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>InactivitySensor</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link InactivitySensorCompositeForTickHourFromClock} object composed of all matching <code>InactivitySensor</code> devices
         */
        public InactivitySensorCompositeForTickHourFromClock whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>InactivitySensor</code> devices to get their sources for the
     * <code>when provided tickHour from Clock</code> interaction contract.
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
    protected final static class InactivitySensorCompositeForTickHourFromClock extends fr.inria.diagen.core.service.composite.Composite<InactivitySensorProxyForTickHourFromClock> {
        private InactivitySensorCompositeForTickHourFromClock(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/SoftwareSensor/InactivitySensor/");
        }
        
        @Override
        protected InactivitySensorProxyForTickHourFromClock instantiateProxy(RemoteServiceInfo rsi) {
            return new InactivitySensorProxyForTickHourFromClock(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link InactivitySensorCompositeForTickHourFromClock}, filtered using the attribute <code>id</code>.
         */
        public InactivitySensorCompositeForTickHourFromClock andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
    }
    
    /**
     * A proxy to one <code>InactivitySensor</code> device to get its sources for the
     * <code>when provided tickHour from Clock</code> interaction contract.
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
    protected final static class InactivitySensorProxyForTickHourFromClock extends Proxy {
        private InactivitySensorProxyForTickHourFromClock(Service service, RemoteServiceInfo remoteServiceInfo) {
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
    // End of discover object for tickHour from Clock
}
