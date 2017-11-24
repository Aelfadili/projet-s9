package fr.inria.phoenix.diasuite.framework.context.getfitbitinfos;

import java.io.Serializable;

/**
 * Container for the indices of the context <code>GetFitbitInfos</code>.
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
public final class GetFitbitInfosIndices implements Serializable {
    private static final long serialVersionUID = 0;
    
    public GetFitbitInfosIndices(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        this._period = period;
    }
    
    // Code for index period
    private fr.inria.phoenix.diasuite.framework.datatype.period.Period _period;
    
    /**
     * Get the <code>period</code> index
     * 
     * @return the value of the <code>period</code> index
     */
    public fr.inria.phoenix.diasuite.framework.datatype.period.Period period() {
        return _period;
    }
    
    /**
     * Set the <code>period</code> index
     * 
     * @param newPeriod the new value for the <code>period</code> index
     */
    public void setPeriod(fr.inria.phoenix.diasuite.framework.datatype.period.Period newPeriod) {
        this._period = newPeriod;
    }
    // End of code for index period
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_period == null) ? 0 : _period.hashCode());
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
        GetFitbitInfosIndices other = (GetFitbitInfosIndices) obj;
        if (_period == null) {
            if (other._period != null)
                return false;
        } else if (!_period.equals(other._period))
            return false;
        return true;
    }
}
