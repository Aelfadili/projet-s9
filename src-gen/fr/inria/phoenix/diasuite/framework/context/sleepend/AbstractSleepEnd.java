package fr.inria.phoenix.diasuite.framework.context.sleepend;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.device.inactivitysensor.InactivityLevelFromInactivitySensor;

/**
 * <pre>
 * context SleepEnd as String {
 * 	when provided inactivityLevel from InactivitySensor
 * 		get tickHour from Clock,
 * 		lastInteraction from InactivitySensor
 * 	maybe publish;
 * }
 * </pre>
 */
@SuppressWarnings("all")
public abstract class AbstractSleepEnd extends Service {
    
    public AbstractSleepEnd(ServiceConfiguration serviceConfiguration) {
        super("/Context/SleepEnd/", serviceConfiguration);
    }
    
    // Methods from the Service class
    @Override
    protected final void internalPostInitialize() {
        postInitialize();
    }
    
    @Override
    protected void postInitialize() {
        // Default implementation of post initialize: subscribe to all required devices
        discoverInactivitySensorForSubscribe.all().subscribeInactivityLevel(); // subscribe to inactivityLevel from all InactivitySensor devices
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("inactivityLevel") && source.isCompatible("/Device/Device/Service/SoftwareSensor/InactivitySensor/")) {
            InactivityLevelFromInactivitySensor inactivityLevelFromInactivitySensor = new InactivityLevelFromInactivitySensor(this, source, (java.lang.Float) value);
            
            SleepEndValuePublishable returnValue = onInactivityLevelFromInactivitySensor(inactivityLevelFromInactivitySensor, new DiscoverForInactivityLevelFromInactivitySensor());
            if(returnValue != null && returnValue.doPublish()) {
                setSleepEnd(returnValue.getValue());
            }
        }
    }
    
    @Override
    public final Object getValueCalled(java.util.Map<String, Object> properties, RemoteServiceInfo source, String valueName,
            Object... indexes) throws Exception {
        if (valueName.equals("sleepEnd")) {
            return getLastValue();
        }
        throw new InvocationException("Unsupported method call: " + valueName);
    }
    // End of methods from the Service class
    
    // Code relative to the return value of the context
    private java.lang.String contextValue;
    
    private void setSleepEnd(java.lang.String newContextValue) {
        contextValue = newContextValue;
        getProcessor().publishValue(getOutProperties(), "sleepEnd", newContextValue);
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
     * A class that represents a value that might be published for the <code>SleepEnd</code> context. It is used by
     * event methods that might or might not publish values for this context.
     */
    protected final static class SleepEndValuePublishable {
        
        // The value of the context
        private java.lang.String value;
        // Whether the value should be published or not
        private boolean doPublish;
        
        public SleepEndValuePublishable(java.lang.String value, boolean doPublish) {
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
     * This method is called when a <code>InactivitySensor</code> device on which we have subscribed publish on its <code>inactivityLevel</code> source.
     * 
     * <pre>
     * when provided inactivityLevel from InactivitySensor
     * 		get tickHour from Clock,
     * 		lastInteraction from InactivitySensor
     * 	maybe publish;
     * </pre>
     * 
     * @param inactivityLevelFromInactivitySensor the value of the <code>inactivityLevel</code> source and the <code>InactivitySensor</code> device that published the value.
     * @param discover a discover object to get value from devices and contexts
     * @return a {@link SleepEndValuePublishable} that says if the context should publish a value and which value it should publish
     */
    protected abstract SleepEndValuePublishable onInactivityLevelFromInactivitySensor(InactivityLevelFromInactivitySensor inactivityLevelFromInactivitySensor, DiscoverForInactivityLevelFromInactivitySensor discover);
    
    // End of interaction contract implementation
    
    // Discover part for InactivitySensor devices
    /**
     * Use this object to discover InactivitySensor devices.
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
     * 
     * @see InactivitySensorDiscoverer
     */
    protected final InactivitySensorDiscoverer discoverInactivitySensorForSubscribe = new InactivitySensorDiscoverer(this);
    
    /**
     * Discover object that will exposes the <code>InactivitySensor</code> devices that can be discovered
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
    protected final static class InactivitySensorDiscoverer {
        private Service serviceParent;
        
        private InactivitySensorDiscoverer(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private InactivitySensorComposite instantiateComposite() {
            return new InactivitySensorComposite(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>InactivitySensor</code> devices
         * 
         * @return a {@link InactivitySensorComposite} object composed of all discoverable <code>InactivitySensor</code>
         */
        public InactivitySensorComposite all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>InactivitySensor</code> devices
         * 
         * @return a {@link InactivitySensorProxy} object pointing to a random discoverable <code>InactivitySensor</code> device
         */
        public InactivitySensorProxy anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>InactivitySensor</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link InactivitySensorComposite} object composed of all matching <code>InactivitySensor</code> devices
         */
        public InactivitySensorComposite whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>InactivitySensor</code> devices
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
    protected final static class InactivitySensorComposite extends fr.inria.diagen.core.service.composite.Composite<InactivitySensorProxy> {
        private InactivitySensorComposite(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/SoftwareSensor/InactivitySensor/");
        }
        
        @Override
        protected InactivitySensorProxy instantiateProxy(RemoteServiceInfo rsi) {
            return new InactivitySensorProxy(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link InactivitySensorComposite}, filtered using the attribute <code>id</code>.
         */
        public InactivitySensorComposite andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Subscribes to the <code>inactivityLevel</code> source. After a call to this method, the context will be notified when a
         * <code>InactivitySensor</code> device of this composite publishes a value on its <code>inactivityLevel</code> source.
         */
        public void subscribeInactivityLevel() {
            subscribeValue("inactivityLevel");
        }
        
        /**
         * Unsubscribes from the <code>inactivityLevel</code> source. After a call to this method, the context will no more be notified
         * when a <code>InactivitySensor</code> device of this composite publishes a value on its <code>inactivityLevel</code> source.
         */
        public void unsubscribeInactivityLevel() {
            unsubscribeValue("inactivityLevel");
        }
    }
    
    /**
     * A proxy to one <code>InactivitySensor</code> device
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
    protected final static class InactivitySensorProxy extends Proxy {
        private InactivitySensorProxy(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Subscribes to the <code>inactivityLevel</code> source. After a call to this method, the context will be notified when the
         * <code>InactivitySensor</code> device of this proxy publishes a value on its <code>inactivityLevel</code> source.
         */
        public void subscribeInactivityLevel() {
            subscribeValue("inactivityLevel");
        }
        
        /**
         * Unsubscribes from the <code>inactivityLevel</code> source. After a call to this method, the context will no more be notified
         * when the <code>InactivitySensor</code> device of this proxy publishes a value on its <code>inactivityLevel</code> source.
         */
        public void unsubscribeInactivityLevel() {
            unsubscribeValue("inactivityLevel");
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover part for InactivitySensor devices
    
    // Discover object for inactivityLevel from InactivitySensor
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided inactivityLevel from InactivitySensor
     * 		get tickHour from Clock,
     * 		lastInteraction from InactivitySensor
     * 	maybe publish;
     * </code>
     */
    protected final class DiscoverForInactivityLevelFromInactivitySensor {
        private final ClockDiscovererForInactivityLevelFromInactivitySensor clockDiscoverer = new ClockDiscovererForInactivityLevelFromInactivitySensor(AbstractSleepEnd.this);
        private final InactivitySensorDiscovererForInactivityLevelFromInactivitySensor inactivitySensorDiscoverer = new InactivitySensorDiscovererForInactivityLevelFromInactivitySensor(AbstractSleepEnd.this);
        
        /**
         * @return a {@link ClockDiscovererForInactivityLevelFromInactivitySensor} object to discover <code>Clock</code> devices
         */
        public ClockDiscovererForInactivityLevelFromInactivitySensor clocks() {
            return clockDiscoverer;
        }
        
        /**
         * @return a {@link InactivitySensorDiscovererForInactivityLevelFromInactivitySensor} object to discover <code>InactivitySensor</code> devices
         */
        public InactivitySensorDiscovererForInactivityLevelFromInactivitySensor inactivitySensors() {
            return inactivitySensorDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Clock</code> devices to get their sources for the
     * <code>when provided inactivityLevel from InactivitySensor</code> interaction contract.
     * 
     * <pre>
     * device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
     * </pre>
     */
    protected final static class ClockDiscovererForInactivityLevelFromInactivitySensor {
        private Service serviceParent;
        
        private ClockDiscovererForInactivityLevelFromInactivitySensor(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private ClockCompositeForInactivityLevelFromInactivitySensor instantiateComposite() {
            return new ClockCompositeForInactivityLevelFromInactivitySensor(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Clock</code> devices
         * 
         * @return a {@link ClockCompositeForInactivityLevelFromInactivitySensor} object composed of all discoverable <code>Clock</code>
         */
        public ClockCompositeForInactivityLevelFromInactivitySensor all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Clock</code> devices
         * 
         * @return a {@link ClockProxyForInactivityLevelFromInactivitySensor} object pointing to a random discoverable <code>Clock</code> device
         */
        public ClockProxyForInactivityLevelFromInactivitySensor anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Clock</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link ClockCompositeForInactivityLevelFromInactivitySensor} object composed of all matching <code>Clock</code> devices
         */
        public ClockCompositeForInactivityLevelFromInactivitySensor whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Clock</code> devices to get their sources for the
     * <code>when provided inactivityLevel from InactivitySensor</code> interaction contract.
     * 
     * <pre>
     * device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
     * </pre>
     */
    protected final static class ClockCompositeForInactivityLevelFromInactivitySensor extends fr.inria.diagen.core.service.composite.Composite<ClockProxyForInactivityLevelFromInactivitySensor> {
        private ClockCompositeForInactivityLevelFromInactivitySensor(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/Clock/");
        }
        
        @Override
        protected ClockProxyForInactivityLevelFromInactivitySensor instantiateProxy(RemoteServiceInfo rsi) {
            return new ClockProxyForInactivityLevelFromInactivitySensor(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link ClockCompositeForInactivityLevelFromInactivitySensor}, filtered using the attribute <code>id</code>.
         */
        public ClockCompositeForInactivityLevelFromInactivitySensor andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
    }
    
    /**
     * A proxy to one <code>Clock</code> device to get its sources for the
     * <code>when provided inactivityLevel from InactivitySensor</code> interaction contract.
     * 
     * <pre>
     * device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
     * </pre>
     */
    protected final static class ClockProxyForInactivityLevelFromInactivitySensor extends Proxy {
        private ClockProxyForInactivityLevelFromInactivitySensor(Service service, RemoteServiceInfo remoteServiceInfo) {
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
    
    /**
     * Discover object that will exposes the <code>InactivitySensor</code> devices to get their sources for the
     * <code>when provided inactivityLevel from InactivitySensor</code> interaction contract.
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
    protected final static class InactivitySensorDiscovererForInactivityLevelFromInactivitySensor {
        private Service serviceParent;
        
        private InactivitySensorDiscovererForInactivityLevelFromInactivitySensor(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private InactivitySensorCompositeForInactivityLevelFromInactivitySensor instantiateComposite() {
            return new InactivitySensorCompositeForInactivityLevelFromInactivitySensor(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>InactivitySensor</code> devices
         * 
         * @return a {@link InactivitySensorCompositeForInactivityLevelFromInactivitySensor} object composed of all discoverable <code>InactivitySensor</code>
         */
        public InactivitySensorCompositeForInactivityLevelFromInactivitySensor all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>InactivitySensor</code> devices
         * 
         * @return a {@link InactivitySensorProxyForInactivityLevelFromInactivitySensor} object pointing to a random discoverable <code>InactivitySensor</code> device
         */
        public InactivitySensorProxyForInactivityLevelFromInactivitySensor anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>InactivitySensor</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link InactivitySensorCompositeForInactivityLevelFromInactivitySensor} object composed of all matching <code>InactivitySensor</code> devices
         */
        public InactivitySensorCompositeForInactivityLevelFromInactivitySensor whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>InactivitySensor</code> devices to get their sources for the
     * <code>when provided inactivityLevel from InactivitySensor</code> interaction contract.
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
    protected final static class InactivitySensorCompositeForInactivityLevelFromInactivitySensor extends fr.inria.diagen.core.service.composite.Composite<InactivitySensorProxyForInactivityLevelFromInactivitySensor> {
        private InactivitySensorCompositeForInactivityLevelFromInactivitySensor(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/SoftwareSensor/InactivitySensor/");
        }
        
        @Override
        protected InactivitySensorProxyForInactivityLevelFromInactivitySensor instantiateProxy(RemoteServiceInfo rsi) {
            return new InactivitySensorProxyForInactivityLevelFromInactivitySensor(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link InactivitySensorCompositeForInactivityLevelFromInactivitySensor}, filtered using the attribute <code>id</code>.
         */
        public InactivitySensorCompositeForInactivityLevelFromInactivitySensor andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
    }
    
    /**
     * A proxy to one <code>InactivitySensor</code> device to get its sources for the
     * <code>when provided inactivityLevel from InactivitySensor</code> interaction contract.
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
    protected final static class InactivitySensorProxyForInactivityLevelFromInactivitySensor extends Proxy {
        private InactivitySensorProxyForInactivityLevelFromInactivitySensor(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
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
    // End of discover object for inactivityLevel from InactivitySensor
}
