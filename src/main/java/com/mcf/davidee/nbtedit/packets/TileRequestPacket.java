package com.mcf.davidee.nbtedit.packets;

import com.mcf.davidee.nbtedit.NBTEdit;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.Level;

public class TileRequestPacket implements IMessage {
	/** The position of the tileEntity requested. */
	private BlockPos pos;

	/** Required default constructor. */
	public TileRequestPacket() {}
	
	public TileRequestPacket(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
	}

	public static class Handler implements IMessageHandler<TileRequestPacket, IMessage> {

		@Override
		public IMessage onMessage(TileRequestPacket packet, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			NBTEdit.log(Level.TRACE, player.getName() + " requested tileEntity at "
					+ packet.pos.getX() + ", " + packet.pos.getY() + ", " + packet.pos.getZ());
			NBTEdit.NETWORK.sendTile(player, packet.pos);
			return null;
		}
	}

}
