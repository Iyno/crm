package com.bjpowernode.crm.commons.domain;


import lombok.Data;

@Data
public class ReturnObject {
    private String code;//处理成功获取失败的标记：1表示处理成功，0表示处理失败
    private String message;//提示信息
    private Object retData;//返回的其他数据
}
