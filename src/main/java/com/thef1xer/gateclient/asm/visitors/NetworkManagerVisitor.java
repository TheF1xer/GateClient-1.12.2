package com.thef1xer.gateclient.asm.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/*
Posts an PacketEvent every time that a Packet is being sent and received
 */
public class NetworkManagerVisitor extends ClassVisitor {
    final String SEND_PACKET;
    final String SEND_PACKET_DESCRIPTOR;
    final String SEND_PACKET_DESCRIPTOR2;

    final String CHANNEL_READ_0;
    final String CHANNEL_READ_0_DESCRIPTOR;

    public NetworkManagerVisitor(ClassVisitor classVisitor, boolean isObfuscated) {
        super(ASM5, classVisitor);
        this.SEND_PACKET = isObfuscated ? "a" : "sendPacket";
        this.SEND_PACKET_DESCRIPTOR = isObfuscated ? "(Lht;)V" : "(Lnet/minecraft/network/Packet;)V";
        this.SEND_PACKET_DESCRIPTOR2 = isObfuscated ? "(Lht;Lio/netty/util/concurrent/GenericFutureListener;[Lio/netty/util/concurrent/GenericFutureListener;)V" : "(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;[Lio/netty/util/concurrent/GenericFutureListener;)V";

        this.CHANNEL_READ_0 = isObfuscated ? "a" : "channelRead0";
        this.CHANNEL_READ_0_DESCRIPTOR = isObfuscated ? "(Lio/netty/channel/ChannelHandlerContext;Lht;)V" : "(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V";
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(SEND_PACKET) && (desc.equals(SEND_PACKET_DESCRIPTOR) || desc.equals(SEND_PACKET_DESCRIPTOR2))) {
            mv = new SendPacketVisitor(mv);
            System.out.println("Method " + SEND_PACKET + " transformed");
        } else if (name.equals(CHANNEL_READ_0) && desc.equals(CHANNEL_READ_0_DESCRIPTOR)) {
            mv = new ChannelRead0Visitor(mv);
        }
        return mv;
    }

    public static class SendPacketVisitor extends MethodVisitor {

        public SendPacketVisitor(MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "com/thef1xer/gateclient/util/EventFactory", "onPacket", "(Lnet/minecraft/network/Packet;)Lnet/minecraft/network/Packet;", false);
            mv.visitVarInsn(ASTORE, 1);
            mv.visitVarInsn(ALOAD, 1);
            Label l0 = new Label();
            mv.visitJumpInsn(IFNONNULL, l0);
            mv.visitInsn(RETURN);
            mv.visitLabel(l0);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
    }

    public static class ChannelRead0Visitor extends MethodVisitor {

        public ChannelRead0Visitor(MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKESTATIC, "com/thef1xer/gateclient/util/EventFactory", "onPacket", "(Lnet/minecraft/network/Packet;)Lnet/minecraft/network/Packet;", false);
            mv.visitVarInsn(ASTORE, 2);
            mv.visitVarInsn(ALOAD, 2);
            Label l0 = new Label();
            mv.visitJumpInsn(IFNONNULL, l0);
            mv.visitInsn(RETURN);
            mv.visitLabel(l0);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
    }
}
