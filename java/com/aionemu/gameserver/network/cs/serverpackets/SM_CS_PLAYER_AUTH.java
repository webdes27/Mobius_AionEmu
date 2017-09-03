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
package com.aionemu.gameserver.network.cs.serverpackets;

import com.aionemu.gameserver.network.cs.ChatServerConnection;
import com.aionemu.gameserver.network.cs.CsServerPacket;

public class SM_CS_PLAYER_AUTH extends CsServerPacket
{
	private final int playerId;
	private final String playerLogin;
	private final String nick;
	
	public SM_CS_PLAYER_AUTH(int playerId, String playerLogin, String nick)
	{
		super(0x01);
		this.playerId = playerId;
		this.playerLogin = playerLogin;
		this.nick = nick;
	}
	
	@Override
	protected void writeImpl(ChatServerConnection con)
	{
		writeD(playerId);
		writeS(playerLogin);
		writeS(nick);
	}
}