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
package com.aionemu.gameserver.utils.rates;

import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.configs.main.RateConfig;

public class RegularRates extends Rates
{
	
	int holidayRate = HolidayRates.getHolidayRate(0);
	
	@Override
	public float getGroupXpRate()
	{
		return RateConfig.GROUPXP_RATE + holidayRate;
	}
	
	@Override
	public float getDropRate()
	{
		return RateConfig.DROP_RATE + holidayRate;
	}
	
	@Override
	public float getApNpcRate()
	{
		return RateConfig.AP_NPC_RATE + holidayRate;
	}
	
	@Override
	public float getApPlayerGainRate()
	{
		return RateConfig.AP_PLAYER_GAIN_RATE + holidayRate;
	}
	
	@Override
	public float getGpPlayerGainRate()
	{
		return RateConfig.GP_PLAYER_GAIN_RATE;
	}
	
	@Override
	public float getXpPlayerGainRate()
	{
		return RateConfig.XP_PLAYER_GAIN_RATE + holidayRate;
	}
	
	@Override
	public float getApPlayerLossRate()
	{
		return RateConfig.AP_PLAYER_LOSS_RATE + holidayRate;
	}
	
	@Override
	public float getGpPlayerLossRate()
	{
		return RateConfig.GP_PLAYER_LOSS_RATE;
	}
	
	@Override
	public float getQuestKinahRate()
	{
		return RateConfig.QUEST_KINAH_RATE + holidayRate;
	}
	
	@Override
	public float getQuestXpRate()
	{
		return RateConfig.QUEST_XP_RATE + holidayRate;
	}
	
	@Override
	public float getQuestApRate()
	{
		return RateConfig.QUEST_AP_RATE + holidayRate;
	}
	
	@Override
	public float getQuestGpRate()
	{
		return RateConfig.QUEST_GP_RATE + holidayRate;
	}
	
	@Override
	public float getQuestAbyssOpRate()
	{
		return RateConfig.QUEST_ABYSS_OP_RATE + holidayRate;
	}
	
	@Override
	public float getQuestExpBoostRate()
	{
		return RateConfig.QUEST_EXP_BOOST_RATE + holidayRate;
	}
	
	@Override
	public float getXpRate()
	{
		return RateConfig.XP_RATE + holidayRate;
	}
	
	@Override
	public float getCraftingXPRate()
	{
		return RateConfig.CRAFTING_XP_RATE + holidayRate;
	}
	
	@Override
	public float getGatheringXPRate()
	{
		return RateConfig.GATHERING_XP_RATE + holidayRate;
	}
	
	@Override
	public int getGatheringCountRate()
	{
		return RateConfig.GATHERING_COUNT_RATE + holidayRate;
	}
	
	@Override
	public float getDpNpcRate()
	{
		return RateConfig.DP_NPC_RATE + holidayRate;
	}
	
	@Override
	public float getDpPlayerRate()
	{
		return RateConfig.DP_PLAYER_RATE + holidayRate;
	}
	
	@Override
	public int getCraftCritRate()
	{
		return CraftConfig.CRAFT_CRIT_RATE + holidayRate;
	}
	
	@Override
	public int getComboCritRate()
	{
		return CraftConfig.CRAFT_COMBO_RATE + holidayRate;
	}
	
	@Override
	public float getDisciplineRewardRate()
	{
		return RateConfig.PVP_ARENA_DISCIPLINE_REWARD_RATE + holidayRate;
	}
	
	@Override
	public float getChaosRewardRate()
	{
		return RateConfig.PVP_ARENA_CHAOS_REWARD_RATE + holidayRate;
	}
	
	@Override
	public float getHarmonyRewardRate()
	{
		return RateConfig.PVP_ARENA_HARMONY_REWARD_RATE + holidayRate;
	}
	
	@Override
	public float getGloryRewardRate()
	{
		return RateConfig.PVP_ARENA_GLORY_REWARD_RATE + holidayRate;
	}
	
	@Override
	public float getTollRewardRate()
	{
		return RateConfig.TOLL_REWARD_RATE + holidayRate;
	}
}