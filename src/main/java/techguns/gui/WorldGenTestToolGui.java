package techguns.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import techguns.TGPackets;
import techguns.packets.PacketSetStructure;
import techguns.world.StructureRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class WorldGenTestToolGui extends GuiScreen {

    private GuiTextField textField;
    private List<String> hints = new ArrayList<>();
    private int hintIndex = 0;
    private static final int MAX_HINTS = 5;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        StructureRegistry.init();

        int textFieldWidth = 200;
        int textFieldHeight = 20;
        int x = (this.width - textFieldWidth) / 2;
        int y = this.height / 2;

        this.textField = new GuiTextField(0, this.fontRenderer, x, y, textFieldWidth, textFieldHeight);
        this.textField.setMaxStringLength(100);
        this.textField.setFocused(true);

        updateHints();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int hintCount = Math.min(hints.size(), MAX_HINTS);
        int baseHintY = textField.y - 5;

        // Draw hints above the text field (from bottom to top)
        for (int i = 0; i < hintCount; i++) {
            String hint = hints.get(i);
            int hintY = baseHintY - ((i + 1) * 12);

            if (i == hintIndex) {
                this.drawCenteredString(this.fontRenderer, TextFormatting.YELLOW + "> " + hint + " <", this.width / 2, hintY, 0xFFFF55);
            } else {
                this.drawCenteredString(this.fontRenderer, hint, this.width / 2, hintY, 0xAAAAAA);
            }
        }

        // Draw label above hints
        int labelY = baseHintY - ((hintCount + 1) * 12) - 5;
        this.drawCenteredString(this.fontRenderer, "Enter Structure Name:", this.width / 2, labelY, 0xFFFFFF);

        // Draw instructions below text field
        this.drawCenteredString(this.fontRenderer, TextFormatting.GRAY + "[Tab] Autocomplete  [Enter] Confirm  [Esc] Cancel", this.width / 2, textField.y + textField.height + 10, 0x888888);

        this.textField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
            return;
        }

        if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
            String input = textField.getText().trim();

            if (!input.isEmpty() && StructureRegistry.isValidStructure(input)) {
                String exactName = StructureRegistry.getExactName(input);
                TGPackets.wrapper.sendToServer(new PacketSetStructure(exactName));
                if (this.mc.player != null) {
                    this.mc.player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Structure set to: " + TextFormatting.YELLOW + exactName));
                }
                this.mc.displayGuiScreen(null);
            }
            return;
        }

        if (keyCode == Keyboard.KEY_TAB) {
            if (!hints.isEmpty()) {
                String selectedHint = hints.get(hintIndex);
                textField.setText(selectedHint);
                textField.setCursorPositionEnd();
                hintIndex = (hintIndex + 1) % Math.min(hints.size(), MAX_HINTS);
                updateHints();
            }
            return;
        }

        if (keyCode == Keyboard.KEY_UP) {
            if (!hints.isEmpty()) {
                hintIndex = (hintIndex + 1) % Math.min(hints.size(), MAX_HINTS);
            }
            return;
        }

        if (keyCode == Keyboard.KEY_DOWN) {
            if (!hints.isEmpty()) {
                hintIndex = (hintIndex - 1 + Math.min(hints.size(), MAX_HINTS)) % Math.min(hints.size(), MAX_HINTS);
            }
            return;
        }

        this.textField.textboxKeyTyped(typedChar, keyCode);
        hintIndex = 0;
        updateHints();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        this.textField.updateCursorCounter();
    }

    private void updateHints() {
        String input = textField.getText();
        if (input.isEmpty()) {
            hints = new ArrayList<>(StructureRegistry.getStructureNames());
        } else {
            hints = StructureRegistry.getMatchingNames(input);
        }
        if (hintIndex >= Math.min(hints.size(), MAX_HINTS)) {
            hintIndex = 0;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}