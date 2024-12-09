package com.dilip.ccms.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "state_id")
    private Integer id;

    @Column(nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(nullable = false)
    @JsonProperty("isoCode")
    private String stateCode;
}
