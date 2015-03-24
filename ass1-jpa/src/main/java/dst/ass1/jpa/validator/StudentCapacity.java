package dst.ass1.jpa.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StudentCapacityValidator.class)
public @interface StudentCapacity {

    int min() default 0;
    int max() default Integer.MAX_VALUE;

    String message() default "{dst.ass1.jpa1.validator.StudentCapacity}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
