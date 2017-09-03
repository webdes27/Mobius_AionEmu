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
package system.handlers.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/**
 * Author Rinzler (Encom) Asteria Chamber or Carpus Isle Storeroom is a fortress group instance for players of level 40 and above. Players can access this instance from Carpus Isle in Sullenscale Rive on Tuesdays, Thursdays, Saturdays and Sundays. Like other Fortress Instances, monsters inside it
 * will aggro regardless of level. As of 4.9, the entrance was moved from Asteria Fortress to Lower Reshanta, due to the fortress being made inactive. Backstory: As the Balaur dominated the fortress, they found a portal in the inner chambers, leading to a small canyon somewhere in Lake Asteria. This
 * beautiful scene was soon adapted to hold the Balaur's previous treasures. To ease up access, the portal was reshaped as a painting, reflecting the beauty of that sanctum and all the spoils it contains. However, as other factions gained control of the stronghold, they soon found out this hidden
 * area, as well as its guards, hoarding several riches. As Ereshkigal's relics found in Drakenspire Depths released strong energy waves, Reshanta was suffered a shift in its temperature. Allowing the rising of the Dragon Lord's armies, it caused the outer fortresses to be neutralised, sealing the
 * original entrance. With the sudden appearance of the central archipelago of Lower Reshanta, strange portals leading to this chambers were discovered, re-enabling access to the affected treasure rooms. Walkthough: The small area is composed of a central circular platform, connected through three
 * bridges to three narrow treasure room. As soon as players cross the Protective Ward, a 15 minute countdown will begin, indicating when the treasure chests will disappear. Each room holds only one Treasure Box, which can only be opened if a player is in possession of the appropriate key, held by
 * the room's unique mob. Each room is heavily guarded. Nonetheless, players should avoid attacking the room's boss, as it will instantly cause all mobs in the room to aggro on the attacker, making it imperative to clear it out before engaging the boss. Each boss has a different difficulty, turning
 * harder from left to right to the middle. The room on the left is led by a <Dakaer Chanter>, holding the <Golden Abyss Key>, used to open the chest behind him. While this priest Balaur guard does not possess unique skills, its defences and HP pool is larger than the regular one. The room on the
 * right is guarded by <Dakaer Tactician>, holding the <Jeweled Abyss Key>. Also not possessing any unique skills. The main treasure room, on the back, will be guarded by <Ebonlord Kiriel>. His skillset include pulling targets to him, several magical attacks, but most notably, a shield which reduces
 * physical damaged inflicted on him which he will keep throughout the fight, making a magic damage dealer more ideal for this encounter. /
 ****/

@InstanceID(300050000)
public class CarpusIsleStoreroomInstance extends GeneralInstanceHandler
{
	private Future<?> carpusIsleStoreroomTask;
	private boolean isStartTimer = false;
	private final List<Npc> CarpusIsleStoreroomTreasureBoxSuscess = new ArrayList<>();
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		spawnCarpusIsleStoreroomRings();
	}
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId)
		{
			case 214762: // Dakaer Tactician.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000033, 1)); // Golden Abyss Key.
					}
				}
				break;
			case 214766: // Dakaer Chanter.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000034, 1)); // Jeweled Abyss Key.
					}
				}
				break;
			case 215444: // Ebonlord Kiriel.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000035, 1)); // Magic Abyss Key.
					}
				}
				break;
		}
	}
	
	private void spawnCarpusIsleStoreroomRings()
	{
		final FlyRing f1 = new FlyRing(new FlyRingTemplate("CARPUS_ISLE_STOREROOM", mapId, new Point3D(479.24, 572.57, 202.72), new Point3D(477.95, 567.64, 212.9), new Point3D(477.97, 563.35, 202.12), 10), instanceId);
		f1.spawn();
	}
	
	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing)
	{
		if (flyingRing.equals("CARPUS_ISLE_STOREROOM"))
		{
			if (!isStartTimer)
			{
				isStartTimer = true;
				System.currentTimeMillis();
				instance.doOnAllPlayers(new Visitor<Player>()
				{
					@Override
					public void visit(Player player)
					{
						if (player.isOnline())
						{
							startCarpusIsleStoreroomChamberTimer();
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900));
							// The Balaur protective magic ward has been activated.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_START_IDABRE);
						}
					}
				});
			}
		}
		return false;
	}
	
	@Override
	public void onEnterInstance(final Player player)
	{
		super.onInstanceCreate(instance);
		CarpusIsleStoreroomTreasureBoxSuscess.add((Npc) spawn(700475, 524.4908f, 706.2591f, 191.8985f, (byte) 90));
		CarpusIsleStoreroomTreasureBoxSuscess.add((Npc) spawn(700476, 522.22754f, 421.55646f, 199.75935f, (byte) 29));
		CarpusIsleStoreroomTreasureBoxSuscess.add((Npc) spawn(700477, 671.581f, 565.1735f, 206.14534f, (byte) 60));
	}
	
	private void startCarpusIsleStoreroomChamberTimer()
	{
		carpusIsleStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				// All Balaur treasure chests have disappeared.
				sendMsg(1400244);
				CarpusIsleStoreroomTreasureBoxSuscess.get(0).getController().onDelete();
				CarpusIsleStoreroomTreasureBoxSuscess.get(1).getController().onDelete();
				CarpusIsleStoreroomTreasureBoxSuscess.get(2).getController().onDelete();
			}
		}, 900000); // 15 Minutes.
	}
	
	private void sendMsg(final String str)
	{
		instance.doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				PacketSendUtility.sendMessage(player, str);
			}
		});
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		removeItems(player);
	}
	
	@Override
	public void onPlayerLogOut(Player player)
	{
		removeItems(player);
	}
	
	private void removeItems(Player player)
	{
		final Storage storage = player.getInventory();
		storage.decreaseByItemId(185000033, storage.getItemCountByItemId(185000033)); // Golden Abyss Key.
		storage.decreaseByItemId(185000034, storage.getItemCountByItemId(185000034)); // Jeweled Abyss Key.
		storage.decreaseByItemId(185000035, storage.getItemCountByItemId(185000035)); // Magic Abyss Key.
	}
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 469.343f, 567.713f, 201.732f, (byte) 0);
	}
	
	@Override
	public boolean onDie(final Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}