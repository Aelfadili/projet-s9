package fr.inria.phoenix.diasuite.framework.controller.coach_activitycontroller;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.context.alertmove.AlertMoveValue;
import fr.inria.phoenix.diasuite.framework.context.congratulation.CongratulationValue;
import fr.inria.phoenix.diasuite.framework.context.propositiondactivity.PropositionDActivityValue;
import fr.inria.phoenix.diasuite.framework.context.propositiongoout.PropositionGoOutValue;

/**
 * Coach_activity controller

<pre>
controller Coach_activityController {
 * 	when provided PropositionGoOut
 * 	do NotifyActivity on ActivityNotifier;
 * 	when provided Congratulation
 * 	do NotifyActivity on ActivityNotifier;
 * 	when provided PropositionDActivity
 * 	do NotifyActivity on ActivityNotifier;
 * 	when provided AlertMove
 * 	do NotifyActivity on ActivityNotifier;
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
        subscribeValue("propositionDActivity", "/Context/PropositionDActivity/"); // subscribe to PropositionDActivity context
        subscribeValue("propositionGoOut", "/Context/PropositionGoOut/"); // subscribe to PropositionGoOut context
        postInitialize();
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("alertMove") && source.isCompatible("/Context/AlertMove/")) {
            AlertMoveValue alertMoveValue = new AlertMoveValue((java.lang.Boolean) value);
            
            onAlertMove(alertMoveValue, new DiscoverForAlertMove());
        }
        if (eventName.equals("congratulation") && source.isCompatible("/Context/Congratulation/")) {
            CongratulationValue congratulationValue = new CongratulationValue((java.lang.Boolean) value);
            
            onCongratulation(congratulationValue, new DiscoverForCongratulation());
        }
        if (eventName.equals("propositionDActivity") && source.isCompatible("/Context/PropositionDActivity/")) {
            PropositionDActivityValue propositionDActivityValue = new PropositionDActivityValue((java.lang.Boolean) value);
            
            onPropositionDActivity(propositionDActivityValue, new DiscoverForPropositionDActivity());
        }
        if (eventName.equals("propositionGoOut") && source.isCompatible("/Context/PropositionGoOut/")) {
            PropositionGoOutValue propositionGoOutValue = new PropositionGoOutValue((java.lang.Boolean) value);
            
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
     * 	do NotifyActivity on ActivityNotifier;
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
     * 	do NotifyActivity on ActivityNotifier;
     * </pre>
     * 
     * @param congratulation the value of the <code>Congratulation</code> context.
     * @param discover a discover object to get context values and action methods
     */
    protected abstract void onCongratulation(CongratulationValue congratulation, DiscoverForCongratulation discover);
    
    /**
     * This method is called when the <code>PropositionDActivity</code> context publishes a value.
     * 
     * <pre>
     * when provided PropositionDActivity
     * 	do NotifyActivity on ActivityNotifier;
     * </pre>
     * 
     * @param propositionDActivity the value of the <code>PropositionDActivity</code> context.
     * @param discover a discover object to get context values and action methods
     */
    protected abstract void onPropositionDActivity(PropositionDActivityValue propositionDActivity, DiscoverForPropositionDActivity discover);
    
    /**
     * This method is called when the <code>AlertMove</code> context publishes a value.
     * 
     * <pre>
     * when provided AlertMove
     * 	do NotifyActivity on ActivityNotifier;
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
     * 	do NotifyActivity on ActivityNotifier;
     * </code>
     */
    protected final class DiscoverForPropositionGoOut {
        private final ActivityNotifierDiscovererForPropositionGoOut activityNotifierDiscoverer = new ActivityNotifierDiscovererForPropositionGoOut(AbstractCoach_activityController.this);
        
        /**
         * @return a {@link ActivityNotifierDiscovererForPropositionGoOut} object to discover <code>ActivityNotifier</code> devices
         */
        public ActivityNotifierDiscovererForPropositionGoOut activityNotifiers() {
            return activityNotifierDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>ActivityNotifier</code> devices to execute action on for the
     * <code>when provided PropositionGoOut</code> interaction contract.
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
    protected final static class ActivityNotifierDiscovererForPropositionGoOut {
        private Service serviceParent;
        
        private ActivityNotifierDiscovererForPropositionGoOut(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private ActivityNotifierCompositeForPropositionGoOut instantiateComposite() {
            return new ActivityNotifierCompositeForPropositionGoOut(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierCompositeForPropositionGoOut} object composed of all discoverable <code>ActivityNotifier</code>
         */
        public ActivityNotifierCompositeForPropositionGoOut all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierProxyForPropositionGoOut} object pointing to a random discoverable <code>ActivityNotifier</code> device
         */
        public ActivityNotifierProxyForPropositionGoOut anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link ActivityNotifierCompositeForPropositionGoOut} object composed of all matching <code>ActivityNotifier</code> devices
         */
        public ActivityNotifierCompositeForPropositionGoOut whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>ActivityNotifier</code> devices to execute action on for the
     * <code>when provided PropositionGoOut</code> interaction contract.
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
    protected final static class ActivityNotifierCompositeForPropositionGoOut extends fr.inria.diagen.core.service.composite.Composite<ActivityNotifierProxyForPropositionGoOut> {
        private ActivityNotifierCompositeForPropositionGoOut(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/SoftwareSensor/ActivityNotifier/");
        }
        
        @Override
        protected ActivityNotifierProxyForPropositionGoOut instantiateProxy(RemoteServiceInfo rsi) {
            return new ActivityNotifierProxyForPropositionGoOut(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link ActivityNotifierCompositeForPropositionGoOut}, filtered using the attribute <code>id</code>.
         */
        public ActivityNotifierCompositeForPropositionGoOut andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Executes the <code>notifyDailyActivity(activity as DailyActivity)</code> action's method on all devices of this composite.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyDailyActivity(activity as DailyActivity)</code> method.
         */
        public void notifyDailyActivity(fr.inria.phoenix.diasuite.framework.datatype.dailyactivity.DailyActivity activity) throws InvocationException {
            launchDiscovering();
            for (ActivityNotifierProxyForPropositionGoOut proxy : proxies) {
                proxy.notifyDailyActivity(activity);
            }
        }
        
        /**
         * Executes the <code>notifyPeriodActivity(activity as PeriodActivity)</code> action's method on all devices of this composite.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyPeriodActivity(activity as PeriodActivity)</code> method.
         */
        public void notifyPeriodActivity(fr.inria.phoenix.diasuite.framework.datatype.periodactivity.PeriodActivity activity) throws InvocationException {
            launchDiscovering();
            for (ActivityNotifierProxyForPropositionGoOut proxy : proxies) {
                proxy.notifyPeriodActivity(activity);
            }
        }
    }
    
    /**
     * A proxy to one <code>ActivityNotifier</code> device to execute action on for the
     * <code>when provided PropositionGoOut</code> interaction contract.
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
    protected final static class ActivityNotifierProxyForPropositionGoOut extends Proxy {
        private ActivityNotifierProxyForPropositionGoOut(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Executes the <code>notifyDailyActivity(activity as DailyActivity)</code> action's method on this device.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyDailyActivity(activity as DailyActivity)</code> method.
         */
        public void notifyDailyActivity(fr.inria.phoenix.diasuite.framework.datatype.dailyactivity.DailyActivity activity) throws InvocationException {
            callOrder("notifyDailyActivity", activity);
        }
        
        /**
         * Executes the <code>notifyPeriodActivity(activity as PeriodActivity)</code> action's method on this device.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyPeriodActivity(activity as PeriodActivity)</code> method.
         */
        public void notifyPeriodActivity(fr.inria.phoenix.diasuite.framework.datatype.periodactivity.PeriodActivity activity) throws InvocationException {
            callOrder("notifyPeriodActivity", activity);
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
     * 	do NotifyActivity on ActivityNotifier;
     * </code>
     */
    protected final class DiscoverForCongratulation {
        private final ActivityNotifierDiscovererForCongratulation activityNotifierDiscoverer = new ActivityNotifierDiscovererForCongratulation(AbstractCoach_activityController.this);
        
        /**
         * @return a {@link ActivityNotifierDiscovererForCongratulation} object to discover <code>ActivityNotifier</code> devices
         */
        public ActivityNotifierDiscovererForCongratulation activityNotifiers() {
            return activityNotifierDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>ActivityNotifier</code> devices to execute action on for the
     * <code>when provided Congratulation</code> interaction contract.
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
    protected final static class ActivityNotifierDiscovererForCongratulation {
        private Service serviceParent;
        
        private ActivityNotifierDiscovererForCongratulation(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private ActivityNotifierCompositeForCongratulation instantiateComposite() {
            return new ActivityNotifierCompositeForCongratulation(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierCompositeForCongratulation} object composed of all discoverable <code>ActivityNotifier</code>
         */
        public ActivityNotifierCompositeForCongratulation all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierProxyForCongratulation} object pointing to a random discoverable <code>ActivityNotifier</code> device
         */
        public ActivityNotifierProxyForCongratulation anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link ActivityNotifierCompositeForCongratulation} object composed of all matching <code>ActivityNotifier</code> devices
         */
        public ActivityNotifierCompositeForCongratulation whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>ActivityNotifier</code> devices to execute action on for the
     * <code>when provided Congratulation</code> interaction contract.
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
    protected final static class ActivityNotifierCompositeForCongratulation extends fr.inria.diagen.core.service.composite.Composite<ActivityNotifierProxyForCongratulation> {
        private ActivityNotifierCompositeForCongratulation(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/SoftwareSensor/ActivityNotifier/");
        }
        
        @Override
        protected ActivityNotifierProxyForCongratulation instantiateProxy(RemoteServiceInfo rsi) {
            return new ActivityNotifierProxyForCongratulation(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link ActivityNotifierCompositeForCongratulation}, filtered using the attribute <code>id</code>.
         */
        public ActivityNotifierCompositeForCongratulation andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Executes the <code>notifyDailyActivity(activity as DailyActivity)</code> action's method on all devices of this composite.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyDailyActivity(activity as DailyActivity)</code> method.
         */
        public void notifyDailyActivity(fr.inria.phoenix.diasuite.framework.datatype.dailyactivity.DailyActivity activity) throws InvocationException {
            launchDiscovering();
            for (ActivityNotifierProxyForCongratulation proxy : proxies) {
                proxy.notifyDailyActivity(activity);
            }
        }
        
        /**
         * Executes the <code>notifyPeriodActivity(activity as PeriodActivity)</code> action's method on all devices of this composite.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyPeriodActivity(activity as PeriodActivity)</code> method.
         */
        public void notifyPeriodActivity(fr.inria.phoenix.diasuite.framework.datatype.periodactivity.PeriodActivity activity) throws InvocationException {
            launchDiscovering();
            for (ActivityNotifierProxyForCongratulation proxy : proxies) {
                proxy.notifyPeriodActivity(activity);
            }
        }
    }
    
    /**
     * A proxy to one <code>ActivityNotifier</code> device to execute action on for the
     * <code>when provided Congratulation</code> interaction contract.
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
    protected final static class ActivityNotifierProxyForCongratulation extends Proxy {
        private ActivityNotifierProxyForCongratulation(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Executes the <code>notifyDailyActivity(activity as DailyActivity)</code> action's method on this device.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyDailyActivity(activity as DailyActivity)</code> method.
         */
        public void notifyDailyActivity(fr.inria.phoenix.diasuite.framework.datatype.dailyactivity.DailyActivity activity) throws InvocationException {
            callOrder("notifyDailyActivity", activity);
        }
        
        /**
         * Executes the <code>notifyPeriodActivity(activity as PeriodActivity)</code> action's method on this device.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyPeriodActivity(activity as PeriodActivity)</code> method.
         */
        public void notifyPeriodActivity(fr.inria.phoenix.diasuite.framework.datatype.periodactivity.PeriodActivity activity) throws InvocationException {
            callOrder("notifyPeriodActivity", activity);
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover object for Congratulation
    
    // Discover object for PropositionDActivity
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided PropositionDActivity
     * 	do NotifyActivity on ActivityNotifier;
     * </code>
     */
    protected final class DiscoverForPropositionDActivity {
        private final ActivityNotifierDiscovererForPropositionDActivity activityNotifierDiscoverer = new ActivityNotifierDiscovererForPropositionDActivity(AbstractCoach_activityController.this);
        
        /**
         * @return a {@link ActivityNotifierDiscovererForPropositionDActivity} object to discover <code>ActivityNotifier</code> devices
         */
        public ActivityNotifierDiscovererForPropositionDActivity activityNotifiers() {
            return activityNotifierDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>ActivityNotifier</code> devices to execute action on for the
     * <code>when provided PropositionDActivity</code> interaction contract.
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
    protected final static class ActivityNotifierDiscovererForPropositionDActivity {
        private Service serviceParent;
        
        private ActivityNotifierDiscovererForPropositionDActivity(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private ActivityNotifierCompositeForPropositionDActivity instantiateComposite() {
            return new ActivityNotifierCompositeForPropositionDActivity(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierCompositeForPropositionDActivity} object composed of all discoverable <code>ActivityNotifier</code>
         */
        public ActivityNotifierCompositeForPropositionDActivity all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierProxyForPropositionDActivity} object pointing to a random discoverable <code>ActivityNotifier</code> device
         */
        public ActivityNotifierProxyForPropositionDActivity anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link ActivityNotifierCompositeForPropositionDActivity} object composed of all matching <code>ActivityNotifier</code> devices
         */
        public ActivityNotifierCompositeForPropositionDActivity whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>ActivityNotifier</code> devices to execute action on for the
     * <code>when provided PropositionDActivity</code> interaction contract.
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
    protected final static class ActivityNotifierCompositeForPropositionDActivity extends fr.inria.diagen.core.service.composite.Composite<ActivityNotifierProxyForPropositionDActivity> {
        private ActivityNotifierCompositeForPropositionDActivity(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/SoftwareSensor/ActivityNotifier/");
        }
        
        @Override
        protected ActivityNotifierProxyForPropositionDActivity instantiateProxy(RemoteServiceInfo rsi) {
            return new ActivityNotifierProxyForPropositionDActivity(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link ActivityNotifierCompositeForPropositionDActivity}, filtered using the attribute <code>id</code>.
         */
        public ActivityNotifierCompositeForPropositionDActivity andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Executes the <code>notifyDailyActivity(activity as DailyActivity)</code> action's method on all devices of this composite.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyDailyActivity(activity as DailyActivity)</code> method.
         */
        public void notifyDailyActivity(fr.inria.phoenix.diasuite.framework.datatype.dailyactivity.DailyActivity activity) throws InvocationException {
            launchDiscovering();
            for (ActivityNotifierProxyForPropositionDActivity proxy : proxies) {
                proxy.notifyDailyActivity(activity);
            }
        }
        
        /**
         * Executes the <code>notifyPeriodActivity(activity as PeriodActivity)</code> action's method on all devices of this composite.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyPeriodActivity(activity as PeriodActivity)</code> method.
         */
        public void notifyPeriodActivity(fr.inria.phoenix.diasuite.framework.datatype.periodactivity.PeriodActivity activity) throws InvocationException {
            launchDiscovering();
            for (ActivityNotifierProxyForPropositionDActivity proxy : proxies) {
                proxy.notifyPeriodActivity(activity);
            }
        }
    }
    
    /**
     * A proxy to one <code>ActivityNotifier</code> device to execute action on for the
     * <code>when provided PropositionDActivity</code> interaction contract.
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
    protected final static class ActivityNotifierProxyForPropositionDActivity extends Proxy {
        private ActivityNotifierProxyForPropositionDActivity(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Executes the <code>notifyDailyActivity(activity as DailyActivity)</code> action's method on this device.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyDailyActivity(activity as DailyActivity)</code> method.
         */
        public void notifyDailyActivity(fr.inria.phoenix.diasuite.framework.datatype.dailyactivity.DailyActivity activity) throws InvocationException {
            callOrder("notifyDailyActivity", activity);
        }
        
        /**
         * Executes the <code>notifyPeriodActivity(activity as PeriodActivity)</code> action's method on this device.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyPeriodActivity(activity as PeriodActivity)</code> method.
         */
        public void notifyPeriodActivity(fr.inria.phoenix.diasuite.framework.datatype.periodactivity.PeriodActivity activity) throws InvocationException {
            callOrder("notifyPeriodActivity", activity);
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover object for PropositionDActivity
    
    // Discover object for AlertMove
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided AlertMove
     * 	do NotifyActivity on ActivityNotifier;
     * </code>
     */
    protected final class DiscoverForAlertMove {
        private final ActivityNotifierDiscovererForAlertMove activityNotifierDiscoverer = new ActivityNotifierDiscovererForAlertMove(AbstractCoach_activityController.this);
        
        /**
         * @return a {@link ActivityNotifierDiscovererForAlertMove} object to discover <code>ActivityNotifier</code> devices
         */
        public ActivityNotifierDiscovererForAlertMove activityNotifiers() {
            return activityNotifierDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>ActivityNotifier</code> devices to execute action on for the
     * <code>when provided AlertMove</code> interaction contract.
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
    protected final static class ActivityNotifierDiscovererForAlertMove {
        private Service serviceParent;
        
        private ActivityNotifierDiscovererForAlertMove(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private ActivityNotifierCompositeForAlertMove instantiateComposite() {
            return new ActivityNotifierCompositeForAlertMove(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierCompositeForAlertMove} object composed of all discoverable <code>ActivityNotifier</code>
         */
        public ActivityNotifierCompositeForAlertMove all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>ActivityNotifier</code> devices
         * 
         * @return a {@link ActivityNotifierProxyForAlertMove} object pointing to a random discoverable <code>ActivityNotifier</code> device
         */
        public ActivityNotifierProxyForAlertMove anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>ActivityNotifier</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link ActivityNotifierCompositeForAlertMove} object composed of all matching <code>ActivityNotifier</code> devices
         */
        public ActivityNotifierCompositeForAlertMove whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>ActivityNotifier</code> devices to execute action on for the
     * <code>when provided AlertMove</code> interaction contract.
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
    protected final static class ActivityNotifierCompositeForAlertMove extends fr.inria.diagen.core.service.composite.Composite<ActivityNotifierProxyForAlertMove> {
        private ActivityNotifierCompositeForAlertMove(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/SoftwareSensor/ActivityNotifier/");
        }
        
        @Override
        protected ActivityNotifierProxyForAlertMove instantiateProxy(RemoteServiceInfo rsi) {
            return new ActivityNotifierProxyForAlertMove(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link ActivityNotifierCompositeForAlertMove}, filtered using the attribute <code>id</code>.
         */
        public ActivityNotifierCompositeForAlertMove andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Executes the <code>notifyDailyActivity(activity as DailyActivity)</code> action's method on all devices of this composite.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyDailyActivity(activity as DailyActivity)</code> method.
         */
        public void notifyDailyActivity(fr.inria.phoenix.diasuite.framework.datatype.dailyactivity.DailyActivity activity) throws InvocationException {
            launchDiscovering();
            for (ActivityNotifierProxyForAlertMove proxy : proxies) {
                proxy.notifyDailyActivity(activity);
            }
        }
        
        /**
         * Executes the <code>notifyPeriodActivity(activity as PeriodActivity)</code> action's method on all devices of this composite.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyPeriodActivity(activity as PeriodActivity)</code> method.
         */
        public void notifyPeriodActivity(fr.inria.phoenix.diasuite.framework.datatype.periodactivity.PeriodActivity activity) throws InvocationException {
            launchDiscovering();
            for (ActivityNotifierProxyForAlertMove proxy : proxies) {
                proxy.notifyPeriodActivity(activity);
            }
        }
    }
    
    /**
     * A proxy to one <code>ActivityNotifier</code> device to execute action on for the
     * <code>when provided AlertMove</code> interaction contract.
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
    protected final static class ActivityNotifierProxyForAlertMove extends Proxy {
        private ActivityNotifierProxyForAlertMove(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Executes the <code>notifyDailyActivity(activity as DailyActivity)</code> action's method on this device.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyDailyActivity(activity as DailyActivity)</code> method.
         */
        public void notifyDailyActivity(fr.inria.phoenix.diasuite.framework.datatype.dailyactivity.DailyActivity activity) throws InvocationException {
            callOrder("notifyDailyActivity", activity);
        }
        
        /**
         * Executes the <code>notifyPeriodActivity(activity as PeriodActivity)</code> action's method on this device.
         * 
         * @param activity the <code>activity</code> parameter of the <code>notifyPeriodActivity(activity as PeriodActivity)</code> method.
         */
        public void notifyPeriodActivity(fr.inria.phoenix.diasuite.framework.datatype.periodactivity.PeriodActivity activity) throws InvocationException {
            callOrder("notifyPeriodActivity", activity);
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
