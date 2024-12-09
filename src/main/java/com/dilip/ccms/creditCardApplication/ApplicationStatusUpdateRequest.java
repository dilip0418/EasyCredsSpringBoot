package com.dilip.ccms.creditCardApplication;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusUpdateRequest {
    private Integer statusId;
    private String comments;
}
