package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.command.validator.sender;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SenderValidatorNotPlayer implements SenderValidator {
	@Override
	public boolean isValid( CommandSender sender ) {
		return !( sender instanceof Player );
	}
}
