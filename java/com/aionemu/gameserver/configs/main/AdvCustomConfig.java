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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class AdvCustomConfig
{
	@Property(key = "gameserver.cube.size", defaultValue = "0")
	public static int CUBE_SIZE;
	
	@Property(key = "gameserver.gameshop.limit", defaultValue = "false")
	public static boolean GAMESHOP_LIMIT;
	
	@Property(key = "gameserver.gameshop.category", defaultValue = "0")
	public static byte GAMESHOP_CATEGORY;
	
	@Property(key = "gameserver.gameshop.limit.time", defaultValue = "60")
	public static long GAMESHOP_LIMIT_TIME;
	
	@Property(key = "gameserver.craft.delaytime,rate", defaultValue = "2")
	public static Integer CRAFT_DELAYTIME_RATE;
	
	@Property(key = "gameserver.godstone.base", defaultValue = "1000")
	public static int BASE_GODSTONE;
}