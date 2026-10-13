package org.example.asimpleshop.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.example.asimpleshop.pojo.Result;
import org.example.asimpleshop.pojo.ToDoList;
import org.example.asimpleshop.service.ToDoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
@RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
public class ToDoListController {

    @Autowired
    private ToDoListService toDoListService;

    /** 创建待办 */
    @PostMapping("/add")
    @ResponseBody
    public Result add(String title, String content) {
        String owner = currentUser();
        boolean ok = toDoListService.add(owner, title, content);
        if (ok) {
            return Result.success("创建成功");
        } else {
            return Result.error("创建失败");
        }
    }


    /** 查询当前用户的待办列表 */
    @GetMapping("/list")
    @ResponseBody
    public Result list() {
        String owner = currentUser();
        return Result.success(toDoListService.listByOwner(owner));
    }



    /** 查询单条详情 */
    @GetMapping("/detail")
    @ResponseBody
    public Result detail(int id) {
        String owner = currentUser();
        ToDoList todo = toDoListService.getById(id, owner);
        if (todo == null) {
            return Result.error("待办不存在或无权访问");
        }
        return Result.success(todo);
    }



    /** 修改标题和内容 */
    @PostMapping("/update")
    @ResponseBody
    public Result update(int id, String title, String content) {
        String owner = currentUser();
        boolean ok = toDoListService.update(id, owner, title, content);
        if (ok) {
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败，待办不存在或无权访问");
        }
    }



    /** 标记完成 / 未完成（取反） */
    @PostMapping("/toggle")
    @ResponseBody
    public Result toggle(int id) {
        String owner = currentUser();
        boolean ok = toDoListService.toggleStatus(id, owner);
        if (ok) {
            return Result.success("操作成功");
        } else {
            return Result.error("操作失败，待办不存在或无权访问");
        }
    }



    /** 删除待办 */
    @PostMapping("/delete")
    @ResponseBody
    public Result delete(int id) {
        String owner = currentUser();
        boolean ok = toDoListService.delete(id, owner);
        if (ok) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败，待办不存在或无权访问");
        }
    }


    
    /** 统一从会话取当前登录用户名，作为 owner */
    private String currentUser() {
        Subject subject = SecurityUtils.getSubject();
        return subject.getPrincipal().toString();
    }
}
