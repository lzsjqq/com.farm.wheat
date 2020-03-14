package com.farm.wheat.share.chan.util;

import lombok.Data;

/**
 * @description:
 * @author: xyc
 * @create: 2020-03-13 23:49
 */
@Data
public class Node<T> {
    public Node<T> next;
    public Node<T> pre;
    public T data;

    Node(Node<T> next, Node<T> pre, T data) {
        super();
        this.next = next;
        this.pre = pre;
        this.data = data;
    }
    public T getNode() {
        return data;
    }
}