package net.senmori.customtextures.util;

public class Reference {

		public static class Permissions
		{
			// dynamic permissions to determine if a player can use a certain resource pack
			public static String worldResourcePack = "customtextures.worlds.";
			public static String regionResourcePack = "customtextures.regions.";
			
			// disable loading of resource packs if they have this permission
			public static String doNotLoadPermission = "customtextures.disable";
			
			// Commands
			public static String commandEnable = "customtextures.options.enable";
			public static String commandEnableOther = "customtextures.options.enable.others";
			
			public static String commandDisable = "customtextures.options.disable";
			public static String commandDisableOther = "customtextures.options.disable.others";
			
			public static String commandSet = "customtextures.options.set";
			public static String commandRemove = "customtextures.options.remove";
		}

}
