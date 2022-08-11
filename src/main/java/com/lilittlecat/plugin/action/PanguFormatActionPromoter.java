package com.lilittlecat.plugin.action;

import com.intellij.codeInsight.actions.ReformatCodeAction;
import com.intellij.openapi.actionSystem.ActionPromoter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.lilittlecat.plugin.setting.PanguFormatSettingsState;

import java.util.ArrayList;
import java.util.List;

/**
 * Support pangu format in reformat code action.
 *
 * @author LiLittleCat
 * @since 2022/8/11
 */
public class PanguFormatActionPromoter implements ActionPromoter {
    @Override
    public List<AnAction> promote(List<AnAction> actions, DataContext context) {
        boolean isPanguFormatWhenReformatCode = PanguFormatSettingsState.getInstance().isPanguFormatWhenReformatCode;
        if (isPanguFormatWhenReformatCode) {
            ArrayList<AnAction> result = new ArrayList<>();
            for (AnAction action : actions) {
                if (action.getClass().equals(ReformatCodeAction.class)) {
                    result.add(new PanguFormatInReformatAction());
                } else {
                    result.add(action);
                }
            }
            return result;
        } else {
            return new ArrayList<>(actions);
        }
    }
}
