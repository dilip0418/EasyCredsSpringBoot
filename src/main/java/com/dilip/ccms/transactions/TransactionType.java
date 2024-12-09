package com.dilip.ccms.transactions;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionType {
    @Id
    private Integer id;
    private String name;
}
