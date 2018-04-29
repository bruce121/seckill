package com.cxn.seckill.validator;

import com.cxn.seckill.util.ValidateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-28 16:53
 * @Version v1.0
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {


    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果输入了required为false，那么不校验value，为空也可以
        if (required) {
            return ValidateUtil.isMobile(value);
        }else{
            if (StringUtils.isEmpty(value)) {
                return true;
            }else{
                return ValidateUtil.isMobile(value);
            }

        }

    }
}
