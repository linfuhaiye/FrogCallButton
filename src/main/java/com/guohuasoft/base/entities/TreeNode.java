package com.guohuasoft.base.entities;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 树节点类型
 *
 * @author Alex
 */
@Data
public class TreeNode<T> {
    /**
     * 对象
     */
    @JSONField(unwrapped = true)
    public T object;

    /**
     * 子对象
     */
    public List<TreeNode<T>> children = new LinkedList<>();

    public TreeNode() {
    }

    public TreeNode(T object) {
        this.object = object;
    }
}
