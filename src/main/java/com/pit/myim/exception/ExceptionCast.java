package com.pit.myim.exception;



/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-14 17:31
 **/
public class ExceptionCast {

    public static void cast(int code,String msg) {

        throw new RRException(msg,code);
    }

}
