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
package com.aionemu.gameserver.model.instance.instancereward;

import static ch.lambdaj.Lambda.maxFrom;
import static ch.lambdaj.Lambda.minFrom;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import static ch.lambdaj.Lambda.sum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.instanceposition.ChaosInstancePosition;
import com.aionemu.gameserver.model.instance.instanceposition.DisciplineInstancePosition;
import com.aionemu.gameserver.model.instance.instanceposition.GenerealInstancePosition;
import com.aionemu.gameserver.model.instance.instanceposition.GloryInstancePosition;
import com.aionemu.gameserver.model.instance.instanceposition.HarmonyInstancePosition;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

public class PvPArenaReward extends InstanceReward<PvPArenaPlayerReward>
{
	private final Map<Integer, Boolean> positions = new HashMap<>();
	private final FastList<Integer> zones = new FastList<>();
	private int round = 1;
	private Integer zone;
	private final int bonusTime;
	private final int capPoints;
	private long instanceTime;
	private final byte buffId;
	protected WorldMapInstance instance;
	private GenerealInstancePosition instancePosition;
	
	public PvPArenaReward(Integer mapId, int instanceId, WorldMapInstance instance)
	{
		super(mapId, instanceId);
		this.instance = instance;
		final boolean isSolo = isSoloArena();
		capPoints = isSolo ? 14400 : 50000;
		bonusTime = isSolo ? 8100 : 12000;
		Collections.addAll(zones, isSolo ? new Integer[]
		{
			1,
			2,
			3,
			4
		} : new Integer[]
		{
			1,
			2,
			3,
			4,
			5,
			6
		});
		int positionSize;
		if (isSolo)
		{
			positionSize = 4;
			buffId = 8;
			instancePosition = new DisciplineInstancePosition();
		}
		else if (isGlory())
		{
			buffId = 7;
			positionSize = 8;
			instancePosition = new GloryInstancePosition();
		}
		else if ((mapId == 300450000) || (mapId == 300570000) || (mapId == 301100000))
		{
			buffId = 7;
			positionSize = 12;
			instancePosition = new HarmonyInstancePosition();
		}
		else
		{
			buffId = 7;
			positionSize = 12;
			instancePosition = new ChaosInstancePosition();
		}
		instancePosition.initsialize(mapId, instanceId);
		for (int i = 1; i <= positionSize; i++)
		{
			positions.put(i, Boolean.FALSE);
		}
		setRndZone();
	}
	
	public final boolean isSoloArena()
	{
		return (mapId == 300360000) || (mapId == 300430000);
	}
	
	public final boolean isGlory()
	{
		return mapId == 300550000;
	}
	
	public int getCapPoints()
	{
		return capPoints;
	}
	
	public final void setRndZone()
	{
		final int index = Rnd.get(zones.size());
		zone = zones.get(index);
		zones.remove(index);
	}
	
	private List<Integer> getFreePositions()
	{
		final List<Integer> p = new ArrayList<>();
		for (final Integer key : positions.keySet())
		{
			if (!positions.get(key))
			{
				p.add(key);
			}
		}
		return p;
	}
	
	public synchronized void setRndPosition(Integer object)
	{
		final PvPArenaPlayerReward reward = getPlayerReward(object);
		final int position = reward.getPosition();
		if (position != 0)
		{
			clearPosition(position, Boolean.FALSE);
		}
		final Integer key = getFreePositions().get(Rnd.get(getFreePositions().size()));
		clearPosition(key, Boolean.TRUE);
		reward.setPosition(key);
	}
	
	public synchronized void clearPosition(int position, Boolean result)
	{
		positions.put(position, result);
	}
	
	public int getRound()
	{
		return round;
	}
	
	public void setRound(int round)
	{
		this.round = round;
	}
	
	public void regPlayerReward(Integer object)
	{
		if (!containPlayer(object))
		{
			addPlayerReward(new PvPArenaPlayerReward(object, bonusTime, buffId));
		}
	}
	
	@Override
	public void addPlayerReward(PvPArenaPlayerReward reward)
	{
		super.addPlayerReward(reward);
	}
	
	@Override
	public PvPArenaPlayerReward getPlayerReward(Integer object)
	{
		return (PvPArenaPlayerReward) super.getPlayerReward(object);
	}
	
	public void portToPosition(Player player)
	{
		final Integer object = player.getObjectId();
		regPlayerReward(object);
		setRndPosition(object);
		final PvPArenaPlayerReward playerReward = getPlayerReward(object);
		playerReward.applyBoostMoraleEffect(player);
		instancePosition.port(player, zone, playerReward.getPosition());
	}
	
	public List<PvPArenaPlayerReward> sortPoints()
	{
		return sort(getInstanceRewards(), on(PvPArenaPlayerReward.class).getScorePoints(), new Comparator<Integer>()
		{
			@Override
			public int compare(Integer o1, Integer o2)
			{
				return o2 != null ? o2.compareTo(o1) : -o1.compareTo(o2);
			}
		});
	}
	
	public boolean canRewardOpportunityToken(PvPArenaPlayerReward rewardedPlayer)
	{
		if (rewardedPlayer != null)
		{
			final int rank = getRank(rewardedPlayer.getScorePoints());
			return (isSoloArena() && (rank == 1)) || (rank > 2);
		}
		return false;
	}
	
	public int getRank(int points)
	{
		int rank = -1;
		for (final PvPArenaPlayerReward reward : sortPoints())
		{
			if (reward.getScorePoints() >= points)
			{
				rank++;
			}
		}
		return rank;
	}
	
	public boolean hasCapPoints()
	{
		if (isSoloArena() && ((maxFrom(getInstanceRewards()).getPoints() - minFrom(getInstanceRewards()).getPoints()) >= 1500))
		{
			return true;
		}
		return maxFrom(getInstanceRewards()).getPoints() >= capPoints;
	}
	
	public int getTotalPoints()
	{
		return sum(getInstanceRewards(), on(PvPArenaPlayerReward.class).getScorePoints());
	}
	
	public boolean canRewarded()
	{
		return (mapId == 300350000) || (mapId == 300360000) || (mapId == 300420000) || (mapId == 300430000) || (mapId == 300450000) || (mapId == 300550000) || (mapId == 300570000) || (mapId == 301100000);
	}
	
	public int getNpcBonusSkill(int npcId)
	{
		switch (npcId)
		{
			case 701175:
			case 701176:
			case 701177:
			case 701178:
				return 0x4E5732;
			case 701189:
			case 701190:
			case 701191:
			case 701192:
				return 0x4FDB3C;
			case 701317:
				return 0x4f8532;
			case 701318:
				return 0x4f8537;
			case 701319:
				return 0x4f853C;
			case 701220:
				return 0x4E5537;
			case 207118:
			case 207119:
				return 0x50C101;
			case 207100:
				return 0x50BE01;
			default:
				return 0;
		}
	}
	
	public int getTime()
	{
		final long result = System.currentTimeMillis() - instanceTime;
		if (isRewarded())
		{
			return 0;
		}
		if (result < 120000)
		{
			return (int) (120000 - result);
		}
		else
		{
			return (int) ((180000 * getRound()) - (result - 120000));
		}
	}
	
	@Override
	public void setInstanceStartTime()
	{
		instanceTime = System.currentTimeMillis();
	}
	
	public void sendPacket()
	{
		final List<Player> players = instance.getPlayersInside();
		instance.doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), getInstanceReward(), players));
			}
		});
	}
	
	public byte getBuffId()
	{
		return buffId;
	}
	
	@Override
	public void clear()
	{
		super.clear();
		positions.clear();
	}
}