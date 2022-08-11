package com.lilittlecat.plugin;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.lilittlecat.plugin.action.PanguFormatEditorAction;
import org.junit.Test;

/**
 * @author LiLittleCat
 * @since 2022/8/9
 */
public class PanguFormatTest extends LightJavaCodeInsightFixtureTestCase {

    @Test
    public void test(){
        myFixture.configureByText("test.md", "你好Hello世界");
        myFixture.testAction(new PanguFormatEditorAction());
        System.out.println(myFixture.getFile().getText());
    }

}
