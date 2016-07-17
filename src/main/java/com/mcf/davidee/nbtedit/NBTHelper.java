package com.mcf.davidee.nbtedit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.*;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NBTHelper {
	
	public static NBTTagCompound nbtRead(DataInputStream in) throws IOException {
		return CompressedStreamTools.read(in);
	}
	
	public static void nbtWrite(NBTTagCompound compound, DataOutput out) throws IOException {
		CompressedStreamTools.write(compound, out);
	}
	
	public static Map<String,NBTBase> getMap(NBTTagCompound tag){
		return ReflectionHelper.getPrivateValue(NBTTagCompound.class, tag, 0);
	}
	
	public static NBTBase getTagAt(NBTTagList tag, int index) {
		List<NBTBase> list = ReflectionHelper.getPrivateValue(NBTTagList.class, tag, 1);
		return list.get(index);
	}

	public static void writeToBuffer(NBTTagCompound nbt, ByteBuf buf) {
		if (nbt == null) {
			buf.writeByte(0);
		} else {
			try {
				CompressedStreamTools.write(nbt, new ByteBufOutputStream(buf));
			} catch (IOException e) {
				throw new EncoderException(e);
			}
		}
	}

	public static NBTTagCompound readNbtFromBuffer(ByteBuf buf) {
		int index = buf.readerIndex();
		byte isNull = buf.readByte();

		if (isNull == 0) {
			return null;
		} else {
			// restore index after checking to make sure the tag wasn't null/
			buf.readerIndex(index);
			try {
				return CompressedStreamTools.read(new ByteBufInputStream(buf), new NBTSizeTracker(2097152L));
			} catch (IOException ioexception) {
				throw new EncoderException(ioexception);
			}
		}
	}
}
