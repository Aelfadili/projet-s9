package fr.inria.phoenix.diasuite.framework.device.fitbit;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.diagen.core.exception.InvocationException;
import fr.inria.diagen.core.network.RemoteServiceInfo;
import fr.inria.diagen.core.service.local.Service;

/**
 * ------------------------------------------------------------
Fitbit							||
------------------------------------------------------------

<pre>
device Fitbit extends Device {
 *         source calories as Integer indexed by period as Period;
 *         source distanceInMeters as Integer indexed by period as Period;
 *         source pulses as Pulse indexed by period as Period;
 *         source steps as Integer indexed by period as Period;
 *         source sleepPeriods as SleepPeriod [] indexed by period as Period;
 *         source alarm as Alarm indexed by name as String;
 *         action ScheduleAlarm;
 *         action Vibrate;
 * }
</pre>
 */
public abstract class AbstractFitbit extends Service {
    
    public AbstractFitbit(ServiceConfiguration serviceConfiguration,
            java.lang.String idValue) {
        super("/Device/Device/Fitbit/", serviceConfiguration);
        updateId(idValue);
    }
    
    // Methods from the Service class
    @Override
    public final java.util.Map<String, Object> getAttributesCalled(java.util.Map<String, Object> properties, RemoteServiceInfo source) {
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put("id", _id);
        return attributes;
    }
    
    @Override
    protected final void internalPostInitialize() {
        postInitialize();
    }
    
    @SuppressWarnings("all")
    @Override
    public final Object orderCalled(java.util.Map<String, Object> properties, RemoteServiceInfo source, String orderName, Object... args) throws Exception {
        if (orderName.equals("removeAlarm")) {
            removeAlarm((java.lang.String) args[0]);
            return null;
        }
        if (orderName.equals("scheduleAlarm")) {
            scheduleAlarm((fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm) args[0]);
            return null;
        }
        if (orderName.equals("vibrateAt")) {
            vibrateAt((fr.inria.phoenix.diasuite.framework.datatype.date.Date) args[0]);
            return null;
        }
        if (orderName.equals("vibrateIn")) {
            vibrateIn((java.lang.Integer) args[0]);
            return null;
        }
        throw new InvocationException("Undefined method name " + orderName);
    }
    
    @Override
    public Object getValueCalled(java.util.Map<String, Object> properties, RemoteServiceInfo source, String orderName, Object... args) throws Exception {
        if (orderName.equals("id")) {
            return _id;
        }
        if (orderName.equals("alarm")) {
            return getAlarm((java.lang.String) args[0]);
        }
        if (orderName.equals("calories")) {
            return getCalories((fr.inria.phoenix.diasuite.framework.datatype.period.Period) args[0]);
        }
        if (orderName.equals("distanceInMeters")) {
            return getDistanceInMeters((fr.inria.phoenix.diasuite.framework.datatype.period.Period) args[0]);
        }
        if (orderName.equals("isAlive")) {
            return getIsAlive();
        }
        if (orderName.equals("pulses")) {
            return getPulses((fr.inria.phoenix.diasuite.framework.datatype.period.Period) args[0]);
        }
        if (orderName.equals("sleepPeriods")) {
            return getSleepPeriods((fr.inria.phoenix.diasuite.framework.datatype.period.Period) args[0]);
        }
        if (orderName.equals("steps")) {
            return getSteps((fr.inria.phoenix.diasuite.framework.datatype.period.Period) args[0]);
        }
        throw new InvocationException("Undefined method name " + orderName);
    }
    // End of methods from the Service class
    
    // Code for the attribute id from device Device
    private java.lang.String _id;
    
    /**
     * Set the value of the <code>id</code> attribute from device <code>Device</code>.
    
    <pre>
    attribute id as String;
    </pre>
    @param newIdValue the new value of <code>id</code>
     */
    protected void updateId(java.lang.String newIdValue) {
        if (_id != newIdValue) {
            _id = newIdValue;
            updateAttribute("id", newIdValue);
        }
    }
    
    /**
     * Returns the value of the <code>id</code> attribute from device <code>Device</code>.
    
    <pre>
    attribute id as String;
    </pre>
    @return the value of <code>id</code>
     */
    public java.lang.String getId() {
        return _id;
    }
    // End of code for the attribute id from device Device
    
    // Code for source alarm from device Fitbit
    private java.util.HashMap<AlarmIndices, fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm> _alarm = new java.util.HashMap<AlarmIndices, fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm>();
    
    /**
     * Publish the value of source <code>alarm</code> from device <code>Fitbit</code>.
    <p>
    alarm
    
    <pre>
    source alarm as Alarm indexed by name as String;
    </pre>
    @param newAlarmValue the new value for the source <code>alarm</code>
    @param name the value of the index <code>name</code>
     */
    protected void publishAlarm(fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm newAlarmValue,
            java.lang.String name) {
        AlarmIndices _indices_ = new AlarmIndices(name);
        _alarm.put(_indices_, newAlarmValue);
        getProcessor().publishValue(getOutProperties(), "alarm", newAlarmValue, name); 
    }
    
    /**
     * Returns the value of source <code>alarm</code> from device <code>Fitbit</code>.
    <p>
    alarm
    
    <pre>
    source alarm as Alarm indexed by name as String;
    </pre>
    @param name the value of the index <code>name</code>
    @return the value of the source <code>alarm</code>
     */
    protected fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm getAlarm(java.lang.String name) throws Exception {
        return _alarm.get(new AlarmIndices(name));
    }
    // End of code for source alarm from device Fitbit
    
    // Code for source calories from device Fitbit
    private java.util.HashMap<CaloriesIndices, java.lang.Integer> _calories = new java.util.HashMap<CaloriesIndices, java.lang.Integer>();
    
    /**
     * Publish the value of source <code>calories</code> from device <code>Fitbit</code>.
    
    <pre>
    source calories as Integer indexed by period as Period;
    </pre>
    @param newCaloriesValue the new value for the source <code>calories</code>
    @param period the value of the index <code>period</code>
     */
    protected void publishCalories(java.lang.Integer newCaloriesValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        CaloriesIndices _indices_ = new CaloriesIndices(period);
        _calories.put(_indices_, newCaloriesValue);
        getProcessor().publishValue(getOutProperties(), "calories", newCaloriesValue, period); 
    }
    
    /**
     * Returns the value of source <code>calories</code> from device <code>Fitbit</code>.
    
    <pre>
    source calories as Integer indexed by period as Period;
    </pre>
    @param period the value of the index <code>period</code>
    @return the value of the source <code>calories</code>
     */
    protected java.lang.Integer getCalories(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
        return _calories.get(new CaloriesIndices(period));
    }
    // End of code for source calories from device Fitbit
    
    // Code for source distanceInMeters from device Fitbit
    private java.util.HashMap<DistanceInMetersIndices, java.lang.Integer> _distanceInMeters = new java.util.HashMap<DistanceInMetersIndices, java.lang.Integer>();
    
    /**
     * Publish the value of source <code>distanceInMeters</code> from device <code>Fitbit</code>.
    
    <pre>
    source distanceInMeters as Integer indexed by period as Period;
    </pre>
    @param newDistanceInMetersValue the new value for the source <code>distanceInMeters</code>
    @param period the value of the index <code>period</code>
     */
    protected void publishDistanceInMeters(java.lang.Integer newDistanceInMetersValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        DistanceInMetersIndices _indices_ = new DistanceInMetersIndices(period);
        _distanceInMeters.put(_indices_, newDistanceInMetersValue);
        getProcessor().publishValue(getOutProperties(), "distanceInMeters", newDistanceInMetersValue, period); 
    }
    
    /**
     * Returns the value of source <code>distanceInMeters</code> from device <code>Fitbit</code>.
    
    <pre>
    source distanceInMeters as Integer indexed by period as Period;
    </pre>
    @param period the value of the index <code>period</code>
    @return the value of the source <code>distanceInMeters</code>
     */
    protected java.lang.Integer getDistanceInMeters(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
        return _distanceInMeters.get(new DistanceInMetersIndices(period));
    }
    // End of code for source distanceInMeters from device Fitbit
    
    // Code for source isAlive from device Device
    private java.lang.Boolean _isAlive;
    
    /**
     * Publish the value of source <code>isAlive</code> from device <code>Device</code>.
    
    <pre>
    source isAlive as Boolean;
    </pre>
    @param newIsAliveValue the new value for the source <code>isAlive</code>
     */
    protected void publishIsAlive(java.lang.Boolean newIsAliveValue) {
        _isAlive = newIsAliveValue;
        getProcessor().publishValue(getOutProperties(), "isAlive", newIsAliveValue); 
    }
    
    /**
     * Returns the value of source <code>isAlive</code> from device <code>Device</code>.
    
    <pre>
    source isAlive as Boolean;
    </pre>
    @return the value of the source <code>isAlive</code>
     */
    protected java.lang.Boolean getIsAlive() throws Exception {
        return _isAlive;
    }
    // End of code for source isAlive from device Device
    
    // Code for source pulses from device Fitbit
    private java.util.HashMap<PulsesIndices, fr.inria.phoenix.diasuite.framework.datatype.pulse.Pulse> _pulses = new java.util.HashMap<PulsesIndices, fr.inria.phoenix.diasuite.framework.datatype.pulse.Pulse>();
    
    /**
     * Publish the value of source <code>pulses</code> from device <code>Fitbit</code>.
    
    <pre>
    source pulses as Pulse indexed by period as Period;
    </pre>
    @param newPulsesValue the new value for the source <code>pulses</code>
    @param period the value of the index <code>period</code>
     */
    protected void publishPulses(fr.inria.phoenix.diasuite.framework.datatype.pulse.Pulse newPulsesValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        PulsesIndices _indices_ = new PulsesIndices(period);
        _pulses.put(_indices_, newPulsesValue);
        getProcessor().publishValue(getOutProperties(), "pulses", newPulsesValue, period); 
    }
    
    /**
     * Returns the value of source <code>pulses</code> from device <code>Fitbit</code>.
    
    <pre>
    source pulses as Pulse indexed by period as Period;
    </pre>
    @param period the value of the index <code>period</code>
    @return the value of the source <code>pulses</code>
     */
    protected fr.inria.phoenix.diasuite.framework.datatype.pulse.Pulse getPulses(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
        return _pulses.get(new PulsesIndices(period));
    }
    // End of code for source pulses from device Fitbit
    
    // Code for source sleepPeriods from device Fitbit
    private java.util.HashMap<SleepPeriodsIndices, java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>> _sleepPeriods = new java.util.HashMap<SleepPeriodsIndices, java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>>();
    
    /**
     * Publish the value of source <code>sleepPeriods</code> from device <code>Fitbit</code>.
    
    <pre>
    source sleepPeriods as SleepPeriod [] indexed by period as Period;
    </pre>
    @param newSleepPeriodsValue the new value for the source <code>sleepPeriods</code>
    @param period the value of the index <code>period</code>
     */
    protected void publishSleepPeriods(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> newSleepPeriodsValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        SleepPeriodsIndices _indices_ = new SleepPeriodsIndices(period);
        _sleepPeriods.put(_indices_, newSleepPeriodsValue);
        getProcessor().publishValue(getOutProperties(), "sleepPeriods", newSleepPeriodsValue, period); 
    }
    
    /**
     * Returns the value of source <code>sleepPeriods</code> from device <code>Fitbit</code>.
    
    <pre>
    source sleepPeriods as SleepPeriod [] indexed by period as Period;
    </pre>
    @param period the value of the index <code>period</code>
    @return the value of the source <code>sleepPeriods</code>
     */
    protected java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> getSleepPeriods(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
        return _sleepPeriods.get(new SleepPeriodsIndices(period));
    }
    // End of code for source sleepPeriods from device Fitbit
    
    // Code for source steps from device Fitbit
    private java.util.HashMap<StepsIndices, java.lang.Integer> _steps = new java.util.HashMap<StepsIndices, java.lang.Integer>();
    
    /**
     * Publish the value of source <code>steps</code> from device <code>Fitbit</code>.
    
    <pre>
    source steps as Integer indexed by period as Period;
    </pre>
    @param newStepsValue the new value for the source <code>steps</code>
    @param period the value of the index <code>period</code>
     */
    protected void publishSteps(java.lang.Integer newStepsValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        StepsIndices _indices_ = new StepsIndices(period);
        _steps.put(_indices_, newStepsValue);
        getProcessor().publishValue(getOutProperties(), "steps", newStepsValue, period); 
    }
    
    /**
     * Returns the value of source <code>steps</code> from device <code>Fitbit</code>.
    
    <pre>
    source steps as Integer indexed by period as Period;
    </pre>
    @param period the value of the index <code>period</code>
    @return the value of the source <code>steps</code>
     */
    protected java.lang.Integer getSteps(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
        return _steps.get(new StepsIndices(period));
    }
    // End of code for source steps from device Fitbit
    
    /**
     * Implement this method to define the <code>removeAlarm</code> order from the <code>ScheduleAlarm</code> action
     * defined in device Fitbit.
     * 
    
    <pre>
    removeAlarm(name as String);
    </pre>
     * @param name parameter 1 of the order.
     */
    protected abstract void removeAlarm(java.lang.String name) throws Exception;
    
    /**
     * Implement this method to define the <code>scheduleAlarm</code> order from the <code>ScheduleAlarm</code> action
     * defined in device Fitbit.
     * 
    
    <pre>
    scheduleAlarm(alarm as Alarm);
    </pre>
     * @param alarm parameter 1 of the order.
     */
    protected abstract void scheduleAlarm(fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm alarm) throws Exception;
    
    /**
     * Implement this method to define the <code>vibrateAt</code> order from the <code>Vibrate</code> action
     * defined in device Fitbit.
     * 
    
    <pre>
    vibrateAt(date as Date);
    </pre>
     * @param date parameter 1 of the order.
     */
    protected abstract void vibrateAt(fr.inria.phoenix.diasuite.framework.datatype.date.Date date) throws Exception;
    
    /**
     * Implement this method to define the <code>vibrateIn</code> order from the <code>Vibrate</code> action
     * defined in device Fitbit.
     * 
    
    <pre>
    vibrateIn(delayInMinutes as Integer);
    </pre>
     * @param delayInMinutes parameter 1 of the order.
     */
    protected abstract void vibrateIn(java.lang.Integer delayInMinutes) throws Exception;
}
