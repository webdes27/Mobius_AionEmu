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
package system.handlers.quest.iluma;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _15563Broken_Archon_Medals extends QuestHandler
{
	private static final int questId = 15563;
	
	public _15563Broken_Archon_Medals()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806114).addOnTalkEvent(questId); // Ilisia.
		qe.registerOnEnterZone(ZoneName.get("ARIEL_SANCTUARY_210100000"), questId);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (zoneName == ZoneName.get("ARIEL_SANCTUARY_210100000"))
		{
			if ((qs == null) || qs.canRepeat())
			{
				env.setQuestId(questId);
				if (QuestService.startQuest(env))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 806114:
				{ // Ilisia.
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 10002);
						}
						case SELECT_REWARD:
						{
							changeQuestStep(env, 0, 0, true);
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806114)
			{ // Ilisia.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}