package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import org.lwjgl.input.Keyboard;

public class XRay extends Module {
    public XRay() {
        super("XRay", "xray", EnumModuleCategory.RENDER, Keyboard.KEY_X);
    }
}
