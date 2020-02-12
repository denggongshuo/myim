package com.pit.myim.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author deng
 * @date 2020/1/13 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RES implements Serializable {

    private static final long serialVersionUID = 1L;

    int code;

    String msg;

    Object data;

    public static RES ok(Object data){
        return new RES(0,"操作成功",data);
    }
    public static RES ok(int code,Object data){
        return new RES(code,"操作成功",data);
    }

    public static RES fail(){
        return new RES(-1,"操作失败",null);
    }
    public static RES fail(String msg){
        return new RES(-1,msg,null);
    }

    public static RES fail(int code,String msg){
        return new RES(code,msg,null);
    }

}
