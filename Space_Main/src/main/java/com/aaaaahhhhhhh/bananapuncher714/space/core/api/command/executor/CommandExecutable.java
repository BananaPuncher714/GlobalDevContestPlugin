package com.aaaaahhhhhhh.bananapuncher714.space.core.api.command.executor;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.command.CommandParameters;

public interface CommandExecutable {
	void execute( CommandSender sender, String[] args, CommandParameters params );
}
