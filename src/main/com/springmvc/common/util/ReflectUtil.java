package com.springmvc.common.util;

import java.lang.reflect.Field;

/**
 * Created by zhangyuguang on 2018/10/18.
 */
public class ReflectUtil {
    // 因为安装了较高版本的jdk The type java.lang.reflect.AnnotatedElement cannot be resolved. It is indirectly referenced from required .class files
    private static Field getField(Object obj , String fm){
        Field f = null;
        for(Class<?> clazz = obj.getClass() ; clazz != Object.class;clazz = clazz.getSuperclass()){
            try {
                f = clazz.getDeclaredField(fm);
            } catch (NoSuchFieldException  e) {
                // TODO: handle exception
                System.out.println("NoSuchFieldException-------111111111111111  e");
               /* e.printStackTrace();*/

            }catch (SecurityException e) {
                // TODO: handle exception
               /* e.printStackTrace();*/
                System.out.println("SecurityException--------11111111111-2222222222  e");
            }
        }
        return f ;
    }

    public static Object getFieldValue(Object obj, String fm) {
        Object result = null;
        Field f = ReflectUtil.getField(obj, fm);
        if (f != null) {
            f.setAccessible(true);
            try {
                result = f.get(obj);
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
             /*   e.printStackTrace();*/
                System.out.println("22222  IllegalArgumentException e ");
            } catch (IllegalAccessException e) {
                // TODO: handle exception
               /* e.printStackTrace();*/
                System.out.println("22222---2222  IllegalAccessException e ");
            }
        }
        return result;
    }

    public static void setFieldValue (Object obj , String fieldName , Object  fieldValue){
        Field f = ReflectUtil.getField(obj, fieldName);
        if(f!=null){
            f.setAccessible(true);
            try{
                f.set(obj, fieldValue);
            }catch (IllegalArgumentException e) {
                // TODO: handle exception
               /* e.printStackTrace();*/
                System.out.println("333333333333   IllegalArgumentException e ");
            }catch (IllegalAccessException e) {
                // TODO: handle exception
              /*  e.printStackTrace();*/
                System.out.println("333333333333--33333   IllegalAccessException e ");
            }
        }
    }
}
