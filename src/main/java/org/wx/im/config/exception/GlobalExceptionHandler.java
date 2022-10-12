package org.wx.im.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.json.JsonParseException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;
import org.wx.im.config.wrapper.R;
import org.wx.im.utils.StrPool;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 全局异常统一处理
 *
 * @author WangXin
 */
@Configuration
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@SuppressWarnings("AlibabaUndefineMagicConstant")
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BizException.class)
    public R<?> bizException(BizException ex) {
        log.warn("BizException: {}", ex.getMessage());
        return R.result(ex.getCode(), null, ex.getMessage(), ex.getLocalizedMessage());
    }

    /**
     * 参数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler({
            ServletRequestBindingException.class,
            HttpMessageNotReadableException.class,
            JsonParseException.class,
            BindException.class})
    public R<?> handleValidationException(Exception e) {
        String msg;
        if (e instanceof BindException) {
            BindException t = (BindException) e;
            msg = t.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException t = (ConstraintViolationException) e;
            msg = t.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        } else if (e instanceof MissingPathVariableException) {
            MissingPathVariableException t = (MissingPathVariableException) e;
            msg = t.getVariableName() + " can not be null";
        } else if (e instanceof JsonParseException || e instanceof HttpMessageNotReadableException) {
            msg = "传入的JSON参数格式有误";
        } else {
            msg = "必填参数缺失";
        }
        log.warn("参数校验不通过：{}", msg);
        return R.validFail(msg);
    }

    @ExceptionHandler(IllegalStateException.class)
    public R<?> illegalStateException(IllegalStateException ex) {
        log.warn("IllegalStateException:", ex);
        return R.result(ExceptionCode.ILLEGAL_ARGUMENT.getCode(), null, ExceptionCode.ILLEGAL_ARGUMENT.getMessage(), ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R<?> missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.warn("MissingServletRequestParameterException:", ex);
        return R.result(ExceptionCode.ILLEGAL_ARGUMENT.getCode(), null,  ex.getParameterName() + " can not be null", ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public R<?> nullPointerException(NullPointerException ex) {
        log.warn("NullPointerException:", ex);
        return R.result(ExceptionCode.NULL_POINT_EX.getCode(), null, ExceptionCode.NULL_POINT_EX.getMessage(), ex.getMessage());
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public R<?> illegalArgumentException(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException:", ex);
        return R.result(ExceptionCode.ILLEGAL_ARGUMENT.getCode(), null, ExceptionCode.ILLEGAL_ARGUMENT.getMessage(), ex.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public R<?> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.warn("HttpMediaTypeNotSupportedException:", ex);
        MediaType contentType = ex.getContentType();
        if (contentType != null) {
            return R.result(ExceptionCode.MEDIA_TYPE_EX.getCode(), null, "请求类型(Content-Type)[" + contentType + "] 与实际接口的请求类型不匹配", ex.getMessage());
        }
        return R.result(ExceptionCode.MEDIA_TYPE_EX.getCode(), null, "无效的Content-Type类型", ex.getMessage());
    }

    /**
     * jsr 规范中的验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<?> constraintViolationException(ConstraintViolationException ex) {
        log.warn("ConstraintViolationException:", ex);
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));

        return R.result(ExceptionCode.BASE_VALID_PARAM, null, message, ex.getMessage());
    }

    /**
     * spring 封装的参数验证异常， 在controller中没有写result参数时，会进入
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("MethodArgumentNotValidException:", ex);
        return R.result(ExceptionCode.BASE_VALID_PARAM, null, Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage(), ex.getMessage());
    }

    private String getPath() {
        String path = StrPool.EMPTY;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            path = request.getRequestURI();
        }
        return path;
    }

    /**
     * 其他异常
     *
     * @param ex 异常
     */
    @ExceptionHandler(Exception.class)
    public R<?> otherExceptionHandler(Exception ex) {
        log.warn("Exception:", ex);
        if (ex.getCause() instanceof BizException) {
            return this.bizException((BizException) ex.getCause());
        }
        return R.result(ExceptionCode.SYSTEM_BUSY.getCode(), null, ExceptionCode.SYSTEM_BUSY.getMessage(), ex.getMessage());
    }


    /**
     * 返回状态码:405
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public R<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("HttpRequestMethodNotSupportedException:", ex);
        return R.result(ExceptionCode.METHOD_NOT_ALLOWED.getCode(), null, ExceptionCode.METHOD_NOT_ALLOWED.getMessage(), ex.getMessage());
    }


    @ExceptionHandler(SQLException.class)
    public R<?> sqlException(SQLException ex) {
        log.warn("SQLException:", ex);
        return R.result(ExceptionCode.SQL_EX.getCode(), null, ExceptionCode.SQL_EX.getMessage(), ex.getMessage());
    }

}
