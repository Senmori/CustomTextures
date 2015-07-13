package net.senmori.customtextures.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Subcommand 
{
	protected CommandSender sender;
	protected Player player;
	protected String args[];

	protected String name;
	protected String description;
	
	protected String permission;
	
	protected boolean needsPlayer;
	
	protected List<String> requiredArgs = new ArrayList<String>();
	protected List<String> optionalArgs = new ArrayList<String>();
	protected List<String> aliases = new ArrayList<String>();
	
	protected abstract void perform();
	
	public void execute(CommandSender sender, String[] args)
	{
		this.sender = sender;
		this.args = args;
		
		if(sender instanceof Player)
			this.player = (Player) sender;
		
		if(needsPlayer && !isPlayer())
		{
			return;
		}
		
		if(args.length < requiredArgs.size())
		{
			return;
		}
		
		if(!hasPermission())
		{
			return;
		}
		
		try
		{
			perform();
		}
		catch(Throwable e)
		{
			
		}
	}
	
	public String getUsageTemplate(boolean displayHelp)
	{
		StringBuilder ret = new StringBuilder();

		ret.append(name + " ");

		for (String s : requiredArgs)
			ret.append(String.format("<%s> ", s));

		for (String s : optionalArgs)
			ret.append(String.format("[%s] ", s));

		if (displayHelp)
			ret.append(" - " + description);

		return ret.toString();
	}
	
	public CommandSender getSender() 
	{
		return sender;
	}

	public Player getPlayer() 
	{
		return player;
	}

	public String[] getArgs() 
	{
		return args;
	}

	public String getName() 
	{
		return name;
	}

	public String getDescription() 
	{
		return description;
	}

	public String getPermission() 
	{
		return permission;
	}

	public List<String> getRequiredArgs() 
	{
		return requiredArgs;
	}

	public List<String> getOptionalArgs() 
	{
		return optionalArgs;
	}

	public List<String> getAliases() 
	{
		return aliases;
	}
	
	public boolean hasPermission()
	{
		return sender.hasPermission(permission);
	}
	
	public boolean isPlayer()
	{
		return player != null;
	}
}

