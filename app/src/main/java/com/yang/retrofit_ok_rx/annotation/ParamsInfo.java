package com.yang.retrofit_ok_rx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ParamsInfo {

    /**
     * 顶部局的id
     * @return
     */
    int contentViewId() default -1;

    /**
     * toolbar的标题id
     * @return
     */
    int toolBarTitle() default -1;

    /**
     * 被选中的navigation的某个item
     * @return
     */
    int itemCheck() default -1;
}
