package com.nvarghese.beowulf.sfc.quartz;

import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class TimeBasedRepeatableTriggerBuilder {

	private final String triggerName;
	private final String triggerGroup;
	private final int intervalSeconds;

	public TimeBasedRepeatableTriggerBuilder(String triggerName, String triggerGroup, int intervalSeconds) {

		super();
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
		this.intervalSeconds = intervalSeconds;
	}

	public Trigger build() {

		Trigger trigger = newTrigger().withIdentity(triggerName, triggerGroup)
		// .startAt(DateBuilder.futureDate(1, IntervalUnit.MINUTE))
				.startNow().withSchedule(simpleSchedule().withIntervalInSeconds(intervalSeconds).repeatForever()).build();

		return trigger;

	}

}
