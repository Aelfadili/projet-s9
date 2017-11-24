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
 * context GetFitbitInfos as SleepPeriod[] indexed by period as Period  {
 * 	when provided currentTime from RoutineScheduler
 * 		get	sleepPeriods from Fitbit,
 * 		tickHour from Clock
 * 		always publish;
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
    
    private GetFitbitInfosIndices indices;
    
    /**
     * Get the value of the indices of the context <code>GetFitbitInfos</code>
     * 
     * @return the value of the indices
     */
    public GetFitbitInfosIndices indices() {
        return indices;
    }
    
    public GetFitbitInfosValue(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value, GetFitbitInfosIndices indices) {
        this.value = value;
        this.indices = indices;
    }
    
    public GetFitbitInfosValue(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> value,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        this.value = value;
        this.indices = new GetFitbitInfosIndices(period);
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
        GetFitbitInfosValue other = (GetFitbitInfosValue) obj;
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
}
