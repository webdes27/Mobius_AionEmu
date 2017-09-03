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

import com.aionemu.commons.utils.Rnd;
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
 * Author Rinzler (Encom) Chamber of Roah or Hamate Isle Storeroom is a fortress group instance for players of level 40 and above. Its entrance is located in Hamate Isle in Sullenscale Rive on Mondays, Wednesdays, Fridays and Sundays. Like other fortress instances, monsters inside it will aggro
 * regardless of the level of the party. As of 4.9, the entrance was moved from Roah Fortress in Upper Reshanta to Lower Reshanta due to the fortress becoming inactive. Backstory: When Balaur held the fortress, they built an inner cave in the deepest parts of the floating island. Within these
 * chambers, they stored their treasures. Due to not being directly connected to the outside, they created a teleport statue, acting as a portal to the hoard. However, as Balaur lost the fortress to Daevas, this secret strongbox was discovered. Daevas accessed this area, but were surprised by the
 * presence of the Balaur guards. They now must venture in, making their way through the remnants, to acquire those amazing riches. As Ereshkigal's relics found in Drakenspire Depths released strong energy waves, Reshanta was suffered a shift in its temperature. Allowing the rising of the Dragon
 * Lord's armies, it caused the outer fortresses to be neutralised, sealing the original entrance. With the sudden appearance of the central archipelago of Lower Reshanta, strange portals leading to this chambers were discovered re-enabling access to the affected treasure rooms. Walkthrough: The
 * instance is composed of the drop point, which is connected to the main hall, which branches into three bridges leading to their respective wings. When players step out of the drop point, a 15-minutes countdown will begin, which when ends will force treasure chests to despawn. Each chamber holds
 * one treasure box, which can be opened with the key dropped from the chamber's head guard. A bigger chest may spawn sometimes spawn in the main hall, in front of the bridge leading to the northern chamber. If players attack the unique mob of the room, all guards which have not been cleared already
 * will attack them, making it imperative to clear the whole room before engaging the fight. Each room has a different difficulty, varying from easy (western room), to medium (eastern room) to hard (northern room). The western room is guarded by a random Naga/Nagarant guard, holding the <Golden
 * Ruins of Roah Key>, which can be used to open the treasure chest behind it, In the eastern room players will find a Drakan guard, holding the <Jeweled Ruins of Roah Key>, also used to open the chest behind the unique mob. Laslty, players may reach the northern chamber, guarded by <Protector
 * Kael>, holding the <Magic Ruins of Roah Key>. This boss will constantly cast shield, reducing physical damage inflicted on him, as well as slowing his enemies' attack and movement speed. /
 ****/

@InstanceID(300070000)
public class HamateIsleStoreroomInstance extends GeneralInstanceHandler
{
	private Future<?> hamateIsleStoreroomTask;
	private boolean isStartTimer = false;
	private final List<Npc> HamateIsleStoreroomTreasureBoxSuscess = new ArrayList<>();
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		spawnHamateIsleStoreroomRings();
		switch (Rnd.get(1, 2))
		{
			case 1:
				spawn(214780, 381.35986f, 510.61307f, 102.618126f, (byte) 111); // Dakaer Diabolist.
				break;
			case 2:
				spawn(214781, 381.35986f, 510.61307f, 102.618126f, (byte) 111); // Dakaer Bloodmender.
				break;
		}
		switch (Rnd.get(1, 2))
		{
			case 1:
				spawn(214782, 625.4933f, 455.0907f, 102.63267f, (byte) 47); // Dakaer Adjutant.
				break;
			case 2:
				spawn(214784, 625.4933f, 455.0907f, 102.63267f, (byte) 47); // Dakaer Physician.
				break;
		}
		switch (Rnd.get(1, 2))
		{
			case 1:
				spawn(215449, 503.947f, 623.82227f, 103.695724f, (byte) 90); // Relic Protector Kael.
				break;
			case 2:
				spawn(215450, 503.947f, 623.82227f, 103.695724f, (byte) 90); // Ebonlord Vasana.
				break;
		}
	}
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId)
		{
			case 214780: // Dakaer Diabolist.
			case 214781: // Dakaer Bloodmender.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000036, 1)); // Golden Ruins Of Roah Key.
					}
				}
				break;
			case 214782: // Dakaer Adjutant.
			case 214784: // Dakaer Physician.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000037, 1)); // Jeweled Ruins Of Roah Key.
					}
				}
				break;
			case 215449: // Relic Protector Kael.
			case 215450: // Ebonlord Vasana.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000038, 1)); // Magic Ruins Of Roah Key.
					}
				}
				break;
		}
	}
	
	private void spawnHamateIsleStoreroomRings()
	{
		final FlyRing f1 = new FlyRing(new FlyRingTemplate("HAMATE_ISLE_STOREROOM", mapId, new Point3D(501.77, 409.53, 94.12), new Point3D(503.93, 409.65, 98.9), new Point3D(506.26, 409.7, 94.15), 10), instanceId);
		f1.spawn();
	}
	
	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing)
	{
		if (flyingRing.equals("HAMATE_ISLE_STOREROOM"))
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
							startHamateIsleStoreroomTimer();
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
		HamateIsleStoreroomTreasureBoxSuscess.add((Npc) spawn(700472, 377.06046f, 512.4419f, 102.618126f, (byte) 114));
		HamateIsleStoreroomTreasureBoxSuscess.add((Npc) spawn(700473, 628.6996f, 451.98642f, 102.63267f, (byte) 48));
		HamateIsleStoreroomTreasureBoxSuscess.add((Npc) spawn(700474, 503.7779f, 630.8419f, 104.54881f, (byte) 90));
	}
	
	private void startHamateIsleStoreroomTimer()
	{
		hamateIsleStoreroomTask = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				// All Balaur treasure chests have disappeared.
				sendMsg(1400244);
				HamateIsleStoreroomTreasureBoxSuscess.get(0).getController().onDelete();
				HamateIsleStoreroomTreasureBoxSuscess.get(1).getController().onDelete();
				HamateIsleStoreroomTreasureBoxSuscess.get(2).getController().onDelete();
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
		storage.decreaseByItemId(185000036, storage.getItemCountByItemId(185000036)); // Golden Ruins Of Roah Key.
		storage.decreaseByItemId(185000037, storage.getItemCountByItemId(185000037)); // Jeweled Ruins Of Roah Key.
		storage.decreaseByItemId(185000038, storage.getItemCountByItemId(185000038)); // Magic Ruins Of Roah Key.
	}
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 503.740f, 395.700f, 94.3400f, (byte) 0);
	}
	
	@Override
	public boolean onDie(final Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}