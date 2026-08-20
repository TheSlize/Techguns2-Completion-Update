package techguns.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.jetbrains.annotations.NotNull;
import techguns.*;
import techguns.api.capabilities.ITGExtendedPlayer;

public class TGExtendedPlayerCapProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ITGExtendedPlayer.class)
	public static final Capability<ITGExtendedPlayer> TG_EXTENDED_PLAYER = null;
	
	/**
	 * The ID of this capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(Tags.MOD_ID, "extendedPlayer");
	
	public static final EnumFacing DEFAULT_FACING = null;
	
	private final ITGExtendedPlayer instance; // = TG_EXTENDED_PLAYER.getDefaultInstance();
		
	public TGExtendedPlayerCapProvider(ITGExtendedPlayer caps) {
		this.instance = caps;
	}

	@Override
	public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {
		return capability == TG_EXTENDED_PLAYER; // actually TG_EXTENDED_PLAYER becomes non-null in runtime..
	}

	@Override
	public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing) {
		return capability == TG_EXTENDED_PLAYER ? TG_EXTENDED_PLAYER.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		 return TG_EXTENDED_PLAYER.getStorage().writeNBT(TG_EXTENDED_PLAYER, this.instance, DEFAULT_FACING);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		TG_EXTENDED_PLAYER.getStorage().readNBT(TG_EXTENDED_PLAYER, this.instance, DEFAULT_FACING, nbt);
	}
	
}
