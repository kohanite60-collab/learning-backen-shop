package org.example.asimpleshop.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("todolist")
public class ToDoList {

    private int id;
    private String owner;
    private String title;
    private String content;

    private int status;   //0:未完成 1:已完成
}
