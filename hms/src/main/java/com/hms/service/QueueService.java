package com.hms.service;

import com.hms.dto.QueueStatusResponse;

import java.util.List;

public interface QueueService {

    String markQueueStarted(String appointmentSlotId);
    String markCurrentAsMissed(String appointmentSlotId);
    String markLateArrival(String appointmentSlotId, int missedQueueNumber);
    List<QueueStatusResponse> getQueueStatus(String appointmentSlotId);
    String completeCurrentAndPromoteNext(String appointmentSlotId);

}
