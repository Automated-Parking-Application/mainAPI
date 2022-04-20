package com.capstone.parking.config;

import com.capstone.parking.model.Note;
import com.capstone.parking.model.ParkingSpaceCronJob;
import com.capstone.parking.service.FirebaseMessagingService;
import com.capstone.parking.service.ParkingSpaceService;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
  @Autowired
  private FirebaseMessagingService firebaseService;
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
      Runnable runnableTask = () -> {
        if (!cron.getId().equals("-1")) {
          int count = parkingSpaceService
              .countAllBacklogParkingReservationByParkingId(Integer.parseInt(cron.getId()));
          if (cron.getType().equals("1")) {
            Map<String, String> newMap = new HashMap<>();
            Note note = new Note("End Time is Coming", "Parking Space has un-checkout "
                + count + " vehicle left", newMap, "");
            try {
              if (count > 0) {
                String result = firebaseService.sendNotificationToATopic(note, cron.getId());
              }
            } catch (FirebaseMessagingException e) {
              e.printStackTrace();
            }
          } else if (cron.getType().equals("2")) {
            // Archive
            try {
              if (count > 0) {
                parkingSpaceService.archiveAllBacklogVehicleByParkingId(Integer.parseInt(cron.getId()));
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      };
      Trigger trigger = new Trigger() {
        @Override
        public Date nextExecutionTime(TriggerContext triggerContext) {
          if (cron.getId().equals("-1")) {
            List<ParkingSpaceCronJob> newCronJobList = parkingSpaceService.getCronJobArray();
            if (!compareCronJob(oldCronJobList, newCronJobList)) {
              taskRegistrar.setTriggerTasksList(new ArrayList<TriggerTask>());
              configureTasks(taskRegistrar); // calling recursively.
              taskRegistrar.destroy(); // destroys previously scheduled tasks.
              taskRegistrar.setScheduler(executor);
              taskRegistrar.afterPropertiesSet(); // this will schedule the task with new cron changes.
              return null; // return null when the cron changed so the trigger will stop.
            }
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
