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
package com.aionemu.gameserver.network.ls.clientpackets;

import java.lang.management.ManagementFactory;

import com.aionemu.gameserver.network.ls.LoginServer;
import com.aionemu.gameserver.network.ls.LsClientPacket;
import com.aionemu.gameserver.network.ls.serverpackets.SM_LS_PONG;

/**
 * @author KID
 */
public class CM_LS_PING extends LsClientPacket
{
	public CM_LS_PING(int opCode)
	{
		super(opCode);
	}
	
	@Override
	protected void readImpl()
	{
		// trigger
	}
	
	@Override
	protected void runImpl()
	{
		int pid = -1;
		try
		{
			pid = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
		}
		catch (final Exception ex)
		{
		}
		
		LoginServer.getInstance().sendPacket(new SM_LS_PONG(pid));
	}
}
