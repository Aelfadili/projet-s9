package fr.inria.phoenix.diasuite.framework.controller.savesleeptime;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.context.sleepbegin.SleepBeginValue;
import fr.inria.phoenix.diasuite.framework.context.sleepend.SleepEndValue;

/**
 * <pre>
 * controller SaveSleepTime {
 * 	when provided SleepBegin
 * 		do PutStringData on Storage;
 * 	when provided SleepEnd
 * 		do PutStringData on Storage;	
 * }
 * </pre>
 */
@SuppressWarnings("all")
public abstract class AbstractSaveSleepTime extends Service {
    
    public AbstractSaveSleepTime(ServiceConfiguration serviceConfiguration) {
        super("/Controller/SaveSleepTime/", serviceConfiguration);
    }
    
    // Methods from the Service class
    @Override
    protected final void internalPostInitialize() {
        subscribeValue("sleepBegin", "/Context/SleepBegin/"); // subscribe to SleepBegin context
        subscribeValue("sleepEnd", "/Context/SleepEnd/"); // subscribe to SleepEnd context
        postInitialize();
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("sleepBegin") && source.isCompatible("/Context/SleepBegin/")) {
            SleepBeginValue sleepBeginValue = new SleepBeginValue((java.lang.String) value);
            
            onSleepBegin(sleepBeginValue, new DiscoverForSleepBegin());
        }
        if (eventName.equals("sleepEnd") && source.isCompatible("/Context/SleepEnd/")) {
            SleepEndValue sleepEndValue = new SleepEndValue((java.lang.String) value);
            
            onSleepEnd(sleepEndValue, new DiscoverForSleepEnd());
        }
    }
    // End of methods from the Service class
    
    // Interaction contract implementation
    /**
     * This method is called when the <code>SleepBegin</code> context publishes a value.
     * 
     * <pre>
     * when provided SleepBegin
     * 		do PutStringData on Storage;
     * </pre>
     * 
     * @param sleepBegin the value of the <code>SleepBegin</code> context.
     * @param discover a discover object to get context values and action methods
     */
    protected abstract void onSleepBegin(SleepBeginValue sleepBegin, DiscoverForSleepBegin discover);
    
    /**
     * This method is called when the <code>SleepEnd</code> context publishes a value.
     * 
     * <pre>
     * when provided SleepEnd
     * 		do PutStringData on Storage;
     * </pre>
     * 
     * @param sleepEnd the value of the <code>SleepEnd</code> context.
     * @param discover a discover object to get context values and action methods
     */
    protected abstract void onSleepEnd(SleepEndValue sleepEnd, DiscoverForSleepEnd discover);
    
    // End of interaction contract implementation
    
    // Discover object for SleepBegin
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided SleepBegin
     * 		do PutStringData on Storage;
     * </code>
     */
    protected final class DiscoverForSleepBegin {
        private final StorageDiscovererForSleepBegin storageDiscoverer = new StorageDiscovererForSleepBegin(AbstractSaveSleepTime.this);
        
        /**
         * @return a {@link StorageDiscovererForSleepBegin} object to discover <code>Storage</code> devices
         */
        public StorageDiscovererForSleepBegin storages() {
            return storageDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Storage</code> devices to execute action on for the
     * <code>when provided SleepBegin</code> interaction contract.
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
    protected final static class StorageDiscovererForSleepBegin {
        private Service serviceParent;
        
        private StorageDiscovererForSleepBegin(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private StorageCompositeForSleepBegin instantiateComposite() {
            return new StorageCompositeForSleepBegin(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Storage</code> devices
         * 
         * @return a {@link StorageCompositeForSleepBegin} object composed of all discoverable <code>Storage</code>
         */
        public StorageCompositeForSleepBegin all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Storage</code> devices
         * 
         * @return a {@link StorageProxyForSleepBegin} object pointing to a random discoverable <code>Storage</code> device
         */
        public StorageProxyForSleepBegin anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Storage</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link StorageCompositeForSleepBegin} object composed of all matching <code>Storage</code> devices
         */
        public StorageCompositeForSleepBegin whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Storage</code> devices to execute action on for the
     * <code>when provided SleepBegin</code> interaction contract.
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
    protected final static class StorageCompositeForSleepBegin extends fr.inria.diagen.core.service.composite.Composite<StorageProxyForSleepBegin> {
        private StorageCompositeForSleepBegin(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/Storage/");
        }
        
        @Override
        protected StorageProxyForSleepBegin instantiateProxy(RemoteServiceInfo rsi) {
            return new StorageProxyForSleepBegin(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link StorageCompositeForSleepBegin}, filtered using the attribute <code>id</code>.
         */
        public StorageCompositeForSleepBegin andId(java.lang.String id) throws CompositeException {
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
            for (StorageProxyForSleepBegin proxy : proxies) {
                proxy.put(pKey, pData);
            }
        }
    }
    
    /**
     * A proxy to one <code>Storage</code> device to execute action on for the
     * <code>when provided SleepBegin</code> interaction contract.
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
    protected final static class StorageProxyForSleepBegin extends Proxy {
        private StorageProxyForSleepBegin(Service service, RemoteServiceInfo remoteServiceInfo) {
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
    // End of discover object for SleepBegin
    
    // Discover object for SleepEnd
    /**
     * An object to discover devices and contexts for the following interaction contract:
     * 
     * <code>
     * when provided SleepEnd
     * 		do PutStringData on Storage;
     * </code>
     */
    protected final class DiscoverForSleepEnd {
        private final StorageDiscovererForSleepEnd storageDiscoverer = new StorageDiscovererForSleepEnd(AbstractSaveSleepTime.this);
        
        /**
         * @return a {@link StorageDiscovererForSleepEnd} object to discover <code>Storage</code> devices
         */
        public StorageDiscovererForSleepEnd storages() {
            return storageDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Storage</code> devices to execute action on for the
     * <code>when provided SleepEnd</code> interaction contract.
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
    protected final static class StorageDiscovererForSleepEnd {
        private Service serviceParent;
        
        private StorageDiscovererForSleepEnd(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private StorageCompositeForSleepEnd instantiateComposite() {
            return new StorageCompositeForSleepEnd(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Storage</code> devices
         * 
         * @return a {@link StorageCompositeForSleepEnd} object composed of all discoverable <code>Storage</code>
         */
        public StorageCompositeForSleepEnd all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Storage</code> devices
         * 
         * @return a {@link StorageProxyForSleepEnd} object pointing to a random discoverable <code>Storage</code> device
         */
        public StorageProxyForSleepEnd anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Storage</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link StorageCompositeForSleepEnd} object composed of all matching <code>Storage</code> devices
         */
        public StorageCompositeForSleepEnd whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Storage</code> devices to execute action on for the
     * <code>when provided SleepEnd</code> interaction contract.
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
    protected final static class StorageCompositeForSleepEnd extends fr.inria.diagen.core.service.composite.Composite<StorageProxyForSleepEnd> {
        private StorageCompositeForSleepEnd(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/Storage/");
        }
        
        @Override
        protected StorageProxyForSleepEnd instantiateProxy(RemoteServiceInfo rsi) {
            return new StorageProxyForSleepEnd(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link StorageCompositeForSleepEnd}, filtered using the attribute <code>id</code>.
         */
        public StorageCompositeForSleepEnd andId(java.lang.String id) throws CompositeException {
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
            for (StorageProxyForSleepEnd proxy : proxies) {
                proxy.put(pKey, pData);
            }
        }
    }
    
    /**
     * A proxy to one <code>Storage</code> device to execute action on for the
     * <code>when provided SleepEnd</code> interaction contract.
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
    protected final static class StorageProxyForSleepEnd extends Proxy {
        private StorageProxyForSleepEnd(Service service, RemoteServiceInfo remoteServiceInfo) {
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
    // End of discover object for SleepEnd
}
