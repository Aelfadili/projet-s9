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
 * 	do	SendMessage on Messenger;
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
     * 	do	SendMessage on Messenger;
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
     * 	do	SendMessage on Messenger;
     * </code>
     */
    protected final class DiscoverForCoupling {
        private final MessengerDiscovererForCoupling messengerDiscoverer = new MessengerDiscovererForCoupling(AbstractSleepAnalyser.this);
        
        /**
         * @return a {@link MessengerDiscovererForCoupling} object to discover <code>Messenger</code> devices
         */
        public MessengerDiscovererForCoupling messengers() {
            return messengerDiscoverer;
        }
    }
    
    /**
     * Discover object that will exposes the <code>Messenger</code> devices to execute action on for the
     * <code>when provided Coupling</code> interaction contract.
     * 
     * <pre>
     * device Messenger extends CommunicationService {
     * 	source lastMessage as Message;
     * 	action SendMessage;
     * }
     * </pre>
     */
    protected final static class MessengerDiscovererForCoupling {
        private Service serviceParent;
        
        private MessengerDiscovererForCoupling(Service serviceParent) {
            super();
            this.serviceParent = serviceParent;
        }
        
        private MessengerCompositeForCoupling instantiateComposite() {
            return new MessengerCompositeForCoupling(serviceParent);
        }
        
        /**
         * Returns a composite of all accessible <code>Messenger</code> devices
         * 
         * @return a {@link MessengerCompositeForCoupling} object composed of all discoverable <code>Messenger</code>
         */
        public MessengerCompositeForCoupling all() {
            return instantiateComposite();
        }
        
        /**
         * Returns a proxy to one out of all accessible <code>Messenger</code> devices
         * 
         * @return a {@link MessengerProxyForCoupling} object pointing to a random discoverable <code>Messenger</code> device
         */
        public MessengerProxyForCoupling anyOne() {
            return all().anyOne();
        }
        
        /**
         * Returns a composite of all accessible <code>Messenger</code> devices whose attribute <code>id</code> matches a given value.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return a {@link MessengerCompositeForCoupling} object composed of all matching <code>Messenger</code> devices
         */
        public MessengerCompositeForCoupling whereId(java.lang.String id) throws CompositeException {
            return instantiateComposite().andId(id);
        }
    }
    
    /**
     * A composite of several <code>Messenger</code> devices to execute action on for the
     * <code>when provided Coupling</code> interaction contract.
     * 
     * <pre>
     * device Messenger extends CommunicationService {
     * 	source lastMessage as Message;
     * 	action SendMessage;
     * }
     * </pre>
     */
    protected final static class MessengerCompositeForCoupling extends fr.inria.diagen.core.service.composite.Composite<MessengerProxyForCoupling> {
        private MessengerCompositeForCoupling(Service serviceParent) {
            super(serviceParent, "/Device/Device/Service/CommunicationService/Messenger/");
        }
        
        @Override
        protected MessengerProxyForCoupling instantiateProxy(RemoteServiceInfo rsi) {
            return new MessengerProxyForCoupling(service, rsi);
        }
        
        /**
         * Returns this composite in which a filter was set to the attribute <code>id</code>.
         * 
         * @param id The <code>id<code> attribute value to match.
         * @return this {@link MessengerCompositeForCoupling}, filtered using the attribute <code>id</code>.
         */
        public MessengerCompositeForCoupling andId(java.lang.String id) throws CompositeException {
            filterByAttribute("id", id);
            return this;
        }
        
        /**
         * Executes the <code>sendMessage(message as Message)</code> action's method on all devices of this composite.
         * 
         * @param message the <code>message</code> parameter of the <code>sendMessage(message as Message)</code> method.
         */
        public void sendMessage(fr.inria.phoenix.diasuite.framework.datatype.message.Message message) throws InvocationException {
            launchDiscovering();
            for (MessengerProxyForCoupling proxy : proxies) {
                proxy.sendMessage(message);
            }
        }
    }
    
    /**
     * A proxy to one <code>Messenger</code> device to execute action on for the
     * <code>when provided Coupling</code> interaction contract.
     * 
     * <pre>
     * device Messenger extends CommunicationService {
     * 	source lastMessage as Message;
     * 	action SendMessage;
     * }
     * </pre>
     */
    protected final static class MessengerProxyForCoupling extends Proxy {
        private MessengerProxyForCoupling(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Executes the <code>sendMessage(message as Message)</code> action's method on this device.
         * 
         * @param message the <code>message</code> parameter of the <code>sendMessage(message as Message)</code> method.
         */
        public void sendMessage(fr.inria.phoenix.diasuite.framework.datatype.message.Message message) throws InvocationException {
            callOrder("sendMessage", message);
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
