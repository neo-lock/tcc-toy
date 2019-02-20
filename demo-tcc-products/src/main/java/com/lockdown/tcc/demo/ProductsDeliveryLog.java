package com.lockdown.tcc.demo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductsDeliveryLog {

    private int pid;

    private String orderNo;

    private int amount;

    private String buyerId;

    private Date createTime;

    private boolean valid;

    public ProductsDeliveryLog(int pid, String orderNo, int amount, String buyerId) {
        this.pid = pid;
        this.orderNo = orderNo;
        this.amount = amount;
        this.buyerId = buyerId;
    }
}
