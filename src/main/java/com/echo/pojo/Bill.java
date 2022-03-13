package com.echo.pojo;


import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Setter
@Getter
public class Bill {
    private int id;
    private String billCode;
    private String productName;
    private String produceDesc;
    private String productUnit;
    private Double productCount;
    private Double totalPrice;
    private int isPayment;
    private int createdBy;
    private Date creationDate;
    private int modifyBy;
    private Date modifyDate;
    private int providerId;
    private String providerName;
    private String createdName;
}
