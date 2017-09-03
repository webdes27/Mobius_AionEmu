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
package system.handlers.ai.portals;

import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.autogroup.AutoGroupType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.portal.PortalPath;
import com.aionemu.gameserver.network.aion.serverpackets.SM_AUTO_GROUP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.PortalService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("specialize_portal")
public class Specialize01PortalAI2 extends PortalAI2
{
	protected int rewardDialogId = 5;
	protected int startingDialogId = 10;
	protected int questDialogId = 10;
	
	@Override
	protected void handleDialogStart(Player player)
	{
		if (getTalkDelay() == 0)
		{
			checkDialog(player);
		}
		else
		{
			super.handleDialogStart(player);
		}
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		final QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		if ((questId > 0) && QuestEngine.getInstance().onDialog(env))
		{
			return true;
		}
		if (dialogId == DialogAction.INSTANCE_PARTY_MATCH.id())
		{
			final AutoGroupType agt = AutoGroupType.getAutoGroup(player.getLevel(), getNpcId());
			if (agt != null)
			{
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(agt.getInstanceMaskId()));
			}
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		}
		else
		{
			if (questId == 0)
			{
				final PortalPath portalPath = DataManager.PORTAL2_DATA.getPortalDialog(getNpcId(), dialogId, player.getRace());
				if (portalPath != null)
				{
					PortalService.port(portalPath, player, getObjectId());
				}
			}
			else
			{
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), dialogId, questId));
			}
		}
		return true;
	}
	
	@Override
	protected void handleUseItemFinish(Player player)
	{
		checkDialog(player);
	}
	
	private void checkDialog(Player player)
	{
		final int npcId = getNpcId();
		final int teleportationDialogId = DataManager.PORTAL2_DATA.getTeleportDialogId(npcId);
		final List<Integer> relatedQuests = QuestEngine.getInstance().getQuestNpc(npcId).getOnTalkEvent();
		boolean playerHasQuest = false;
		boolean playerCanStartQuest = false;
		if (!relatedQuests.isEmpty())
		{
			for (final int questId : relatedQuests)
			{
				final QuestState qs = player.getQuestStateList().getQuestState(questId);
				if ((qs != null) && ((qs.getStatus() == QuestStatus.START) || (qs.getStatus() == QuestStatus.REWARD)))
				{
					playerHasQuest = true;
					break;
				}
				else if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
				{
					if (QuestService.checkStartConditions(new QuestEnv(getOwner(), player, questId, 0), false))
					{
						playerCanStartQuest = true;
						continue;
					}
				}
			}
		}
		if (playerHasQuest)
		{
			boolean isRewardStep = false;
			for (final int questId : relatedQuests)
			{
				final QuestState qs = player.getQuestStateList().getQuestState(questId);
				if ((qs != null) && (qs.getStatus() == QuestStatus.REWARD))
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), rewardDialogId, questId));
					isRewardStep = true;
					break;
				}
			}
			if (!isRewardStep)
			{
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), questDialogId));
			}
		}
		else if (playerCanStartQuest)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), startingDialogId));
		}
		else
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), teleportationDialogId, 0));
		}
	}
}