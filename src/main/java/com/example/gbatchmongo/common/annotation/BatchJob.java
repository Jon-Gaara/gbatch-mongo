package com.example.gbatchmongo.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BatchJob {
    String value();

    String init() default "";

    String destroy() default "";
}
