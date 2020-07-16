package com.gcy.bootwithutils.common.constants;

import com.gcy.bootwithutils.common.dto.Pagination;
import com.github.pagehelper.PageInfo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

/*
 * @Author gcy
 * @Description common response data structure
 * @Date 10:18 2020/5/15
 * @Param
 * @return
 **/

@Getter
@Setter
public class Result<T> {

    private int errCode;

    private String errMessage;

    private T data;

    public Result(int errCode) {
        this.errCode = errCode;
    }

    public Result(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public Result(int errCode, String errMessage, T data) {
        this.errCode = errCode;
        this.errMessage = errMessage;
        this.data = data;
    }

    /*
     * @Author gcy
     * @Description base success
     * @Date 16:09 2020/5/15
     * @Param []
     * @return com.gcy.bootwithutils.common.constants.Result<T>
     **/
    public static <T>Result<T> success(){return new Result<T>(200, "操作成功");}

    /*
     * @Author gcy
     * @Description success with data
     * @Date 16:09 2020/5/15
     * @Param [data]
     * @return com.gcy.bootwithutils.common.constants.Result<T>
     **/
    public static <T>Result<T> success(T data){return new Result<T>(200, "操作成功", data);}

    /*
     * @Author gcy
     * @Description success with pagination info
     * @Date 16:10 2020/5/15
     * @Param [pageInfo]
     * @return com.gcy.bootwithutils.common.constants.Result<com.gcy.bootwithutils.common.dto.Pagination<E>>
     **/
    public static <E> Result<Pagination<E>> pageSuccess(PageInfo<E> pageInfo) {
        return new Result<Pagination<E>>(200, "操作成功", new Pagination<E>(pageInfo));
    }

    /*
     * @Author gcy
     * @Description base fail
     * @Date 16:11 2020/5/15
     * @Param []
     * @return com.gcy.bootwithutils.common.constants.Result<T>
     **/
    public static <T> Result<T> failed() {
        return new Result<T>(400, "操作失败");
    }

    /*
     * @Author gcy
     * @Description fail with message
     * @Date 16:12 2020/5/15
     * @Param [message]
     * @return com.gcy.bootwithutils.common.constants.Result<T>
     **/
    public static <T>Result<T> failed(String message){
        return new Result<T>(400, message);
    }

    /*
     * @Author gcy
     * @Description base validation
     * @Date 16:12 2020/5/15
     * @Param [message]
     * @return com.gcy.bootwithutils.common.constants.Result<T>
     **/
    public static <T> Result<T> validateFailed(String message) {
        return new Result<T>(404, message);
    }

    /*
     * @Author gcy
     * @Description validation with message
     * @Date 16:13 2020/5/15
     * @Param [result]
     * @return com.gcy.bootwithutils.common.constants.Result<T>
     **/
    public <T>Result<T> validateFailed(BindingResult result){
        return validateFailed(result.getFieldError().getDefaultMessage());
    }

    /*
     * @Author gcy
     * @Description unauthorized
     * @Date 16:16 2020/5/15
     * @Param [data]
     * @return com.gcy.bootwithutils.common.constants.Result<T>
     **/
    public static <T> Result<T> unauthorized(T data) {
        return new Result<T>(401, "未登录或token已经过期", data);
    }

    /*
     * @Author gcy
     * @Description forbidden
     * @Date 16:16 2020/5/15
     * @Param [data]
     * @return com.gcy.bootwithutils.common.constants.Result<T>
     **/
    public static <T> Result<T> forbidden(T data) {
        return new Result<T>(403, "没有相关权限", data);
    }

    /*
     * @Author gcy
     * @Description error
     * @Date 16:17 2020/5/15
     * @Param []
     * @return com.gcy.bootwithutils.common.constants.Result<T>
     **/
    public static <T> Result<T> error() {
        return new Result<T>(500, "服务器错误");
    }
}
