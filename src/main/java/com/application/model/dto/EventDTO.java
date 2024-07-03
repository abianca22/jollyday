package com.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Integer id;
    private Double collectedAmount;
    private Integer celebratedUserId;
    private Integer collectingPlaceId;
    private LocalDate creationDate;
    private Integer collectorId;

}
