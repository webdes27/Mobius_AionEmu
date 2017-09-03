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
package system.handlers.quest.cygnea;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _10506Mind_Over_Matter extends QuestHandler
{
	public static final int questId = 10506;
	
	public _10506Mind_Over_Matter()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			804709,
			804710,
			702666,
			702667
		};
		for (final int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182215604, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(236259).addOnKillEvent(questId); // Beritra Invasion Spelltongue.
		qe.registerQuestNpc(236263).addOnKillEvent(questId); // Noep's Ego.
		qe.registerOnEnterZone(ZoneName.get("LF5_SENSORYAREA_Q10506_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF5_SENSORYAREA_Q10506_2_210070000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 10505, true);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final Npc npc = (Npc) env.getVisibleObject();
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 804709)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
					}
					case STEP_TO_1:
					{
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 804710)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1352);
						}
						else if (var == 5)
						{
							return sendQuestDialog(env, 2716);
						}
						else if (var == 7)
						{
							return sendQuestDialog(env, 3399);
						}
					}
					case STEP_TO_2:
					{
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
					case STEP_TO_6:
					{
						QuestService.addNewSpawn(210070000, 1, 236263, npc.getX(), npc.getY(), npc.getZ(), (byte) 0); // Noep's Ego.
						changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
					}
					case SET_REWARD:
					{
						giveQuestItem(env, 182215613, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 702666)
			{ // Beritra Invasion Corridor.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						TeleportService2.teleportTo(env.getPlayer(), 210070000, 2837.0f, 2991.0f, 680.0f, (byte) 67, TeleportAnimation.JUMP_ANIMATION);
						return true;
					}
				}
			}
			if (targetId == 702667)
			{ // Beritra Invasion Corridor.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						TeleportService2.teleportTo(env.getPlayer(), 210070000, 1894.7863f, 2455.1982f, 336.875f, (byte) 109, TeleportAnimation.JUMP_ANIMATION);
						return true;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804710)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD)
				{
					return sendQuestDialog(env, 5);
				}
				else
				{
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 3)
			{
				final int var1 = qs.getQuestVarById(1);
				if ((var1 >= 0) && (var1 < 1))
				{
					return defaultOnKillEvent(env, 236259, var1, var1 + 1, 1); // Beritra Invasion Spelltongue.
				}
				else if (var1 == 1)
				{
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
			}
			if (var == 6)
			{
				return defaultOnKillEvent(env, 236263, 6, 7); // Noep's Ego.
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("LF5_SENSORYAREA_Q10506_210070000"))
			{
				if (var == 2)
				{
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("LF5_SENSORYAREA_Q10506_2_210070000"))
			{
				if (var == 4)
				{
					changeQuestStep(env, 4, 5, false);
					return true;
				}
			}
		}
		return false;
	}
}