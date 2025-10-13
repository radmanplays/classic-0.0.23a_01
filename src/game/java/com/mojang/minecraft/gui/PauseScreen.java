package com.mojang.minecraft.gui;

public final class PauseScreen extends Screen {
	public final void init() {
		this.buttons.clear();
		this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4, "Options..."));
		this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 24, "Generate new level..."));
		this.buttons.add(new Button(2, this.width / 2 - 100, this.height / 4 + 48, "Save level.."));
		this.buttons.add(new Button(3, this.width / 2 - 100, this.height / 4 + 72, "Load level.."));
		this.buttons.add(new Button(4, this.width / 2 - 100, this.height / 4 + 120, "Back to game"));
		if(this.minecraft.user == null) {
			((Button)this.buttons.get(2)).enabled = false;
			((Button)this.buttons.get(3)).enabled = false;
		}

		if(this.minecraft.connectionManager != null) {
			((Button)this.buttons.get(1)).enabled = false;
			((Button)this.buttons.get(2)).enabled = false;
			((Button)this.buttons.get(3)).enabled = false;
		}

	}

	protected final void buttonClicked(Button var1) {
		if(var1.id == 0) {
			this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
		}

		if(var1.id == 1) {
			this.minecraft.setScreen(new NewLevelScreen(this));
		}

		if(var1.id == 4) {
			this.minecraft.setScreen((Screen)null);
			this.minecraft.grabMouse();
		}

	}

	public final void render(int var1, int var2) {
		fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.font, "Game menu", this.width / 2, 40, 16777215);
		super.render(var1, var2);
	}
}
