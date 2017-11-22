package fr.inria.phoenix.diasuite.framework.context.propositiondactivity;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.device.activitynotifier.DailyActivityFromActivityNotifier;

/**
 * <pre>
context PropositionDActivity as Boolean {
 * 	when provided dailyActivity from ActivityNotifier
 * 	get dailyActivity from ActivityNotifier
 * 	maybe publish;
 * }
</pre>
 */
@SuppressWarnings("all")
public abstract class AbstractPropositionDActivity extends Service {
    
    public AbstractPropositionDActivity(ServiceConfiguration serviceConfiguration) {
        super("/Context/PropositionDActivity/", serviceConfiguration);
    }
    
    // Methods from the Service class
    @Override
    protected final void internalPostInitialize() {
        postInitialize();
    }
    
    @Override
    protected void postInitialize() {
        // Default implementation of post initialize: subscribe to all required devices
        discoverActivityNotifierForSubscribe.all().subscribeDailyActivity(); // subscribe to dailyActivity from all ActivityNotifier devices
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("dailyActivity") && source.isCompatible("/Device/Device/Service/SoftwareSensor/ActivityNotifier/")) {
            DailyActivityFromActivityNotifier dailyActivityFromActivityNotifier = new DailyActivityFromActivityNotifier(this, source, (fr.inria.phoenix.diasuite.framework.datatype.dailyactivity.DailyActivity) value);
            
            PropositionDActivityValuePublishable returnValue = onDailyActivityFromActivityNotifier(dailyActivityFromActivityNotifier, new DiscoverForDailyActivityFromActivityNotifier());
            if(returnValue != null && returnValue.doPublish()) {
                setPropositionDActivity(returnValue.getValue());
            }
        }
    }
    
    @Override
    public final Object getValueCalled(java.util.Map<String, Object> properties, RemoteServiceInfo source, String valueName,
            Object... indexes) throws Exception {
        if (valueName.equals("propositionDActivity")) {
            return getLastValue();
        }
        throw new InvocationException("Unsupported method call: " + valueName);
    }
    // End of methods from the Service class
    
    // Code relative to the return value of the context
    private java.lang.Boolean contextValue;
    
    private void setPropositionDActivity(java.lang.Boolean newContextValue) {
        contextValue = newContextValue;
        getProcessor().publishValue(getOutProperties(), "propositionDActivity", newContextValue);
    }
    
    /**
     * Get the last value of the context
     * 
     * @return the latest value published by the context
     */
    protected final java.lang.Boolean getLastValue() {
        return contextValue;
    }
    
    /**
     * A class that represents a value that might be published for the <code>PropositionDActivity</code> context. It is used by
     * event methods that might or might not publish values for this context.
     */
    protected final static class PropositionDActivityValuePublishable {
        
        // The value of the context
        private java.lang.Boolean value;
        // Whether the value should be published or not
        private boolean doPublish;
        
        public PropositionDActivityValuePublishable(java.lang.Boolean value, boolean doPublish) {
            this.value = value;
            this.doPublish = doPublish;
        }
        
        /**
         * @return the value of the context that might be published
         */
        public java.lang.Boolean getValue() {
            return value;
        }
        
        /**
         * Sets the value that might be published
         * 
         * @param value the value that will be published if {@link #doPublish()} returns true
         */
        public void setValue(java.lang.Boolean value) {
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
     * This method is called when a <code>ActivityNotifier</code> device on which we have subscribed publish on its <code>dailyActivity</code> source.
    
    <pre>
    when provided dailyActivity from ActivityNotifier
     * 	get dailyActivity from ActivityNotifier
     * 	maybe publish;
    </pre>
     * 
     * @param dailyActivityFromActivityNotifier the value of the <code>dailyActivity</code> source and the <code>ActivityNotifier</code> device that published the value.
     * @param discover a discover object to get value from devices and contexts
     * @return a {@link PropositionDActivityValuePublishable} that says if the context should publish a value and which value it should publish
     */
    protected abstract PropositionDActivityValuePublishable onDailyActivityFromActivityNotifier(DailyActivityFromActivityNotifier dailyActivityFromActivityNotifier, DiscoverForDailyActivityFromActivityNotifier discover);
    
    // End of interaction contract implementation
    
    // Discover part for ActivityNotifier devices
    /**
     * Use this object to discover ActivityNotifier devices.
    <p>
    ------------------------------------------------------------
    ActivityNotifier					||
    ------------------------------------------------------------
    
    <pre>
    device ActivityNotifier extends SoftwareSensor {
     * 	source dailyActivity as DailyActivity;
     * 	source periodActivity as PeriodActivity;
     * 	action NotifyActivity;
     * }
    </pre>
     * 
     * @see ActivityNotifierDiscoverer
     */
    protected final ActivityNotifierDiscoverer discoverActivityNotifierForSubscribe = new ActivityNotifierDiscoverer(this);
    
    /**
     * Discover object that will exposes the <code>ActivityNotifier</code> devices that can be discovered
    <p>
    ------------------------------------------------------------
    ActivityNotifier					||
    ------------------------------------------------------------
    
    <pre>
    device ActivityNotifier extends SoftwareSensor {
     * 	source dailyActivity as DailyActivity;
     * 	source periodActivity as PeriodActivity;
     * 	action NotifyActivity;
     * }
    </pre>
     */
    protected final static class ActivityNotifierDiscoverer {
        private Service serviceParent;
        
        private ActivityNotifierDiscoverer(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private ActivityNotifierComposite instantiateComposite() {
            return new ActivityNotifierComposite(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierComposite} object composed of all discoverable <code>ActivityNotifier</code>
         */
        public ActivityNotifierComposite all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierProxy} object pointing to a random discoverable <code>ActivityNotifier</code> device
         */
        public ActivityNotifierProxy anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link ActivityNotifierComposite} object composed of all matching <code>ActivityNotifier</code> devices
         */
        public ActivityNotifierComposite whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>ActivityNotifier</code> devices
    <p>
    ------------------------------------------------------------
    ActivityNotifier					||
    ------------------------------------------------------------
    
    <pre>
    device ActivityNotifier extends SoftwareSensor {
     * 	source dailyActivity as DailyActivity;
     * 	source periodActivity as PeriodActivity;
     * 	action NotifyActivity;
     * }
    </pre>
     */
    protected final static class ActivityNotifierComposite extends fr.inria.diagen.core.service.composite.Composite<ActivityNotifierProxy> {
        private ActivityNotifierComposite(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/SoftwareSensor/ActivityNotifier/");
        }
        
        @Override
        protected ActivityNotifierProxy instantiateProxy(RemoteServiceInfo rsi) {
            return new ActivityNotifierProxy(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link ActivityNotifierComposite}, filtered using the attribute <code>id</code>.
         */
        public ActivityNotifierComposite andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Subscribes to the <code>dailyActivity</code> source. After a call to this method, the context will be notified when a
         * <code>ActivityNotifier</code> device of this composite publishes a value on its <code>dailyActivity</code> source.
         */
        public void subscribeDailyActivity() {
            subscribeValue("dailyActivity");
        }
        
        /**
         * Unsubscribes from the <code>dailyActivity</code> source. After a call to this method, the context will no more be notified
         * when a <code>ActivityNotifier</code> device of this composite publishes a value on its <code>dailyActivity</code> source.
         */
        public void unsubscribeDailyActivity() {
            unsubscribeValue("dailyActivity");
        }
    }
    
    /**
     * A proxy to one <code>ActivityNotifier</code> device
    <p>
    ------------------------------------------------------------
    ActivityNotifier					||
    ------------------------------------------------------------
    
    <pre>
    device ActivityNotifier extends SoftwareSensor {
     * 	source dailyActivity as DailyActivity;
     * 	source periodActivity as PeriodActivity;
     * 	action NotifyActivity;
     * }
    </pre>
     */
    protected final static class ActivityNotifierProxy extends Proxy {
        private ActivityNotifierProxy(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Subscribes to the <code>dailyActivity</code> source. After a call to this method, the context will be notified when the
         * <code>ActivityNotifier</code> device of this proxy publishes a value on its <code>dailyActivity</code> source.
         */
        public void subscribeDailyActivity() {
            subscribeValue("dailyActivity");
        }
        
        /**
         * Unsubscribes from the <code>dailyActivity</code> source. After a call to this method, the context will no more be notified
         * when the <code>ActivityNotifier</code> device of this proxy publishes a value on its <code>dailyActivity</code> source.
         */
        public void unsubscribeDailyActivity() {
            unsubscribeValue("dailyActivity");
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover part for ActivityNotifier devices
    
    // Discover object for dailyActivity from ActivityNotifier
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided dailyActivity from ActivityNotifier
     * 	get dailyActivity from ActivityNotifier
     * 	maybe publish;
     * </code>
     */
    protected final class DiscoverForDailyActivityFromActivityNotifier {
        private final ActivityNotifierDiscovererForDailyActivityFromActivityNotifier activityNotifierDiscoverer = new ActivityNotifierDiscovererForDailyActivityFromActivityNotifier(AbstractPropositionDActivity.this);
        
        /**
         * @return a {@link ActivityNotifierDiscovererForDailyActivityFromActivityNotifier} object to discover <code>ActivityNotifier</code> devices
         */
        public ActivityNotifierDiscovererForDailyActivityFromActivityNotifier activityNotifiers() {
            return activityNotifierDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>ActivityNotifier</code> devices to get their sources for the
     * <code>when provided dailyActivity from ActivityNotifier</code> interaction contract.
    <p>
    ------------------------------------------------------------
    ActivityNotifier					||
    ------------------------------------------------------------
    
    <pre>
    device ActivityNotifier extends SoftwareSensor {
     * 	source dailyActivity as DailyActivity;
     * 	source periodActivity as PeriodActivity;
     * 	action NotifyActivity;
     * }
    </pre>
     */
    protected final static class ActivityNotifierDiscovererForDailyActivityFromActivityNotifier {
        private Service serviceParent;
        
        private ActivityNotifierDiscovererForDailyActivityFromActivityNotifier(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private ActivityNotifierCompositeForDailyActivityFromActivityNotifier instantiateComposite() {
            return new ActivityNotifierCompositeForDailyActivityFromActivityNotifier(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierCompositeForDailyActivityFromActivityNotifier} object composed of all discoverable <code>ActivityNotifier</code>
         */
        public ActivityNotifierCompositeForDailyActivityFromActivityNotifier all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierProxyForDailyActivityFromActivityNotifier} object pointing to a random discoverable <code>ActivityNotifier</code> device
         */
        public ActivityNotifierProxyForDailyActivityFromActivityNotifier anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link ActivityNotifierCompositeForDailyActivityFromActivityNotifier} object composed of all matching <code>ActivityNotifier</code> devices
         */
        public ActivityNotifierCompositeForDailyActivityFromActivityNotifier whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>ActivityNotifier</code> devices to get their sources for the
     * <code>when provided dailyActivity from ActivityNotifier</code> interaction contract.
    <p>
    ------------------------------------------------------------
    ActivityNotifier					||
    ------------------------------------------------------------
    
    <pre>
    device ActivityNotifier extends SoftwareSensor {
     * 	source dailyActivity as DailyActivity;
     * 	source periodActivity as PeriodActivity;
     * 	action NotifyActivity;
     * }
    </pre>
     */
    protected final static class ActivityNotifierCompositeForDailyActivityFromActivityNotifier extends fr.inria.diagen.core.service.composite.Composite<ActivityNotifierProxyForDailyActivityFromActivityNotifier> {
        private ActivityNotifierCompositeForDailyActivityFromActivityNotifier(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/SoftwareSensor/ActivityNotifier/");
        }
        
        @Override
        protected ActivityNotifierProxyForDailyActivityFromActivityNotifier instantiateProxy(RemoteServiceInfo rsi) {
            return new ActivityNotifierProxyForDailyActivityFromActivityNotifier(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link ActivityNotifierCompositeForDailyActivityFromActivityNotifier}, filtered using the attribute <code>id</code>.
         */
        public ActivityNotifierCompositeForDailyActivityFromActivityNotifier andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
    }
    
    /**
     * A proxy to one <code>ActivityNotifier</code> device to get its sources for the
     * <code>when provided dailyActivity from ActivityNotifier</code> interaction contract.
    <p>
    ------------------------------------------------------------
    ActivityNotifier					||
    ------------------------------------------------------------
    
    <pre>
    device ActivityNotifier extends SoftwareSensor {
     * 	source dailyActivity as DailyActivity;
     * 	source periodActivity as PeriodActivity;
     * 	action NotifyActivity;
     * }
    </pre>
     */
    protected final static class ActivityNotifierProxyForDailyActivityFromActivityNotifier extends Proxy {
        private ActivityNotifierProxyForDailyActivityFromActivityNotifier(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Returns the value of the <code>dailyActivity</code> source of this <code>ActivityNotifier</code> device
         * 
         * @return the value of the <code>dailyActivity</code> source
         */
        public fr.inria.phoenix.diasuite.framework.datatype.dailyactivity.DailyActivity getDailyActivity() throws InvocationException {
            return (fr.inria.phoenix.diasuite.framework.datatype.dailyactivity.DailyActivity) callGetValue("dailyActivity");
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover object for dailyActivity from ActivityNotifier
}
