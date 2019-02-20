package com.lockdown.tcc.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Products {

    private int id;

    private int amount;

    private long version;

    public void updateVersion(){
        this.version += 1;
    }

}
