package fr.inria.phoenix.diasuite.framework.controller.coach_activitycontroller;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.context.alertmove.AlertMoveValue;
import fr.inria.phoenix.diasuite.framework.context.congratulation.CongratulationValue;
import fr.inria.phoenix.diasuite.framework.context.propositiongoout.PropositionGoOutValue;

/**
 * Coach_activity controller

<pre>
controller Coach_activityController {
 * 	when provided PropositionGoOut
 * 	do SendCriticalNotification on Notifier;
 * 	when provided Congratulation
 * 	do SendCriticalNotification on Notifier;
 * 	when provided AlertMove
 * 	do SendCriticalNotification on Notifier;
 * }
</pre>
 */
@SuppressWarnings("all")
public abstract class AbstractCoach_activityController extends Service {
    
    public AbstractCoach_activityController(ServiceConfiguration serviceConfiguration) {
        super("/Controller/Coach_activityController/", serviceConfiguration);
    }
    
    // Methods from the Service class
    @Override
    protected final void internalPostInitialize() {
        subscribeValue("alertMove", "/Context/AlertMove/"); // subscribe to AlertMove context
        subscribeValue("congratulation", "/Context/Congratulation/"); // subscribe to Congratulation context
        subscribeValue("propositionGoOut", "/Context/PropositionGoOut/"); // subscribe to PropositionGoOut context
        postInitialize();
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("alertMove") && source.isCompatible("/Context/AlertMove/")) {
            AlertMoveValue alertMoveValue = new AlertMoveValue((fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification) value);
            
            onAlertMove(alertMoveValue, new DiscoverForAlertMove());
        }
        if (eventName.equals("congratulation") && source.isCompatible("/Context/Congratulation/")) {
            CongratulationValue congratulationValue = new CongratulationValue((fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification) value);
            
            onCongratulation(congratulationValue, new DiscoverForCongratulation());
        }
        if (eventName.equals("propositionGoOut") && source.isCompatible("/Context/PropositionGoOut/")) {
            PropositionGoOutValue propositionGoOutValue = new PropositionGoOutValue((fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification) value);
            
            onPropositionGoOut(propositionGoOutValue, new DiscoverForPropositionGoOut());
        }
    }
    // End of methods from the Service class
    
    // Interaction contract implementation
    /**
     * This method is called when the <code>PropositionGoOut</code> context publishes a value.
     * 
     * <pre>
     * when provided PropositionGoOut
     * 	do SendCriticalNotification on Notifier;
     * </pre>
     * 
     * @param propositionGoOut the value of the <code>PropositionGoOut</code> context.
     * @param discover a discover object to get context values and action methods
     */
    protected abstract void onPropositionGoOut(PropositionGoOutValue propositionGoOut, DiscoverForPropositionGoOut discover);
    
    /**
     * This method is called when the <code>Congratulation</code> context publishes a value.
     * 
     * <pre>
     * when provided Congratulation
     * 	do SendCriticalNotification on Notifier;
     * </pre>
     * 
     * @param congratulation the value of the <code>Congratulation</code> context.
     * @param discover a discover object to get context values and action methods
     */
    protected abstract void onCongratulation(CongratulationValue congratulation, DiscoverForCongratulation discover);
    
    /**
     * This method is called when the <code>AlertMove</code> context publishes a value.
     * 
     * <pre>
     * when provided AlertMove
     * 	do SendCriticalNotification on Notifier;
     * </pre>
     * 
     * @param alertMove the value of the <code>AlertMove</code> context.
     * @param discover a discover object to get context values and action methods
     */
    protected abstract void onAlertMove(AlertMoveValue alertMove, DiscoverForAlertMove discover);
    
    // End of interaction contract implementation
    
    // Discover object for PropositionGoOut
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided PropositionGoOut
     * 	do SendCriticalNotification on Notifier;
     * </code>
     */
    protected final class DiscoverForPropositionGoOut {
        private final NotifierDiscovererForPropositionGoOut notifierDiscoverer = new NotifierDiscovererForPropositionGoOut(AbstractCoach_activityController.this);
        
        /**
         * @return a {@link NotifierDiscovererForPropositionGoOut} object to discover <code>Notifier</code> devices
         */
        public NotifierDiscovererForPropositionGoOut notifiers() {
            return notifierDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Notifier</code> devices to execute action on for the
     * <code>when provided PropositionGoOut</code> interaction contract.
    
    <pre>
    device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
    </pre>
     */
    protected final static class NotifierDiscovererForPropositionGoOut {
        private Service serviceParent;
        
        private NotifierDiscovererForPropositionGoOut(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private NotifierCompositeForPropositionGoOut instantiateComposite() {
            return new NotifierCompositeForPropositionGoOut(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Notifier</code> devices
         * 
         * @return a {@link NotifierCompositeForPropositionGoOut} object composed of all discoverable <code>Notifier</code>
         */
        public NotifierCompositeForPropositionGoOut all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Notifier</code> devices
         * 
         * @return a {@link NotifierProxyForPropositionGoOut} object pointing to a random discoverable <code>Notifier</code> device
         */
        public NotifierProxyForPropositionGoOut anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Notifier</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link NotifierCompositeForPropositionGoOut} object composed of all matching <code>Notifier</code> devices
         */
        public NotifierCompositeForPropositionGoOut whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Notifier</code> devices to execute action on for the
     * <code>when provided PropositionGoOut</code> interaction contract.
    
    <pre>
    device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
    </pre>
     */
    protected final static class NotifierCompositeForPropositionGoOut extends fr.inria.diagen.core.service.composite.Composite<NotifierProxyForPropositionGoOut> {
        private NotifierCompositeForPropositionGoOut(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/HomeService/Notifier/");
        }
        
        @Override
        protected NotifierProxyForPropositionGoOut instantiateProxy(RemoteServiceInfo rsi) {
            return new NotifierProxyForPropositionGoOut(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link NotifierCompositeForPropositionGoOut}, filtered using the attribute <code>id</code>.
         */
        public NotifierCompositeForPropositionGoOut andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Executes the <code>sendCriticalNotification(notification as CriticalNotification)</code> action's method on all devices of this composite.
         * 
         * @param notification the <code>notification</code> parameter of the <code>sendCriticalNotification(notification as CriticalNotification)</code> method.
         */
        public void sendCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForPropositionGoOut proxy : proxies) {
                proxy.sendCriticalNotification(notification);
            }
        }
        
        /**
         * Executes the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> action's method on all devices of this composite.
         * 
         * @param notification the <code>notification</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         * @param displayDate the <code>displayDate</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         */
        public void registerCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification,
                fr.inria.phoenix.diasuite.framework.datatype.date.Date displayDate) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForPropositionGoOut proxy : proxies) {
                proxy.registerCriticalNotification(notification, displayDate);
            }
        }
        
        /**
         * Executes the <code>cancelCriticalNotification(id as String)</code> action's method on all devices of this composite.
         * 
         * @param id the <code>id</code> parameter of the <code>cancelCriticalNotification(id as String)</code> method.
         */
        public void cancelCriticalNotification(java.lang.String id) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForPropositionGoOut proxy : proxies) {
                proxy.cancelCriticalNotification(id);
            }
        }
    }
    
    /**
     * A proxy to one <code>Notifier</code> device to execute action on for the
     * <code>when provided PropositionGoOut</code> interaction contract.
    
    <pre>
    device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
    </pre>
     */
    protected final static class NotifierProxyForPropositionGoOut extends Proxy {
        private NotifierProxyForPropositionGoOut(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Executes the <code>sendCriticalNotification(notification as CriticalNotification)</code> action's method on this device.
         * 
         * @param notification the <code>notification</code> parameter of the <code>sendCriticalNotification(notification as CriticalNotification)</code> method.
         */
        public void sendCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification) throws InvocationException {
            callOrder("sendCriticalNotification", notification);
        }
        
        /**
         * Executes the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> action's method on this device.
         * 
         * @param notification the <code>notification</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         * @param displayDate the <code>displayDate</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         */
        public void registerCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification,
                fr.inria.phoenix.diasuite.framework.datatype.date.Date displayDate) throws InvocationException {
            callOrder("registerCriticalNotification", notification, displayDate);
        }
        
        /**
         * Executes the <code>cancelCriticalNotification(id as String)</code> action's method on this device.
         * 
         * @param id the <code>id</code> parameter of the <code>cancelCriticalNotification(id as String)</code> method.
         */
        public void cancelCriticalNotification(java.lang.String id) throws InvocationException {
            callOrder("cancelCriticalNotification", id);
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover object for PropositionGoOut
    
    // Discover object for Congratulation
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided Congratulation
     * 	do SendCriticalNotification on Notifier;
     * </code>
     */
    protected final class DiscoverForCongratulation {
        private final NotifierDiscovererForCongratulation notifierDiscoverer = new NotifierDiscovererForCongratulation(AbstractCoach_activityController.this);
        
        /**
         * @return a {@link NotifierDiscovererForCongratulation} object to discover <code>Notifier</code> devices
         */
        public NotifierDiscovererForCongratulation notifiers() {
            return notifierDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Notifier</code> devices to execute action on for the
     * <code>when provided Congratulation</code> interaction contract.
    
    <pre>
    device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
    </pre>
     */
    protected final static class NotifierDiscovererForCongratulation {
        private Service serviceParent;
        
        private NotifierDiscovererForCongratulation(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private NotifierCompositeForCongratulation instantiateComposite() {
            return new NotifierCompositeForCongratulation(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Notifier</code> devices
         * 
         * @return a {@link NotifierCompositeForCongratulation} object composed of all discoverable <code>Notifier</code>
         */
        public NotifierCompositeForCongratulation all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Notifier</code> devices
         * 
         * @return a {@link NotifierProxyForCongratulation} object pointing to a random discoverable <code>Notifier</code> device
         */
        public NotifierProxyForCongratulation anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Notifier</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link NotifierCompositeForCongratulation} object composed of all matching <code>Notifier</code> devices
         */
        public NotifierCompositeForCongratulation whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Notifier</code> devices to execute action on for the
     * <code>when provided Congratulation</code> interaction contract.
    
    <pre>
    device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
    </pre>
     */
    protected final static class NotifierCompositeForCongratulation extends fr.inria.diagen.core.service.composite.Composite<NotifierProxyForCongratulation> {
        private NotifierCompositeForCongratulation(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/HomeService/Notifier/");
        }
        
        @Override
        protected NotifierProxyForCongratulation instantiateProxy(RemoteServiceInfo rsi) {
            return new NotifierProxyForCongratulation(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link NotifierCompositeForCongratulation}, filtered using the attribute <code>id</code>.
         */
        public NotifierCompositeForCongratulation andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Executes the <code>sendCriticalNotification(notification as CriticalNotification)</code> action's method on all devices of this composite.
         * 
         * @param notification the <code>notification</code> parameter of the <code>sendCriticalNotification(notification as CriticalNotification)</code> method.
         */
        public void sendCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForCongratulation proxy : proxies) {
                proxy.sendCriticalNotification(notification);
            }
        }
        
        /**
         * Executes the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> action's method on all devices of this composite.
         * 
         * @param notification the <code>notification</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         * @param displayDate the <code>displayDate</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         */
        public void registerCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification,
                fr.inria.phoenix.diasuite.framework.datatype.date.Date displayDate) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForCongratulation proxy : proxies) {
                proxy.registerCriticalNotification(notification, displayDate);
            }
        }
        
        /**
         * Executes the <code>cancelCriticalNotification(id as String)</code> action's method on all devices of this composite.
         * 
         * @param id the <code>id</code> parameter of the <code>cancelCriticalNotification(id as String)</code> method.
         */
        public void cancelCriticalNotification(java.lang.String id) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForCongratulation proxy : proxies) {
                proxy.cancelCriticalNotification(id);
            }
        }
    }
    
    /**
     * A proxy to one <code>Notifier</code> device to execute action on for the
     * <code>when provided Congratulation</code> interaction contract.
    
    <pre>
    device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
    </pre>
     */
    protected final static class NotifierProxyForCongratulation extends Proxy {
        private NotifierProxyForCongratulation(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Executes the <code>sendCriticalNotification(notification as CriticalNotification)</code> action's method on this device.
         * 
         * @param notification the <code>notification</code> parameter of the <code>sendCriticalNotification(notification as CriticalNotification)</code> method.
         */
        public void sendCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification) throws InvocationException {
            callOrder("sendCriticalNotification", notification);
        }
        
        /**
         * Executes the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> action's method on this device.
         * 
         * @param notification the <code>notification</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         * @param displayDate the <code>displayDate</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         */
        public void registerCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification,
                fr.inria.phoenix.diasuite.framework.datatype.date.Date displayDate) throws InvocationException {
            callOrder("registerCriticalNotification", notification, displayDate);
        }
        
        /**
         * Executes the <code>cancelCriticalNotification(id as String)</code> action's method on this device.
         * 
         * @param id the <code>id</code> parameter of the <code>cancelCriticalNotification(id as String)</code> method.
         */
        public void cancelCriticalNotification(java.lang.String id) throws InvocationException {
            callOrder("cancelCriticalNotification", id);
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover object for Congratulation
    
    // Discover object for AlertMove
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided AlertMove
     * 	do SendCriticalNotification on Notifier;
     * </code>
     */
    protected final class DiscoverForAlertMove {
        private final NotifierDiscovererForAlertMove notifierDiscoverer = new NotifierDiscovererForAlertMove(AbstractCoach_activityController.this);
        
        /**
         * @return a {@link NotifierDiscovererForAlertMove} object to discover <code>Notifier</code> devices
         */
        public NotifierDiscovererForAlertMove notifiers() {
            return notifierDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Notifier</code> devices to execute action on for the
     * <code>when provided AlertMove</code> interaction contract.
    
    <pre>
    device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
    </pre>
     */
    protected final static class NotifierDiscovererForAlertMove {
        private Service serviceParent;
        
        private NotifierDiscovererForAlertMove(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private NotifierCompositeForAlertMove instantiateComposite() {
            return new NotifierCompositeForAlertMove(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Notifier</code> devices
         * 
         * @return a {@link NotifierCompositeForAlertMove} object composed of all discoverable <code>Notifier</code>
         */
        public NotifierCompositeForAlertMove all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Notifier</code> devices
         * 
         * @return a {@link NotifierProxyForAlertMove} object pointing to a random discoverable <code>Notifier</code> device
         */
        public NotifierProxyForAlertMove anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Notifier</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link NotifierCompositeForAlertMove} object composed of all matching <code>Notifier</code> devices
         */
        public NotifierCompositeForAlertMove whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Notifier</code> devices to execute action on for the
     * <code>when provided AlertMove</code> interaction contract.
    
    <pre>
    device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
    </pre>
     */
    protected final static class NotifierCompositeForAlertMove extends fr.inria.diagen.core.service.composite.Composite<NotifierProxyForAlertMove> {
        private NotifierCompositeForAlertMove(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/HomeService/Notifier/");
        }
        
        @Override
        protected NotifierProxyForAlertMove instantiateProxy(RemoteServiceInfo rsi) {
            return new NotifierProxyForAlertMove(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link NotifierCompositeForAlertMove}, filtered using the attribute <code>id</code>.
         */
        public NotifierCompositeForAlertMove andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Executes the <code>sendCriticalNotification(notification as CriticalNotification)</code> action's method on all devices of this composite.
         * 
         * @param notification the <code>notification</code> parameter of the <code>sendCriticalNotification(notification as CriticalNotification)</code> method.
         */
        public void sendCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForAlertMove proxy : proxies) {
                proxy.sendCriticalNotification(notification);
            }
        }
        
        /**
         * Executes the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> action's method on all devices of this composite.
         * 
         * @param notification the <code>notification</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         * @param displayDate the <code>displayDate</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         */
        public void registerCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification,
                fr.inria.phoenix.diasuite.framework.datatype.date.Date displayDate) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForAlertMove proxy : proxies) {
                proxy.registerCriticalNotification(notification, displayDate);
            }
        }
        
        /**
         * Executes the <code>cancelCriticalNotification(id as String)</code> action's method on all devices of this composite.
         * 
         * @param id the <code>id</code> parameter of the <code>cancelCriticalNotification(id as String)</code> method.
         */
        public void cancelCriticalNotification(java.lang.String id) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForAlertMove proxy : proxies) {
                proxy.cancelCriticalNotification(id);
            }
        }
    }
    
    /**
     * A proxy to one <code>Notifier</code> device to execute action on for the
     * <code>when provided AlertMove</code> interaction contract.
    
    <pre>
    device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
    </pre>
     */
    protected final static class NotifierProxyForAlertMove extends Proxy {
        private NotifierProxyForAlertMove(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Executes the <code>sendCriticalNotification(notification as CriticalNotification)</code> action's method on this device.
         * 
         * @param notification the <code>notification</code> parameter of the <code>sendCriticalNotification(notification as CriticalNotification)</code> method.
         */
        public void sendCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification) throws InvocationException {
            callOrder("sendCriticalNotification", notification);
        }
        
        /**
         * Executes the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> action's method on this device.
         * 
         * @param notification the <code>notification</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         * @param displayDate the <code>displayDate</code> parameter of the <code>registerCriticalNotification(notification as CriticalNotification, displayDate as Date)</code> method.
         */
        public void registerCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification notification,
                fr.inria.phoenix.diasuite.framework.datatype.date.Date displayDate) throws InvocationException {
            callOrder("registerCriticalNotification", notification, displayDate);
        }
        
        /**
         * Executes the <code>cancelCriticalNotification(id as String)</code> action's method on this device.
         * 
         * @param id the <code>id</code> parameter of the <code>cancelCriticalNotification(id as String)</code> method.
         */
        public void cancelCriticalNotification(java.lang.String id) throws InvocationException {
            callOrder("cancelCriticalNotification", id);
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover object for AlertMove
}
