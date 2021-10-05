package me.thef1xer.gateclient.asm.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*
Posts an GetLiquidCollisionBoundingBoxEvent that allows you to change the bounding box
 */
public class BlockLiquidVisitor extends ClassVisitor {
    final String GET_COLLISION;
    final String GET_COLLISION_DESC;

    public BlockLiquidVisitor(ClassVisitor cv, boolean isObfuscated) {
        super(ASM5, cv);
        this.GET_COLLISION = isObfuscated ? "a" : "getCollisionBoundingBox";
        this.GET_COLLISION_DESC = isObfuscated ? "(Lawt;Lamy;Let;)Lbhb;" : "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;";
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(GET_COLLISION) && desc.equals(GET_COLLISION_DESC)) {
            System.out.println("Method " + name + " transformed");
            mv = new GetCollisionBoundingBoxVisitor(mv);
        }
        return mv;
    }

    public static class GetCollisionBoundingBoxVisitor extends MethodVisitor {
        public GetCollisionBoundingBoxVisitor(MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == ARETURN) {
                mv.visitMethodInsn(INVOKESTATIC, "me/thef1xer/gateclient/util/EventFactory", "getCollisionBoundingBox", "()Lnet/minecraft/util/math/AxisAlignedBB;", false);
            }
            super.visitInsn(opcode);
        }
    }
}
