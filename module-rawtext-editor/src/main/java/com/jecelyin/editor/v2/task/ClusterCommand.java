package com.jecelyin.editor.v2.task;

import com.jecelyin.editor.v2.common.Command;
import com.jecelyin.editor.v2.ui.EditorDelegate;

import java.util.ArrayList;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class ClusterCommand {
    private ArrayList<EditorDelegate> buffer;
    private Command command;

    public ClusterCommand(ArrayList<EditorDelegate> buffer) {
        this.buffer = buffer;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void doNextCommand() {
        if (buffer == null || buffer.size() == 0)
            return;
        EditorDelegate editorFragment = buffer.remove(0);
        //无法继续执行时，这里同步手动执行一下
        if (!editorFragment.doCommand(command)) {
            doNextCommand();
        }
    }
}
