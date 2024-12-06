package com.aionemu.commons.services.cron;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class RunnableRunner implements Job {
   public static final String KEY_RUNNABLE_OBJECT = "cronservice.scheduled.runnable.instance";
   public static final String KEY_PROPERTY_IS_LONGRUNNING_TASK = "cronservice.scheduled.runnable.islognrunning";
   public static final String KEY_CRON_EXPRESSION = "cronservice.scheduled.runnable.cronexpression";

   public void execute(JobExecutionContext context) throws JobExecutionException {
      JobDataMap jdm = context.getJobDetail().getJobDataMap();
      Runnable r = (Runnable)jdm.get("cronservice.scheduled.runnable.instance");
      boolean longRunning = jdm.getBoolean("cronservice.scheduled.runnable.islognrunning");
      if (longRunning) {
         this.executeLongRunningRunnable(r);
      } else {
         this.executeRunnable(r);
      }

   }

   public abstract void executeRunnable(Runnable var1);

   public abstract void executeLongRunningRunnable(Runnable var1);
}
