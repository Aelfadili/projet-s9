package fr.inria.phoenix.diasuite.framework.context.getsavedtime;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.service.composite.CompositeException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

import fr.inria.phoenix.diasuite.framework.device.storage.DataFromStorage;

/**
 * controller SaveSleepTime {
 * 	when provided SleepBegin
 * 		do PutStringData on Storage;
 * 	when provided SleepEnd
 * 		do PutStringData on Storage;	
 * }
 * 
 * <pre>
 * context GetSavedTime as String[] {
 * 	when provided data from Storage
 * 		maybe publish;
 * }
 * </pre>
 */
@SuppressWarnings("all")
public abstract class AbstractGetSavedTime extends Service {
    
    public AbstractGetSavedTime(ServiceConfiguration serviceConfiguration) {
        super("/Context/GetSavedTime/", serviceConfiguration);
    }
    
    // Methods from the Service class
    @Override
    protected final void internalPostInitialize() {
        postInitialize();
    }
    
    @Override
    protected void postInitialize() {
        // Default implementation of post initialize: subscribe to all required devices
        discoverStorageForSubscribe.all().subscribeData(); // subscribe to data from all Storage devices
    }
    
    @Override
    public final void valueReceived(java.util.Map<String, Object> properties, RemoteServiceInfo source, String eventName, Object value, Object... indexes) {
        if (eventName.equals("data") && source.isCompatible("/Device/Device/Service/Storage/")) {
            DataFromStorage dataFromStorage = new DataFromStorage(this, source, (java.lang.String) value,
                    (java.lang.String) indexes[0]);
            
            GetSavedTimeValuePublishable returnValue = onDataFromStorage(dataFromStorage);
            if(returnValue != null && returnValue.doPublish()) {
                setGetSavedTime(returnValue.getValue());
            }
        }
    }
    
    @Override
    public final Object getValueCalled(java.util.Map<String, Object> properties, RemoteServiceInfo source, String valueName,
            Object... indexes) throws Exception {
        if (valueName.equals("getSavedTime")) {
            return getLastValue();
        }
        throw new InvocationException("Unsupported method call: " + valueName);
    }
    // End of methods from the Service class
    
    // Code relative to the return value of the context
    private java.util.List<java.lang.String> contextValue;
    
    private void setGetSavedTime(java.util.List<java.lang.String> newContextValue) {
        contextValue = newContextValue;
        getProcessor().publishValue(getOutProperties(), "getSavedTime", newContextValue);
    }
    
    /**
     * Get the last value of the context
     * 
     * @return the latest value published by the context
     */
    protected final java.util.List<java.lang.String> getLastValue() {
        return contextValue;
    }
    
    /**
     * A class that represents a value that might be published for the <code>GetSavedTime</code> context. It is used by
     * event methods that might or might not publish values for this context.
     */
    protected final static class GetSavedTimeValuePublishable {
        
        // The value of the context
        private java.util.List<java.lang.String> value;
        // Whether the value should be published or not
        private boolean doPublish;
        
        public GetSavedTimeValuePublishable(java.util.List<java.lang.String> value, boolean doPublish) {
            this.value = value;
            this.doPublish = doPublish;
        }
        
        /**
         * @return the value of the context that might be published
         */
        public java.util.List<java.lang.String> getValue() {
            return value;
        }
        
        /**
         * Sets the value that might be published
         * 
         * @param value the value that will be published if {@link #doPublish()} returns true
         */
        public void setValue(java.util.List<java.lang.String> value) {
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
     * This method is called when a <code>Storage</code> device on which we have subscribed publish on its <code>data</code> source.
     * 
     * <pre>
     * when provided data from Storage
     * 		maybe publish;
     * </pre>
     * 
     * @param dataFromStorage the value of the <code>data</code> source and the <code>Storage</code> device that published the value.
     * @return a {@link GetSavedTimeValuePublishable} that says if the context should publish a value and which value it should publish
     */
    protected abstract GetSavedTimeValuePublishable onDataFromStorage(DataFromStorage dataFromStorage);
    
    // End of interaction contract implementation
    
    // Discover part for Storage devices
    /**
     * Use this object to discover Storage devices.
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
     * 
     * @see StorageDiscoverer
     */
    protected final StorageDiscoverer discoverStorageForSubscribe = new StorageDiscoverer(this);
    
    /**
     * Discover object that will exposes the <code>Storage</code> devices that can be discovered
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
    protected final static class StorageDiscoverer {
        private Service serviceParent;
        
        private StorageDiscoverer(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private StorageComposite instantiateComposite() {
            return new StorageComposite(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Storage</code> devices
         * 
         * @return a {@link StorageComposite} object composed of all discoverable <code>Storage</code>
         */
        public StorageComposite all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Storage</code> devices
         * 
         * @return a {@link StorageProxy} object pointing to a random discoverable <code>Storage</code> device
         */
        public StorageProxy anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Storage</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link StorageComposite} object composed of all matching <code>Storage</code> devices
         */
        public StorageComposite whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Storage</code> devices
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
    protected final static class StorageComposite extends fr.inria.diagen.core.service.composite.Composite<StorageProxy> {
        private StorageComposite(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/Storage/");
        }
        
        @Override
        protected StorageProxy instantiateProxy(RemoteServiceInfo rsi) {
            return new StorageProxy(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link StorageComposite}, filtered using the attribute <code>id</code>.
         */
        public StorageComposite andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Subscribes to the <code>data</code> source. After a call to this method, the context will be notified when a
         * <code>Storage</code> device of this composite publishes a value on its <code>data</code> source.
         */
        public void subscribeData() {
            subscribeValue("data");
        }
        
        /**
         * Unsubscribes from the <code>data</code> source. After a call to this method, the context will no more be notified
         * when a <code>Storage</code> device of this composite publishes a value on its <code>data</code> source.
         */
        public void unsubscribeData() {
            unsubscribeValue("data");
        }
    }
    
    /**
     * A proxy to one <code>Storage</code> device
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
    protected final static class StorageProxy extends Proxy {
        private StorageProxy(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Subscribes to the <code>data</code> source. After a call to this method, the context will be notified when the
         * <code>Storage</code> device of this proxy publishes a value on its <code>data</code> source.
         */
        public void subscribeData() {
            subscribeValue("data");
        }
        
        /**
         * Unsubscribes from the <code>data</code> source. After a call to this method, the context will no more be notified
         * when the <code>Storage</code> device of this proxy publishes a value on its <code>data</code> source.
         */
        public void unsubscribeData() {
            unsubscribeValue("data");
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
    // End of discover part for Storage devices
}
