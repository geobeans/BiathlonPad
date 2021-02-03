package cn.geobeans.biathlon.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Shooting extends LitePalSupport {

    @Column(unique = true, defaultValue = "unknown")
    private String name;

    @Column(index = true)
    private float price;


}