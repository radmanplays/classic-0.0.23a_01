package net.lax1dude.eaglercraft.internal;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.User;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EagUtils;
import net.lax1dude.eaglercraft.socket.AddressResolver;
import net.lax1dude.eaglercraft.socket.AddressResolver.ServerInfo;
import net.peytonsound.ResourceLoader;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class LWJGLEntryPoint {

	public static Thread mainThread = null;

	public static void main_(String[] args) {
		mainThread = Thread.currentThread();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("Could not set system look and feel: " + e.toString());
		}

		boolean hideRenderDocDialog = false;
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equalsIgnoreCase("hide-renderdoc")) {
				hideRenderDocDialog = true;
			}
		}

		if (!hideRenderDocDialog) {
			LaunchRenderDocDialog lr = new LaunchRenderDocDialog();
			lr.setLocationRelativeTo(null);
			lr.setVisible(true);

			while (lr.isVisible()) {
				EagUtils.sleep(100);
			}

			lr.dispose();
		}

		getPlatformOptionsFromArgs(args);

		EagRuntime.create();

		Minecraft minecraft = new Minecraft(854, 480, false);

		ServerInfo serverInfo = null;
		String username = null;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("--server") && i + 1 < args.length) {
				serverInfo = AddressResolver.resolveURI(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("--username") && i + 1 < args.length) {
				username = args[i + 1];
			}
		}
		if(serverInfo != null) {
			if (username != null) {
				minecraft.user = new User(username, "");
				System.out.println("Using username: " + username);
			}
	
			if (serverInfo.ip != null) {
				minecraft.setServer(serverInfo.ip);
				System.out.println("Connecting to server " + serverInfo.ip);
			}
		}
		
		File[] f = new File("resources").listFiles();
		
		for(File f1 : f) {
			loadResource(f1);
		}
		
		(new Thread(minecraft)).run();

	}
	
	private static void loadResource(File file) {
		if(file.isDirectory()) {
			for(File f : file.listFiles()) {
				loadResource(f);
			}
		} else {
			ResourceLoader.onResourceLoad(file.getPath().replace("\\", "/"));
		}
	}

	private static void getPlatformOptionsFromArgs(String[] args) {
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equalsIgnoreCase("fullscreen")) {
				PlatformInput.setStartupFullscreen(true);
			} else if (args[i].equalsIgnoreCase("gles=200")) {
				PlatformRuntime.requestGL(200);
			} else if (args[i].equalsIgnoreCase("gles=300")) {
				PlatformRuntime.requestGL(300);
			} else if (args[i].equalsIgnoreCase("gles=310")) {
				PlatformRuntime.requestGL(310);
			} else if (args[i].equalsIgnoreCase("disableKHRDebug")) {
				PlatformRuntime.requestDisableKHRDebug(true);
			} else {
				EnumPlatformANGLE angle = EnumPlatformANGLE.fromId(args[i]);
				if (angle != EnumPlatformANGLE.DEFAULT) {
					PlatformRuntime.requestANGLE(angle);
				}
			}
		}
	}

}