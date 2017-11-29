package fr.inria.phoenix.diasuite.framework.datatype.alarm;

import java.io.Serializable;

/**
 * <pre>
structure Alarm {
 * 	name as String;
 * 	date as Date;
 * 	minutesBetweenReminders as Integer;
 * 	reminders as Integer;
 * }
</pre>
 */
public class Alarm implements Serializable {
    private static final long serialVersionUID = 0;

    // Code for field name
    private java.lang.String name;
    
    /**
     * Returns the value of the name field.
    
    <pre>
    name as String
    </pre>
    @return the value of name
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Set the value of the name field.
    
    <pre>
    name as String
    </pre>
    @param name the new value of name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    // End of code for field name

    // Code for field date
    private fr.inria.phoenix.diasuite.framework.datatype.date.Date date;
    
    /**
     * Returns the value of the date field.
    
    <pre>
    date as Date
    </pre>
    @return the value of date
     */
    public fr.inria.phoenix.diasuite.framework.datatype.date.Date getDate() {
        return date;
    }
    
    /**
     * Set the value of the date field.
    
    <pre>
    date as Date
    </pre>
    @param date the new value of date
     */
    public void setDate(fr.inria.phoenix.diasuite.framework.datatype.date.Date date) {
        this.date = date;
    }
    // End of code for field date

    // Code for field minutesBetweenReminders
    private java.lang.Integer minutesBetweenReminders;
    
    /**
     * Returns the value of the minutesBetweenReminders field.
    
    <pre>
    minutesBetweenReminders as Integer
    </pre>
    @return the value of minutesBetweenReminders
     */
    public java.lang.Integer getMinutesBetweenReminders() {
        return minutesBetweenReminders;
    }
    
    /**
     * Set the value of the minutesBetweenReminders field.
    
    <pre>
    minutesBetweenReminders as Integer
    </pre>
    @param minutesBetweenReminders the new value of minutesBetweenReminders
     */
    public void setMinutesBetweenReminders(java.lang.Integer minutesBetweenReminders) {
        this.minutesBetweenReminders = minutesBetweenReminders;
    }
    // End of code for field minutesBetweenReminders

    // Code for field reminders
    private java.lang.Integer reminders;
    
    /**
     * Returns the value of the reminders field.
    
    <pre>
    reminders as Integer
    </pre>
    @return the value of reminders
     */
    public java.lang.Integer getReminders() {
        return reminders;
    }
    
    /**
     * Set the value of the reminders field.
    
    <pre>
    reminders as Integer
    </pre>
    @param reminders the new value of reminders
     */
    public void setReminders(java.lang.Integer reminders) {
        this.reminders = reminders;
    }
    // End of code for field reminders

    public Alarm() {
    }

    public Alarm(java.lang.String name,
            fr.inria.phoenix.diasuite.framework.datatype.date.Date date,
            java.lang.Integer minutesBetweenReminders,
            java.lang.Integer reminders) {
        this.name = name;
        this.date = date;
        this.minutesBetweenReminders = minutesBetweenReminders;
        this.reminders = reminders;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((minutesBetweenReminders == null) ? 0 : minutesBetweenReminders.hashCode());
        result = prime * result + ((reminders == null) ? 0 : reminders.hashCode());
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
        Alarm other = (Alarm) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (minutesBetweenReminders == null) {
            if (other.minutesBetweenReminders != null)
                return false;
        } else if (!minutesBetweenReminders.equals(other.minutesBetweenReminders))
            return false;
        if (reminders == null) {
            if (other.reminders != null)
                return false;
        } else if (!reminders.equals(other.reminders))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Alarm [" + 
            "name=" + name +", " + 
            "date=" + date +", " + 
            "minutesBetweenReminders=" + minutesBetweenReminders +", " + 
            "reminders=" + reminders +
        "]";
    }
}
