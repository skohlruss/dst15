package dst.ass1.jpa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by pavol on 24.3.2015.
 */
public class StudentCapacityValidator implements ConstraintValidator<StudentCapacity, Integer> {

    private int min;
    private int max;

    @Override
    public void initialize(StudentCapacity constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }

        if (value < min || value > max) {
            return false;
        }

        return true;
    }
}
