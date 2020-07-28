
package com.jecelyin.editor.v2.widget;

/**
 * Interface used to communicate from the v21-specific code for configuring a DrawerLayout
 * to the DrawerLayout itself.
 */
interface DrawerLayoutImpl {
    void setChildInsets(Object insets, boolean drawStatusBar);
}
