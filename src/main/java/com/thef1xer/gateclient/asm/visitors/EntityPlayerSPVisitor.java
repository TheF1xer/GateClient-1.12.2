package com.thef1xer.gateclient.asm.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class EntityPlayerSPVisitor extends ClassVisitor {
    final String IS_USER;
    final String IS_USER_DESC;

    public EntityPlayerSPVisitor(ClassVisitor cv, boolean isObfuscated) {
        super(ASM5, cv);
        this.IS_USER = isObfuscated ? "cZ" : "isUser";
        this.IS_USER_DESC = "()Z";
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(IS_USER) && desc.equals(IS_USER_DESC)) {
            System.out.println("Method " + name + " transformed");
            mv = new IsUserVisitor(mv);
        }
        return mv;
    }

    public static class IsUserVisitor extends MethodVisitor {
        public IsUserVisitor(MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == IRETURN) {
                mv.visitMethodInsn(INVOKESTATIC, "com/thef1xer/gateclient/util/EventFactory", "isUser", "()Z", false);
            }
            super.visitInsn(opcode);
        }
    }
}
