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
package system.handlers.ai.instance.empyreanCrucible;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("king_consierd")
public class KingConsierdAI2 extends AggressiveNpcAI2
{
	private final List<Integer> percents = new ArrayList<>();
	private final AtomicBoolean isHome = new AtomicBoolean(true);
	private Future<?> eventTask;
	private Future<?> skillTask;
	
	@Override
	public void handleSpawned()
	{
		super.handleSpawned();
		addPercents();
	}
	
	@Override
	public void handleDespawned()
	{
		cancelTasks();
		percents.clear();
		super.handleDespawned();
	}
	
	@Override
	public void handleDied()
	{
		cancelTasks();
		super.handleDied();
	}
	
	@Override
	public void handleBackHome()
	{
		cancelTasks();
		addPercents();
		super.handleBackHome();
	}
	
	@Override
	public void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
		if (isHome.compareAndSet(true, false))
		{
			startBloodThirstTask();
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					SkillEngine.getInstance().getSkill(getOwner(), 19691, 1, getTarget()).useNoAnimationSkill();
					ThreadPoolManager.getInstance().schedule(new Runnable()
					{
						@Override
						public void run()
						{
							SkillEngine.getInstance().getSkill(getOwner(), 17954, 10, getTarget()).useNoAnimationSkill();
						}
					}, 4000);
				}
			}, 2000);
		}
	}
	
	private void startBloodThirstTask()
	{
		eventTask = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				SkillEngine.getInstance().getSkill(getOwner(), 19624, 10, getOwner()).useNoAnimationSkill();
			}
		}, 180 * 1000);
	}
	
	private void startSkillTask()
	{
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				if (isAlreadyDead())
				{
					cancelTasks();
				}
				else
				{
					SkillEngine.getInstance().getSkill(getOwner(), 17951, 10, getTarget()).useNoAnimationSkill();
					final List<Player> players = getLifedPlayers();
					if (!players.isEmpty())
					{
						final int size = players.size();
						if (players.size() < 6)
						{
							for (final Player p : players)
							{
								spawnBabyConsierd(p);
							}
						}
						else
						{
							final int count = Rnd.get(1, size);
							for (int i = 0; i < count; i++)
							{
								if (players.isEmpty())
								{
									break;
								}
								spawnBabyConsierd(players.get(Rnd.get(players.size())));
								SkillEngine.getInstance().getSkill(getOwner(), 17952, 10, getTarget()).useNoAnimationSkill();
							}
						}
					}
				}
			}
		}, 3000, 15000);
	}
	
	private void spawnBabyConsierd(Player player)
	{
		final float x = player.getX();
		final float y = player.getY();
		final float z = player.getZ();
		if ((x > 0) && (y > 0) && (z > 0))
		{
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					if (!isAlreadyDead())
					{
						spawn(282378, x, y, z, (byte) 0); // Baby Consierd.
					}
				}
			}, 3000);
		}
	}
	
	private List<Player> getLifedPlayers()
	{
		final List<Player> players = new ArrayList<>();
		for (final Player player : getKnownList().getKnownPlayers().values())
		{
			if (!CreatureActions.isAlreadyDead(player))
			{
				players.add(player);
			}
		}
		return players;
	}
	
	private void cancelTasks()
	{
		if ((eventTask != null) && !eventTask.isDone())
		{
			eventTask.cancel(true);
		}
		if ((skillTask != null) && !skillTask.isCancelled())
		{
			skillTask.cancel(true);
		}
	}
	
	private void checkPercentage(int percentage)
	{
		for (final Integer percent : percents)
		{
			if (percentage <= percent)
			{
				percents.remove(percent);
				if (percent == 75)
				{
					startSkillTask();
				}
				else if (percent == 25)
				{
					SkillEngine.getInstance().getSkill(getOwner(), 19690, 1, getTarget()).useNoAnimationSkill();
				}
				break;
			}
		}
	}
	
	private void addPercents()
	{
		percents.clear();
		Collections.addAll(percents, new Integer[]
		{
			75,
			25
		});
	}
}