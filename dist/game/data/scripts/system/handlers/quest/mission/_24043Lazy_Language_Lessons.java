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
package system.handlers.quest.mission;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnSearchResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.MathUtil;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _24043Lazy_Language_Lessons extends QuestHandler
{
	private static final int questId = 24043;
	
	public _24043Lazy_Language_Lessons()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			278003,
			278086,
			278039,
			279027,
			204210
		};
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(253610).addOnAttackEvent(questId);
		for (final int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		final int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 278003:
				{ // Hisui.
					switch (dialog)
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
							return defaultCloseDialog(env, 0, 1);
						}
					}
					break;
				}
				case 278086:
				{ // Sinjah.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 1)
							{
								return sendQuestDialog(env, 1352);
							}
						}
						case STEP_TO_2:
						{
							return defaultCloseDialog(env, 1, 2);
						}
					}
					break;
				}
				case 278039:
				{ // Grunn.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 3)
							{
								return sendQuestDialog(env, 2034);
							}
						}
						case STEP_TO_4:
						{
							return defaultCloseDialog(env, 3, 4);
						}
					}
					break;
				}
				case 279027:
				{ // Kaoranerk.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 4)
							{
								return sendQuestDialog(env, 2375);
							}
							else if (var == 6)
							{
								return sendQuestDialog(env, 3057);
							}
						}
						case SELECT_ACTION_3058:
						{
							removeQuestItem(env, 182215373, 1);
							playQuestMovie(env, 293);
							return sendQuestDialog(env, 3058);
						}
						case STEP_TO_5:
						{
							return defaultCloseDialog(env, 4, 5);
						}
						case SET_REWARD:
						{
							return defaultCloseDialog(env, 6, 6, true, false);
						}
					}
					break;
				}
				case 204210:
				{ // Phosphor.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 5)
							{
								return sendQuestDialog(env, 2716);
							}
						}
						case STEP_TO_6:
						{
							return defaultCloseDialog(env, 5, 6, 182215373, 1, 0, 0);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 278003)
			{ // Hisui.
				if (env.getDialog() == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 10002);
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
	public boolean onAttackEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 2)
			{
				final Npc npc = (Npc) env.getVisibleObject();
				final SpawnSearchResult searchResult = DataManager.SPAWNS_DATA2.getFirstSpawnByNpcId(npc.getWorldId(), 278086); // Sinjah.
				if (MathUtil.getDistance(searchResult.getSpot().getX(), searchResult.getSpot().getY(), searchResult.getSpot().getZ(), npc.getX(), npc.getY(), npc.getZ()) <= 30)
				{
					npc.getController().onDie(player);
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 24042, true);
	}
}