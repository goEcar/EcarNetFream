package com.ecar.ecarnetwork.http.util;

/**
 * 常量类
 */
public class ConstantsLib {


    private ConstantsLib() {

    }


    public static ConstantsLib getInstance() {
        return SingletonHolder.constantsLib;
    }


    private static class SingletonHolder {
        private static final ConstantsLib constantsLib = new ConstantsLib();
    }



}
