package com.aionemu.commons.services;

import com.aionemu.commons.services.cron.CronServiceException;
import com.aionemu.commons.services.cron.RunnableRunner;
import com.aionemu.commons.utils.GenericValidator;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CronService {
   private static final Logger log = LoggerFactory.getLogger(CronService.class);
   private static CronService instance;
   private Scheduler scheduler;
   private Class<? extends RunnableRunner> runnableRunner;

   public static CronService getInstance() {
      return instance;
   }

   public static synchronized void initSingleton(Class<? extends RunnableRunner> runableRunner) {
      if (instance != null) {
         throw new CronServiceException("CronService is already initialized");
      } else {
         CronService cs = new CronService();
         cs.init(runableRunner);
         instance = cs;
      }
   }

   private CronService() {
   }

   public synchronized void init(Class<? extends RunnableRunner> runnableRunner) {
      if (this.scheduler == null) {
         if (runnableRunner == null) {
            throw new CronServiceException("RunnableRunner class must be defined");
         } else {
            this.runnableRunner = runnableRunner;
            Properties properties = new Properties();
            properties.setProperty("org.quartz.threadPool.threadCount", "1");

            try {
               this.scheduler = (new StdSchedulerFactory(properties)).getScheduler();
               this.scheduler.start();
            } catch (SchedulerException var4) {
               throw new CronServiceException("Failed to initialize CronService", var4);
            }
         }
      }
   }

   public void shutdown() {
      Scheduler localScheduler;
      synchronized(this) {
         if (this.scheduler == null) {
            return;
         }

         localScheduler = this.scheduler;
         this.scheduler = null;
         this.runnableRunner = null;
      }

      try {
         localScheduler.shutdown(false);
      } catch (SchedulerException var4) {
         log.error("Failed to shutdown CronService correctly", var4);
      }

   }

   public void schedule(Runnable r, String cronExpression) {
      this.schedule(r, cronExpression, false);
   }

   public void schedule(Runnable r, String cronExpression, boolean longRunning) {
      try {
         JobDataMap jdm = new JobDataMap();
         jdm.put("cronservice.scheduled.runnable.instance", r);
         jdm.put("cronservice.scheduled.runnable.islognrunning", longRunning);
         jdm.put("cronservice.scheduled.runnable.cronexpression", cronExpression);
         String jobId = "Started at ms" + System.currentTimeMillis() + "; ns" + System.nanoTime();
         JobKey jobKey = new JobKey("JobKey:" + jobId);
         JobDetail jobDetail = JobBuilder.newJob(this.runnableRunner).usingJobData(jdm).withIdentity(jobKey).build();
         CronScheduleBuilder csb = CronScheduleBuilder.cronSchedule(cronExpression);
         CronTrigger trigger = (CronTrigger)TriggerBuilder.newTrigger().withSchedule(csb).build();
         this.scheduler.scheduleJob(jobDetail, trigger);
      } catch (Exception var10) {
         throw new CronServiceException("Failed to start job", var10);
      }
   }

   public void cancel(Runnable r) {
      Map<Runnable, JobDetail> map = this.getRunnables();
      JobDetail jd = (JobDetail)map.get(r);
      this.cancel(jd);
   }

   public void cancel(JobDetail jd) {
      if (jd != null) {
         if (jd.getKey() == null) {
            throw new CronServiceException("JobDetail should have JobKey");
         } else {
            try {
               this.scheduler.deleteJob(jd.getKey());
            } catch (SchedulerException var3) {
               throw new CronServiceException("Failed to delete Job", var3);
            }
         }
      }
   }

   protected Collection<JobDetail> getJobDetails() {
      if (this.scheduler == null) {
         return Collections.emptySet();
      } else {
         try {
            Set<JobKey> keys = this.scheduler.getJobKeys((GroupMatcher)null);
            if (GenericValidator.isBlankOrNull((Collection)keys)) {
               return Collections.emptySet();
            } else {
               Set<JobDetail> result = Sets.newHashSetWithExpectedSize(keys.size());
               Iterator i$ = keys.iterator();

               while(i$.hasNext()) {
                  JobKey jk = (JobKey)i$.next();
                  result.add(this.scheduler.getJobDetail(jk));
               }

               return result;
            }
         } catch (Exception var5) {
            throw new CronServiceException("Can't get all active job details", var5);
         }
      }
   }

   public Map<Runnable, JobDetail> getRunnables() {
      Collection<JobDetail> jobDetails = this.getJobDetails();
      if (GenericValidator.isBlankOrNull(jobDetails)) {
         return Collections.emptyMap();
      } else {
         Map<Runnable, JobDetail> result = Maps.newHashMap();
         Iterator i$ = jobDetails.iterator();

         while(i$.hasNext()) {
            JobDetail jd = (JobDetail)i$.next();
            if (!GenericValidator.isBlankOrNull((Map)jd.getJobDataMap()) && jd.getJobDataMap().containsKey("cronservice.scheduled.runnable.instance")) {
               result.put((Runnable)jd.getJobDataMap().get("cronservice.scheduled.runnable.instance"), jd);
            }
         }

         return Collections.unmodifiableMap(result);
      }
   }

   public List<? extends Trigger> getJobTriggers(JobDetail jd) {
      return this.getJobTriggers(jd.getKey());
   }

   public List<? extends Trigger> getJobTriggers(JobKey jk) {
      if (this.scheduler == null) {
         return Collections.emptyList();
      } else {
         try {
            return this.scheduler.getTriggersOfJob(jk);
         } catch (SchedulerException var3) {
            throw new CronServiceException("Can't get triggers for JobKey " + jk, var3);
         }
      }
   }
}
