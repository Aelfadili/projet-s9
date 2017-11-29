package fr.inria.phoenix.diasuite.framework.context.getfitbitinfos;

import java.io.Serializable;

/**
 * An object to store a value published by the context <code>GetFitbitInfos</code>.
 * <p>
 * context SleepEnd as String {
 * 	when provided inactivityLevel from InactivitySensor
 * 		get currentTime from RoutineScheduler,
 * 		lastInteraction from InactivitySensor
 * 	maybe publish;
 * }
 * controller SaveSleepTime {
 * 	when provided SleepBegin
 * 		do PutStringData on Storage;
 * 	when provided SleepEnd
 * 		do PutStringData on Storage;	
 * }
 * context GetSavedTime as String[] {
 * 	when provided data from Storage
 * 		maybe publish;
 * }
 *
 * <pre>
 * context GetFitbitInfos as SleepPeriod[] {
 * 	when provided currentTime from RoutineScheduler
 * 		get	lastSynchronization from Fitbit, sleepPeriods from Fitbit
 * 		maybe publish;
 * 	when provided tickHour from Clock
 * 		get lastSynchronization from Fitbit, sleepPeriods from Fitbit
 * 		maybe publish;
 * }
 * </pre>
 */
public final class GetFitbitInfosValue implements Serializable {
    private static final long serialVersionUID = 0;
    
    private java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value;
    
    /**
     * Get the value of the context <code>GetFitbitInfos</code>
     * 
     * <p>
     * context SleepEnd as String {
     * 	when provided inactivityLevel from InactivitySensor
     * 		get currentTime from RoutineScheduler,
     * 		lastInteraction from InactivitySensor
     * 	maybe publish;
     * }
     * controller SaveSleepTime {
     * 	when provided SleepBegin
     * 		do PutStringData on Storage;
     * 	when provided SleepEnd
     * 		do PutStringData on Storage;	
     * }
     * context GetSavedTime as String[] {
     * 	when provided data from Storage
     * 		maybe publish;
     * }
     * 
     * @return the value of the context <code>GetFitbitInfos</code>
     */
    public java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value() {
        return value;
    }
    
    public GetFitbitInfosValue(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value) {
        this.value = value;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        GetFitbitInfosValue other = (GetFitbitInfosValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
