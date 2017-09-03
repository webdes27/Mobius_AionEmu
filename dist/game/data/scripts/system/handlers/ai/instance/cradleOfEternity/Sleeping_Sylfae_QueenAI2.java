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
package system.handlers.ai.instance.cradleOfEternity;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.utils.PacketSendUtility;

import system.handlers.ai.GeneralNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("Sleeping_Sylfae_Queen")
public class Sleeping_Sylfae_QueenAI2 extends GeneralNpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		switch (getNpcId())
		{
			case 834039:
			{ // Sleeping Sylfae Queen.
				super.handleDialogStart(player);
				break;
			}
			default:
			{
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1352));
				break;
			}
		}
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		final QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		final PlayerEffectController effectController = player.getEffectController();
		if (QuestEngine.getInstance().onDialog(env) && (dialogId != 1011))
		{
			return true;
		}
		if (dialogId == 10000)
		{
			switch (getNpcId())
			{
				case 834039: // Sleeping Sylfae Queen.
					effectController.removeEffect(21340); // Sylfae Queens Blessing.
					break;
			}
		}
		else if ((dialogId == 1011) && (questId != 0))
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), dialogId, questId));
		}
		return true;
	}
}