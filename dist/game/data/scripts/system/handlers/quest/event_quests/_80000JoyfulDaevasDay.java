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
package system.handlers.quest.event_quests;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.EventService;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Rolandas
 */

public class _80000JoyfulDaevasDay extends QuestHandler
{
	
	private static final int questId = 80000;
	
	public _80000JoyfulDaevasDay()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(798415).addOnTalkEvent(questId); // Ias
		qe.registerOnLevelUp(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (env.getTargetId() == 0)
		{
			if (env.getDialog() == QuestDialog.ACCEPT_QUEST)
			{
				QuestService.startEventQuest(env, QuestStatus.START);
				closeDialogWindow(env);
				return true;
			}
		}
		else if (env.getTargetId() == 798415) // Ias
		{
			if (qs != null)
			{
				if ((env.getDialog() == QuestDialog.START_DIALOG) && (qs.getStatus() == QuestStatus.START))
				{
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD)
				{
					qs.setQuestVar(1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (!EventService.getInstance().checkQuestIsActive(questId) && (qs != null))
		{
			QuestService.abandonQuest(player, questId);
		}
		return true;
	}
}
