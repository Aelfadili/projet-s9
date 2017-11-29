package fr.inria.phoenix.diasuite.framework.device.fitbit;

import java.io.Serializable;

import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;
import fr.inria.diagen.core.service.proxy.Proxy;

/**
 * An object to store a value published on the source <code>calories</code> of the device <code>Fitbit</code>.
 *
 * <pre>
 * device Fitbit extends Device {
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
 * </pre>
 */
public final class CaloriesFromFitbit implements Serializable {
    private static final long serialVersionUID = 0;
    
    private java.lang.Integer value;
    
    /**
     * Get the value of the source <code>calories</code>
     * 
     * @return the value of the source <code>calories</code>
     */
    public java.lang.Integer value() {
        return value;
    }
    
    private CaloriesIndices indices;
    
    /**
     * Get the value of the indices of the source <code>calories</code>
     * 
     * @return the value of the indices
     */
    public CaloriesIndices indices() {
        return indices;
    }
    
    public CaloriesFromFitbit(Service service, RemoteServiceInfo remoteServiceInfo, java.lang.Integer value, CaloriesIndices indices) {
        this.sender = new FitbitProxy(service, remoteServiceInfo);
        this.value = value;
        this.indices = indices;
    }
    
    public CaloriesFromFitbit(Service service, RemoteServiceInfo remoteServiceInfo, java.lang.Integer value,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        this.sender = new FitbitProxy(service, remoteServiceInfo);
        this.value = value;
        this.indices = new CaloriesIndices(period);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + ((indices == null) ? 0 : indices.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CaloriesFromFitbit other = (CaloriesFromFitbit) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        if (indices == null) {
            if (other.indices != null)
                return false;
        } else if (!indices.equals(other.indices))
            return false;
        return true;
    }
    
    // Proxy to the sender, i.e., the device that published the source
    private FitbitProxy sender;
    
    /**
     * Get the sender of the source <code>calories</code>. I.e., the <code>Fitbit</code> device that published the source.
     * 
     * @return A proxy to the <code>Fitbit</code> that triggered the source
     */
    public FitbitProxy sender() {
        return sender;
    }
    
    /**
     * A proxy to a <code>Fitbit</code> that discloses subscription/unsubscription methods.
     */
    public class FitbitProxy extends Proxy {
        private FitbitProxy(Service service, RemoteServiceInfo remoteServiceInfo) {
            super(service, remoteServiceInfo);
        }
        
        /**
         * Subcribes to publication of source <code>calories</code> from this <code>Fitbit</code>.
         */
        public void subscribeCalories() {
            getService().getProcessor().subscribeValue(getService().getOutProperties(), new fr.inria.diagen.core.service.filter.SubscriptionFilter(this.getRemoteServiceInfo()), "calories");
        }
        
        /**
         * Unsubcribes from publication of source <code>calories</code> from this <code>Fitbit</code>.
         */
        public void unsubscribeCalories() {
            getService().getProcessor().unsubscribeValue(getService().getOutProperties(), new fr.inria.diagen.core.service.filter.SubscriptionFilter(this.getRemoteServiceInfo()), "calories");
        }
        
        /**
         * @return the value of the <code>id</code> attribute
         */
        public java.lang.String id() {
            return (java.lang.String) callGetValue("id");
        }
    }
}
