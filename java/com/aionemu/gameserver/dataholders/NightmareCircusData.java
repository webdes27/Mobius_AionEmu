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
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.nightmarecircus.NightmareCircusLocation;
import com.aionemu.gameserver.model.templates.nightmarecircus.NightmareCircusTemplate;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "nightmare_circus")
public class NightmareCircusData
{
	@XmlElement(name = "nightmare_location")
	private List<NightmareCircusTemplate> nightmareCircusTemplates;
	
	@XmlTransient
	private final FastMap<Integer, NightmareCircusLocation> nightmareCircus = new FastMap<>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for (final NightmareCircusTemplate template : nightmareCircusTemplates)
		{
			nightmareCircus.put(template.getId(), new NightmareCircusLocation(template));
		}
	}
	
	public int size()
	{
		return nightmareCircus.size();
	}
	
	public FastMap<Integer, NightmareCircusLocation> getNightmareCircusLocations()
	{
		return nightmareCircus;
	}
}