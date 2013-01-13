// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.slimevoid;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.EnumOptions;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.StringTranslate;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, EnumOptions, GuiSmallButton, 
//            GameSettings, GuiSlider, GuiButton, ScaledResolution

public class GuiSlimeVoidSettings extends GuiScreen
{

    public GuiSlimeVoidSettings(GuiScreen guiscreen, GameSettings gamesettings)
    {
        parentGuiScreen = guiscreen;
        guiGameSettings = gamesettings;
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        title = stringtranslate.translateKey("options.slimevoid");
        
        int yOffset = 0;
        int dynId = 100;
/*        for (mod_SlimeVoid mod : mod_SlimeVoid.modsList) {
        	if (mod.guiSettings != null) {
        		yOffset = yOffset + 20;
        		controlList.add(new GuiButton(dynId, width / 2 - 100, height / 6 + 168 - yOffset, mod.getName()));
        		dynId = dynId + 1;
        	}
        }*/
        controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, stringtranslate.translateKey("gui.done")));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        int i = guiGameSettings.guiScale;
        if(guibutton.id < 100 && (guibutton instanceof GuiSmallButton))
        {
            guiGameSettings.setOptionValue(((GuiSmallButton)guibutton).returnEnumOptions(), 1);
            guibutton.displayString = guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
        }
        if (guibutton.id >= 100 && guibutton.id < 200){
        	mc.gameSettings.saveOptions();
        	//mc.displayGuiScreen(mod_SlimeVoid.modsList.get(guibutton.id - 100).guiSettings);
        }
        if(guibutton.id == 200)
        {
            mc.gameSettings.saveOptions();
            mc.displayGuiScreen(parentGuiScreen);
        }
        if(guiGameSettings.guiScale != i)
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int j = scaledresolution.getScaledWidth();
            int k = scaledresolution.getScaledHeight();
            setWorldAndResolution(mc, j, k);
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, title, width / 2, 20, 0xffffff);
        super.drawScreen(i, j, f);
    }

    private GuiScreen parentGuiScreen;
    protected String title;
    private GameSettings guiGameSettings;
    private static EnumOptions videoOptions[];

}
