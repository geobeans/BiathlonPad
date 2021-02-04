package cn.geobeans.biathlon.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class Shooting extends LitePalSupport {
    @Column(index = true,nullable = false)
    private String name;

    private String model;
    private boolean prone;
    private String lane;
    private String venue;

    private float speed_1;
    private int direct_1;
    private float x1;
    private float y1;
    private Date time1;

    private float speed_2;
    private int direct_2;
    private float x2;
    private float y2;
    private Date time2;

    private float speed_3;
    private int direct_3;
    private float x3;
    private float y3;
    private Date time3;

    private float speed_4;
    private int direct_4;
    private float x4;
    private float y4;
    private Date time4;

    private float speed_5;
    private int direct_5;
    private float x5;
    private float y5;
    private Date time5;

    private float speed_6;
    private int direct_6;
    private float x6;
    private float y6;
    private Date time6;

    private float speed_7;
    private int direct_7;
    private float x7;
    private float y7;
    private Date time7;

    private float speed_8;
    private int direct_8;
    private float x8;
    private float y8;
    private Date time8;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isProne() {
        return prone;
    }

    public void setProne(boolean prone) {
        this.prone = prone;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public float getSpeed_1() {
        return speed_1;
    }

    public void setSpeed_1(float speed_1) {
        this.speed_1 = speed_1;
    }

    public int getDirect_1() {
        return direct_1;
    }

    public void setDirect_1(int direct_1) {
        this.direct_1 = direct_1;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public Date getTime1() {
        return time1;
    }

    public void setTime1(Date time1) {
        this.time1 = time1;
    }

    public float getSpeed_2() {
        return speed_2;
    }

    public void setSpeed_2(float speed_2) {
        this.speed_2 = speed_2;
    }

    public int getDirect_2() {
        return direct_2;
    }

    public void setDirect_2(int direct_2) {
        this.direct_2 = direct_2;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    public Date getTime2() {
        return time2;
    }

    public void setTime2(Date time2) {
        this.time2 = time2;
    }

    public float getSpeed_3() {
        return speed_3;
    }

    public void setSpeed_3(float speed_3) {
        this.speed_3 = speed_3;
    }

    public int getDirect_3() {
        return direct_3;
    }

    public void setDirect_3(int direct_3) {
        this.direct_3 = direct_3;
    }

    public float getX3() {
        return x3;
    }

    public void setX3(float x3) {
        this.x3 = x3;
    }

    public float getY3() {
        return y3;
    }

    public void setY3(float y3) {
        this.y3 = y3;
    }

    public Date getTime3() {
        return time3;
    }

    public void setTime3(Date time3) {
        this.time3 = time3;
    }

    public float getSpeed_4() {
        return speed_4;
    }

    public void setSpeed_4(float speed_4) {
        this.speed_4 = speed_4;
    }

    public int getDirect_4() {
        return direct_4;
    }

    public void setDirect_4(int direct_4) {
        this.direct_4 = direct_4;
    }

    public float getX4() {
        return x4;
    }

    public void setX4(float x4) {
        this.x4 = x4;
    }

    public float getY4() {
        return y4;
    }

    public void setY4(float y4) {
        this.y4 = y4;
    }

    public Date getTime4() {
        return time4;
    }

    public void setTime4(Date time4) {
        this.time4 = time4;
    }

    public float getSpeed_5() {
        return speed_5;
    }

    public void setSpeed_5(float speed_5) {
        this.speed_5 = speed_5;
    }

    public int getDirect_5() {
        return direct_5;
    }

    public void setDirect_5(int direct_5) {
        this.direct_5 = direct_5;
    }

    public float getX5() {
        return x5;
    }

    public void setX5(float x5) {
        this.x5 = x5;
    }

    public float getY5() {
        return y5;
    }

    public void setY5(float y5) {
        this.y5 = y5;
    }

    public Date getTime5() {
        return time5;
    }

    public void setTime5(Date time5) {
        this.time5 = time5;
    }

    public float getSpeed_6() {
        return speed_6;
    }

    public void setSpeed_6(float speed_6) {
        this.speed_6 = speed_6;
    }

    public int getDirect_6() {
        return direct_6;
    }

    public void setDirect_6(int direct_6) {
        this.direct_6 = direct_6;
    }

    public float getX6() {
        return x6;
    }

    public void setX6(float x6) {
        this.x6 = x6;
    }

    public float getY6() {
        return y6;
    }

    public void setY6(float y6) {
        this.y6 = y6;
    }

    public Date getTime6() {
        return time6;
    }

    public void setTime6(Date time6) {
        this.time6 = time6;
    }

    public float getSpeed_7() {
        return speed_7;
    }

    public void setSpeed_7(float speed_7) {
        this.speed_7 = speed_7;
    }

    public int getDirect_7() {
        return direct_7;
    }

    public void setDirect_7(int direct_7) {
        this.direct_7 = direct_7;
    }

    public float getX7() {
        return x7;
    }

    public void setX7(float x7) {
        this.x7 = x7;
    }

    public float getY7() {
        return y7;
    }

    public void setY7(float y7) {
        this.y7 = y7;
    }

    public Date getTime7() {
        return time7;
    }

    public void setTime7(Date time7) {
        this.time7 = time7;
    }

    public float getSpeed_8() {
        return speed_8;
    }

    public void setSpeed_8(float speed_8) {
        this.speed_8 = speed_8;
    }

    public int getDirect_8() {
        return direct_8;
    }

    public void setDirect_8(int direct_8) {
        this.direct_8 = direct_8;
    }

    public float getX8() {
        return x8;
    }

    public void setX8(float x8) {
        this.x8 = x8;
    }

    public float getY8() {
        return y8;
    }

    public void setY8(float y8) {
        this.y8 = y8;
    }

    public Date getTime8() {
        return time8;
    }

    public void setTime8(Date time8) {
        this.time8 = time8;
    }
}