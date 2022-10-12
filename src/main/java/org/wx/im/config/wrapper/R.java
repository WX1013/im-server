package org.wx.im.config.wrapper;

import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.wx.im.config.exception.BaseException;
import org.wx.im.config.exception.BizException;
import org.wx.im.config.exception.ExceptionCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 封装请求结果对象
 * @Author: WangXin
 * @Date: 2021/8/5 下午6:20
 **/
@Getter
@Setter
@SuppressWarnings("ALL")
@Accessors(chain = true)
public class R<T> {
    public static final String DEF_ERROR_MESSAGE = "系统繁忙，请稍候再试";
    public static final String HYSTRIX_ERROR_MESSAGE = "请求超时，请稍候再试";
    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;
    public static final int TIMEOUT_CODE = 503;
    /**
     * 统一参数验证异常
     */
    public static final int VALID_EX_CODE = 400;
    public static final int OPERATION_EX_CODE = 400;
    /**
     * 调用是否成功标识，0：成功，-1:系统繁忙，此时请开发者稍候再试 详情见[ExceptionCode]
     */
    @ApiModelProperty(value = "响应编码:0/200-请求处理成功")
    private int state;

    /**
     * 调用结果
     */
    @ApiModelProperty(value = "响应数据")
    private T data;

    /**
     * 结果消息，如果调用成功，消息通常为空T
     */
    @ApiModelProperty(value = "提示消息")
    private String msg = "SUCCESS";

    /**
     * 附加数据
     */
    @ApiModelProperty(value = "附加数据")
    private Map<Object, Object> extra;

    /**
     * 响应时间
     */
    @ApiModelProperty(value = "响应时间戳")
    private long timestamp = System.currentTimeMillis();

    /**
     * 系统报错时，抛出的原生信息
     */
    @ApiModelProperty(value = "异常消息")
    private String errorMsg = null;

    private R() {
        this.timestamp = System.currentTimeMillis();
    }

    public R(int code, T data, String msg) {
        this.state = code;
        this.data = data;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
    }

    public R(int code, T data, String msg, String errorMsg) {
        this(code, data, msg);
        this.errorMsg = errorMsg;
    }

    public static <E> R<E> result(int code, E data, String msg) {
        return new R<>(code, data, msg);
    }

    public static <E> R<E> result(int code, E data, String msg, String errorMsg) {
        return new R<>(code, data, msg, errorMsg);
    }

    /**
     * 请求成功消息
     *
     * @param data 结果
     * @return RPC调用结果
     */
    public static <E> R<E> success(E data) {
        return new R<>(SUCCESS_CODE, data, "SUCCESS");
    }

    public static R<Boolean> success() {
        return new R<>(SUCCESS_CODE, true, "SUCCESS");
    }


    public static <E> R<E> successDef(E data) {
        return new R<>(SUCCESS_CODE, data, "SUCCESS");
    }

    public static <E> R<E> successDef() {
        return new R<>(SUCCESS_CODE, null, "SUCCESS");
    }

    public static <E> R<E> successDef(E data, String msg) {
        return new R<>(SUCCESS_CODE, data, msg);
    }

    /**
     * 请求成功方法 ，data返回值，msg提示信息
     *
     * @param data 结果
     * @param msg  消息
     * @return RPC调用结果
     */
    public static <E> R<E> success(E data, String msg) {
        return new R<>(SUCCESS_CODE, data, msg);
    }

    /**
     * 请求失败消息
     *
     * @param msg
     * @return
     */
    public static <E> R<E> fail(int code, String msg) {
        return new R<>(code, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> R<E> fail(int code, String msg, String errorMsg) {
        return new R<>(code, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg, errorMsg);
    }

    public static <E> R<E> fail(String msg) {
        return fail(OPERATION_EX_CODE, msg);
    }

    public static <E> R<E> fail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new R<>(OPERATION_EX_CODE, null, String.format(message, args));
    }

    public static <E> R<E> fail(BaseException exceptionCode) {
        return validFail(exceptionCode);
    }

    public static <E> R<E> fail(BizException exception) {
        if (exception == null) {
            return fail(DEF_ERROR_MESSAGE);
        }
        return new R<>(exception.getCode(), null, exception.getMessage(), exception.getMessage());
    }

    public static <E> R<E> fail(ExceptionCode exception, String errorMsg) {
        if (exception == null) {
            return fail(DEF_ERROR_MESSAGE);
        }
        return new R<>(exception.getCode(), null, exception.getMessage(), errorMsg);
    }

    /**
     * 请求失败消息，根据异常类型，获取不同的提供消息
     *
     * @param throwable 异常
     * @return RPC调用结果
     */
    public static <E> R<E> fail(Throwable throwable) {
        String msg = throwable != null ? throwable.getMessage() : DEF_ERROR_MESSAGE;
        return fail(FAIL_CODE, msg, msg);
    }

    public static <E> R<E> validFail(String msg) {
        return new R<>(VALID_EX_CODE, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
    }

    public static <E> R<E> validFail(String msg, Object... args) {
        String message = (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg;
        return new R<>(VALID_EX_CODE, null, String.format(message, args));
    }

    public static <E> R<E> validFail(BaseException exceptionCode) {
        return new R<>(exceptionCode.getCode(), null,
                (exceptionCode.getMessage() == null || exceptionCode.getMessage().isEmpty()) ? DEF_ERROR_MESSAGE : exceptionCode.getMessage());
    }

    public static <E> R<E> timeout() {
        return fail(TIMEOUT_CODE, HYSTRIX_ERROR_MESSAGE);
    }


    public R<T> put(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<>(16);
        }
        this.extra.put(key, value);
        return this;
    }

    public R<T> putAll(Map<Object, Object> extra) {
        if (this.extra == null) {
            this.extra = new HashMap<>(16);
        }
        this.extra.putAll(extra);
        return this;
    }

    /**
     * 逻辑处理是否成功
     *
     * @return 是否成功
     */
    public Boolean getIsSuccess() {
        return this.state == SUCCESS_CODE || this.state == 0;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
