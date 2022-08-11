package com.lilittlecat.plugin.action;

import com.intellij.openapi.actionSystem.ActionPromoter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;

import java.util.List;

/**
 * @author LiLittleCat
 * @since 2022/8/11
 */
public class PanguFormatActionPromoter implements ActionPromoter {
    @Override
    public List<AnAction> promote(List<AnAction> actions, DataContext context) {
        // todo, order actions use the same key shortcut
        return null;
    }
}
