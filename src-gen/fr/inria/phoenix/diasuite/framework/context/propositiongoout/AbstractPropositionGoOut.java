package fr.inria.phoenix.diasuite.framework.context.propositiongoout;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.device.clock.TickHourFromClock;

/**
 * Boolean ou String ?
Sortie context : Si nombre de pas insuffisant

<pre>
context PropositionGoOut as CriticalNotification {
 * 	when provided tickHour from Clock
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
        discoverClockForSubscribe.all().subscribeTickHour(); // subscribe to tickHour from all Clock devices
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("tickHour") && source.isCompatible("/Device/Device/Service/Clock/")) {
            TickHourFromClock tickHourFromClock = new TickHourFromClock(this, source, (java.lang.Integer) value);
            
            PropositionGoOutValuePublishable returnValue = onTickHourFromClock(tickHourFromClock, new DiscoverForTickHourFromClock());
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
    private fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification contextValue;
    
    private void setPropositionGoOut(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification newContextValue) {
        contextValue = newContextValue;
        getProcessor().publishValue(getOutProperties(), "propositionGoOut", newContextValue);
    }
    
    /**
     * Get the last value of the context
     * 
     * @return the latest value published by the context
     */
    protected final fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification getLastValue() {
        return contextValue;
    }
    
    /**
     * A class that represents a value that might be published for the <code>PropositionGoOut</code> context. It is used by
     * event methods that might or might not publish values for this context.
     */
    protected final static class PropositionGoOutValuePublishable {
        
        // The value of the context
        private fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification value;
        // Whether the value should be published or not
        private boolean doPublish;
        
        public PropositionGoOutValuePublishable(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification value, boolean doPublish) {
            this.value = value;
            this.doPublish = doPublish;
        }
        
        /**
         * @return the value of the context that might be published
         */
        public fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification getValue() {
            return value;
        }
        
        /**
         * Sets the value that might be published
         * 
         * @param value the value that will be published if {@link #doPublish()} returns true
         */
        public void setValue(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification value) {
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
    
    <pre>
    when provided tickHour from Clock
     * 	get steps from Fitbit,
     * 		events from Agenda
     * 	maybe publish;
    </pre>
     * 
     * @param tickHourFromClock the value of the <code>tickHour</code> source and the <code>Clock</code> device that published the value.
     * @param discover a discover object to get value from devices and contexts
     * @return a {@link PropositionGoOutValuePublishable} that says if the context should publish a value and which value it should publish
     */
    protected abstract PropositionGoOutValuePublishable onTickHourFromClock(TickHourFromClock tickHourFromClock, DiscoverForTickHourFromClock discover);
    
    // End of interaction contract implementation
    
    // Discover part for Clock devices
    /**
     * Use this object to discover Clock devices.
    
    <pre>
    device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
    </pre>
     * 
     * @see ClockDiscoverer
     */
    protected final ClockDiscoverer discoverClockForSubscribe = new ClockDiscoverer(this);
    
    /**
     * Discover object that will exposes the <code>Clock</code> devices that can be discovered
    
    <pre>
    device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
    </pre>
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
    
    <pre>
    device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
    </pre>
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
    
    <pre>
    device Clock extends Service {
     * 	source tickSecond as Integer;
     * 	source tickMinute as Integer;
     * 	source tickHour as Integer;
     * }
    </pre>
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
     * 	get steps from Fitbit,
     * 		events from Agenda
     * 	maybe publish;
     * </code>
     */
    protected final class DiscoverForTickHourFromClock {
        private final FitbitDiscovererForTickHourFromClock fitbitDiscoverer = new FitbitDiscovererForTickHourFromClock(AbstractPropositionGoOut.this);
        private final AgendaDiscovererForTickHourFromClock agendaDiscoverer = new AgendaDiscovererForTickHourFromClock(AbstractPropositionGoOut.this);
        
        /**
         * @return a {@link FitbitDiscovererForTickHourFromClock} object to discover <code>Fitbit</code> devices
         */
        public FitbitDiscovererForTickHourFromClock fitbits() {
            return fitbitDiscoverer;
        }
        
        /**
         * @return a {@link AgendaDiscovererForTickHourFromClock} object to discover <code>Agenda</code> devices
         */
        public AgendaDiscovererForTickHourFromClock agendas() {
            return agendaDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Fitbit</code> devices to get their sources for the
     * <code>when provided tickHour from Clock</code> interaction contract.
    <p>
    ------------------------------------------------------------
    Fitbit							||
    ------------------------------------------------------------
    
    <pre>
    device Fitbit extends Device {
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
    </pre>
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
    <p>
    ------------------------------------------------------------
    Fitbit							||
    ------------------------------------------------------------
    
    <pre>
    device Fitbit extends Device {
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
    </pre>
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
    <p>
    ------------------------------------------------------------
    Fitbit							||
    ------------------------------------------------------------
    
    <pre>
    device Fitbit extends Device {
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
    </pre>
     */
    protected final static class FitbitProxyForTickHourFromClock extends Proxy {
        private FitbitProxyForTickHourFromClock(Service service, RemoteServiceInfo remoteServiceInfo) {
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
     * <code>when provided tickHour from Clock</code> interaction contract.
    
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
    protected final static class AgendaDiscovererForTickHourFromClock {
        private Service serviceParent;
        
        private AgendaDiscovererForTickHourFromClock(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private AgendaCompositeForTickHourFromClock instantiateComposite() {
            return new AgendaCompositeForTickHourFromClock(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Agenda</code> devices
         * 
         * @return a {@link AgendaCompositeForTickHourFromClock} object composed of all discoverable <code>Agenda</code>
         */
        public AgendaCompositeForTickHourFromClock all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Agenda</code> devices
         * 
         * @return a {@link AgendaProxyForTickHourFromClock} object pointing to a random discoverable <code>Agenda</code> device
         */
        public AgendaProxyForTickHourFromClock anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Agenda</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link AgendaCompositeForTickHourFromClock} object composed of all matching <code>Agenda</code> devices
         */
        public AgendaCompositeForTickHourFromClock whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
        
        /**
         * Returns a composite of all accessible <code>Agenda</code> devices whose attribute <code>owner</code> matches a given value.
         * 
         * @param owner The <code>owner<code> attribute value to match.
         * @return a {@link AgendaCompositeForTickHourFromClock} object composed of all matching <code>Agenda</code> devices
         */
        public AgendaCompositeForTickHourFromClock whereOwner(java.lang.String owner) throws CompositeException {
            return instantiateComposite().andOwner(owner);
        }
    }
    
    /**
     * A composite of several <code>Agenda</code> devices to get their sources for the
     * <code>when provided tickHour from Clock</code> interaction contract.
    
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
    protected final static class AgendaCompositeForTickHourFromClock extends fr.inria.diagen.core.service.composite.Composite<AgendaProxyForTickHourFromClock> {
        private AgendaCompositeForTickHourFromClock(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/HomeService/Agenda/");
        }
        
        @Override
        protected AgendaProxyForTickHourFromClock instantiateProxy(RemoteServiceInfo rsi) {
            return new AgendaProxyForTickHourFromClock(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link AgendaCompositeForTickHourFromClock}, filtered using the attribute <code>id</code>.
         */
        public AgendaCompositeForTickHourFromClock andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>owner</code>.
         * 
         * @param owner The <code>owner<code> attribute value to match.
         * @return this {@link AgendaCompositeForTickHourFromClock}, filtered using the attribute <code>owner</code>.
         */
        public AgendaCompositeForTickHourFromClock andOwner(java.lang.String owner) throws CompositeException {
            filterByAttribute("owner", owner);
            return this;
        }
    }
    
    /**
     * A proxy to one <code>Agenda</code> device to get its sources for the
     * <code>when provided tickHour from Clock</code> interaction contract.
    
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
    protected final static class AgendaProxyForTickHourFromClock extends Proxy {
        private AgendaProxyForTickHourFromClock(Service service, RemoteServiceInfo remoteServiceInfo) {
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
    // End of discover object for tickHour from Clock
}
