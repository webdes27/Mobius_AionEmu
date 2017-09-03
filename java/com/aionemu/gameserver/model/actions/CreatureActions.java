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
package com.aionemu.gameserver.model.actions;

import com.aionemu.gameserver.model.gameobjects.Creature;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class CreatureActions
{
	public static String getName(Creature creature)
	{
		return creature.getName();
	}
	
	public static boolean isAlreadyDead(Creature creature)
	{
		return creature.getLifeStats().isAlreadyDead();
	}
	
	public static void delete(Creature creature)
	{
		if (creature != null)
		{
			creature.getController().onDelete();
		}
	}
}