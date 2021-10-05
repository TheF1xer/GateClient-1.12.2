package me.thef1xer.gateclient.asm.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*
Posts an RenderModelEvent every time a block is rendered
 */
public class BlockRendererDispatcherVisitor extends ClassVisitor {
    final String RENDER_MODEL_FLAT;
    final String RENDER_BLOCK_DESCRIPTOR;

    public BlockRendererDispatcherVisitor(ClassVisitor classVisitor, boolean isObfuscated) {
        super(ASM5, classVisitor);
        this.RENDER_MODEL_FLAT = isObfuscated ? "a" : "renderBlock";
        this.RENDER_BLOCK_DESCRIPTOR = isObfuscated ? "(Lawt;Let;Lamy;Lbuk;)Z" : "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z";
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(RENDER_MODEL_FLAT)&& desc.equals(RENDER_BLOCK_DESCRIPTOR)) {
            System.out.println("Method " + name + " transformed");
            mv = new RenderBlockVisitor(mv);
        }
        return mv;
    }

    public static class RenderBlockVisitor extends MethodVisitor {

        public RenderBlockVisitor(MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "me/thef1xer/gateclient/util/EventFactory", "renderBlock", "(Lnet/minecraft/block/state/IBlockState;)Z", false);
            Label l1 = new Label();
            mv.visitJumpInsn(IFEQ, l1);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(17, l2);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
            mv.visitLabel(l1);
            mv.visitLineNumber(19, l1);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
    }
}
