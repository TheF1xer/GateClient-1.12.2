package me.thef1xer.gateclient.asm.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*
Posts an SetOpaqueCubeEvent to the Event Bus when a Cube is set to Opaque
 */

public class VisGraphVisitor extends ClassVisitor {
    final String SET_OPAQUE;
    final String SET_OPAQUE_DESCRIPTOR;

    public VisGraphVisitor(ClassVisitor classVisitor, boolean isObfuscated) {
        super(ASM5, classVisitor);
        SET_OPAQUE = isObfuscated ? "a" : "setOpaqueCube";
        SET_OPAQUE_DESCRIPTOR = isObfuscated ? "(Let;)V" : "(Lnet/minecraft/util/math/BlockPos;)V";
    }

    @Override
    public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
        MethodVisitor mv = cv.visitMethod(i, s, s1, s2, strings);
        if (s.equals(SET_OPAQUE) && s1.equals(SET_OPAQUE_DESCRIPTOR)) {
            mv = new SetOpaqueCubeVisitor(mv);
            System.out.println("Method " + SET_OPAQUE + " transformed");
        }
        return mv;
    }

    public static class SetOpaqueCubeVisitor extends MethodVisitor {

        public SetOpaqueCubeVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(29, l0);
            mv.visitMethodInsn(INVOKESTATIC, "com/thef1xer/gateclient/util/EventFactory", "setOpaqueCube", "()Z", false);
            Label l1 = new Label();
            mv.visitJumpInsn(IFEQ, l1);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(30, l2);
            mv.visitInsn(RETURN);
            mv.visitLabel(l1);
            mv.visitLineNumber(32, l1);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
    }
}
