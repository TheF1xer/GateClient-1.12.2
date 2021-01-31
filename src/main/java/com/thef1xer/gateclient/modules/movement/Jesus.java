package com.thef1xer.gateclient.modules.movement;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import org.lwjgl.input.Keyboard;

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", "jesus", EnumModuleCategory.MOVEMENT, Keyboard.KEY_J);
    }
}
