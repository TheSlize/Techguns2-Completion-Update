package techguns.blocks;

import net.minecraft.util.IStringSerializable;
import org.jetbrains.annotations.NotNull;

public enum EnumConcreteType implements IStringSerializable{
	
	CONCRETE_BROWN,
	CONCRETE_BROWN_LIGHT,
	CONCRETE_GREY,
	CONCRETE_GREY_DARK,
	CONCRETE_BROWN_PIPES,
	CONCRETE_BROWN_LIGHT_SCAFF,
	CONCRETE_BROWN_LIGHT_PIPES,
	CONCRETE_GREY_PIPES,
	CONCRETE_GREY_DARK_PIPES;

	@Override
	public @NotNull String getName() {
		return this.name().toLowerCase();
	}

}
