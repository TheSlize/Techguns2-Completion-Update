package techguns.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import techguns.TGConfig;
import techguns.Tags;

public class TechgunsConfigGui extends GuiConfig {

    public static List<IConfigElement> getConfigElems() {
        List<IConfigElement> elements = new ArrayList<>();

        for (String categoryName : TGConfig.config.getCategoryNames()) {
            ConfigCategory category = TGConfig.config.getCategory(categoryName);
            if (category != null && !category.getValues().isEmpty()) {
                elements.add(new ConfigElement(category));
            }
        }

        return elements;
    }

    public TechgunsConfigGui(GuiScreen parentScreen) {
        super(parentScreen, getConfigElems(), Tags.MOD_ID, Tags.MOD_ID + ".configID", false, false, Tags.MOD_NAME);
    }


}
