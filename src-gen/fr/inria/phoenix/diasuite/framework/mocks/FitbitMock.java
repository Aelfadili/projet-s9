package fr.inria.phoenix.diasuite.framework.mocks;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.phoenix.diasuite.framework.device.fitbit.AbstractFitbit;
import fr.inria.phoenix.diasuite.framework.device.fitbit.PulsesIndices;
import fr.inria.phoenix.diasuite.framework.device.fitbit.CaloriesIndices;
import fr.inria.phoenix.diasuite.framework.device.fitbit.DistanceInMetersIndices;
import fr.inria.phoenix.diasuite.framework.device.fitbit.StepsIndices;
import fr.inria.phoenix.diasuite.framework.device.fitbit.HeartActivityIndices;
import fr.inria.phoenix.diasuite.framework.device.fitbit.SleepPeriodsIndices;
import fr.inria.phoenix.diasuite.framework.device.fitbit.PhysiologicalActivitiesIndices;
import fr.inria.phoenix.diasuite.framework.device.fitbit.AlarmIndices;

// @internal
public final class FitbitMock {
    
    /**
     * The timeout in milliseconds to wait action. Set it if your application computations are long.
     */
    public long TIMEOUT = Mock.TIMEOUT;
    
    class Proxy extends AbstractFitbit {
        private Proxy(ServiceConfiguration serviceConfiguration,
                java.lang.String idValue) {
            super(serviceConfiguration, idValue);
        }
        
        public void _updateId(java.lang.String newIdValue) {
            updateId(newIdValue);
        }
        
        protected fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm getAlarm(java.lang.String name) throws Exception {
            fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm result = __alarm.get(new AlarmIndices(name));
            if(result == null)
                return super.getAlarm(name);
            return result;
        }
        
        public void _publishAlarm(fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm newAlarmValue,
                java.lang.String name) {
            publishAlarm(newAlarmValue,
                name);
        }
        
        protected java.lang.Integer getCalories(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
            java.lang.Integer result = __calories.get(new CaloriesIndices(period));
            if(result == null)
                return super.getCalories(period);
            return result;
        }
        
        public void _publishCalories(java.lang.Integer newCaloriesValue,
                fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
            publishCalories(newCaloriesValue,
                period);
        }
        
        protected java.lang.Integer getDistanceInMeters(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
            java.lang.Integer result = __distanceInMeters.get(new DistanceInMetersIndices(period));
            if(result == null)
                return super.getDistanceInMeters(period);
            return result;
        }
        
        public void _publishDistanceInMeters(java.lang.Integer newDistanceInMetersValue,
                fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
            publishDistanceInMeters(newDistanceInMetersValue,
                period);
        }
        
        protected fr.inria.phoenix.diasuite.framework.datatype.heartactivity.HeartActivity getHeartActivity(fr.inria.phoenix.diasuite.framework.datatype.period.Period period,
                fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate heartZone) throws Exception {
            fr.inria.phoenix.diasuite.framework.datatype.heartactivity.HeartActivity result = __heartActivity.get(new HeartActivityIndices(period, heartZone));
            if(result == null)
                return super.getHeartActivity(period, heartZone);
            return result;
        }
        
        public void _publishHeartActivity(fr.inria.phoenix.diasuite.framework.datatype.heartactivity.HeartActivity newHeartActivityValue,
                fr.inria.phoenix.diasuite.framework.datatype.period.Period period,
                fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate heartZone) {
            publishHeartActivity(newHeartActivityValue,
                period,
                heartZone);
        }
        
        protected java.lang.Boolean getIsAlive() throws Exception {
            java.lang.Boolean result = __isAlive;
            if(result == null)
                return super.getIsAlive();
            return result;
        }
        
        public void _publishIsAlive(java.lang.Boolean newIsAliveValue) {
            publishIsAlive(newIsAliveValue);
        }
        
        protected fr.inria.phoenix.diasuite.framework.datatype.date.Date getLastSynchronization() throws Exception {
            fr.inria.phoenix.diasuite.framework.datatype.date.Date result = __lastSynchronization;
            if(result == null)
                return super.getLastSynchronization();
            return result;
        }
        
        public void _publishLastSynchronization(fr.inria.phoenix.diasuite.framework.datatype.date.Date newLastSynchronizationValue) {
            publishLastSynchronization(newLastSynchronizationValue);
        }
        
        protected java.util.List<fr.inria.phoenix.diasuite.framework.datatype.physiologicalactivity.PhysiologicalActivity> getPhysiologicalActivities(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
            java.util.List<fr.inria.phoenix.diasuite.framework.datatype.physiologicalactivity.PhysiologicalActivity> result = __physiologicalActivities.get(new PhysiologicalActivitiesIndices(period));
            if(result == null)
                return super.getPhysiologicalActivities(period);
            return result;
        }
        
        public void _publishPhysiologicalActivities(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.physiologicalactivity.PhysiologicalActivity> newPhysiologicalActivitiesValue,
                fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
            publishPhysiologicalActivities(newPhysiologicalActivitiesValue,
                period);
        }
        
        protected fr.inria.phoenix.diasuite.framework.datatype.pulse.Pulse getPulses(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
            fr.inria.phoenix.diasuite.framework.datatype.pulse.Pulse result = __pulses.get(new PulsesIndices(period));
            if(result == null)
                return super.getPulses(period);
            return result;
        }
        
        public void _publishPulses(fr.inria.phoenix.diasuite.framework.datatype.pulse.Pulse newPulsesValue,
                fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
            publishPulses(newPulsesValue,
                period);
        }
        
        protected java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> getSleepPeriods(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
            java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> result = __sleepPeriods.get(new SleepPeriodsIndices(period));
            if(result == null)
                return super.getSleepPeriods(period);
            return result;
        }
        
        public void _publishSleepPeriods(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> newSleepPeriodsValue,
                fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
            publishSleepPeriods(newSleepPeriodsValue,
                period);
        }
        
        protected java.lang.Integer getSteps(fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
            java.lang.Integer result = __steps.get(new StepsIndices(period));
            if(result == null)
                return super.getSteps(period);
            return result;
        }
        
        public void _publishSteps(java.lang.Integer newStepsValue,
                fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
            publishSteps(newStepsValue,
                period);
        }
        
        protected void registerPhysiologicalDailyActivity(fr.inria.phoenix.diasuite.framework.datatype.dailyactivityname.DailyActivityName name,
                fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
            synchronized(__actions) {
                java.util.Queue<Object> __action = new java.util.LinkedList<Object>();
                __action.add("registerPhysiologicalDailyActivity");
                __action.add(name);
                __action.add(period);
                __actions.add(__action);
                __actions.notifyAll();
             }
        }
        
        protected void registerPhysiologicalPeriodActivity(fr.inria.phoenix.diasuite.framework.datatype.periodactivityname.PeriodActivityName name,
                fr.inria.phoenix.diasuite.framework.datatype.period.Period period) throws Exception {
            synchronized(__actions) {
                java.util.Queue<Object> __action = new java.util.LinkedList<Object>();
                __action.add("registerPhysiologicalPeriodActivity");
                __action.add(name);
                __action.add(period);
                __actions.add(__action);
                __actions.notifyAll();
             }
        }
        
        protected void removeAlarm(java.lang.String name) throws Exception {
            synchronized(__actions) {
                java.util.Queue<Object> __action = new java.util.LinkedList<Object>();
                __action.add("removeAlarm");
                __action.add(name);
                __actions.add(__action);
                __actions.notifyAll();
             }
        }
        
        protected void scheduleAlarm(fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm alarm) throws Exception {
            synchronized(__actions) {
                java.util.Queue<Object> __action = new java.util.LinkedList<Object>();
                __action.add("scheduleAlarm");
                __action.add(alarm);
                __actions.add(__action);
                __actions.notifyAll();
             }
        }
        
        protected void storePhysiologicalActivity(fr.inria.phoenix.diasuite.framework.datatype.physiologicalactivity.PhysiologicalActivity physioActivity) throws Exception {
            synchronized(__actions) {
                java.util.Queue<Object> __action = new java.util.LinkedList<Object>();
                __action.add("storePhysiologicalActivity");
                __action.add(physioActivity);
                __actions.add(__action);
                __actions.notifyAll();
             }
        }
        
        protected void vibrateAt(fr.inria.phoenix.diasuite.framework.datatype.date.Date date) throws Exception {
            synchronized(__actions) {
                java.util.Queue<Object> __action = new java.util.LinkedList<Object>();
                __action.add("vibrateAt");
                __action.add(date);
                __actions.add(__action);
                __actions.notifyAll();
             }
        }
        
        protected void vibrateIn(java.lang.Integer delayInMinutes) throws Exception {
            synchronized(__actions) {
                java.util.Queue<Object> __action = new java.util.LinkedList<Object>();
                __action.add("vibrateIn");
                __action.add(delayInMinutes);
                __actions.add(__action);
                __actions.notifyAll();
             }
        }
    }
    
    Proxy proxy;
    
    FitbitMock(ServiceConfiguration serviceConfiguration,
            java.lang.String idValue) {
        this.proxy = new Proxy(serviceConfiguration, idValue);
    }
    
    
    // Code for the attribute id from device Device
    /**
     * Set the value of the <code>id</code> attribute from device <code>Device</code>.
     * 
     * <pre>
     * attribute id as String;
     * </pre>
     * @param newIdValue the new value of <code>id</code>
     * @return this for fluent interface
     */
    public FitbitMock id(java.lang.String newIdValue) {
        proxy._updateId(newIdValue);
        return this;
    }
    // End of code for the attribute id from device Device
    
    // Code for source alarm from device Fitbit
    /**
     * Publish the value of source <code>alarm</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source alarm as Alarm indexed by name as String;
     * </pre>
     * @param newAlarmValue the new value for the source <code>alarm</code>
     * @param name the value of the index <code>name</code>
     * @return this for fluent interface
     */
    public FitbitMock alarm(fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm newAlarmValue,
            java.lang.String name) {
        proxy._publishAlarm(newAlarmValue,
            name);
        return this;
    }
    
    private java.util.HashMap<AlarmIndices, fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm> __alarm = new java.util.HashMap<AlarmIndices, fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm>();
    /**
     * Set the value (without publication) of source <code>alarm</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source alarm as Alarm indexed by name as String;
     * </pre>
     * @param newAlarmValue the new value for the source <code>alarm</code>
     * @param name the value of the index <code>name</code>
     * @return this for fluent interface
     */
    public FitbitMock setAlarm(fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm newAlarmValue,
            java.lang.String name) {
        AlarmIndices _indices_ = new AlarmIndices(name);
        __alarm.put(_indices_, newAlarmValue);
        return this;
    }
    // End of code for source alarm from device Fitbit
    
    // Code for source calories from device Fitbit
    /**
     * Publish the value of source <code>calories</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source calories as Integer indexed by period as Period;
     * </pre>
     * @param newCaloriesValue the new value for the source <code>calories</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock calories(java.lang.Integer newCaloriesValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        proxy._publishCalories(newCaloriesValue,
            period);
        return this;
    }
    
    private java.util.HashMap<CaloriesIndices, java.lang.Integer> __calories = new java.util.HashMap<CaloriesIndices, java.lang.Integer>();
    /**
     * Set the value (without publication) of source <code>calories</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source calories as Integer indexed by period as Period;
     * </pre>
     * @param newCaloriesValue the new value for the source <code>calories</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock setCalories(java.lang.Integer newCaloriesValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        CaloriesIndices _indices_ = new CaloriesIndices(period);
        __calories.put(_indices_, newCaloriesValue);
        return this;
    }
    // End of code for source calories from device Fitbit
    
    // Code for source distanceInMeters from device Fitbit
    /**
     * Publish the value of source <code>distanceInMeters</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source distanceInMeters as Integer indexed by period as Period;
     * </pre>
     * @param newDistanceInMetersValue the new value for the source <code>distanceInMeters</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock distanceInMeters(java.lang.Integer newDistanceInMetersValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        proxy._publishDistanceInMeters(newDistanceInMetersValue,
            period);
        return this;
    }
    
    private java.util.HashMap<DistanceInMetersIndices, java.lang.Integer> __distanceInMeters = new java.util.HashMap<DistanceInMetersIndices, java.lang.Integer>();
    /**
     * Set the value (without publication) of source <code>distanceInMeters</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source distanceInMeters as Integer indexed by period as Period;
     * </pre>
     * @param newDistanceInMetersValue the new value for the source <code>distanceInMeters</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock setDistanceInMeters(java.lang.Integer newDistanceInMetersValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        DistanceInMetersIndices _indices_ = new DistanceInMetersIndices(period);
        __distanceInMeters.put(_indices_, newDistanceInMetersValue);
        return this;
    }
    // End of code for source distanceInMeters from device Fitbit
    
    // Code for source heartActivity from device Fitbit
    /**
     * Publish the value of source <code>heartActivity</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source heartActivity as HeartActivity indexed by period as Period, heartZone as HeartRate;
     * </pre>
     * @param newHeartActivityValue the new value for the source <code>heartActivity</code>
     * @param period the value of the index <code>period</code>
     * @param heartZone the value of the index <code>heartZone</code>
     * @return this for fluent interface
     */
    public FitbitMock heartActivity(fr.inria.phoenix.diasuite.framework.datatype.heartactivity.HeartActivity newHeartActivityValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period,
            fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate heartZone) {
        proxy._publishHeartActivity(newHeartActivityValue,
            period,
            heartZone);
        return this;
    }
    
    private java.util.HashMap<HeartActivityIndices, fr.inria.phoenix.diasuite.framework.datatype.heartactivity.HeartActivity> __heartActivity = new java.util.HashMap<HeartActivityIndices, fr.inria.phoenix.diasuite.framework.datatype.heartactivity.HeartActivity>();
    /**
     * Set the value (without publication) of source <code>heartActivity</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source heartActivity as HeartActivity indexed by period as Period, heartZone as HeartRate;
     * </pre>
     * @param newHeartActivityValue the new value for the source <code>heartActivity</code>
     * @param period the value of the index <code>period</code>
     * @param heartZone the value of the index <code>heartZone</code>
     * @return this for fluent interface
     */
    public FitbitMock setHeartActivity(fr.inria.phoenix.diasuite.framework.datatype.heartactivity.HeartActivity newHeartActivityValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period,
            fr.inria.phoenix.diasuite.framework.datatype.heartrate.HeartRate heartZone) {
        HeartActivityIndices _indices_ = new HeartActivityIndices(period, heartZone);
        __heartActivity.put(_indices_, newHeartActivityValue);
        return this;
    }
    // End of code for source heartActivity from device Fitbit
    
    // Code for source isAlive from device Device
    /**
     * Publish the value of source <code>isAlive</code> from device <code>Device</code>.
     * 
     * <pre>
     * source isAlive as Boolean;
     * </pre>
     * @param newIsAliveValue the new value for the source <code>isAlive</code>
     * @return this for fluent interface
     */
    public FitbitMock isAlive(java.lang.Boolean newIsAliveValue) {
        proxy._publishIsAlive(newIsAliveValue);
        return this;
    }
    
    private java.lang.Boolean __isAlive = null;
    /**
     * Set the value (without publication) of source <code>isAlive</code> from device <code>Device</code>.
     * 
     * <pre>
     * source isAlive as Boolean;
     * </pre>
     * @param newIsAliveValue the new value for the source <code>isAlive</code>
     * @return this for fluent interface
     */
    public FitbitMock setIsAlive(java.lang.Boolean newIsAliveValue) {
        __isAlive = newIsAliveValue;
        return this;
    }
    // End of code for source isAlive from device Device
    
    // Code for source lastSynchronization from device Fitbit
    /**
     * Publish the value of source <code>lastSynchronization</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source lastSynchronization as Date;
     * </pre>
     * @param newLastSynchronizationValue the new value for the source <code>lastSynchronization</code>
     * @return this for fluent interface
     */
    public FitbitMock lastSynchronization(fr.inria.phoenix.diasuite.framework.datatype.date.Date newLastSynchronizationValue) {
        proxy._publishLastSynchronization(newLastSynchronizationValue);
        return this;
    }
    
    private fr.inria.phoenix.diasuite.framework.datatype.date.Date __lastSynchronization = null;
    /**
     * Set the value (without publication) of source <code>lastSynchronization</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source lastSynchronization as Date;
     * </pre>
     * @param newLastSynchronizationValue the new value for the source <code>lastSynchronization</code>
     * @return this for fluent interface
     */
    public FitbitMock setLastSynchronization(fr.inria.phoenix.diasuite.framework.datatype.date.Date newLastSynchronizationValue) {
        __lastSynchronization = newLastSynchronizationValue;
        return this;
    }
    // End of code for source lastSynchronization from device Fitbit
    
    // Code for source physiologicalActivities from device Fitbit
    /**
     * Publish the value of source <code>physiologicalActivities</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source physiologicalActivities as PhysiologicalActivity [] indexed by period as Period;
     * </pre>
     * @param newPhysiologicalActivitiesValue the new value for the source <code>physiologicalActivities</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock physiologicalActivities(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.physiologicalactivity.PhysiologicalActivity> newPhysiologicalActivitiesValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        proxy._publishPhysiologicalActivities(newPhysiologicalActivitiesValue,
            period);
        return this;
    }
    
    private java.util.HashMap<PhysiologicalActivitiesIndices, java.util.List<fr.inria.phoenix.diasuite.framework.datatype.physiologicalactivity.PhysiologicalActivity>> __physiologicalActivities = new java.util.HashMap<PhysiologicalActivitiesIndices, java.util.List<fr.inria.phoenix.diasuite.framework.datatype.physiologicalactivity.PhysiologicalActivity>>();
    /**
     * Set the value (without publication) of source <code>physiologicalActivities</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source physiologicalActivities as PhysiologicalActivity [] indexed by period as Period;
     * </pre>
     * @param newPhysiologicalActivitiesValue the new value for the source <code>physiologicalActivities</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock setPhysiologicalActivities(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.physiologicalactivity.PhysiologicalActivity> newPhysiologicalActivitiesValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        PhysiologicalActivitiesIndices _indices_ = new PhysiologicalActivitiesIndices(period);
        __physiologicalActivities.put(_indices_, newPhysiologicalActivitiesValue);
        return this;
    }
    // End of code for source physiologicalActivities from device Fitbit
    
    // Code for source pulses from device Fitbit
    /**
     * Publish the value of source <code>pulses</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source pulses as Pulse indexed by period as Period;
     * </pre>
     * @param newPulsesValue the new value for the source <code>pulses</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock pulses(fr.inria.phoenix.diasuite.framework.datatype.pulse.Pulse newPulsesValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        proxy._publishPulses(newPulsesValue,
            period);
        return this;
    }
    
    private java.util.HashMap<PulsesIndices, fr.inria.phoenix.diasuite.framework.datatype.pulse.Pulse> __pulses = new java.util.HashMap<PulsesIndices, fr.inria.phoenix.diasuite.framework.datatype.pulse.Pulse>();
    /**
     * Set the value (without publication) of source <code>pulses</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source pulses as Pulse indexed by period as Period;
     * </pre>
     * @param newPulsesValue the new value for the source <code>pulses</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock setPulses(fr.inria.phoenix.diasuite.framework.datatype.pulse.Pulse newPulsesValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        PulsesIndices _indices_ = new PulsesIndices(period);
        __pulses.put(_indices_, newPulsesValue);
        return this;
    }
    // End of code for source pulses from device Fitbit
    
    // Code for source sleepPeriods from device Fitbit
    /**
     * Publish the value of source <code>sleepPeriods</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source sleepPeriods as SleepPeriod [] indexed by period as Period;
     * </pre>
     * @param newSleepPeriodsValue the new value for the source <code>sleepPeriods</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock sleepPeriods(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> newSleepPeriodsValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        proxy._publishSleepPeriods(newSleepPeriodsValue,
            period);
        return this;
    }
    
    private java.util.HashMap<SleepPeriodsIndices, java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>> __sleepPeriods = new java.util.HashMap<SleepPeriodsIndices, java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod>>();
    /**
     * Set the value (without publication) of source <code>sleepPeriods</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source sleepPeriods as SleepPeriod [] indexed by period as Period;
     * </pre>
     * @param newSleepPeriodsValue the new value for the source <code>sleepPeriods</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock setSleepPeriods(java.util.List<fr.inria.phoenix.diasuite.framework.datatype.sleepperiod.SleepPeriod> newSleepPeriodsValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        SleepPeriodsIndices _indices_ = new SleepPeriodsIndices(period);
        __sleepPeriods.put(_indices_, newSleepPeriodsValue);
        return this;
    }
    // End of code for source sleepPeriods from device Fitbit
    
    // Code for source steps from device Fitbit
    /**
     * Publish the value of source <code>steps</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source steps as Integer indexed by period as Period;
     * </pre>
     * @param newStepsValue the new value for the source <code>steps</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock steps(java.lang.Integer newStepsValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        proxy._publishSteps(newStepsValue,
            period);
        return this;
    }
    
    private java.util.HashMap<StepsIndices, java.lang.Integer> __steps = new java.util.HashMap<StepsIndices, java.lang.Integer>();
    /**
     * Set the value (without publication) of source <code>steps</code> from device <code>Fitbit</code>.
     * 
     * <pre>
     * source steps as Integer indexed by period as Period;
     * </pre>
     * @param newStepsValue the new value for the source <code>steps</code>
     * @param period the value of the index <code>period</code>
     * @return this for fluent interface
     */
    public FitbitMock setSteps(java.lang.Integer newStepsValue,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        StepsIndices _indices_ = new StepsIndices(period);
        __steps.put(_indices_, newStepsValue);
        return this;
    }
    // End of code for source steps from device Fitbit
    
    private java.util.Queue<java.util.Queue<Object>> __actions = new java.util.LinkedList<java.util.Queue<Object>>();
    
    /**
     * Check that the <code>registerPhysiologicalDailyActivity</code> order from the <code>RegisterPhysiologicalActivity</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * registerPhysiologicalDailyActivity(name as DailyActivityName, period as Period);
     * </pre>    @return true if the action happened with the given parameters, remaining parameters are ignored
     */
    public boolean expectRegisterPhysiologicalDailyActivity() {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("registerPhysiologicalDailyActivity".equals(__action.poll())))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    /**
     * Check that the <code>registerPhysiologicalDailyActivity</code> order from the <code>RegisterPhysiologicalActivity</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * registerPhysiologicalDailyActivity(name as DailyActivityName, period as Period);
     * </pre>
     * @param name parameter 1 of the order.
     *     @return true if the action happened with the given parameters, remaining parameters are ignored
     */
    public boolean expectRegisterPhysiologicalDailyActivity(fr.inria.phoenix.diasuite.framework.datatype.dailyactivityname.DailyActivityName name) {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("registerPhysiologicalDailyActivity".equals(__action.poll())))
                    return false;
                if(!name.equals(__action.poll()))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    /**
     * Check that the <code>registerPhysiologicalDailyActivity</code> order from the <code>RegisterPhysiologicalActivity</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * registerPhysiologicalDailyActivity(name as DailyActivityName, period as Period);
     * </pre>
     * @param name parameter 1 of the order.
     * @param period parameter 2 of the order.
     *     @return true if the action happened with the given parameters
     */
    public boolean expectRegisterPhysiologicalDailyActivity(fr.inria.phoenix.diasuite.framework.datatype.dailyactivityname.DailyActivityName name,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("registerPhysiologicalDailyActivity".equals(__action.poll())))
                    return false;
                if(!name.equals(__action.poll()))
                    return false;
                if(!period.equals(__action.poll()))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    
    /**
     * Check that the <code>registerPhysiologicalPeriodActivity</code> order from the <code>RegisterPhysiologicalActivity</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * registerPhysiologicalPeriodActivity(name as PeriodActivityName, period as Period);
     * </pre>    @return true if the action happened with the given parameters, remaining parameters are ignored
     */
    public boolean expectRegisterPhysiologicalPeriodActivity() {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("registerPhysiologicalPeriodActivity".equals(__action.poll())))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    /**
     * Check that the <code>registerPhysiologicalPeriodActivity</code> order from the <code>RegisterPhysiologicalActivity</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * registerPhysiologicalPeriodActivity(name as PeriodActivityName, period as Period);
     * </pre>
     * @param name parameter 1 of the order.
     *     @return true if the action happened with the given parameters, remaining parameters are ignored
     */
    public boolean expectRegisterPhysiologicalPeriodActivity(fr.inria.phoenix.diasuite.framework.datatype.periodactivityname.PeriodActivityName name) {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("registerPhysiologicalPeriodActivity".equals(__action.poll())))
                    return false;
                if(!name.equals(__action.poll()))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    /**
     * Check that the <code>registerPhysiologicalPeriodActivity</code> order from the <code>RegisterPhysiologicalActivity</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * registerPhysiologicalPeriodActivity(name as PeriodActivityName, period as Period);
     * </pre>
     * @param name parameter 1 of the order.
     * @param period parameter 2 of the order.
     *     @return true if the action happened with the given parameters
     */
    public boolean expectRegisterPhysiologicalPeriodActivity(fr.inria.phoenix.diasuite.framework.datatype.periodactivityname.PeriodActivityName name,
            fr.inria.phoenix.diasuite.framework.datatype.period.Period period) {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("registerPhysiologicalPeriodActivity".equals(__action.poll())))
                    return false;
                if(!name.equals(__action.poll()))
                    return false;
                if(!period.equals(__action.poll()))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    
    /**
     * Check that the <code>removeAlarm</code> order from the <code>ScheduleAlarm</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * removeAlarm(name as String);
     * </pre>    @return true if the action happened with the given parameters, remaining parameters are ignored
     */
    public boolean expectRemoveAlarm() {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("removeAlarm".equals(__action.poll())))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    /**
     * Check that the <code>removeAlarm</code> order from the <code>ScheduleAlarm</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * removeAlarm(name as String);
     * </pre>
     * @param name parameter 1 of the order.
     *     @return true if the action happened with the given parameters
     */
    public boolean expectRemoveAlarm(java.lang.String name) {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("removeAlarm".equals(__action.poll())))
                    return false;
                if(!name.equals(__action.poll()))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    
    /**
     * Check that the <code>scheduleAlarm</code> order from the <code>ScheduleAlarm</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * scheduleAlarm(alarm as Alarm);
     * </pre>    @return true if the action happened with the given parameters, remaining parameters are ignored
     */
    public boolean expectScheduleAlarm() {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("scheduleAlarm".equals(__action.poll())))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    /**
     * Check that the <code>scheduleAlarm</code> order from the <code>ScheduleAlarm</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * scheduleAlarm(alarm as Alarm);
     * </pre>
     * @param alarm parameter 1 of the order.
     *     @return true if the action happened with the given parameters
     */
    public boolean expectScheduleAlarm(fr.inria.phoenix.diasuite.framework.datatype.alarm.Alarm alarm) {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("scheduleAlarm".equals(__action.poll())))
                    return false;
                if(!alarm.equals(__action.poll()))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    
    /**
     * Check that the <code>storePhysiologicalActivity</code> order from the <code>RegisterPhysiologicalActivity</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * storePhysiologicalActivity(physioActivity as PhysiologicalActivity);
     * </pre>    @return true if the action happened with the given parameters, remaining parameters are ignored
     */
    public boolean expectStorePhysiologicalActivity() {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("storePhysiologicalActivity".equals(__action.poll())))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    /**
     * Check that the <code>storePhysiologicalActivity</code> order from the <code>RegisterPhysiologicalActivity</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * storePhysiologicalActivity(physioActivity as PhysiologicalActivity);
     * </pre>
     * @param physioActivity parameter 1 of the order.
     *     @return true if the action happened with the given parameters
     */
    public boolean expectStorePhysiologicalActivity(fr.inria.phoenix.diasuite.framework.datatype.physiologicalactivity.PhysiologicalActivity physioActivity) {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("storePhysiologicalActivity".equals(__action.poll())))
                    return false;
                if(!physioActivity.equals(__action.poll()))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    
    /**
     * Check that the <code>vibrateAt</code> order from the <code>Vibrate</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * vibrateAt(date as Date);
     * </pre>    @return true if the action happened with the given parameters, remaining parameters are ignored
     */
    public boolean expectVibrateAt() {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("vibrateAt".equals(__action.poll())))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    /**
     * Check that the <code>vibrateAt</code> order from the <code>Vibrate</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * vibrateAt(date as Date);
     * </pre>
     * @param date parameter 1 of the order.
     *     @return true if the action happened with the given parameters
     */
    public boolean expectVibrateAt(fr.inria.phoenix.diasuite.framework.datatype.date.Date date) {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("vibrateAt".equals(__action.poll())))
                    return false;
                if(!date.equals(__action.poll()))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    
    /**
     * Check that the <code>vibrateIn</code> order from the <code>Vibrate</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * vibrateIn(delayInMinutes as Integer);
     * </pre>    @return true if the action happened with the given parameters, remaining parameters are ignored
     */
    public boolean expectVibrateIn() {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("vibrateIn".equals(__action.poll())))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
    /**
     * Check that the <code>vibrateIn</code> order from the <code>Vibrate</code> action
     * defined in device Fitbit was called.
     * 
     * 
     * <pre>
     * vibrateIn(delayInMinutes as Integer);
     * </pre>
     * @param delayInMinutes parameter 1 of the order.
     *     @return true if the action happened with the given parameters
     */
    public boolean expectVibrateIn(java.lang.Integer delayInMinutes) {
        try {
            synchronized(__actions) {
                java.util.Queue<Object> __action = __actions.poll();
                if(__action == null) {
                    __actions.wait(TIMEOUT);
                    __action = __actions.poll();
                }
                if(__action == null)
                    return false;
                if(!("vibrateIn".equals(__action.poll())))
                    return false;
                if(!delayInMinutes.equals(__action.poll()))
                    return false;
                return true;
            }
         } catch(InterruptedException e) {
             return false;
         }
    }
}
