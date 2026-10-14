package org.example.asimpleshop.handler;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.example.asimpleshop.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常兜底。
 *
 * 作用：把 Controller 抛出的、未被中途 catch 的、能回到 Spring MVC 处理链的异常，
 * 转成统一的 Result JSON，避免直接抛出 Whitelabel Error Page（500 白页）。
 *
 * 注意：Shiro 的 UnauthenticatedException 与 UnauthorizedException 是兄弟关系，
 * 同属 AuthorizationException 的子类；而 AuthenticationException 与之平级，
 * 需要单独处理。Spring 匹配时优先选最具体的异常类型。
 */


@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 未登录：需要先登录 */
    @ExceptionHandler(UnauthenticatedException.class)
    public Result handleUnauthenticated(UnauthenticatedException e) {
        return new Result(401, "未登录，请先登录", null);
    }

    /** 已登录但权限不足 */
    @ExceptionHandler(UnauthorizedException.class)
    public Result handleUnauthorized(UnauthorizedException e) {
        return new Result(403, "权限不足", null);
    }

    /** 其他 Shiro 授权异常（兜底） */
    @ExceptionHandler(AuthorizationException.class)
    public Result handleAuthorization(AuthorizationException e) {
        return new Result(403, "权限校验失败", null);
    }

    /** 登录失败（用户名或密码错误等） */
    @ExceptionHandler(AuthenticationException.class)
    public Result handleAuthentication(AuthenticationException e) {
        return new Result(401, "用户名或密码错误", null);
    }
}
