package com.dilip.ccms.personalDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
public class EmploymentStatus {

    @Id
    private Integer id;
    private String statusName;
}
