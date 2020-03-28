package com.farm.wheat.share.chan.util;

import lombok.Data;

/**
 * @description:
 * @author: xyc
 * @create: 2020-03-13 23:51
 */
@Data
public class Linked<T> {
    private Node<T> first;
    private Node<T> last;
    private int size;


    public void add(T data) {
        if (first == null) {
            Node<T> node = new Node(null, null, data);
            first = node;
            last = node;
        } else {
            Node<T> node = new Node(null, last, data);
            node.index = ++last.index;
            last.next = node;
            last = node;
        }
        this.size++;
    }

    /**
     * 获取前几个节点
     *
     * @param preSize
     * @return
     */
    public static <T> Node<T> getPreNode(int preSize, Node<T> node) {
        Node<T> pre = node.getPre();
        if (null == pre) {
            return null;
        }
        if (preSize - 1 == 0) {
            return pre;
        }
        return getPreNode(preSize - 1, pre);
    }


    public void remove(T data) {
        if (data == null) {
            for (Node<T> cur = this.first; cur != null; ) {
                Node<T> tmp = cur.next;
                if (cur.data == null) {
                    if (cur == first) {
                        if (cur == last) {
                            cur = null;
                        } else {
                            first = cur.next;
                            cur.next = null;
                            cur = null;
                            first.pre = null;
                            cur = first;
                        }
                    } else if (cur == last) {
                        last = cur.pre;
                        cur.pre = null;
                        cur = null;
                        last.next = null;
                        cur = tmp;
                    } else {
                        cur.pre.next = cur.next;
                        cur.next.pre = cur.pre;
                        cur.next = null;
                        cur.pre = null;
                        cur = null;
                        cur = tmp;
                    }
                    this.size--;
                } else {
                    cur = cur.next;
                }
            }
        } else {
            for (Node<T> cur = this.first; cur != null; ) {
                Node<T> tmp = cur.next;
                if (data.equals(cur.data)) {
                    if (cur == first) {
                        if (cur == last) {
                            cur = null;
                        } else {
                            first = cur.next;
                            cur.next = null;
                            cur = null;
                            first.pre = null;
                            cur = first;
                        }
                    } else if (cur == last) {

                        last = cur.pre;
                        cur.pre = null;
                        cur = null;
                        last.next = null;
                        cur = tmp;
                    } else {

                        cur.pre.next = cur.next;
                        cur.next.pre = cur.pre;
                        cur.next = null;
                        cur.pre = null;
                        cur = null;
                        cur = tmp;
                    }
                    this.size--;
                } else {
                    cur = cur.next;
                }
            }
        }
    }


    public void set(int index, T data) {
        Node<T> node = node(index);
        if (node != null)
            node.data = data;
    }


    public T get(int index) {
        Node<T> node = node(index);
        if (node != null)
            return node.data;
        else {
            try {
                throw new NullPointerException();
            } catch (NullPointerException e) {
                System.out.println("Null：no such element");
                return null;
            }
        }
    }

    public Node<T> getNode(int index) {
        Node<T> node = node(index);
        if (node != null)
            return node;
        else {
            try {
                throw new NullPointerException();
            } catch (NullPointerException e) {
                System.out.println("Null：no such element");
                return null;
            }
        }
    }


    public int getSize() {
        return this.size;
    }

    private Node node(int index) {
        if (index >= this.size)
            return null;
        int len = this.size;
        if (index < len) {
            if (index > len / 2) {
                Node cur = last;
                int step = len - index - 1;
                while (step-- > 0) {
                    cur = cur.pre;
                }
                return cur;
            } else {
                Node cur = first;
                int step = index;
                while (step-- > 0) {
                    cur = cur.next;
                }
                return cur;
            }
        }
        return null;
    }


    public void printList() {
        int count = this.size;
        Node cur = first;
        System.out.print("打印链表元素：");
        while (count-- > 0) {
            System.out.print("  " + cur.data);
            cur = cur.next;
        }
        System.out.println();
    }


    public boolean contains(T data) {
        if (data == null) {

            for (Node cur = this.first; cur != null; cur = cur.next) {
                if (cur.data == null)
                    return true;
            }

        } else {

            for (Node cur = this.first; cur != null; cur = cur.next) {
                if (data.equals(cur.data))
                    return true;
            }

        }
        return false;
    }


    public boolean isEmpty() {
        return this.size <= 0;
    }


    public void clear() {
        Node cur = first;
        Node tmp = null;

        while (cur != null) {
            tmp = cur.next;
            cur.next = null;
            cur.pre = null;
            cur = tmp;
        }

        first = last = null;
        this.size = 0;
    }


    public Object[] toArray() {
        if (size <= 0)
            return null;

        // 不支持泛型数组
        Object[] arr = new Object[this.size];
        int i = 0;

        for (Node cur = first; cur != null; cur = cur.next, i++)
            arr[i] = cur.data;

        return arr;
    }


}