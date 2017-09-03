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
package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class AbsoluteEXPPointHealInstantEffect extends EffectTemplate
{
	@XmlAttribute(required = true)
	protected int points;
	
	@Override
	public void applyEffect(Effect effect)
	{
		if ((effect.getEffected() instanceof Player))
		{
			final Player player = (Player) effect.getEffected();
			player.getCommonData().addGoldenStarEnergy(3150000); // 0.5%
			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
		}
	}
}