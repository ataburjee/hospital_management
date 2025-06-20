package com.hms.service;

import com.hms.dto.QueueStatusResponse;

import java.util.List;

public interface QueueService {

    String markQueueStarted(Long appointmentSlotId);
    String markCurrentAsMissed(Long appointmentSlotId);
    String markLateArrival(Long appointmentSlotId, int missedQueueNumber);
    List<QueueStatusResponse> getQueueStatus(Long appointmentSlotId);
    String completeCurrentAndPromoteNext(Long appointmentSlotId);

}
