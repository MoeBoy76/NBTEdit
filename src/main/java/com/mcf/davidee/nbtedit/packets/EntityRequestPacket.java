package com.mcf.davidee.nbtedit.packets;

import com.mcf.davidee.nbtedit.NBTEdit;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.Level;

public class EntityRequestPacket implements IMessage {
	/** The id of the entity being requested. */
	private int entityID;

	/** Required default constructor. */
	public EntityRequestPacket() {}

	public EntityRequestPacket(int entityID) {
		this.entityID = entityID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityID);
	}

	public static class Handler implements IMessageHandler<EntityRequestPacket, IMessage> {

		@Override
		public IMessage onMessage(EntityRequestPacket packet, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			NBTEdit.log(Level.TRACE, player.getName() + " requested entity with Id #" + packet.entityID);
			NBTEdit.NETWORK.sendEntity(player, packet.entityID);
			return null;
		}
	}
}
