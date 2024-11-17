package com.jiang.Enums;

public enum CommentOrderTypeEnum {
    HOS(0,"good_count desc,comment_id asc","最热"),
    NEW(1,"comment_id desc","最新");
    private Integer type;
    private String orderSql;
    private String desc;

     CommentOrderTypeEnum(Integer type,String orderSql,String desc){
        this.type =type;
        this.orderSql = orderSql;
        this.desc=desc;
    }
    public Integer getType() {
        return type;
    }

    public String getOrderSql() {
        return orderSql;
    }

    public String getDesc() {
        return desc;
    }
}
