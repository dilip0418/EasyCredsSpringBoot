package com.dilip.ccms.transactions;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionCategory {
    @Id
    private Integer id;
    private String name;
}
