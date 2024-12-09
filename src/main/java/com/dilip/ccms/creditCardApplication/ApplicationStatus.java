package com.dilip.ccms.creditCardApplication;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class ApplicationStatus {

    @Id
    private Integer id;
    private String name;
}
