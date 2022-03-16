package com.capstone.parking.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.capstone.parking.model.ParkingSpaceCronJob;
import com.capstone.parking.service.ParkingSpaceService;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer, DisposableBean {
  @Autowired
  private ParkingSpaceService parkingSpaceService;
  ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

  public static boolean compareCronJob(final List<ParkingSpaceCronJob> x, final List<ParkingSpaceCronJob> y) {
    if (x == null) {
      return y == null;
    }

    if (x.size() != y.size()) {
      return false;
    }

    Set<ParkingSpaceCronJob> setX = new HashSet<>(x);
    Set<ParkingSpaceCronJob> cloneSetX = new HashSet<>(x);
    Set<ParkingSpaceCronJob> setY = new HashSet<>(y);
    Set<ParkingSpaceCronJob> cloneSetY = new HashSet<>(y);

    setX.removeAll(setY);
    cloneSetY.removeAll(cloneSetX);
    return setX.isEmpty() && cloneSetY.isEmpty();
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    parkingSpaceService.getCronJobArray().forEach(item -> System.out.println(item.toString()));

    List<ParkingSpaceCronJob> oldCronJobList = parkingSpaceService.getCronJobArray();
    oldCronJobList.forEach(cron -> {
      Runnable runnableTask = () -> System.out.println("Task executed at -> " + cron.getId());

      Trigger trigger = new Trigger() {
        @Override
        public Date nextExecutionTime(TriggerContext triggerContext) {
          List<ParkingSpaceCronJob> newCronJobList = parkingSpaceService.getCronJobArray();
          if (!compareCronJob(oldCronJobList, newCronJobList)) {
            taskRegistrar.setTriggerTasksList(new ArrayList<TriggerTask>());
            configureTasks(taskRegistrar); // calling recursively.
            taskRegistrar.destroy(); // destroys previously scheduled tasks.
            taskRegistrar.setScheduler(executor);
            taskRegistrar.afterPropertiesSet(); // this will schedule the task with new cron changes.
            return null; // return null when the cron changed so the trigger will stop.
          }
          CronTrigger crontrigger = new CronTrigger(cron.getCronExpression());
          return crontrigger.nextExecutionTime(triggerContext);
        }
      };
      taskRegistrar.addTriggerTask(runnableTask, trigger);
    });
  }

  @Override
  public void destroy() throws Exception {
    if (executor != null) {
      executor.shutdownNow();
    }
  }
}
