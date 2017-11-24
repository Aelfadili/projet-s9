package fr.inria.phoenix.diasuite.framework.controller.sleepanalyser;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.context.coupling.CouplingValue;

/**
 * <pre>
 * controller SleepAnalyser {
 * 	when provided Coupling
 * 	do SendNonCriticalNotification on Notifier,
 * 	 PutStringData on Storage;
 * }
 * </pre>
 */
@SuppressWarnings("all")
public abstract class AbstractSleepAnalyser extends Service {
    
    public AbstractSleepAnalyser(ServiceConfiguration serviceConfiguration) {
        super("/Controller/SleepAnalyser/", serviceConfiguration);
    }
    
    // Methods from the Service class
    @Override
    protected final void internalPostInitialize() {
        subscribeValue("coupling", "/Context/Coupling/"); // subscribe to Coupling context
        postInitialize();
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("coupling") && source.isCompatible("/Context/Coupling/")) {
            CouplingValue couplingValue = new CouplingValue((java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>) value,
                    (fr.inria.phoenix.diasuite.framework.datatype.period.Period) indexes[0]);
            
            onCoupling(couplingValue, new DiscoverForCoupling());
        }
    }
    // End of methods from the Service class
    
    // Interaction contract implementation
    /**
     * This method is called when the <code>Coupling</code> context publishes a value.
     * 
     * <pre>
     * when provided Coupling
     * 	do SendNonCriticalNotification on Notifier,
     * 	 PutStringData on Storage;
     * </pre>
     * 
     * @param coupling the value of the <code>Coupling</code> context.
     * @param discover a discover object to get context values and action methods
     */
    protected abstract void onCoupling(CouplingValue coupling, DiscoverForCoupling discover);
    
    // End of interaction contract implementation
    
    // Discover object for Coupling
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided Coupling
     * 	do SendNonCriticalNotification on Notifier,
     * 	 PutStringData on Storage;
     * </code>
     */
    protected final class DiscoverForCoupling {
        private final NotifierDiscovererForCoupling notifierDiscoverer = new NotifierDiscovererForCoupling(AbstractSleepAnalyser.this);
        private final StorageDiscovererForCoupling storageDiscoverer = new StorageDiscovererForCoupling(AbstractSleepAnalyser.this);
        
        /**
         * @return a {@link NotifierDiscovererForCoupling} object to discover <code>Notifier</code> devices
         */
        public NotifierDiscovererForCoupling notifiers() {
            return notifierDiscoverer;
        }
        
        /**
         * @return a {@link StorageDiscovererForCoupling} object to discover <code>Storage</code> devices
         */
        public StorageDiscovererForCoupling storages() {
            return storageDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Notifier</code> devices to execute action on for the
     * <code>when provided Coupling</code> interaction contract.
     * 
     * <pre>
     * device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
     * </pre>
     */
    protected final static class NotifierDiscovererForCoupling {
        private Service serviceParent;
        
        private NotifierDiscovererForCoupling(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private NotifierCompositeForCoupling instantiateComposite() {
            return new NotifierCompositeForCoupling(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Notifier</code> devices
         * 
         * @return a {@link NotifierCompositeForCoupling} object composed of all discoverable <code>Notifier</code>
         */
        public NotifierCompositeForCoupling all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Notifier</code> devices
         * 
         * @return a {@link NotifierProxyForCoupling} object pointing to a random discoverable <code>Notifier</code> device
         */
        public NotifierProxyForCoupling anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Notifier</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link NotifierCompositeForCoupling} object composed of all matching <code>Notifier</code> devices
         */
        public NotifierCompositeForCoupling whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Notifier</code> devices to execute action on for the
     * <code>when provided Coupling</code> interaction contract.
     * 
     * <pre>
     * device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
     * </pre>
     */
    protected final static class NotifierCompositeForCoupling extends fr.inria.diagen.core.service.composite.Composite<NotifierProxyForCoupling> {
        private NotifierCompositeForCoupling(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/HomeService/Notifier/");
        }
        
        @Override
        protected NotifierProxyForCoupling instantiateProxy(RemoteServiceInfo rsi) {
            return new NotifierProxyForCoupling(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link NotifierCompositeForCoupling}, filtered using the attribute <code>id</code>.
         */
        public NotifierCompositeForCoupling andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Executes the <code>sendNonCriticalNotification(notification as NonCriticalNotification)</code> action's method on all devices of this composite.
         * 
         * @param notification the <code>notification</code> parameter of the <code>sendNonCriticalNotification(notification as NonCriticalNotification)</code> method.
         */
        public void sendNonCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.noncriticalnotification.NonCriticalNotification notification) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForCoupling proxy : proxies) {
                proxy.sendNonCriticalNotification(notification);
            }
        }
        
        /**
         * Executes the <code>registerNonCriticalNotification(notification as NonCriticalNotification, displayDate as Date, expirationDate as Date)</code> action's method on all devices of this composite.
         * 
         * @param notification the <code>notification</code> parameter of the <code>registerNonCriticalNotification(notification as NonCriticalNotification, displayDate as Date, expirationDate as Date)</code> method.
         * @param displayDate the <code>displayDate</code> parameter of the <code>registerNonCriticalNotification(notification as NonCriticalNotification, displayDate as Date, expirationDate as Date)</code> method.
         * @param expirationDate the <code>expirationDate</code> parameter of the <code>registerNonCriticalNotification(notification as NonCriticalNotification, displayDate as Date, expirationDate as Date)</code> method.
         */
        public void registerNonCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.noncriticalnotification.NonCriticalNotification notification,
                fr.inria.phoenix.diasuite.framework.datatype.date.Date displayDate,
                fr.inria.phoenix.diasuite.framework.datatype.date.Date expirationDate) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForCoupling proxy : proxies) {
                proxy.registerNonCriticalNotification(notification, displayDate, expirationDate);
            }
        }
        
        /**
         * Executes the <code>cancelNonCriticalNotification(id as String)</code> action's method on all devices of this composite.
         * 
         * @param id the <code>id</code> parameter of the <code>cancelNonCriticalNotification(id as String)</code> method.
         */
        public void cancelNonCriticalNotification(java.lang.String id) throws InvocationException {
            launchDiscovering();
            for (NotifierProxyForCoupling proxy : proxies) {
                proxy.cancelNonCriticalNotification(id);
            }
        }
    }
    
    /**
     * A proxy to one <code>Notifier</code> device to execute action on for the
     * <code>when provided Coupling</code> interaction contract.
     * 
     * <pre>
     * device Notifier extends HomeService {
     * 	source cancelled as Boolean indexed by id as String;
     * 	source expired as Boolean indexed by id as String;
     * 	source reply as Integer indexed by id as String;
     * 	action SendCriticalNotification;
     * 	action SendNonCriticalNotification;
     * }
     * </pre>
     */
    protected final static class NotifierProxyForCoupling extends Proxy {
        private NotifierProxyForCoupling(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Executes the <code>sendNonCriticalNotification(notification as NonCriticalNotification)</code> action's method on this device.
         * 
         * @param notification the <code>notification</code> parameter of the <code>sendNonCriticalNotification(notification as NonCriticalNotification)</code> method.
         */
        public void sendNonCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.noncriticalnotification.NonCriticalNotification notification) throws InvocationException {
            callOrder("sendNonCriticalNotification", notification);
        }
        
        /**
         * Executes the <code>registerNonCriticalNotification(notification as NonCriticalNotification, displayDate as Date, expirationDate as Date)</code> action's method on this device.
         * 
         * @param notification the <code>notification</code> parameter of the <code>registerNonCriticalNotification(notification as NonCriticalNotification, displayDate as Date, expirationDate as Date)</code> method.
         * @param displayDate the <code>displayDate</code> parameter of the <code>registerNonCriticalNotification(notification as NonCriticalNotification, displayDate as Date, expirationDate as Date)</code> method.
         * @param expirationDate the <code>expirationDate</code> parameter of the <code>registerNonCriticalNotification(notification as NonCriticalNotification, displayDate as Date, expirationDate as Date)</code> method.
         */
        public void registerNonCriticalNotification(fr.inria.phoenix.diasuite.framework.datatype.noncriticalnotification.NonCriticalNotification notification,
                fr.inria.phoenix.diasuite.framework.datatype.date.Date displayDate,
                fr.inria.phoenix.diasuite.framework.datatype.date.Date expirationDate) throws InvocationException {
            callOrder("registerNonCriticalNotification", notification, displayDate, expirationDate);
        }
        
        /**
         * Executes the <code>cancelNonCriticalNotification(id as String)</code> action's method on this device.
         * 
         * @param id the <code>id</code> parameter of the <code>cancelNonCriticalNotification(id as String)</code> method.
         */
        public void cancelNonCriticalNotification(java.lang.String id) throws InvocationException {
            callOrder("cancelNonCriticalNotification", id);
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    
    /**
     * Discover object that will exposes the <code>Storage</code> devices to execute action on for the
     * <code>when provided Coupling</code> interaction contract.
     * <p>
     * --------
     * Storage
     * --------
     * 
     * <pre>
     * device Storage extends Service {
     * 	source data as String indexed by key as String;
     * 	action PutStringData;
     * }
     * </pre>
     */
    protected final static class StorageDiscovererForCoupling {
        private Service serviceParent;
        
        private StorageDiscovererForCoupling(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private StorageCompositeForCoupling instantiateComposite() {
            return new StorageCompositeForCoupling(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Storage</code> devices
         * 
         * @return a {@link StorageCompositeForCoupling} object composed of all discoverable <code>Storage</code>
         */
        public StorageCompositeForCoupling all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Storage</code> devices
         * 
         * @return a {@link StorageProxyForCoupling} object pointing to a random discoverable <code>Storage</code> device
         */
        public StorageProxyForCoupling anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Storage</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link StorageCompositeForCoupling} object composed of all matching <code>Storage</code> devices
         */
        public StorageCompositeForCoupling whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Storage</code> devices to execute action on for the
     * <code>when provided Coupling</code> interaction contract.
     * <p>
     * --------
     * Storage
     * --------
     * 
     * <pre>
     * device Storage extends Service {
     * 	source data as String indexed by key as String;
     * 	action PutStringData;
     * }
     * </pre>
     */
    protected final static class StorageCompositeForCoupling extends fr.inria.diagen.core.service.composite.Composite<StorageProxyForCoupling> {
        private StorageCompositeForCoupling(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/Storage/");
        }
        
        @Override
        protected StorageProxyForCoupling instantiateProxy(RemoteServiceInfo rsi) {
            return new StorageProxyForCoupling(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link StorageCompositeForCoupling}, filtered using the attribute <code>id</code>.
         */
        public StorageCompositeForCoupling andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Executes the <code>put(pKey as String, pData as String)</code> action's method on all devices of this composite.
         * 
         * @param pKey the <code>pKey</code> parameter of the <code>put(pKey as String, pData as String)</code> method.
         * @param pData the <code>pData</code> parameter of the <code>put(pKey as String, pData as String)</code> method.
         */
        public void put(java.lang.String pKey,
                java.lang.String pData) throws InvocationException {
            launchDiscovering();
            for (StorageProxyForCoupling proxy : proxies) {
                proxy.put(pKey, pData);
            }
        }
    }
    
    /**
     * A proxy to one <code>Storage</code> device to execute action on for the
     * <code>when provided Coupling</code> interaction contract.
     * <p>
     * --------
     * Storage
     * --------
     * 
     * <pre>
     * device Storage extends Service {
     * 	source data as String indexed by key as String;
     * 	action PutStringData;
     * }
     * </pre>
     */
    protected final static class StorageProxyForCoupling extends Proxy {
        private StorageProxyForCoupling(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Executes the <code>put(pKey as String, pData as String)</code> action's method on this device.
         * 
         * @param pKey the <code>pKey</code> parameter of the <code>put(pKey as String, pData as String)</code> method.
         * @param pData the <code>pData</code> parameter of the <code>put(pKey as String, pData as String)</code> method.
         */
        public void put(java.lang.String pKey,
                java.lang.String pData) throws InvocationException {
            callOrder("put", pKey, pData);
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover object for Coupling
}
