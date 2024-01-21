package com.bjpowernode.crm.uuid;

import java.util.UUID;

public class UUIDTest {
    /**
     * jdvautil提供的每次生成不一样的32位
     */
    public static void main(String[] arg){
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        System.out.println(uuid);
    }
}
