package com.maxnotifications;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("max notifications")
public interface MaxNotificationsConfig extends Config
{
	@ConfigItem(
		keyName = "keyName dummy",
		name = "Name dummy",
		description = "Desc dummy"
	)
	default String greeting()
	{
		return "This is a greetings";
	}
}
