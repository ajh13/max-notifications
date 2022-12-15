package com.maxnotifications;

import com.google.inject.Provides;
import javax.inject.Inject;

import jdk.internal.joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.apache.commons.lang3.StringUtils;

import java.util.logging.Level;

@Slf4j
@PluginDescriptor(
	name = "Global Notifications"
)
public class MaxNotificationsPlugin extends Plugin
{
	private static final String STAT_NOTIFICATION = "%s has reached %s in %s!!!";
	@Inject
	private Client client;
	@Inject
	private MaxNotificationsConfig config;
	private int statChangedCount = 0;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Global notifications has started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Global notifications has stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
		if (gameStateChanged.getGameState() == GameState.LOGIN_SCREEN)
		{
			statChangedCount = 0;
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged gameStatChanged)
	{
		log.info("Stat change: " + gameStatChanged);
		if (statChangedCount == 0){
			final String username = client.getLocalPlayer().getName();

			final Skill skill = gameStatChanged.getSkill();
			final String skillName = skill.getName().toLowerCase();
			final int level = gameStatChanged.getLevel();
			// Store this in a DB or send a signal to other clients
			client.addChatMessage(ChatMessageType.BROADCAST,
					"GN", String.format(STAT_NOTIFICATION, username, level, skillName), null);
		}

		statChangedCount += 1;
	}

	@Provides
	MaxNotificationsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MaxNotificationsConfig.class);
	}
}
