package me.thef1xer.gateclient.asm.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*
Changes both occurrences of "this.mc.player.turn(f2, f3 * (float)i)" in the "updateCameraAndRender" method to
"Minecraft.getMinecraft().getRenderViewEntity().turn(f2, f3 * (float)i)" in order to make the Freecam Module work

THIS BARELY WORKS AND I NEED TO REWRITE EVERYTHING BUT I JUST DON'T KNOW HOW WOULD IT BE BETTER
 */

public class EntityRendererVisitor extends ClassVisitor {
    final String UPDATE_CAMERA;
    final String UPDATE_CAMERA_DESCRIPTOR;
    boolean isObfuscated;

    public EntityRendererVisitor(ClassVisitor classVisitor, boolean isObfuscated) {
        super(ASM5, classVisitor);
        UPDATE_CAMERA = isObfuscated ? "a" : "updateCameraAndRender";
        UPDATE_CAMERA_DESCRIPTOR = isObfuscated ? "(FJ)V" : "(FJ)V";
        this.isObfuscated = isObfuscated;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(UPDATE_CAMERA) && desc.equals(UPDATE_CAMERA_DESCRIPTOR)) {
            mv = new UpdateCameraAndRendererVisitor(mv, isObfuscated);
            System.out.println("Method " + UPDATE_CAMERA + " transformed");
        }
        return mv;
    }

    public static class UpdateCameraAndRendererVisitor extends MethodVisitor{
        boolean line1033;
        boolean line1039;
        boolean isObfuscated;

        public UpdateCameraAndRendererVisitor(MethodVisitor mv, boolean isObfuscated) {
            super(ASM5, mv);
            this.isObfuscated = isObfuscated;
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            super.visitLineNumber(line, start);
            line1033 = line == 1033;
            line1039 = line == 1039;
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            if ((line1033 || line1039) && owner.equals(isObfuscated ? "bib" : "net/minecraft/client/Minecraft")) {
                mv.visitMethodInsn(INVOKEVIRTUAL, isObfuscated ? "bib" : "net/minecraft/client/Minecraft", isObfuscated ? "aa" : "getRenderViewEntity", isObfuscated ? "()Lvg;" : "()Lnet/minecraft/entity/Entity;", false);
            } else {
                super.visitFieldInsn(opcode, owner, name, desc);
            }
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (line1033 || line1039) {
                mv.visitMethodInsn(INVOKEVIRTUAL, isObfuscated ? "vg" : "net/minecraft/entity/Entity", isObfuscated ? "c" : "turn", "(FF)V", false);
            } else {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
            }
        }
    }
}
