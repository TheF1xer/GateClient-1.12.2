package me.thef1xer.gateclient.asm.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*
Posts a ShouldSideBeRendered and a GetAmbientOcclusionLightValue event
 */
public class BlockStateContainerVisitor extends ClassVisitor {
    final String SHOULD_SIDE;
    final String SHOULD_SIDE_DESCRIPTOR;

    final String GET_LIGHT;
    final String GET_LIGHT_DESCRIPTOR;

    public BlockStateContainerVisitor(ClassVisitor cv, boolean isObfuscated) {
        super(ASM5, cv);

        this.SHOULD_SIDE = isObfuscated ? "c" : "shouldSideBeRendered";
        this.SHOULD_SIDE_DESCRIPTOR = isObfuscated ? "(Lamy;Let;Lfa;)Z" : "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z";

        this.GET_LIGHT = isObfuscated ? "j" : "getAmbientOcclusionLightValue";
        this.GET_LIGHT_DESCRIPTOR = "()F";
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(SHOULD_SIDE) && desc.equals(SHOULD_SIDE_DESCRIPTOR)) {
            mv = new ShouldSideBeRenderedVisitor(mv);
            System.out.println("Method " + name + " transformed");
        } else if (name.equals(GET_LIGHT) && desc.equals(GET_LIGHT_DESCRIPTOR)) {
            mv = new GetAmbientOcclusionLightValueVisitor(mv);
            System.out.println("Method " + name + " transformed");
        }
        return mv;
    }

    public static class ShouldSideBeRenderedVisitor extends MethodVisitor {
        public ShouldSideBeRenderedVisitor(MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == IRETURN) {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitVarInsn(ALOAD, 3);
                mv.visitMethodInsn(INVOKESTATIC, "com/thef1xer/gateclient/util/EventFactory", "shouldSideBeRendered", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", false);
            }
            super.visitInsn(opcode);
        }
    }

    public static class GetAmbientOcclusionLightValueVisitor extends MethodVisitor {
        public GetAmbientOcclusionLightValueVisitor(MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == FRETURN){
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESTATIC, "com/thef1xer/gateclient/util/EventFactory", "getAmbientOcclusionLightValue","(Lnet/minecraft/block/state/IBlockState;)F", false);
            }

            super.visitInsn(opcode);
        }
    }
}
