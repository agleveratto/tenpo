package com.agl.tenpo.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String executedDate;
    private Integer num1;
    private Integer num2;
    private Integer percentage;
    @Column(name = "total")
    private Double result;
    private Integer statusCode;
}