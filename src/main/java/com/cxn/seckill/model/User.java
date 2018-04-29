package com.cxn.seckill.model;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-26 21:33
 * @Version v1.0
 */
public class User {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
