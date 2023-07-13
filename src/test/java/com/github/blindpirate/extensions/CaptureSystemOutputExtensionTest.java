package com.github.blindpirate.extensions;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;

public class CaptureSystemOutputExtensionTest {
    @Test
    @CaptureSystemOutput
    void systemOut(CaptureSystemOutput.OutputCapture outputCapture) {
        outputCapture.expect(containsString("System.out!"));
        //然后当你执行到这个的时候，已经不是Java自己的打印流了，而是经过自己包装的流了。
        System.out.println("Printed to System.out!");
        //在你执行完之后，别忘了，还有一个afterEach呢，你现在还保存的输出的数据呢。
        //然后内部就是去比较你输入的字符串和hamcrest规定的规则是否匹配。
    }
    @CaptureSystemOutput
    void systemMain(CaptureSystemOutput.OutputCapture outputCapture) {
        outputCapture.expect(containsString("System.out!"));
        //然后当你执行到这个的时候，已经不是Java自己的打印流了，而是经过自己包装的流了。
        System.out.println("Printed to System.out!");
        //在你执行完之后，别忘了，还有一个afterEach呢，你现在还保存的输出的数据呢。
        //然后内部就是去比较你输入的字符串和hamcrest规定的规则是否匹配
    }
    @Test
    @CaptureSystemOutput
    void systemErr(CaptureSystemOutput.OutputCapture outputCapture) {
        outputCapture.expect(containsString("System.err!"));

        System.err.println("Printed to System.err!");
    }
}
