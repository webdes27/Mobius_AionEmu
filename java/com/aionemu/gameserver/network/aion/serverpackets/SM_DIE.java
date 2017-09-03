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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_DIE extends AionServerPacket
{
	private final boolean hasRebirth;
	private final boolean hasItem;
	private final int remainingKiskTime;
	private int type = 0;
	private final boolean invasion;
	
	public SM_DIE(boolean hasRebirth, boolean hasItem, int remainingKiskTime, int type)
	{
		this(hasRebirth, hasItem, remainingKiskTime, type, false);
	}
	
	public SM_DIE(boolean hasRebirth, boolean hasItem, int remainingKiskTime, int type, boolean invasion)
	{
		this.hasRebirth = hasRebirth;
		this.hasItem = hasItem;
		this.remainingKiskTime = remainingKiskTime;
		this.type = type;
		this.invasion = invasion;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeC((hasRebirth ? 1 : 0));
		writeC((hasItem ? 1 : 0));
		writeD(remainingKiskTime);
		writeC(type);
		writeC(invasion ? 0x80 : 0x00);
	}
}