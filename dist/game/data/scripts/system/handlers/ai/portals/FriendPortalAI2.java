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

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.DialogPage;
import com.aionemu.gameserver.model.gameobjects.SummonedHouseNpc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("friendportal")
public class FriendPortalAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		final SummonedHouseNpc me = (SummonedHouseNpc) getOwner();
		final int playerOwner = me.getCreator().getOwnerId();
		final boolean allowed = (player.getObjectId() == playerOwner) || (player.getFriendList().getFriend(playerOwner) != null) || ((player.getLegion() != null) && player.getLegion().isMember(playerOwner));
		if (allowed)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), DialogPage.HOUSING_FRIENDLIST.id()));
		}
		else
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_HOUSING_TELEPORT_CANT_USE);
		}
	}
	
	@Override
	protected void handleDialogFinish(Player player)
	{
	}
}