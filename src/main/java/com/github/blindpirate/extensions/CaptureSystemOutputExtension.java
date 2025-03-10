/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.blindpirate.extensions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.github.blindpirate.extensions.CaptureSystemOutput.OutputCapture;

/**
 * JUnit Jupiter extension for capturing output to {@code System.out} and
 * {@code System.err} with expectations supported via Hamcrest matchers.
 *
 * <p>Based on code from Spring Boot's
 * <a href="https://github.com/spring-projects/spring-boot/blob/d3c34ee3d1bfd3db4a98678c524e145ef9bca51c/spring-boot-project/spring-boot-tools/spring-boot-test-support/src/main/java/org/springframework/boot/testsupport/rule/OutputCapture.java">OutputCapture</a>
 * rule for JUnit 4 by Phillip Webb and Andy Wilkinson.
 *
 * @author Sam Brannen
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
class CaptureSystemOutputExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        //在所有的方法执行之前，就把输入流和输出流换成了自己的输入和输出流
        getOutputCapture(context).captureOutput();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        OutputCapture outputCapture = getOutputCapture(context);
        try {
            //在结束之后，获取到自己添加的hamcrest校验规则，然后判断是否匹配
            if (!outputCapture.matchers.isEmpty()) {
                String output = outputCapture.toString();
                assertThat(output, allOf(outputCapture.matchers));
            }
        }
        finally {
            outputCapture.releaseOutput();
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        //com.github.blindpirate.extensions.CaptureSystemOutputExtensionTest
        // 中的一个方法 systemOut，
        // 该方法接受一个参数
        // com.github.blindpirate.extensions.CaptureSystemOutput$OutputCapture。
        boolean isTestMethodLevel = extensionContext.getTestMethod().isPresent();
        //这里在判断类型是否是OutputCapture类型的，如果这两个条件都满足了，那么这个参数就是支持的，当然，这个支持的定义还是自己定义的。
        boolean isOutputCapture = parameterContext.getParameter().getType() == OutputCapture.class;
        return isTestMethodLevel && isOutputCapture;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return getOutputCapture(extensionContext);
    }

    private OutputCapture getOutputCapture(ExtensionContext context) {
        return getStore(context).getOrComputeIfAbsent(OutputCapture.class);
    }

    private Store getStore(ExtensionContext context) {
        return context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
    }

}
