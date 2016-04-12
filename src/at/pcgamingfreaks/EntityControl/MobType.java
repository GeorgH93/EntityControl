/*
* Copyright (C) 2014-2015 GeorgH93
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package at.pcgamingfreaks.EntityControl;

public enum MobType
{
	// Monster
	CREEPER		(50,  MobCategory.MONSTER),
    SKELETON	(51,  MobCategory.MONSTER),
    SPIDER		(52,  MobCategory.MONSTER),
    ZOMBIE		(54,  MobCategory.MONSTER),
    SLIME		(55,  MobCategory.MONSTER),
    GHAST		(56,  MobCategory.MONSTER),
    PIGZOMBIE	(57,  MobCategory.MONSTER),
    ENDERMAN	(58,  MobCategory.MONSTER),
    CAVESPIDER	(59,  MobCategory.MONSTER),
    SILVERFISH	(60,  MobCategory.MONSTER),
    BLAZE		(61,  MobCategory.MONSTER),
    MAGMACUBE	(62,  MobCategory.MONSTER),
    ENDERMITE	(67,  MobCategory.MONSTER),
    GUARDIAN	(68,  MobCategory.MONSTER),
    // Animals
    BAT			(65,  MobCategory.ANIMAL),
    PIG			(90,  MobCategory.ANIMAL),
    SHEEP		(91,  MobCategory.ANIMAL),
    COW			(92,  MobCategory.ANIMAL),
    CHICKEN		(93,  MobCategory.ANIMAL),
    SQUID		(94,  MobCategory.ANIMAL),
    WOLF		(95,  MobCategory.ANIMAL),
    MOOSHROOM	(96,  MobCategory.ANIMAL),
    OCELOT		(98,  MobCategory.ANIMAL),
    HORSE		(100, MobCategory.ANIMAL),
    RABBIT		(101, MobCategory.ANIMAL),
    // Other
    WITCH		(66,  MobCategory.NPC),
    VILLAGER	(120, MobCategory.NPC),
    UNKNOWN		(0, MobCategory.UNKNOWN);

	private int id = 0;
	private String name;
    private MobCategory category = MobCategory.UNKNOWN;

    MobType(int ID, MobCategory Category)
    {
    	id = ID;
    	category = Category;
    	name = EntityControl.lang.get("Entity.EID" + ID);
    }

    public int getEntityId()
    {
    	return id;
    }
    
    public MobCategory getCategory()
    {
    	return category;
    }
    
    public String getPermName()
    {
    	return name().toLowerCase();
    }
    
    public String getName()
    {
    	return name;
    }
    
    public enum MobCategory
    {
        MONSTER,  ANIMAL,  NPC,  UNKNOWN
    }
    
    public static MobType getMobTypeFromId(int id)
	{
		for (MobType mobType : MobType.values())
		{
			if (id == mobType.getEntityId())
			{
				return mobType;
			}
		}
	    return MobType.UNKNOWN;
	}
}