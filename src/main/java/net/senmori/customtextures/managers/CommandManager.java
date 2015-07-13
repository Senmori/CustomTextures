package net.senmori.customtextures.managers;

import java.util.ArrayList;
import java.util.List;

import net.senmori.customtextures.CustomTextures;
import net.senmori.customtextures.commands.Subcommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor
{
	private CustomTextures plugin;
	private String commandPrefix;
	private List<Subcommand> commands = new ArrayList<Subcommand>();
	
	private static List<CommandManager> managers = new ArrayList<CommandManager>();

	public CommandManager(CustomTextures plugin)
	{
		this.plugin = plugin;
		addCommandManager(this);
		
	}

	public void registerCommand(Subcommand command)
	{
		commands.add(command);
	}

	public void setCommandPrefix(String commandPrefix)
	{
		this.commandPrefix = commandPrefix;
		plugin.getCommand(commandPrefix).setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		List<String> argsList = new ArrayList<String>();
		
		if (args.length > 0)
		{
			String commandName = args[0].toLowerCase();
			
			for (int i = 1; i < args.length; i++)
			{
				argsList.add(args[i]);
			}
			
			for (Subcommand command : commands)
			{
				if (command.getName().equals(commandName) || command.getAliases().contains(commandName))
				{
					command.execute(sender, argsList.toArray(new String[argsList.size()]));
					return true;
				}
			}
		}
		else
		{
			Bukkit.dispatchCommand(sender, "help " + commandPrefix);
		}

		return true;
	}
	
	public String getCommandPrefix()
	{
		return commandPrefix;
	}
	
	public List<Subcommand> getCommands()
	{
		return commands;
	}
	
	public static CommandManager getCommandManager(String prefix)
	{
		for(CommandManager m : managers)
		{
			if(m.commandPrefix.equals(prefix))
			{
				return m;
			}
		}
		
		return null;
	}
	
	public static void addCommandManager(CommandManager m)
	{
		managers.add(m);
	}
	
	public static void removeCommandManager(CommandManager m)
	{
		managers.remove(m);
	}
}