package com.qf.v13itemweb;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author maizifeng
 * @Date 2019/6/18
 */

@Data

public class Student {
    private Integer id;
    private String name;
    private Date date;

    public Student() {
    }

    public Student(Integer id, String name, Date date) {

        this.id = id;
        this.name = name;
        this.date = date;
    }
}
