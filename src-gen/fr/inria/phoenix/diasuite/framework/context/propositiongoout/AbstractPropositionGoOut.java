package fr.inria.phoenix.diasuite.framework.context.propositiongoout;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.device.fitbit.StepsFromFitbit;

/**
 * Boolean ou String ?
Sortie context : Si nombre de pas insuffisant

<pre>
context PropositionGoOut as Boolean {
 * 	when provided steps from Fitbit
 * 	get steps from Fitbit,
 * 		events from Agenda
 * 	maybe publish;
 * 	}
</pre>
 */
@SuppressWarnings("all")
public abstract class AbstractPropositionGoOut extends Service {
    
    public AbstractPropositionGoOut(ServiceConfiguration serviceConfiguration) {
        super("/Context/PropositionGoOut/", serviceConfiguration);
    }
    
    // Methods from the Service class
    @Override
    protected final void internalPostInitialize() {
        postInitialize();
    }
    
    @Override
    protected void postInitialize() {
        // Default implementation of post initialize: subscribe to all required devices
        discoverFitbitForSubscribe.all().subscribeSteps(); // subscribe to steps from all Fitbit devices
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("steps") && source.isCompatible("/Device/Device/Fitbit/")) {
            StepsFromFitbit stepsFromFitbit = new StepsFromFitbit(this, source, (java.lang.Integer) value,
                    (fr.inria.phoenix.diasuite.framework.datatype.period.Period) indexes[0]);
            
            PropositionGoOutValuePublishable returnValue = onStepsFromFitbit(stepsFromFitbit, new DiscoverForStepsFromFitbit());
            if(returnValue != null && returnValue.doPublish()) {
                setPropositionGoOut(returnValue.getValue());
            }
        }
    }
    
    @Override
    public final Object getValueCalled(java.util.Map<String, Object> properties, RemoteServiceInfo source, String valueName,
            Object... indexes) throws Exception {
        if (valueName.equals("propositionGoOut")) {
            return getLastValue();
        }
        throw new InvocationException("Unsupported method call: " + valueName);
    }
    // End of methods from the Service class
    
    // Code relative to the return value of the context
    private java.lang.Boolean contextValue;
    
    private void setPropositionGoOut(java.lang.Boolean newContextValue) {
        contextValue = newContextValue;
        getProcessor().publishValue(getOutProperties(), "propositionGoOut", newContextValue);
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
     * A class that represents a value that might be published for the <code>PropositionGoOut</code> context. It is used by
     * event methods that might or might not publish values for this context.
     */
    protected final static class PropositionGoOutValuePublishable {
        
        // The value of the context
        private java.lang.Boolean value;
        // Whether the value should be published or not
        private boolean doPublish;
        
        public PropositionGoOutValuePublishable(java.lang.Boolean value, boolean doPublish) {
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
     * This method is called when a <code>Fitbit</code> device on which we have subscribed publish on its <code>steps</code> source.
    
    <pre>
    when provided steps from Fitbit
     * 	get steps from Fitbit,
     * 		events from Agenda
     * 	maybe publish;
    </pre>
     * 
     * @param stepsFromFitbit the value of the <code>steps</code> source and the <code>Fitbit</code> device that published the value.
     * @param discover a discover object to get value from devices and contexts
     * @return a {@link PropositionGoOutValuePublishable} that says if the context should publish a value and which value it should publish
     */
    protected abstract PropositionGoOutValuePublishable onStepsFromFitbit(StepsFromFitbit stepsFromFitbit, DiscoverForStepsFromFitbit discover);
    
    // End of interaction contract implementation
    
    // Discover part for Fitbit devices
    /**
     * Use this object to discover Fitbit devices.
    <p>
    ------------------------------------------------------------
    Fitbit							||
    ------------------------------------------------------------
    
    <pre>
    device Fitbit extends Device {
     *         source calories as Integer indexed by period as Period;
     *         source distanceInMeters as Integer indexed by period as Period;
     *         source pulses as Pulse indexed by period as Period;
     *         source steps as Integer indexed by period as Period;
     *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
     *         source alarm as Alarm indexed by name as String;
     *         action ScheduleAlarm;
     *         action Vibrate;
     * }
    </pre>
     * 
     * @see FitbitDiscoverer
     */
    protected final FitbitDiscoverer discoverFitbitForSubscribe = new FitbitDiscoverer(this);
    
    /**
     * Discover object that will exposes the <code>Fitbit</code> devices that can be discovered
    <p>
    ------------------------------------------------------------
    Fitbit							||
    ------------------------------------------------------------
    
    <pre>
    device Fitbit extends Device {
     *         source calories as Integer indexed by period as Period;
     *         source distanceInMeters as Integer indexed by period as Period;
     *         source pulses as Pulse indexed by period as Period;
     *         source steps as Integer indexed by period as Period;
     *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
     *         source alarm as Alarm indexed by name as String;
     *         action ScheduleAlarm;
     *         action Vibrate;
     * }
    </pre>
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
    <p>
    ------------------------------------------------------------
    Fitbit							||
    ------------------------------------------------------------
    
    <pre>
    device Fitbit extends Device {
     *         source calories as Integer indexed by period as Period;
     *         source distanceInMeters as Integer indexed by period as Period;
     *         source pulses as Pulse indexed by period as Period;
     *         source steps as Integer indexed by period as Period;
     *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
     *         source alarm as Alarm indexed by name as String;
     *         action ScheduleAlarm;
     *         action Vibrate;
     * }
    </pre>
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
         * Subscribes to the <code>steps</code> source. After a call to this method, the context will be notified when a
         * <code>Fitbit</code> device of this composite publishes a value on its <code>steps</code> source.
         */
        public void subscribeSteps() {
            subscribeValue("steps");
        }
        
        /**
         * Unsubscribes from the <code>steps</code> source. After a call to this method, the context will no more be notified
         * when a <code>Fitbit</code> device of this composite publishes a value on its <code>steps</code> source.
         */
        public void unsubscribeSteps() {
            unsubscribeValue("steps");
        }
    }
    
    /**
     * A proxy to one <code>Fitbit</code> device
    <p>
    ------------------------------------------------------------
    Fitbit							||
    ------------------------------------------------------------
    
    <pre>
    device Fitbit extends Device {
     *         source calories as Integer indexed by period as Period;
     *         source distanceInMeters as Integer indexed by period as Period;
     *         source pulses as Pulse indexed by period as Period;
     *         source steps as Integer indexed by period as Period;
     *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
     *         source alarm as Alarm indexed by name as String;
     *         action ScheduleAlarm;
     *         action Vibrate;
     * }
    </pre>
     */
    protected final static class FitbitProxy extends Proxy {
        private FitbitProxy(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Subscribes to the <code>steps</code> source. After a call to this method, the context will be notified when the
         * <code>Fitbit</code> device of this proxy publishes a value on its <code>steps</code> source.
         */
        public void subscribeSteps() {
            subscribeValue("steps");
        }
        
        /**
         * Unsubscribes from the <code>steps</code> source. After a call to this method, the context will no more be notified
         * when the <code>Fitbit</code> device of this proxy publishes a value on its <code>steps</code> source.
         */
        public void unsubscribeSteps() {
            unsubscribeValue("steps");
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover part for Fitbit devices
    
    // Discover object for steps from Fitbit
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided steps from Fitbit
     * 	get steps from Fitbit,
     * 		events from Agenda
     * 	maybe publish;
     * </code>
     */
    protected final class DiscoverForStepsFromFitbit {
        private final FitbitDiscovererForStepsFromFitbit fitbitDiscoverer = new FitbitDiscovererForStepsFromFitbit(AbstractPropositionGoOut.this);
        private final AgendaDiscovererForStepsFromFitbit agendaDiscoverer = new AgendaDiscovererForStepsFromFitbit(AbstractPropositionGoOut.this);
        
        /**
         * @return a {@link FitbitDiscovererForStepsFromFitbit} object to discover <code>Fitbit</code> devices
         */
        public FitbitDiscovererForStepsFromFitbit fitbits() {
            return fitbitDiscoverer;
        }
        
        /**
         * @return a {@link AgendaDiscovererForStepsFromFitbit} object to discover <code>Agenda</code> devices
         */
        public AgendaDiscovererForStepsFromFitbit agendas() {
            return agendaDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Fitbit</code> devices to get their sources for the
     * <code>when provided steps from Fitbit</code> interaction contract.
    <p>
    ------------------------------------------------------------
    Fitbit							||
    ------------------------------------------------------------
    
    <pre>
    device Fitbit extends Device {
     *         source calories as Integer indexed by period as Period;
     *         source distanceInMeters as Integer indexed by period as Period;
     *         source pulses as Pulse indexed by period as Period;
     *         source steps as Integer indexed by period as Period;
     *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
     *         source alarm as Alarm indexed by name as String;
     *         action ScheduleAlarm;
     *         action Vibrate;
     * }
    </pre>
     */
    protected final static class FitbitDiscovererForStepsFromFitbit {
        private Service serviceParent;
        
        private FitbitDiscovererForStepsFromFitbit(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private FitbitCompositeForStepsFromFitbit instantiateComposite() {
            return new FitbitCompositeForStepsFromFitbit(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Fitbit</code> devices
         * 
         * @return a {@link FitbitCompositeForStepsFromFitbit} object composed of all discoverable <code>Fitbit</code>
         */
        public FitbitCompositeForStepsFromFitbit all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Fitbit</code> devices
         * 
         * @return a {@link FitbitProxyForStepsFromFitbit} object pointing to a random discoverable <code>Fitbit</code> device
         */
        public FitbitProxyForStepsFromFitbit anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Fitbit</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link FitbitCompositeForStepsFromFitbit} object composed of all matching <code>Fitbit</code> devices
         */
        public FitbitCompositeForStepsFromFitbit whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Fitbit</code> devices to get their sources for the
     * <code>when provided steps from Fitbit</code> interaction contract.
    <p>
    ------------------------------------------------------------
    Fitbit							||
    ------------------------------------------------------------
    
    <pre>
    device Fitbit extends Device {
     *         source calories as Integer indexed by period as Period;
     *         source distanceInMeters as Integer indexed by period as Period;
     *         source pulses as Pulse indexed by period as Period;
     *         source steps as Integer indexed by period as Period;
     *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
     *         source alarm as Alarm indexed by name as String;
     *         action ScheduleAlarm;
     *         action Vibrate;
     * }
    </pre>
     */
    protected final static class FitbitCompositeForStepsFromFitbit extends fr.inria.diagen.core.service.composite.Composite<FitbitProxyForStepsFromFitbit> {
        private FitbitCompositeForStepsFromFitbit(Service serviceParent) {
            super(serviceParent, "/Device/Device/Fitbit/");
        }
        
        @Override
        protected FitbitProxyForStepsFromFitbit instantiateProxy(RemoteServiceInfo rsi) {
            return new FitbitProxyForStepsFromFitbit(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link FitbitCompositeForStepsFromFitbit}, filtered using the attribute <code>id</code>.
         */
        public FitbitCompositeForStepsFromFitbit andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
    }
    
    /**
     * A proxy to one <code>Fitbit</code> device to get its sources for the
     * <code>when provided steps from Fitbit</code> interaction contract.
    <p>
    ------------------------------------------------------------
    Fitbit							||
    ------------------------------------------------------------
    
    <pre>
    device Fitbit extends Device {
     *         source calories as Integer indexed by period as Period;
     *         source distanceInMeters as Integer indexed by period as Period;
     *         source pulses as Pulse indexed by period as Period;
     *         source steps as Integer indexed by period as Period;
     *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
     *         source alarm as Alarm indexed by name as String;
     *         action ScheduleAlarm;
     *         action Vibrate;
     * }
    </pre>
     */
    protected final static class FitbitProxyForStepsFromFitbit extends Proxy {
        private FitbitProxyForStepsFromFitbit(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Returns the value of the <code>steps</code> source of this <code>Fitbit</code> device
         * 
         * @param period the value of the <code>period</code> index.
         * @return the value of the <code>steps</code> source
         */
        public java.lang.Integer getSteps(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws InvocationException {
            return (java.lang.Integer) callGetValue("steps", period);
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    
    /**
     * Discover object that will exposes the <code>Agenda</code> devices to get their sources for the
     * <code>when provided steps from Fitbit</code> interaction contract.
    
    <pre>
    device Agenda extends HomeService {
     * 	attribute owner as String;
     * 	source currentEvents as AgendaEvent [];
     * 	source nextEvent as AgendaEvent;
     * 	source nextEvents as AgendaEvent [];
     * 	source events as AgendaEvent [] indexed by filter as AgendaEvent;
     * 	action AgendaEventAction;
     * }
    </pre>
     */
    protected final static class AgendaDiscovererForStepsFromFitbit {
        private Service serviceParent;
        
        private AgendaDiscovererForStepsFromFitbit(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private AgendaCompositeForStepsFromFitbit instantiateComposite() {
            return new AgendaCompositeForStepsFromFitbit(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Agenda</code> devices
         * 
         * @return a {@link AgendaCompositeForStepsFromFitbit} object composed of all discoverable <code>Agenda</code>
         */
        public AgendaCompositeForStepsFromFitbit all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Agenda</code> devices
         * 
         * @return a {@link AgendaProxyForStepsFromFitbit} object pointing to a random discoverable <code>Agenda</code> device
         */
        public AgendaProxyForStepsFromFitbit anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Agenda</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link AgendaCompositeForStepsFromFitbit} object composed of all matching <code>Agenda</code> devices
         */
        public AgendaCompositeForStepsFromFitbit whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
        
        /**
         * Returns a composite of all accessible <code>Agenda</code> devices whose attribute <code>owner</code> matches a given value.
         * 
         * @param owner The <code>owner<code> attribute value to match.
         * @return a {@link AgendaCompositeForStepsFromFitbit} object composed of all matching <code>Agenda</code> devices
         */
        public AgendaCompositeForStepsFromFitbit whereOwner(java.lang.String owner) throws CompositeException {
            return instantiateComposite().andOwner(owner);
        }
    }
    
    /**
     * A composite of several <code>Agenda</code> devices to get their sources for the
     * <code>when provided steps from Fitbit</code> interaction contract.
    
    <pre>
    device Agenda extends HomeService {
     * 	attribute owner as String;
     * 	source currentEvents as AgendaEvent [];
     * 	source nextEvent as AgendaEvent;
     * 	source nextEvents as AgendaEvent [];
     * 	source events as AgendaEvent [] indexed by filter as AgendaEvent;
     * 	action AgendaEventAction;
     * }
    </pre>
     */
    protected final static class AgendaCompositeForStepsFromFitbit extends fr.inria.diagen.core.service.composite.Composite<AgendaProxyForStepsFromFitbit> {
        private AgendaCompositeForStepsFromFitbit(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/HomeService/Agenda/");
        }
        
        @Override
        protected AgendaProxyForStepsFromFitbit instantiateProxy(RemoteServiceInfo rsi) {
            return new AgendaProxyForStepsFromFitbit(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link AgendaCompositeForStepsFromFitbit}, filtered using the attribute <code>id</code>.
         */
        public AgendaCompositeForStepsFromFitbit andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>owner</code>.
         * 
         * @param owner The <code>owner<code> attribute value to match.
         * @return this {@link AgendaCompositeForStepsFromFitbit}, filtered using the attribute <code>owner</code>.
         */
        public AgendaCompositeForStepsFromFitbit andOwner(java.lang.String owner) throws CompositeException {
            filterByAttribute("owner", owner);
            return this;
        }
    }
    
    /**
     * A proxy to one <code>Agenda</code> device to get its sources for the
     * <code>when provided steps from Fitbit</code> interaction contract.
    
    <pre>
    device Agenda extends HomeService {
     * 	attribute owner as String;
     * 	source currentEvents as AgendaEvent [];
     * 	source nextEvent as AgendaEvent;
     * 	source nextEvents as AgendaEvent [];
     * 	source events as AgendaEvent [] indexed by filter as AgendaEvent;
     * 	action AgendaEventAction;
     * }
    </pre>
     */
    protected final static class AgendaProxyForStepsFromFitbit extends Proxy {
        private AgendaProxyForStepsFromFitbit(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Returns the value of the <code>events</code> source of this <code>Agenda</code> device
         * 
         * @param filter the value of the <code>filter</code> index.
         * @return the value of the <code>events</code> source
         */
        public java.util.List<fr.inria.phoenix.diasuite.framework.datatype.agendaevent.AgendaEvent> getEvents(fr.inria.phoenix.diasuite.framework.datatype.agendaevent.AgendaEvent filter) throws InvocationException {
            return (java.util.List<fr.inria.phoenix.diasuite.framework.datatype.agendaevent.AgendaEvent>) callGetValue("events", filter);
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
        
        /**
         * @return the value of the <code>owner</code> attribute
         */
        public java.lang.String owner() {
            return (java.lang.String) callGetValue("owner");
        }
    }
    // End of discover object for steps from Fitbit
}
