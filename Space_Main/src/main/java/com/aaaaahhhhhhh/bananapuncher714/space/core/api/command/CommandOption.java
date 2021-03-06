package com.aaaaahhhhhhh.bananapuncher714.space.core.api.command;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.command.executor.CommandExecutable;

public class CommandOption {
	protected CommandExecutable exe;
	protected String[] args;
	protected CommandParameters parameter;
	
	public CommandOption( CommandExecutable exe, String[] args, CommandParameters parameter ) {
		this.exe = exe;
		this.args = args;
		this.parameter = parameter;
	}
	
	public int getArgumentSize() {
		return args.length;
	}
	
	public void execute( CommandSender sender ) {
		exe.execute( sender, args, parameter );
	}
}
