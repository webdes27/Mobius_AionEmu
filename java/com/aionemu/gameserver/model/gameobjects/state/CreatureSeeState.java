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
package com.aionemu.gameserver.model.gameobjects.state;

/**
 * @author Sweetkr
 */
public enum CreatureSeeState
{
	NORMAL(0), // Normal
	SEARCH1(1), // See-Through: Hide I
	SEARCH2(2), // See-Through: Hide II
	SEARCH5(5), // no idea :)
	SEARCH10(10),
	SEARCH20(20);
	
	private int id;
	
	private CreatureSeeState(int id)
	{
		this.id = id;
	}
	
	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}
}
