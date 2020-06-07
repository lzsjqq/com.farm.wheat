package com.farm.wheat.common.utils;

import org.python.core.PyBaseString;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @description: 执行python脚本
 * @author: xyc
 * @create: 2020-05-05 15:06
 */
public class PythonUtil {

    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("D:\\workspace\\py\\test\\daily.py");

        // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
        PyFunction pyFunction = interpreter.get("daily", PyFunction.class);
        String a = "sh600000";
        //调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
        PyObject pyobj = pyFunction.__call__(new PyString(a));
        System.out.println("the anwser is: " + pyobj);
    }
}