package fr.inria.phoenix.scenario.coach_activity.context;


import java.util.Calendar;

import fr.inria.diagen.core.ServiceConfiguration;
import fr.inria.phoenix.diasuite.framework.context.congratulation.AbstractCongratulation;
import fr.inria.phoenix.diasuite.framework.datatype.criticalnotification.CriticalNotification;
import fr.inria.phoenix.diasuite.framework.datatype.date.Date;
import fr.inria.phoenix.diasuite.framework.datatype.period.Period;
import fr.inria.phoenix.diasuite.framework.device.clock.TickHourFromClock;

public class Congratulation<CongratulationValuePublishable> extends AbstractCongratulation {
	
	public int time = 20;
	public int step = 2000;
	public Date soir;
	public Congratulation(ServiceConfiguration serviceConfiguration) {
		super(serviceConfiguration);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected fr.inria.phoenix.diasuite.framework.context.congratulation.AbstractCongratulation.CongratulationValuePublishable onTickHourFromClock(TickHourFromClock tickHourFromClock,
			DiscoverForTickHourFromClock discover) {
		
		if ( tickHourFromClock.equals(time)) {// besoin ou pas? 
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date midnight = new Date(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
		
		Date now = midnight;
		now.setHour(time);
		int dailyStep = discover.fitbits().anyOne().getSteps(new Period(midnight, now));
		
		if (step < dailyStep) {
			  CriticalNotification notification = new CriticalNotification("Goalreached", "Congratulation", "Félicitation !!!  Vous avez atteint votre objectif journalier",null);
			// Return quelque chose??? 
		}
		}
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}