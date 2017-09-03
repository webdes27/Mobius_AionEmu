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
package system.handlers.quest.heiron;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Balthazar
 * @reworked & modified Gigi
 */
public class _1644AVeryOldLetter extends QuestHandler
{
	
	private static final int questId = 1644;
	
	public _1644AVeryOldLetter()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(204545).addOnTalkEvent(questId);
		qe.registerQuestNpc(204537).addOnTalkEvent(questId);
		qe.registerQuestNpc(204546).addOnTalkEvent(questId);
		qe.registerQuestItem(182201765, questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		
		if (targetId == 0)
		{
			if (env.getDialogId() == 1002)
			{
				QuestService.startQuest(env);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;
			}
		}
		
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			switch (targetId)
			{
				case 204545:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							if (qs.getQuestVarById(0) == 0)
							{
								return sendQuestDialog(env, 1352);
							}
							else if (qs.getQuestVarById(0) == 2)
							{
								return sendQuestDialog(env, 2034);
							}
						}
						case STEP_TO_1:
						{
							return defaultCloseDialog(env, 0, 1);
						}
						case STEP_TO_3:
						{
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return defaultCloseDialog(env, 2, 3);
						}
					}
				}
				case 204537:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 1693);
						}
						case STEP_TO_2:
						{
							return defaultCloseDialog(env, 1, 2, 0, 0, 182201765, 1);
						}
					}
				}
			}
		}
		else if ((qs != null) && (qs.getStatus() == QuestStatus.REWARD))
		{
			if (targetId == 204546)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 2375);
					}
					default:
					{
						return sendQuestEndDialog(env);
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final int itemObjId = item.getObjectId();
		final int id = item.getItemTemplate().getTemplateId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (id != 182201765)
		{
			return HandlerResult.UNKNOWN;
		}
		
		if (qs != null)
		{
			if (qs.getStatus() == QuestStatus.COMPLETE)
			{
				removeQuestItem(env, 182201765, 1);
				return HandlerResult.FAILED;
			}
		}
		
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
				sendQuestDialog(env, 4);
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}
}
