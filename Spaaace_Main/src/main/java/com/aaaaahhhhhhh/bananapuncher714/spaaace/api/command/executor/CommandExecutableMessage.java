package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.command.executor;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.command.CommandParameters;

public class CommandExecutableMessage implements CommandExecutable {
	protected String message;
	
	public CommandExecutableMessage( String message ) {
		this.message = message;
	}
	
	@Override
	public void execute( CommandSender sender, String[] args, CommandParameters params ) {
		sender.sendMessage( message );
	}
}
