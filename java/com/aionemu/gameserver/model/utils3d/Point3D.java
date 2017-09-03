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
package com.aionemu.gameserver.model.utils3d;

/**
 * @author M@xx modified by Wakizashi
 */
public class Point3D
{
	
	public double x;
	public double y;
	public double z;
	
	public Point3D()
	{
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}
	
	public Point3D(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double distance(Point3D p)
	{
		final double dx = x - p.x;
		final double dy = y - p.y;
		final double dz = z - p.z;
		return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
	}
	
	@Override
	public String toString()
	{
		return "x=" + x + ", y=" + y + ", z=" + z;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getZ()
	{
		return z;
	}
}
