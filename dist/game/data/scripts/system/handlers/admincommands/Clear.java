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
package system.handlers.admincommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.FindGroupService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author KID
 */
public class Clear extends AdminCommand
{
	
	public Clear()
	{
		super("clear");
	}
	
	@Override
	public void execute(Player admin, String... params)
	{
		if (params[0].equalsIgnoreCase("groups"))
		{
			PacketSendUtility.sendMessage(admin, "Not implemented, if need this - pm to AT");
		}
		else if (params[0].equalsIgnoreCase("allys"))
		{
			PacketSendUtility.sendMessage(admin, "Not implemented, if need this - pm to AT");
		}
		else if (params[0].equalsIgnoreCase("findgroup"))
		{
			FindGroupService.getInstance().clean();
		}
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "<usage //clear groups | allys | findgroup");
	}
}
