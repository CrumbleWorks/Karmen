package org.crumbleworks.forge.karmen.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.crumbleworks.forge.karmen.Karmen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "NNPH";
		config.addIcon("gfx/GrpLogo32x32.png", FileType.Internal);
		
		new LwjglApplication(new Karmen(), config);
	}
}
