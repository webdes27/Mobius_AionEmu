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
package system.handlers.ai.worlds.iluma;

import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.windstreams.Location2D;
import com.aionemu.gameserver.model.templates.windstreams.WindstreamTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_WINDSTREAM_ANNOUNCE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("LF6_WindStream")
public class LF6_WindStreamAI2 extends NpcAI2
{
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		final Npc npc = getOwner();
		startWindStream(npc);
		announceWindPathInvasion();
		windStreamAnnounce(getOwner(), 0);
	}
	
	private void startWindStream(final Npc npc)
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				final Npc npc2 = (Npc) spawn(210100000, 857863, 1494.05f, 1411.85f, 335.94f, (byte) 0, 0, 1);
				windStreamAnnounce(npc2, 1);
				despawnNpc(857862);
				spawn(857863, 1494.4084f, 1411.7194f, 331.51108f, (byte) 0, 1281);
				PacketSendUtility.broadcastPacket(npc2, new SM_WINDSTREAM_ANNOUNCE(1, 210100000, 302, 1));
				if (npc2 != null)
				{
					npc2.getController().onDelete();
				}
				if (npc != null)
				{
					npc.getController().onDelete();
				}
			}
		}, 5000);
	}
	
	private void announceWindPathInvasion()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				// The wind road to the defense frigate will vanish in 30 seconds.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_G1_Windpath_Off_01, 30000);
			}
		});
	}
	
	private void windStreamAnnounce(final Npc npc, final int state)
	{
		final WindstreamTemplate template = DataManager.WINDSTREAM_DATA.getStreamTemplate(npc.getPosition().getMapId());
		for (final Location2D wind : template.getLocations().getLocation())
		{
			if (wind.getId() == 302)
			{
				wind.setState(state);
				break;
			}
		}
		npc.getPosition().getWorld().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				PacketSendUtility.sendPacket(player, new SM_WINDSTREAM_ANNOUNCE(1, 210100000, 302, state));
			}
		});
	}
	
	private void despawnNpc(int npcId)
	{
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null)
		{
			final List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (final Npc npc : npcs)
			{
				npc.getController().onDelete();
			}
		}
	}
}