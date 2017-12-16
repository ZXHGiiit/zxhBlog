package com.zxh.util;

import com.zxh.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/12/16.
 */
public class MyBeanUtils {

    /**
     * 获取对象中属性为空的属性名称数组
     * @param src
     * @return
     */
    public static String[] getNullPropertyName(Object src) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(src);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        List<String> nullPropertyNames = new ArrayList<>();
        for(PropertyDescriptor pd : pds) {
            String properName = pd.getName();
            if(beanWrapper.getPropertyValue(properName) == null) {
                nullPropertyNames.add(properName);
            }
        }
        return nullPropertyNames.toArray(new String[nullPropertyNames.size()]);
    }


    public static void main(String[] args) {
        String[] propers = getNullPropertyName(new User());
        User user = new User();
        user.setAccount("xinghang.zhou");
        user.setName("周星航");
        //BeanUtils.copyProperties(new User(), user, propers);
        BeanUtils.copyProperties(new User(), user);
        System.out.println(user.toString());
    }

}
