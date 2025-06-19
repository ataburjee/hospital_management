package com.hms.service;

public interface QueueService {

    String markQueueStarted(Long appointmentSlotId);
    String markCurrentAsMissed(Long appointmentSlotId);
    String markLateArrival(Long appointmentSlotId, int missedQueueNumber);

}
