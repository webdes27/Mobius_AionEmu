/*
 * This file is part of the Aion-Emu project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package system.handlers.admincommands;

import java.io.IOException;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Luno
 */
public class SpawnNpc extends AdminCommand
{
	
	public SpawnNpc()
	{
		super("spawn");
	}
	
	@Override
	public void execute(Player admin, String... params)
	{
		if (params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //spawn <template_id> <respawn_time> (0 for temp)");
			return;
		}
		
		int respawnTime = 295;
		
		if (params.length >= 2)
		{
			respawnTime = Integer.valueOf(params[1]);
		}
		
		final int templateId = Integer.parseInt(params[0]);
		final float x = admin.getX();
		final float y = admin.getY();
		final float z = admin.getZ();
		final byte heading = admin.getHeading();
		final int worldId = admin.getWorldId();
		
		final SpawnTemplate spawn = SpawnEngine.addNewSpawn(worldId, templateId, x, y, z, heading, respawnTime);
		
		if (spawn == null)
		{
			PacketSendUtility.sendMessage(admin, "There is no template with id " + templateId);
			return;
		}
		
		final VisibleObject visibleObject = SpawnEngine.spawnObject(spawn, admin.getInstanceId());
		
		if (visibleObject == null)
		{
			PacketSendUtility.sendMessage(admin, "Spawn id " + templateId + " was not found!");
		}
		else if (respawnTime > 0)
		{
			try
			{
				DataManager.SPAWNS_DATA2.saveSpawn(admin, visibleObject, false);
			}
			catch (final IOException e)
			{
				e.printStackTrace();
				PacketSendUtility.sendMessage(admin, "Could not save spawn");
			}
		}
		
		final String objectName = visibleObject.getObjectTemplate().getName();
		PacketSendUtility.sendMessage(admin, objectName + " spawned");
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "syntax //spawn <template_id> <respawn_time> (0 for temp)");
	}
}
