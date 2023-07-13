package com.github.blindpirate.extensions;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;

public class CaptureSystemOutputExtensionTest {
    /**
     * 为什么@CaptureSystemOutput必须和@Test一起才能发挥作用呢？因为@CaptureSystemOutput
     * 注解上面贴着一个@ExtendWith(CaptureSystemOutputExtension.class)
     * 说明要去拓展junit5的东西了，然后
     *
     class CaptureSystemOutputExtension implements
     BeforeEachCallback, AfterEachCallback, ParameterResolver
     因为这个类实现了BeforeEachCallback, AfterEachCallback,这两个接口，
     就相当于有了在所有的方法前，方法后进行操作
     而实现了ParameterResolver之后就自定义了是否生效，
     boolean isOutputCapture = parameterContext.getParameter().getType() == OutputCapture.class;
     这句代码就规定了参数必须是CaptureSystemOutput.OutputCapture类型的，
     然后因为把Java自带的输出流给替换了，所以可以捕捉到。最终使用hamcrest进行去比较字符串
     *
     *
     **/
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
