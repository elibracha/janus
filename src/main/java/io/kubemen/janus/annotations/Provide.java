package io.kubemen.janus.annotations;

import java.lang.annotation.*;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Provide {
    public String image() default "";
    public String tag() default "latest";
}
