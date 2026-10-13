package org.example.asimpleshop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.example.asimpleshop.mapper.ToDoListMapper;
import org.example.asimpleshop.pojo.ToDoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 待办事项服务。
 * 约定：所有操作都以「当前登录用户名」作为 owner 条件，
 * 因此查/改/删天然被限定在自己名下，越权传别人的 id 也匹配不到。
 */
@Component
public class ToDoListService {

    @Autowired
    private ToDoListMapper toDoListMapper;

    /** 创建待办，status=0 表示未完成 */
    public boolean add(String owner, String title, String content) {
        ToDoList todo = new ToDoList();
        todo.setOwner(owner);
        todo.setTitle(title);
        todo.setContent(content);
        todo.setStatus(0);
        return toDoListMapper.insert(todo) > 0;
    }

    /** 查询当前用户的全部待办 */
    public List<ToDoList> listByOwner(String owner) {
        QueryWrapper<ToDoList> wrapper = new QueryWrapper<>();
        wrapper.eq("owner", owner);
        return toDoListMapper.selectList(wrapper);
    }

    /** 查询单条详情（限定 owner，防越权） */
    public ToDoList getById(int id, String owner) {
        QueryWrapper<ToDoList> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("owner", owner);
        return toDoListMapper.selectOne(wrapper);
    }

    /** 修改标题和内容（限定 owner） */
    public boolean update(int id, String owner, String title, String content) {
        UpdateWrapper<ToDoList> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("owner", owner);
        wrapper.set("title", title);
        wrapper.set("content", content);
        return toDoListMapper.update(null, wrapper) > 0;
    }

    /** 切换完成状态：0 <-> 1（限定 owner） */
    public boolean toggleStatus(int id, String owner) {
        ToDoList todo = getById(id, owner);
        if (todo == null) {
            return false;
        }
        int next;
        if (todo.getStatus() == 0) {
            next = 1;
        } else {
            next = 0;
        }
        UpdateWrapper<ToDoList> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("owner", owner);
        wrapper.set("status", next);
        return toDoListMapper.update(null, wrapper) > 0;
    }

    /** 删除待办（限定 owner） */
    public boolean delete(int id, String owner) {
        QueryWrapper<ToDoList> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("owner", owner);
        return toDoListMapper.delete(wrapper) > 0;
    }
}
